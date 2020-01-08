package com.seeu.framework.websocket;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Sharable
public class WebsocketMsgDecoder extends ProtobufDecoder {
    public WebsocketMsgDecoder(MessageLite prototype) {
        super(prototype, null);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte type = msg.readByte();
        if (type == 1) {
            byte[] by = new byte[msg.readableBytes()];
            msg.readBytes(by);
            out.add(new String(by));
        } else if (type == 2) {
            super.decode(ctx, Unpooled.copiedBuffer(msg), out);
        }
    }
}
