package com.seeu.discover;

import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.googlecode.protobuf.format.JsonFormat;
import com.seeu.framework.annotations.RpcMethod;
import com.seeu.framework.annotations.RpcService;
import com.seeu.framework.rpc.RpcMsg.ServerType;
import com.seeu.framework.rpc.RpcServerHandler;
import com.seeu.proto.Discover;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RpcService
public class DiscoverHandler {
    private Map<ServerType, Map<Integer, Discover.register>> serverTypeSetMap = new ConcurrentHashMap<>();

    @Autowired
    private RpcServerHandler handler;

    @RpcMethod(method = "register", proto = "Discover.register", type = ServerType.UNKNOWN)
    public MessageLite register(MessageLite message) {
        log.info("server register: {}", JsonFormat.printToString((Message) message));
        Discover.register register = (Discover.register) message;

        ServerType type = register.getSvrType();
        Map<Integer, Discover.register> serverMap = serverTypeSetMap
                .computeIfAbsent(type, k -> new ConcurrentHashMap<>());
        serverMap.put(register.getSvrId(), register);

        return null;
    }

    @RpcMethod(method = "unregister", proto = "Discover.unregister", type = ServerType.UNKNOWN)
    public MessageLite unregister(MessageLite message) {
        Discover.unregister unregister = (Discover.unregister) message;

        ServerType type = unregister.getSvrType();
        Map<Integer, Discover.register> serverMap = serverTypeSetMap.get(type);
        if (null == serverMap) {
            return null;
        }

        serverMap.remove(unregister.getSvrId());

        return null;
    }

    @RpcMethod(method = "getServiceInfo", proto = "Discover.getServiceInfoReq", type = ServerType.UNKNOWN)
    public MessageLite getServiceInfo(MessageLite message) {
        Discover.getServiceInfoReq infoReq = (Discover.getServiceInfoReq) message;

        Discover.getServiceInfoResp.Builder builder = Discover.getServiceInfoResp.newBuilder();

        Map<Integer, Discover.register> serverMap = serverTypeSetMap.get(infoReq.getSvrType());
        if (null == serverMap) {
            builder.setSvrType(ServerType.UNKNOWN);
        } else {
            builder.setSvrType(infoReq.getSvrType());

            Discover.register register = serverMap.get(infoReq.getSvrId());
            if (null == register) {
                register = serverMap.values().iterator().next();
            }

            if (null != register) {
                builder.setSvrId(infoReq.getSvrId());
                builder.setHost(register.getHost());
                builder.setPort(register.getPort());
            } else {
                builder.setSvrType(ServerType.UNKNOWN);
            }
        }

        return builder.build();
    }
}
