@echo off

:: ????proto???????��????????????????????%proto%_pb.js??
:: ???��access.proto?????access_pb.js
:: ????js????????��??????????????????��???????????????��???????com.leyu.message?�I?com.leyu.message.access
:: ?????????????????��??????��???access.proto??home.proto???????��???????????��???
:: ????let message = new com.leyu.message.access.xxx????let message = new com.leyu.message.home.xxx
:: setlocal enabledelayedexpansion
:: for /r .\proto\ %%i in (*.proto) do (
::	echo "%%~nxi => %%~ni_pb.js"
::	tools\protoc --js_out=import_style=commonjs,binary:.\assets\proto\ --proto_path=.\proto\ %%~nxi
	:: ??????????�I???????��? !== ???? == ???????????????????
	:: for /f "tokens=*" %%x in (.\assets\proto\%%~ni_pb.js) do (
	::for /f "delims=" %%x in (.\assets\proto\%%~ni_pb.js) do (
	::	echo %%x
	::	set "var=%%x"
	::	if not !var!.==. (
	::		echo !var!
	::		set "var=!var:com.leyu.message=com.leyu.message.%%~ni!"
	::		echo !var!
	::		echo !var!!
	::		echo !var!!>>.\assets\proto\%%~ni_pb_new.js
	::	)
	::)
	
	::ren .\assets\proto\%%~ni_pb.js %%~ni_pb_bak.js
	::rename .\assets\proto\%%~ni_pb_new.js %%~ni_pb.js
::)

for /r .\ %%i in (*.proto) do (
	echo "%%~nxi => %%~ni_pb.js"
	protoc --js_out=import_style=commonjs,binary:..\client\assets\proto\ --proto_path=.\ %%~nxi
)

node .\replace.js start

pause