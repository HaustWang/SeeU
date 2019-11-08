package com.seeu.discover;

import com.seeu.framework.rpc.RpcBaseServer;
import com.seeu.framework.rpc.RpcServerHandler;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DiscoverInitializer {

    @Value("${server.port}")
    private int port;

    @Autowired
    private RpcBaseServer server;

    @Autowired
    private RpcServerHandler handler;

    @PostConstruct
    public void init() {
        server.start("0.0.0.0", port, handler);
    }
}
