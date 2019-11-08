package com.seeu.framework.discover;

import com.google.protobuf.MessageLite;
import com.seeu.framework.annotations.RpcCaller;
import com.seeu.framework.annotations.RpcMethod;
import com.seeu.framework.annotations.RpcService;
import com.seeu.framework.rpc.RpcMsg.ServerType;

@RpcCaller
public interface RegisterHandler {
    @RpcMethod(method = "register", proto = "Discover.register", type = ServerType.UNKNOWN)
    MessageLite register(MessageLite message);

    @RpcMethod(method = "unregister", proto = "Discover.unregister", type = ServerType.UNKNOWN)
    MessageLite unregister(MessageLite message);
}
