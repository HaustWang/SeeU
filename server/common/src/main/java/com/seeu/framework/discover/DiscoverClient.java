package com.seeu.framework.discover;

import com.google.protobuf.ByteString;
import com.googlecode.protobuf.format.JsonFormat;
import com.seeu.framework.rpc.RpcBaseClient;
import com.seeu.framework.rpc.RpcClientHandler;
import com.seeu.framework.rpc.RpcClientManger;
import com.seeu.framework.rpc.RpcMsg;
import com.seeu.framework.rpc.RpcMsg.ServerType;
import com.seeu.framework.utils.FuncUtil;
import com.seeu.proto.Discover;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DiscoverClient extends RpcBaseClient {
    @Autowired
    RpcClientHandler handler;
    @Autowired
    ServerInfo serverInfo;

    String serviceHost;
    int servicePort;

    @Autowired
    RpcClientManger rpcClientManger;

    private RpcMsg.request.Builder generateDiscoverMsg(String method, ByteString content,
                                                       boolean needResponse) {
        return RpcMsg.request.newBuilder()
                .setContent(content)
                .setMsgSeq(FuncUtil.GenerateMsgSeq(serverInfo.getServerType(), serverInfo.getServerId(),
                        serverInfo.getStartInstant()))
                .setMethod(method)
                .setSvrType(ServerType.UNKNOWN)
                .setSvrId(0)
                .setNeedResponse(needResponse);
    }

    public void init(String serviceHost, int servicePort) {
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
        this.setTargetId(0);
        this.setTargetType(ServerType.DISCOVER);
    }

    public boolean connect(String host, int port) {
        if (!connect(host, port, handler)) {
            log.error("connect to discover: {}:{} failed!", host, port);
            return false;
        }

        return true;
    }

    public void destroy() {
        try {
            Discover.unregister unregister = Discover.unregister.newBuilder()
                    .setSvrType(serverInfo.getServerType()).setSvrId(serverInfo.getServerId())
                    .build();

            RpcMsg.request.Builder builder = generateDiscoverMsg("unregister",
                    unregister.toByteString(), false);
            handler.write(builder, this);
        } catch (Exception e) {
            log.error("Catch an exception: ", e);
        }

        super.close();
        rpcClientManger.removeClient(this);
    }

    public Discover.getServiceInfoResp getServiceInfo(ServerType serverType, int serverId) {
        Discover.getServiceInfoReq infoReq = Discover.getServiceInfoReq.newBuilder()
                .setSvrType(serverType).setSvrId(serverId).build();

        RpcMsg.request.Builder builder = generateDiscoverMsg("getServiceInfo",
                infoReq.toByteString(), true);
        try {
            RpcMsg.response resp = handler.write(builder.build(), this);
            if (null == resp) {
                log.warn("get service info failed, type: {}, id: {}", serverType.toString(),
                        serverId);
                return null;
            }

            return Discover.getServiceInfoResp.parseFrom(resp.getContent());
        } catch (Exception e) {
            log.error("catch an exception: ", e);
            return null;
        }
    }

    @Override
    public void clientActive() {
        try {
            Discover.register register = Discover.register.newBuilder()
                    .setSvrType(serverInfo.getServerType()).setSvrId(serverInfo.getServerId())
                    .setHost(serviceHost).setPort(servicePort)
                    .build();

            RpcMsg.request.Builder builder = generateDiscoverMsg("register", register.toByteString(),
                    false);
            handler.write(builder, this);

            log.info("connect to discover: {}:{}, register:{}", host, port, JsonFormat.printToString(builder.build()));

            rpcClientManger.addClient(this);
        } catch (Exception e) {
            log.error("Catch an exception: ", e);
        }
    }

    @Override
    public void clientInactive() {
        this.connect(host, port);
    }
}
