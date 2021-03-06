package com.seeu.framework.rpc;

import com.google.protobuf.MessageOrBuilder;
import com.seeu.framework.rpc.RpcMsg.request.Builder;
import com.seeu.framework.rpc.RpcMsg.response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Component
@Sharable
public class RpcClientHandler extends SimpleChannelInboundHandler<response> {
    private static ExecutorService service =
            Executors.newFixedThreadPool(10 + Runtime.getRuntime().availableProcessors() * 2);
    @Autowired
    RpcClientManger rpcClientManger;
    private Map<Integer, response> responseMap = new ConcurrentHashMap<>();
    private Map<Integer, Object> threadObjectMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, response rsp)
            throws Exception {

        if (threadObjectMap.containsKey(rsp.getMsgSeq())) {
            responseMap.put(rsp.getMsgSeq(), rsp);
            Object obj = threadObjectMap.remove(rsp.getMsgSeq());
            synchronized (obj) {
                obj.notify();
            }
        } else {
            log.error("receive response msg, but have no any request. {}, {}", rsp.getMsgSeq(),
                    rsp.getMethod());
        }
    }

    public response write(MessageOrBuilder messageOrBuilder, RpcBaseClient client) throws Exception {
        if (messageOrBuilder instanceof RpcMsg.request) {
            return write((RpcMsg.request) messageOrBuilder, client);
        } else if (messageOrBuilder instanceof RpcMsg.request.Builder) {
            return write(((Builder) messageOrBuilder).build(), client);
        } else {
            throw new Exception("writeMsg but type error: " + messageOrBuilder.getClass().getName());
        }
    }

    public response write(RpcMsg.request msg, RpcBaseClient client) throws Exception {
        Channel channel = client.getChannel();

        if (!msg.getNeedResponse()) {
            channel.writeAndFlush(msg);
            return null;
        }

        try {
            return (response) service.submit(new Callable<Object>() {
                @Override
                public synchronized Object call() throws InterruptedException {
                    log.info("send message: {}", msg);
                    channel.writeAndFlush(msg).sync();
                    putThreadObject(msg.getMsgSeq(), msg);

                    synchronized (msg) {
                        msg.wait();
                    }

                    return getResponse(msg.getMsgSeq());
                }
            }).get(100, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            log.error("FakeRpcClientMessageHandler write msg but get response timeout, msg:{}",
                    msg);
            synchronized (msg) {
                msg.notify();
            }
            threadObjectMap.remove(msg.getMsgSeq());
            return null;
        }
    }

    void putThreadObject(Integer seqId, Object obj) {
        if (threadObjectMap.containsKey(seqId)) {
            log.error("Already contains the thread object for msg {}", seqId);
            return;
        }

        threadObjectMap.put(seqId, obj);
    }

    response getResponse(Integer reqId) {
        return responseMap.remove(reqId);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        RpcBaseClient client = rpcClientManger.removeClient(ctx.channel());
        client.clientInactive();
    }
}
