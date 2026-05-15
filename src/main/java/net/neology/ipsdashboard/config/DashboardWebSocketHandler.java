package net.neology.ipsdashboard.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.neology.ipsdashboard.dto.DashboardStatsDto;
import net.neology.ipsdashboard.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class DashboardWebSocketHandler
        extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardWebSocketHandler.class);

    private final SessionRegistry sessionRegistry;
    private final DashboardService dashboardService;

    private final ObjectMapper objectMapper;

    public DashboardWebSocketHandler(
            SessionRegistry sessionRegistry,
            DashboardService dashboardService,
            ObjectMapper objectMapper) {

        this.sessionRegistry = sessionRegistry;
        this.dashboardService = dashboardService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOGGER.info("WebSocket connected: {}", session.getId());
        sessionRegistry.add(session);
        try {
            DashboardStatsDto stats = dashboardService.getDashboardStats();
            if (stats != null) {
                String payload =objectMapper.writeValueAsString(stats);
                session.sendMessage(new TextMessage(payload));
                LOGGER.info("Sent cached dashboard to session {}", session.getId());
            } else {
                LOGGER.warn("No cached dashboard available yet");
            }
        } catch (Exception e) {
            LOGGER.error("Error sending cached dashboard to session", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        LOGGER.info("WebSocket closed: {}", session.getId());
        sessionRegistry.remove(session);
    }

    public void broadcast() {
        int sessionCount = sessionRegistry.getAll().size();
        LOGGER.info("Broadcasting cached dashboard, session count: {}", sessionCount);
        DashboardStatsDto stats = dashboardService.getDashboardStats();
        if (stats == null) {
            LOGGER.info("Broadcast skipped: cached stats is null");
            return;
        }
        String payload;
        try {
            payload = objectMapper.writeValueAsString(stats);
        } catch (Exception e) {
            LOGGER.error("Error serializing dashboard stats", e);
            return;
        }
        for (WebSocketSession session : sessionRegistry.getAll()) {
            try {
                session.sendMessage(new TextMessage(payload));
            } catch (Exception e) {
                LOGGER.warn("Failed to send message to session {}", session.getId());
            }
        }
    }
}