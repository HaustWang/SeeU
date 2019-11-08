@echo off

protoc --java_out=..\server\common\src\main\java --proto_path=.\ wsMsg.proto
protoc --java_out=..\server\common\src\main\java --proto_path=.\ rpcMsg.proto
protoc --java_out=..\server\common\src\main\java --proto_path=.\ discover.proto
