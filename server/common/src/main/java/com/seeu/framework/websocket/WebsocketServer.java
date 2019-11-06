package com.seeu.framework.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.io.InputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WebsocketServer {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketServer.class);
    private static final String keyStoreType = "JKS";
    private static final String protocol = "TLS";
    private static WebsocketServer ourInstance = new WebsocketServer();

    public static WebsocketServer getInstance() {
        return ourInstance;
    }

    public void start(int port, final WebsocketHandler handler, final boolean isSecret,
        final String cerfile, final String password) {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 100);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    logger.debug("init websocket server:" + port + " handler:" + handler);

                    ChannelPipeline pipeline = channel.pipeline();

                    if (isSecret && cerfile != null && !cerfile.equals("")) {
                        //使用wss
                        KeyStore ks = KeyStore.getInstance(keyStoreType);
                        InputStream ksInputStream = this.getClass()
                            .getResourceAsStream("/" + cerfile);/// 证书存放地址
                        logger.debug("open jks file: {}, {}, {}", cerfile, password,
                            ksInputStream.available());
                        ks.load(ksInputStream,
                            password.toCharArray());        //KeyManagerFactory充当基于密钥内容源的密钥管理器的工厂。
                        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                            .getDefaultAlgorithm());//getDefaultAlgorithm:获取默认的 KeyManagerFactory 算法名称。
                        kmf.init(ks, password
                            .toCharArray());        //SSLContext的实例表示安全套接字协议的实现，它充当用于安全套接字工厂或 SSLEngine 的工厂。

                        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
                        trustManagerFactory.init(ks);

                        SSLContext sslContext = SSLContext.getInstance(protocol);
                        sslContext
                            .init(kmf.getKeyManagers(), trustManagerFactory.getTrustManagers(),
                                null);

                        SSLEngine engine = sslContext.createSSLEngine();
                        //这里setNeedClientAuth是否需要客户端验证，如果设置为真，则会报验证异常。
                        //setWantClientAuth客户端可以验证，也可以不验证
//					engine.setWantClientAuth(true);
                        engine.setNeedClientAuth(false);
                        engine.setUseClientMode(false);

                        logger.debug("key init {}, {}, {}, {}", kmf.getAlgorithm(),
                            sslContext.getProtocol(), engine.getEnabledProtocols(),
                            engine.getEnabledCipherSuites());

//                        pipeline.addLast("ssl", new SslHandler(engine));
                        pipeline.addLast(new SslHandler(engine));
                    }

                    // 设置30秒没有读到数据，则触发一个READER_IDLE事件。
                    // pipeline.addLast(new IdleStateHandler(30, 0, 0));
                    pipeline.addLast("http-codec", new HttpServerCodec());
                    pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                    pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                    pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(60 * 3));

                    pipeline.addLast(new WebSocketServerProtocolHandler("/", null, true));
                    pipeline.addLast(new WebSocketMessagePreProcess());

                    pipeline.addLast(new WebsocketMsgDecoder(WsMsg.wsMsg.getDefaultInstance()));

                    pipeline.addLast(handler);

                    pipeline.addLast(new ProtobufEncoder());
                }
            });

            Channel ch = bootstrap.bind(port).sync().channel();
            ch.closeFuture().sync();

        } catch (Exception e) {
            logger.error("catch an exception:", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
