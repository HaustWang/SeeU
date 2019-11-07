@echo off

for /r .\ %%i in (*.proto) do (
	echo "%%~nxi => %%~ni_pb.js"
	protoc --js_out=import_style=commonjs,binary:..\client\assets\proto\ --proto_path=.\ %%~nxi
)

node .\replace.js start

pause