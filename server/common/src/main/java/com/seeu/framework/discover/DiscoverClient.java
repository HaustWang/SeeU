package com.seeu.framework.discover;

import com.google.protobuf.ByteString;
import com.googlecode.protobuf.format.JsonFormat;
import com.seeu.framework.rpc.RpcBaseClient;
import com.seeu.framework.rpc.RpcClientHandler;
import com.seeu.framework.rpc.RpcMsg;
import com.seeu.framework.rpc.RpcMsg.ServerType;
import com.seeu.framework.utils.FuncUtil;
import com.seeu.proto.Discover;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiscoverClient extends RpcBaseClient {

    private static final Logger logger = LoggerFactory.getLogger(DiscoverClient.class);

    @Autowired
    RpcClientHandler handler;
    @Autowired
    ServerInfo serverInfo;

    String serviceHost;
    int servicePort;

    private RpcMsg.request.Builder generateDiscoverMsg(String method, ByteString content,
        boolean needResponse) {
        return RpcMsg.request.newBuilder()
            .setContent(content)
            .setMsgSeq(FuncUtil.GenerateMsgSeq(serverInfo.getServerType(), serverInfo.getServerId(),
                serverInfo.getStartInstant()))
            .setMethod(method)
            .setSvrType(serverInfo.getServerType())
            .setSvrId(serverInfo.getServerId())
            .setNeedResponse(needResponse);
    }

    public void init(String serviceHost, int servicePort) {
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
    }

    public boolean connect(String host, int port) {
        if(!connect(host, port, handler)) {
            logger.error("connect to discover: {}:{} failed!", host, port);
            return false;
        }

        Discover.register register = Discover.register.newBuilder()
            .setSvrType(serverInfo.getServerType()).setSvrId(serverInfo.getServerId())
            .setHost(serviceHost).setPort(servicePort)
            .build();

        RpcMsg.request.Builder builder = generateDiscoverMsg("register", register.toByteString(),
            false);
        getChannel().writeAndFlush(builder);

        logger.info("connect to discover: {}:{}, register:{}", host, port, JsonFormat.printToString(builder.build()));
        return true;
    }

    public void destroy() {
        Discover.unregister unregister = Discover.unregister.newBuilder()
            .setSvrType(serverInfo.getServerType()).setSvrId(serverInfo.getServerId())
            .build();

        RpcMsg.request.Builder builder = generateDiscoverMsg("unregister",
            unregister.toByteString(), false);
        getChannel().writeAndFlush(builder);

        super.close();
    }

    public Discover.getServiceInfoResp getServiceInfo(ServerType serverType, int serverId) {
        Discover.getServiceInfoReq infoReq = Discover.getServiceInfoReq.newBuilder()
            .setSvrType(serverType).setSvrId(serverId).build();

        RpcMsg.request.Builder builder = generateDiscoverMsg("getServiceInfo",
            infoReq.toByteString(), true);
        try {
            RpcMsg.response resp = handler.write(builder.build(), this);
            if (null == resp) {
                logger.warn("get service info failed, type: {}, id: {}", serverType.toString(),
                    serverId);
                return null;
            }

            return Discover.getServiceInfoResp.parseFrom(resp.getContent());
        } catch (Exception e) {
            logger.error("catch an exception: ", e);
            return null;
        }
    }
}
