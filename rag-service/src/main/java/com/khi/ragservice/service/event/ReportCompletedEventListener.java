package com.khi.ragservice.service.event;

import com.khi.ragservice.dto.ReportCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportCompletedEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void onReportCompleted(ReportCompletedEvent event) {
        log.info("[RAG WS] 리포트: {} 분석 완료 이벤트 확인", event.getReportId());

        Map<String, Object> payload = Map.of(
          "type","REPORT_COMPLETED",
          "reportId", event.getReportId()
        );

        // user 중복 방지
        Set<String> targets = Set.of(
                event.getRequestUserId1(),
                event.getRequestUserId2()
        );

        for (String userId : targets) {
            log.info("[RAG WS] 수신 userId: {}", userId);

            messagingTemplate.convertAndSendToUser(
                    userId,
                    "/queue/notify",
                    payload
            );
        }
    }
}
