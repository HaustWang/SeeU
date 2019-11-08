package com.seeu.framework.rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RpcBaseServer {
    private final static Logger logger = LoggerFactory.getLogger(RpcBaseServer.class);

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public ChannelFuture start(String host, int port, final RpcServerHandler handler) {
        ChannelFuture f = null;
        InetSocketAddress address = new InetSocketAddress(host, port);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new IdleStateHandler(35, 0, 0, TimeUnit.SECONDS));
                        socketChannel.pipeline().addLast(new ProtobufDecoder(RpcMsg.request.getDefaultInstance()));
                        socketChannel.pipeline().addLast(new ProtobufEncoder());

                        socketChannel.pipeline().addLast(handler);
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            f = b.bind(address).syncUninterruptibly();
            channel = f.channel();
        } catch (Exception e) {
            logger.error("Rpc server start error:", e);
        } finally {
            if (f != null && f.isSuccess()) {
                logger.info("Rpc server listening {} on port {} ready for connections...", address.getHostName(), address.getPort());
            } else {
                logger.error("Rpc server start up error!");
            }
        }

        return f;
    }

    public void close() {
        logger.info("Shutdown Rpc server...");
        if (channel != null) {
            channel.close();
        }

        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        logger.info("Shutdown Rpc server success!");
    }
}
