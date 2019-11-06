package com.seeu.access;

import com.seeu.framework.websocket.WebsocketServer;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccessServerInitializer {
	private static final Logger logger = LoggerFactory.getLogger(AccessServerInitializer.class);

	@Value("${websocket.port}")
	private int port;
	@Value("${server.wss.open}")
	private boolean openWss;
	@Value("${server.wss.certificate}")
	private String certificate;
	@Value("${server.wss.password}")
	private String password;

	@Autowired
	private WebsocketServer websocketServer;

	@Autowired
	private AccessWebsocketHandler websocketHandler;

	@PostConstruct
	public void start() {
		new Thread(() -> {
			logger.debug("AccessServerInitializer init {}, {}, {}, {}", port, openWss, certificate, password);
			websocketServer.start(port, websocketHandler, openWss, certificate, password);
		}).start();
	}
}
