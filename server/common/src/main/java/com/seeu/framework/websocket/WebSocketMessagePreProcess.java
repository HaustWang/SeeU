package com.seeu.framework.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class WebSocketMessagePreProcess extends MessageToMessageDecoder<WebSocketFrame> {
    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out)
            throws Exception {

        //只有二进制或者文本流继续下传
        //在数据前添加一个byte表示类型，1--文本流，2--二进制流
        if (frame instanceof TextWebSocketFrame || frame instanceof BinaryWebSocketFrame) {
            ByteBuf byteBuf = Unpooled.buffer(frame.content().readableBytes() + 1);
            byteBuf.writeByte(frame instanceof TextWebSocketFrame ? 1 : 2);
            byte[] by = new byte[frame.content().readableBytes()];
            frame.content().readBytes(by);
            byteBuf.writeBytes(by);
            out.add(byteBuf);
        } else if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
        }
    }
}
