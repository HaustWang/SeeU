package com.seeu.framework.discover;

import com.seeu.framework.rpc.RpcBaseClient;
import com.seeu.framework.rpc.RpcClientHandler;
import com.seeu.framework.rpc.RpcMsg;
import com.seeu.framework.utils.FuncUtil;
import com.seeu.proto.Discover;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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

    @PostConstruct
    public void start() {
        super.connect(host, port, handler);
        RpcMsg.request.Builder builder = RpcMsg.request.newBuilder();

        Discover.register register = Discover.register.newBuilder()
            .setSvrType(serverInfo.getServerType()).setSvrId(serverInfo.getServerId())
            .build();

        builder.setContent(register.toByteString())
            .setMsgSeq(FuncUtil.GenerateMsgSeq(serverInfo.getServerType(), serverInfo.getServerId(),
                serverInfo.getStartInstant()))
            .setMethod("register")
            .setSvrType(serverInfo.getServerType())
            .setSvrId(serverInfo.getServerId())
            .setNeedResponse(false);
        getChannel().writeAndFlush(builder);
    }

    @PreDestroy
    public void destory() {
        super.close();
        RpcMsg.request.Builder builder = RpcMsg.request.newBuilder();

        Discover.unregister unregister = Discover.unregister.newBuilder()
            .setSvrType(serverInfo.getServerType()).setSvrId(serverInfo.getServerId())
            .build();

        builder.setContent(unregister.toByteString())
            .setMsgSeq(FuncUtil.GenerateMsgSeq(serverInfo.getServerType(), serverInfo.getServerId(),
                serverInfo.getStartInstant()))
            .setMethod("unregister")
            .setSvrType(serverInfo.getServerType())
            .setSvrId(serverInfo.getServerId())
            .setNeedResponse(false);
        getChannel().writeAndFlush(builder);
    }
}
