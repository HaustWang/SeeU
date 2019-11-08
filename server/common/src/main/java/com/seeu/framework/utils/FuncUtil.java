package com.seeu.framework.utils;

import com.seeu.framework.rpc.RpcMsg.ServerType;
import java.time.Instant;

public class FuncUtil {

    public static long getMilliSecsFrom(Instant instant) {
        Instant now = Instant.now();
        return now.toEpochMilli() - instant.toEpochMilli();
    }

    public static int GenerateMsgSeq(ServerType type, int serverId, Instant instant) {
        long inter = getMilliSecsFrom(instant);
        return (int)(inter & 0x00ffffff | type.getNumber() << 28 | serverId << 24);
    }
}
