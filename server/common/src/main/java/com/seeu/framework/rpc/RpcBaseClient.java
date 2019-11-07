package com.seeu.framework.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcBaseClient {

    private static final Logger logger = LoggerFactory.getLogger(RpcBaseClient.class);

    protected final EventLoopGroup workerGroup = new NioEventLoopGroup();
    protected RpcClientHandler handler;
    protected Bootstrap bootstrap;
    protected ChannelFuture future;
    protected Channel channel;
    protected String host;
    protected int port;

    public boolean isConnected() {
        return null != channel && channel.isActive();
    }

    public Channel getChannel() {
        return channel;
    }

    public boolean connect(String host, int port, final RpcClientHandler handler) {
        try {
            this.host = host;
            this.port = port;
            this.handler = handler;
            logger.debug("Netty client connect, address is " + host + ":" + port);
            bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline()
                        .addLast(new IdleStateHandler(0, 30, 0, TimeUnit.SECONDS));
                    socketChannel.pipeline()
                        .addLast(new ProtobufDecoder(RpcMsg.response.getDefaultInstance()));
                    socketChannel.pipeline().addLast(new ProtobufEncoder());

                    socketChannel.pipeline().addLast(handler);
                }
            });

            return doConnect();
        } catch (Exception e) {
            logger.error("catch an exception:", e);
            return false;
        }
    }

    public boolean doConnect() {
        if (channel != null && channel.isActive()) {
            return true;
        }

        try {
            logger.debug("Netty client do connect, address is " + host + ":" + port);
            InetSocketAddress address = new InetSocketAddress(host, port);
            future = bootstrap.connect(address).sync();
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        channel = channelFuture.channel();
                        logger.info("operationComplete {}", channel.remoteAddress().toString());

                    } else {
                        channelFuture.channel().eventLoop().schedule(new Runnable() {
                            @Override
                            public void run() {
                                doConnect();
                            }
                        }, 10, TimeUnit.SECONDS);
                    }
                }
            });
            return true;
        } catch (Exception e) {
            logger.error("catch an exception:", e);
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            service.schedule(() -> {
                doConnect();
                service.shutdown();
            }, 10, TimeUnit.SECONDS);
            return false;
        }
    }

    public void close() {
        try {
            future.channel().close();
            workerGroup.shutdownGracefully();
        } catch (Exception e) {
            logger.error("catch an exception:", e);
        }
    }
}
