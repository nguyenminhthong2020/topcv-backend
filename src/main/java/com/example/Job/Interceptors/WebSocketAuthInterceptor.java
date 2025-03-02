package com.example.Job.interceptors;

import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    @Value("${app.jwt-secret}") // Lấy secret từ application.properties
    private String secretKey;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            // the request doesn't have Authorization header so this will always be null
            String authHeader = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);


            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // Bỏ "Bearer "
                if (validateToken(token)) {
                    attributes.put("user", getUsernameFromToken(token));
                    return true;
                }
            }
        }
        // don't reject in the handshake phrase, because we don't have AUTHORIZATION in header
        return true; // Từ chối nếu token không hợp lệ
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {
        // Không cần xử lý gì ở đây
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
