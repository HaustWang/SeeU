package com.seeu.framework.websocket;

import com.google.protobuf.MessageLite;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class WebsocketHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 判断是否关闭链路的指令
        if (msg instanceof String) {
            handleJsonMessage(ctx, (String) msg);
            return;
        }

        if (msg instanceof MessageLite) {
            handleProtobufMessage(ctx, (MessageLite) msg);
        }
    }

    /**
     * channel 通道 Read 读取 Complete 完成 在通道读取完成后会在这个方法里通知，对应可以做刷新操作 ctx.flush()
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof HandshakeComplete) {
            recordHttpUsefulHeaders(ctx, ((HandshakeComplete) evt).requestHeaders());
        }
    }

    /**
     * exception 异常 Caught 抓住 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("catch an exception: ", cause);
        ctx.close();
    }

    /**
     * 处理消息
     *
     * @param ctx 当前连接的上下文
     * @param msg websocket消息内容
     */
    public abstract void handleJsonMessage(ChannelHandlerContext ctx, String msg);

    /**
     * 处理protobuf消息
     *
     * @param ctx 当前连接的上下文
     * @param msg websocket消息内容
     */
    public abstract void handleProtobufMessage(ChannelHandlerContext ctx, MessageLite msg);

    public abstract void recordHttpUsefulHeaders(ChannelHandlerContext ctx,
                                                 HttpHeaders httpHeaders);
}
