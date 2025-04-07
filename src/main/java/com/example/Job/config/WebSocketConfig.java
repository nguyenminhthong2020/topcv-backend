package com.example.Job.config;

import com.example.Job.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
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


import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* Breakdown of WebSocket Phases
 1. HTTP Handshake (Initial WebSocket connection request)

 2. CONNECT (STOMP command) → configureClientInboundChannel is executed here

 3. CONNECTED (Server acknowledges connection)

 4.SUBSCRIBE (Client subscribes to topics/queues)

 5.MESSAGE (Client sends/receives messages)

 6.DISCONNECT (Client closes the connection)

 For example in log:
- GET "/ws/info?t=1743051128215", parameters={masked}
- Mapped to org.springframework.web.socket.sockjs.support.SockJsHttpRequestHandler@44b99a5f
- GET http://localhost:8081/ws/info?t=1743051128215
- GET "/ws/553/yvfk1new/websocket", parameters={}
-  Mapped to org.springframework.web.socket.sockjs.support.SockJsHttpRequestHandler@44b99a5f
- GET http://localhost:8081/ws/553/yvfk1new/websocket
- Completed 101 SWITCHING_PROTOCOLS
0. New WebSocketServerSockJsSession[id=yvfk1new]
1. Decoded CONNECT {Authorization=[Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJaYWxvQGdtYWlsLmNvbSIsImV4cCI6MTc0MzUwNDEwMSwiaWF0IjoxNzQyODk5MzAxLCJ1c2VyIjp7ImlkIjoyLCJlbWFpbCI6IlphbG9AZ21haWwuY29tIiwibmFtZSI6IlRlY2ggU29sdXRpb25zIEx0ZC4iLCJyb2xlIjoiQ09NUEFOWSJ9fQ.u64WJSuschLUO0djN0MI7DGA3Y7aikut1NhCPwcqpw-bvnqXQNfle2ofRP4iuxgtKHGeEKfZr8GOqUyWBeZCkA], accept-version=[1.2,1.1,1.0], heart-beat=[10000,10000]} session=null
2. Processing CONNECT user=2 session=yvfk1new
3.  Encoding STOMP CONNECTED, headers={version=[1.2], heart-beat=[0,0], user-name=[2]}
4. Decoded SUBSCRIBE {id=[sub-0], destination=[/user/queue/messages]} session=null
5. Translated /user/queue/messages -> [/queue/messages-useryvfk1new]
6. Processing SUBSCRIBE destination=/queue/messages-useryvfk1new subscriptionId=sub-0 session=yvfk1new user=2 payload=byte[0]

*/
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
    private final JwtUtil jwtUtil;
    private final ConcurrentHashMap<String, String> activeSessions = new ConcurrentHashMap<>();
    public static String userPrivateMessagingDestination = "/queue/messages";
    public static String userPrivateNotificationDestination = "/queue/notifications";

    public WebSocketConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                // .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*")
//                .addInterceptors(new WebSocketAuthInterceptor()) // Thêm Interceptor để xác thực token
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setUserDestinationPrefix("/user");

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

                        String userId = jwtUtil.extractUserIdFromToken(token);

                        if(userId != null){
                            // If user already has an active session, reject the connection

                            if(activeSessions.containsKey(userId)){
                                log.info("New connection attempt denied for user: " + userId);
                                return null; // Drop the new connection attempt
                            }

                            // Store new session

                            String sessionId = accessor.getSessionId();
                            activeSessions.put(userId, sessionId);

                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                    userId,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            );
                            // this will set the user-name with the user_id in the accessor header
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

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();

        // Remove the user session when disconnected
        activeSessions.entrySet().removeIf(entry -> {
            if (entry.getValue().equals(sessionId)) {
                System.out.println("User " + entry.getKey() + " disconnected.");
                return true;
            }
            return false;
        });
    }

}
