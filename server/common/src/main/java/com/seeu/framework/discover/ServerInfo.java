package com.seeu.framework.discover;

import com.seeu.framework.rpc.RpcMsg.ServerType;
import java.time.Instant;
import javax.annotation.PostConstruct;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ServerInfo {

    ServerType serverType;
    Instant startInstant;
    int serverId;

    @PostConstruct
    public void init() {
        startInstant = Instant.now();
    }
}
