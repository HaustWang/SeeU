package com.haust.access;

import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AccessUserInfo implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(AccessUserInfo.class);
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

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getUid() {
		return uid;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getDevId() {
		return devId;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameSvrId(int gameSvrId) {
		this.gameSvrId = gameSvrId;
	}

	public int getGameSvrId() {
		return gameSvrId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public long getGameId() {
		return gameId;
	}

	public void setUserSvrId(int userSvrId) {
		this.userSvrId = userSvrId;
	}

	public int getUserSvrId() {
		return userSvrId;
	}

	public int getProxyId() {
		return proxyId;
	}

	public void setProxyId(int proxyId) {
		this.proxyId = proxyId;
	}

	public void sendMsg(Object msg) {
//		if (null == ctx) {
//			logger.error("send msg to user but ctx is null, uid:" + uid + ", devId:" + devId + ", msg:" + msg.toString());
//			return;
//		}
//
//		if (null != msg) {
//			logger.debug("send msg to client:" + msg.toString());
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
