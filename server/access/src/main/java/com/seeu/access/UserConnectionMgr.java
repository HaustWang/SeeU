package com.seeu.access;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;

@Slf4j
@Component
public class UserConnectionMgr implements Serializable {
    private static final long serialVersionUID = -6746333134606172819L;

    private HashMap<Long, AccessUserInfo> uid2InfoMap = new HashMap<>();
    private HashMap<String, AccessUserInfo> devId2InfoMap = new HashMap<>();
    private HashMap<String, AccessUserInfo> ctx2InfoMap = new HashMap<>();

//    @Bean
//    public UserConnectionMgr userConnectionMgrBean() {
//        return new UserConnectionMgr();
//    }

    public AccessUserInfo getUserInfoByUid(long uid) {
        return uid2InfoMap.get(uid);
    }

    public AccessUserInfo getUserInfoByDevId(String devId) {
        return devId2InfoMap.get(devId);
    }

    public AccessUserInfo getUserInfoByCtx(ChannelHandlerContext ctx) {
        return ctx2InfoMap.get(ctx.channel().remoteAddress().toString());
    }

    public AccessUserInfo addUserInfo(long uid, String devId, ChannelHandlerContext ctx) {
        AccessUserInfo info = null;
        if (uid != 0) {
            info = uid2InfoMap.get(uid);
        }

        if (info == null && devId != null && !devId.equals("")) {
            info = devId2InfoMap.get(devId);
        }

        if (null != info) {
            return info;
        }

        info = new AccessUserInfo();
        info.setUid(uid);
        info.setDevId(devId);
        info.setCtx(ctx);

        log.debug("add user info:" + info.toString());

        if (uid != 0)
            uid2InfoMap.put(uid, info);

        if (devId != null && !devId.equals(""))
            devId2InfoMap.put(devId, info);

        if (ctx != null && !ctx.isRemoved()) {
            ctx2InfoMap.put(ctx.channel().remoteAddress().toString(), info);
        }

        return info;
    }

    public void addUserInfoByUid(long uid, AccessUserInfo info) {
        uid2InfoMap.put(uid, info);
    }

    public void addUserInfoByDevId(String devId, AccessUserInfo info) {
        devId2InfoMap.put(devId, info);
    }

    public void addUserInfoByCtx(ChannelHandlerContext ctx, AccessUserInfo info) {
        info.setCtx(ctx);
        ctx2InfoMap.put(ctx.channel().remoteAddress().toString(), info);
    }

    public void updateUserInfoCtx(ChannelHandlerContext ctx, AccessUserInfo info) {
        ctx2InfoMap.remove(info.getCtx().channel().remoteAddress().toString());
        info.setCtx(ctx);
        info.setUid(0);
        ctx2InfoMap.put(ctx.channel().remoteAddress().toString(), info);
    }

    public AccessUserInfo delUserInfo(ChannelHandlerContext ctx) {
        AccessUserInfo info = ctx2InfoMap.remove(ctx.channel().remoteAddress().toString());
        if (null != info) {
            devId2InfoMap.remove(info.getDevId());
            uid2InfoMap.remove(info.getUid());
        }

        return info;
    }

    public void sendMsgByUid(long uid, String msgid, Object msg) {
        if (0 == uid) {
            broadcast(msgid, msg);
            return;
        }

        AccessUserInfo info = getUserInfoByUid(uid);
        if (null == info) {
            log.error("send msg to user uid: " + uid + " but cannot get user info!");
            return;
        }

        info.sendMsg(msgid, msg);
    }

    public void sendMsgByDevId(String devId, String msgid, Object msg) {
        if (devId.equals("0") || devId.isEmpty()) {
            broadcast(msgid, msg);
            return;
        }

        AccessUserInfo info = getUserInfoByDevId(devId);
        if (null == info) {
            log.error("send msg to user devId: " + devId + " but cannot get user info!");
            return;
        }

        info.sendMsg(msgid, msg);
    }

    public void broadcast(String msgid, Object msg) {
//		WebsocketMsg wMsg = new WebsocketMsg();
//		wMsg.setMsgid(msgid);
//		wMsg.setContent(msg);
//		for (AccessUserInfo info : uid2InfoMap.values()) {
//			info.sendMsg(wMsg);
//		}
    }
}
