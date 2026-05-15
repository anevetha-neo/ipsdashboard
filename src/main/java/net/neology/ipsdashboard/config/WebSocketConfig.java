package net.neology.ipsdashboard.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);

    private final DashboardWebSocketHandler handler;

    public WebSocketConfig(DashboardWebSocketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        LOGGER.info("Registering WebSocketHandlers");
        registry.addHandler(handler, "/ips-ws").setAllowedOrigins("*");
    }
}