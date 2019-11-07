package com.seeu.framework.rpc;

import com.seeu.framework.rpc.RpcMsg.request;
import com.seeu.framework.scanner.RpcInvokerHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServerHandler extends SimpleChannelInboundHandler<request> {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, request req)
        throws Exception {
        RpcInvokerHolder.serviceInvoke(req.getMethod(), req.getContent());
    }

    void write(RpcMsg.response rsp, RpcBaseServer server) throws Exception {
        Channel channel = server.getChannel();

        channel.writeAndFlush(rsp);
    }
}
