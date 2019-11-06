package com.seeu.access;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.protobuf.MessageLite;
import com.googlecode.protobuf.format.JsonFormat;
import com.seeu.framework.websocket.WebsocketHandler;
import com.seeu.framework.websocket.WsMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@Component
public class AccessWebsocketHandler extends WebsocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(AccessWebsocketHandler.class);
    private static Gson gson = new Gson();
    private static JsonParser parser = new JsonParser();

    private Map<String, List<String>> httpHeaderMap = new HashMap<>();

    @Autowired
    private UserConnectionMgr userConnectionMgr;

    public void recordHttpUsefulHeaders(ChannelHandlerContext ctx, HttpHeaders httpHeaders) {
        String real_ip = httpHeaders.get("X-Real-IP");
        logger.info("the connection recordHttpUsefulHeaders httpHeaders is {}; ctx is {}",
            httpHeaders,
            ctx.channel().remoteAddress().toString());
        if (null == real_ip) {
            logger.info("the connection is not by transfer! remote: {}",
                ctx.channel().remoteAddress().toString());
            return;
        }

        String agent = httpHeaders.get("User-Agent");

        List<String> list = new ArrayList<>(2);
        list.add(real_ip);
        list.add(agent);
        httpHeaderMap.put(ctx.channel().remoteAddress().toString(), list);
        logger.debug("httpHeaderMap info: {}", httpHeaderMap);
    }

    private String getIp(ChannelHandlerContext ctx) {
        SocketAddress address = ctx.channel().remoteAddress();
        List<String> list = httpHeaderMap.get(address.toString());
        logger.info("get ip and list is {}, ctx is {}", list, address);
        if (null == list) {
            return ((InetSocketAddress) address).getAddress().getHostAddress();
        } else {
            return list.get(0);
        }
    }

    public void handleJsonMessage(ChannelHandlerContext ctx, String msg) {
        try {
            //这里的二进制必须时JSON格式并且与WsMsg.wsMsg消息格式一致.
            logger.debug(
                "receive msg:" + msg + ", ctx: " + ctx.toString() + ", remoteAddress:" + ctx
                    .channel().remoteAddress().toString());

            WsMsg.wsMsg.Builder builder = WsMsg.wsMsg.newBuilder();
            JsonFormat.merge(msg, builder);
            handleProtobufMessage(ctx, builder.build());
        } catch (Exception e) {
            logger.error("catch an exception: msg：{}, cause: {}, exception:", e.getMessage(),
                e.getCause(), e);

        }
    }

    @Override
    public void handleProtobufMessage(ChannelHandlerContext ctx, MessageLite msg) {
        try {
            if (!(msg instanceof WsMsg.wsMsg)) {
                throw new Exception("the message is not instance of WsMsg.wsMsg");
            }

            WsMsg.wsMsg wsMsg = (WsMsg.wsMsg) msg;

            //消息处理
            logger.debug("handleProtobufMessage receive msg: " + JsonFormat.printToString(wsMsg));
        } catch (Exception e) {
            logger.error("catch an exception: msg：{}, cause: {}, exception:", e.getMessage(),
                e.getCause(), e);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("channelInactive, ctx:" + ctx.toString() + ", remoteAddress:" + ctx.channel()
            .remoteAddress().toString());
        AccessUserInfo info = userConnectionMgr.delUserInfo(ctx);
        if (null != info) {
            //通知具体业务用户短线,内容自己实现,可参照以下代码
//            proxyConnectionMgr
//                .sendMsg(ServerType.USER_SERVER, info.getUserSvrId(), info.getUid(),
//                    Message.USERDISCONNECT, null, info.getProxyId());
//            //proxyConnectionMgr.sendMsg(ServerConst.ServerType.HOME_SERVER, info.getUid(), Message.USERDISCONNECT, null);
//
//            if (info.getGameType() != null && info.getGameSvrId() != 0) {
//                proxyConnectionMgr.sendMsg(info.getGameType(), info.getGameSvrId(), info.getUid(),
//                    Message.USERDISCONNECT, null);
//            }
        }
    }
}
