package com.seeu.framework.discover;

import com.google.protobuf.ByteString;
import com.seeu.framework.rpc.RpcBaseClient;
import com.seeu.framework.rpc.RpcClientHandler;
import com.seeu.framework.rpc.RpcMsg;
import com.seeu.framework.rpc.RpcMsg.ServerType;
import com.seeu.framework.utils.FuncUtil;
import com.seeu.proto.Discover;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DiscoverClient extends RpcBaseClient {

    @Autowired
    RpcClientHandler handler;

    @Autowired
    ServerInfo serverInfo;

    @Value("${discover.host}")
    String host;

    @Value("${discover.port}")
    int port;

    @Value("${service.host}")
    String serviceHost;

    @Value("${service.port}")
    int servicePort;


    private static final Logger logger = LoggerFactory.getLogger(DiscoverClient.class);

    private RpcMsg.request.Builder generateDiscoverMsg(String method, ByteString content, boolean needResponse) {
        return RpcMsg.request.newBuilder()
            .setContent(content)
            .setMsgSeq(FuncUtil.GenerateMsgSeq(serverInfo.getServerType(), serverInfo.getServerId(),
                serverInfo.getStartInstant()))
            .setMethod(method)
            .setSvrType(serverInfo.getServerType())
            .setSvrId(serverInfo.getServerId())
            .setNeedResponse(needResponse);
    }

    @PostConstruct
    public void start() {
        super.connect(host, port, handler);
        Discover.register register = Discover.register.newBuilder()
            .setSvrType(serverInfo.getServerType()).setSvrId(serverInfo.getServerId())
            .setHost(serviceHost).setPort(servicePort)
            .build();

        RpcMsg.request.Builder builder = generateDiscoverMsg("register", register.toByteString(), false);
        getChannel().writeAndFlush(builder);
    }

    @PreDestroy
    public void destory() {
        super.close();


        Discover.unregister unregister = Discover.unregister.newBuilder()
            .setSvrType(serverInfo.getServerType()).setSvrId(serverInfo.getServerId())
            .build();

        RpcMsg.request.Builder builder = generateDiscoverMsg("unregister", unregister.toByteString(), false);
        getChannel().writeAndFlush(builder);
    }

    public Discover.getServiceInfoResp getServiceInfo(ServerType serverType, int serverId) {
        Discover.getServiceInfoReq infoReq = Discover.getServiceInfoReq.newBuilder()
            .setSvrType(serverType).setSvrId(serverId).build();

        RpcMsg.request.Builder builder = generateDiscoverMsg("getServiceInfo", infoReq.toByteString(), true);
        try {
            RpcMsg.response resp = handler.write(builder.build(), this);
            if(null == resp) {
                logger.warn("get service info failed, type: {}, id: {}", serverType.toString(), serverId);
                return null;
            }

            return  Discover.getServiceInfoResp.parseFrom(resp.getContent());
        } catch (Exception e) {
            logger.error("catch an exception: ", e);
            return null;
        }
    }
}
