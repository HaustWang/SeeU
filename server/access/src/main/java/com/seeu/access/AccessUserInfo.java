package com.seeu.access;

import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
public class AccessUserInfo implements Serializable {
    private static final long serialVersionUID = 9016749822339221016L;

    private long uid = 0;
    private String devId;
    private transient ChannelHandlerContext ctx;
    private String gameType;
    private int gameSvrId;
    private long gameId;
    private int userSvrId;
    private int proxyId;
    private transient Gson gson = new Gson();

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public int getGameSvrId() {
        return gameSvrId;
    }

    public void setGameSvrId(int gameSvrId) {
        this.gameSvrId = gameSvrId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public int getUserSvrId() {
        return userSvrId;
    }

    public void setUserSvrId(int userSvrId) {
        this.userSvrId = userSvrId;
    }

    public int getProxyId() {
        return proxyId;
    }

    public void setProxyId(int proxyId) {
        this.proxyId = proxyId;
    }

    public void sendMsg(Object msg) {
//		if (null == ctx) {
//			log.error("send msg to user but ctx is null, uid:" + uid + ", devId:" + devId + ", msg:" + msg.toString());
//			return;
//		}
//
//		if (null != msg) {
//			log.debug("send msg to client:" + msg.toString());
//
//			if (msg instanceof String) {
//				ctx.writeAndFlush(new TextWebSocketFrame((String) msg));
//			} else {
//				ctx.writeAndFlush(new TextWebSocketFrame(gson.toJson(msg)));
//			}
//		}
    }

    public void sendMsg(String msgid, Object msg) {
//		WebsocketMsg wMsg = new WebsocketMsg();
//		wMsg.setMsgid(msgid);
//		if (null != msg) {
//			wMsg.setContent(msg);
//		}
//
//		sendMsg(wMsg);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "@" + uid + "#"
                + devId + "#" + ctx.toString() + "#"
                + gameType + "#" + gameSvrId + "#" + userSvrId;
    }
}
