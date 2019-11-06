package com.haust.framework.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketMessagePreProcess extends MessageToMessageDecoder<WebSocketFrame> {

    Logger logger = LoggerFactory.getLogger(WebSocketMessagePreProcess.class);

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
