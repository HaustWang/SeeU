@echo off

for /r .\ %%i in (*.proto) do (
	protoc --java_out=..\server\common\src\main\java --proto_path=.\ %%~nxi
)