package com.seeu.framework.websocket;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
public class WebsocketMsgDecoder extends ProtobufDecoder {
    private Logger logger = LoggerFactory.getLogger(WebsocketMsgDecoder.class);

    public WebsocketMsgDecoder(MessageLite prototype) {
        super(prototype, (ExtensionRegistry)null);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte type = msg.readByte();
        if(type == 1) {
            byte[] by = new byte[msg.readableBytes()];
            msg.readBytes(by);
            out.add(new String(by));
        } else if(type == 2){
            super.decode(ctx, Unpooled.copiedBuffer(msg), out);
        }
    }
}
