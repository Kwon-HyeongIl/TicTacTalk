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
import org.springframework.stereotype.Component;

import java.security.Principal;

@Slf4j
@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(@NotNull Message<?> msg, @NotNull MessageChannel ch) {
        StompHeaderAccessor acc = StompHeaderAccessor.wrap(msg);

        if (StompCommand.SUBSCRIBE.equals(acc.getCommand()) && acc.getUser() == null) {
            throw new IllegalStateException("Unauthenticated SUBSCRIBE");
        }

        Principal user = acc.getUser();
        String userName = (user != null) ? user.getName() : "anonymous";

        if (StompCommand.CONNECT.equals(acc.getCommand())) {
            log.info("[WS CONNECT] session={} user={}",
                    acc.getSessionId(),
                    userName);
        } else if (StompCommand.DISCONNECT.equals(acc.getCommand())) {
            log.info("[WebSocket DISCONNECT] session={} user={}",
                    acc.getSessionId(),
                    userName);
        }  else if (StompCommand.SUBSCRIBE.equals(acc.getCommand())) {
            log.info("[WebSocket SUBSCRIBE] session={} user={} dest={}",
                    acc.getSessionId(),
                    userName,
                    acc.getDestination());
        } else if (StompCommand.UNSUBSCRIBE.equals(acc.getCommand())) {
            log.info("📤 STOMP UNSUBSCRIBE: {}", acc.getDestination());
        }

        return msg;
    }
}
