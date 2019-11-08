package com.seeu.framework.discover;

import com.seeu.framework.rpc.RpcMsg.ServerType;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class ServerInfo {

    ServerType serverType;
    Instant startInstant;
    int serverId;

    public ServerType getServerType() {
        return serverType;
    }

    public void setServerType(ServerType serverType) {
        this.serverType = serverType;
    }

    public int getServerId() {
        return serverId;
    }

    public Instant getStartInstant() {
        return startInstant;
    }

    public void init() {
        startInstant = Instant.now();
    }
}
