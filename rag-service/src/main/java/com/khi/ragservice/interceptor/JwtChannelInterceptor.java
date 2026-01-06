package com.khi.ragservice.interceptor;

import com.khi.ragservice.client.UserClient;
import com.khi.ragservice.dto.UserInfo;
import com.khi.ragservice.util.JwtTokenProvider;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Slf4j
@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final UserClient userClient;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtChannelInterceptor(UserClient userClient, JwtTokenProvider jwtTokenProvider) {
        this.userClient = userClient;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Message<?> preSend(@NotNull Message<?> msg, @NotNull MessageChannel ch) {
        StompHeaderAccessor acc = MessageHeaderAccessor.getAccessor(msg, StompHeaderAccessor.class);

        if (acc == null) {
            log.warn("StompHeaderAccessor is null, wrapping message.");
            acc = StompHeaderAccessor.wrap(msg);
        }

        if (StompCommand.CONNECT.equals(acc.getCommand())) {
            log.info("CONNECT received");

            String authHeader = acc.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Authorization header required");
            }

            // JWT 파싱
            String token = authHeader.substring(7);
            String userId = jwtTokenProvider.getUserIdFromToken(token);

            // UserInfo 조회
            try {
                UserInfo user = userClient.getUserInfo(userId);
                log.info("👤 UserDetails 로드 완료 - username: {}", UserInfo.getNickname(user));
            } catch (Exception e) {
                log.error("⚠️ UserInfo 조회 실패: {}", userId, e);
                throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
            }

            Principal userPrincipal = () -> userId;
            acc.setUser(userPrincipal);

            log.info("✅ WebSocket 인증 완료");
            log.info("   - Principal name: {}", userPrincipal.getName());
            log.info("   - Session userId: {}", userId);

        } else if (StompCommand.DISCONNECT.equals(acc.getCommand())) {
            log.info("[WebSocket DISCONNECT] session={} user={}",
                    acc.getSessionId(),
                    acc.getUser().getName());
        }  else if (StompCommand.SUBSCRIBE.equals(acc.getCommand())) {
            log.info("[WebSocket SUBSCRIBE] session={} user={} dest={}",
                    acc.getSessionId(),
                    acc.getUser().getName(),
                    acc.getDestination());
        } else if (StompCommand.UNSUBSCRIBE.equals(acc.getCommand())) {
            log.info("📤 STOMP UNSUBSCRIBE: {}", acc.getDestination());
        }

        return msg;
    }
}
