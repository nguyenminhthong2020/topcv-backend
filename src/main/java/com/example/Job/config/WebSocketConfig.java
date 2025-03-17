package com.example.Job.config;

import com.example.Job.security.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.example.Job.interceptors.WebSocketAuthInterceptor;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private JwtTokenProvider jwtTokenProvider;

    public WebSocketConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                // .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new WebSocketAuthInterceptor()) // Thêm Interceptor để xác thực token
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // register an Interceptor for validate websocket channel connection because can’t send Authorization headers in the handshake phase
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if(StompCommand.CONNECT.equals(accessor.getCommand())){
                    String authToken = accessor.getFirstNativeHeader("Authorization");

                    if(authToken != null && authToken.startsWith("Bearer ")){
                        String token = authToken.substring(7);

                        String userId = jwtTokenProvider.extractUserIdFromToken(token);

                        if(userId != null){
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                    userId,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            );
                            accessor.setUser(authenticationToken);
                        }else {
                            throw new RuntimeException("Invalid token");
                        }
                    }
                }

                return message;
            }
        });
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration
                // .setTimeToFirstMessage(30_000) // 30s timeout nếu không nhận được tin nhắn
                // đầu tiên
                .setSendTimeLimit(20 * 60 * 1000); // 20 phút không hoạt động thì đóng kết nối
    }

}
