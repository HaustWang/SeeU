package com.seeu.framework.rpc;

import com.google.protobuf.MessageLite;
import com.seeu.framework.rpc.RpcMsg.request;
import com.seeu.framework.scanner.RpcInvokerHolder;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Sharable
public class RpcServerHandler extends SimpleChannelInboundHandler<request> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, request req)
            throws Exception {
        MessageLite resp = (MessageLite) RpcInvokerHolder
                .serviceInvoke(req.getSvrType(),
                        req.getMethod(), req.getContent());
        if (null == resp) {
            return;
        }

        RpcMsg.response.Builder builder = RpcMsg.response.newBuilder()
                .setContent(resp.toByteString())
                .setMethod(req.getMethod())
                .setMsgSeq(req.getMsgSeq())
                .setSvrType(req.getSvrType())
                .setSvrId(req.getSvrId());

        channelHandlerContext.writeAndFlush(builder);
    }
}
