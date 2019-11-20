package com.seeu.framework.rpc;

import com.google.protobuf.MessageLite;
import com.seeu.framework.discover.DiscoverClient;
import com.seeu.framework.discover.ServerInfo;
import com.seeu.framework.rpc.RpcMsg.ServerType;
import com.seeu.framework.utils.FuncUtil;
import com.seeu.proto.Discover;
import io.netty.channel.Channel;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RpcClientManger {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientManger.class);

    @Autowired
    ServerInfo serverInfo;

    @Autowired
    DiscoverClient discoverClient;

    @Autowired
    RpcClientHandler clientHandler;

    Map<ServerType, Map<Integer, RpcBaseClient>> rpcClientMap = new HashMap<>();
    Map<String, RpcBaseClient> rpcChannelClientMap = new HashMap<>();

    public RpcMsg.request generateRpcRequest(String method, MessageLite msg,
        boolean needResponse) {
        RpcMsg.request.Builder builder = RpcMsg.request.newBuilder();
        builder.setNeedResponse(needResponse)
            .setContent(msg.toByteString())
            .setMethod(method)
            .setSvrType(serverInfo.getServerType())
            .setSvrId(serverInfo.getServerId())
            .setMsgSeq(FuncUtil.GenerateMsgSeq(serverInfo.getServerType(), serverInfo.getServerId(),
                serverInfo.getStartInstant()));
        return builder.build();
    }

    public void broadcast(String method, MessageLite msg) {
        RpcMsg.request request = generateRpcRequest(method, msg, false);
        for (Map<Integer, RpcBaseClient> clientMap : rpcClientMap.values()) {
            for (RpcBaseClient client : clientMap.values()) {
                try {
                    clientHandler.write(request, client);
                } catch (Exception e) {
                    logger.error("broadcast to {}:{] catch an exception: ", client.getHost(),
                        client.getPort(), e);
                }
            }
        }
    }

    public void broadcastByType(ServerType serverType, String method, MessageLite msg) {
        RpcMsg.request request = generateRpcRequest(method, msg, false);
        Map<Integer, RpcBaseClient> clientMap = rpcClientMap
            .computeIfAbsent(serverType, k -> new HashMap<>());

        for (RpcBaseClient client : clientMap.values()) {
            try {
                clientHandler.write(request, client);
            } catch (Exception e) {
                logger.error("broadcast to {}:{] catch an exception: ", client.getHost(),
                    client.getPort(), e);
            }
        }
    }

    public RpcMsg.response request(ServerType serverType, int serverId, String method,
        MessageLite msg, boolean needResponse) {
        Map<Integer, RpcBaseClient> clientMap = rpcClientMap
            .computeIfAbsent(serverType, k -> new HashMap<>());
        RpcBaseClient client = clientMap.get(serverId);
        if (null == client) {
            //获取client信息，并建立建立连接
            Discover.getServiceInfoResp infoResp = discoverClient
                .getServiceInfo(serverType, serverId);
            if (null == infoResp) {
                logger.error("can't get any server, serverType: {}, serverId: {}", serverType,
                    serverId);
                return null;
            }

            if (infoResp.getSvrId() != serverId) {
                logger.warn("get server {} and id {} but return id is {}", serverType, serverId,
                    infoResp.getSvrId());
            }

            client = clientMap.get(infoResp.getSvrId());
            if (null != client) {
                client = new RpcBaseClient();
                client.connect(infoResp.getHost(), infoResp.getPort(), clientHandler);

                clientMap.put(serverId, client);

                rpcChannelClientMap.put(client.getChannel().toString(), client);
            }
        }

        RpcMsg.request request = generateRpcRequest(method, msg, needResponse);
        try {
            return clientHandler.write(request, client);
        } catch (Exception e) {
            logger.error(
                "broadcast to servertype: {}, serverid: {}, address: {}:{] catch an exception: ",
                serverType, serverId, client.getHost(), client.getPort(), e);
            return null;
        }
    }

    public RpcMsg.response request(ServerType serverType, int serverId, String method,
        MessageLite msg) {
        return request(serverType, serverId, method, msg, true);
    }

    public void push(ServerType serverType, int serverId, String method,
        MessageLite msg) {
        request(serverType, serverId, method, msg, false);
    }

    private String getChannelKey(Channel channel) {
        return channel.id().toString();
    }

    public void addClient(RpcBaseClient client) {
        Map<Integer, RpcBaseClient> clientMap = rpcClientMap
            .computeIfAbsent(client.getTargetType(), k -> new HashMap<>());
        clientMap.put(client.getTargetId(), client);

        rpcChannelClientMap.put(getChannelKey(client.getChannel()), client);
    }

    public RpcBaseClient removeClient(RpcBaseClient client) {
        Map<Integer, RpcBaseClient> clientMap = rpcClientMap.get(client.getTargetType());
        if(null != clientMap) {
            clientMap.remove(client.getTargetId());
        }

        return rpcChannelClientMap.remove(getChannelKey(client.getChannel()));
    }

    public RpcBaseClient removeClient(Channel channel) {
        RpcBaseClient client = rpcChannelClientMap.remove(getChannelKey(channel));
        if(null != client) {
            Map<Integer, RpcBaseClient> clientMap = rpcClientMap.get(client.getTargetType());
            if(null != clientMap) {
                clientMap.remove(client.getTargetId());
            }
        }

        return client;
    }
}
