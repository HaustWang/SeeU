package com.seeu.discover;

import com.google.protobuf.MessageLite;
import com.seeu.framework.rpc.RpcMsg.ServerType;
import com.seeu.framework.rpc.RpcServerHandler;
import com.seeu.proto.Discover;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiscoverHandler implements RegisterHandler {

    private Map<ServerType, Set<Integer>> serverTypeSetMap = new ConcurrentHashMap<>();

    @Autowired
    private RpcServerHandler handler;

    @Override
    public MessageLite register(MessageLite message) {
        Discover.register register = (Discover.register) message;

        ServerType type = register.getSvrType();
        Set<Integer> serverSet = serverTypeSetMap.computeIfAbsent(type, k -> new HashSet<>());
        serverSet.add(register.getSvrId());

        return null;
    }

    @Override
    public MessageLite unregister(MessageLite message) {
        Discover.unregister unregister = (Discover.unregister) message;

        ServerType type = unregister.getSvrType();
        Set<Integer> serverSet = serverTypeSetMap.get(type);
        if(null == serverSet) {
            return null;
        }

        serverSet.remove(unregister.getSvrId());

        return null;
    }
}
