package be.stepnote.websocket;

import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Configuration
@EnableWebSocket
@Slf4j
@RequiredArgsConstructor
public class WebSocketMessage implements WebSocketConfigurer {

    private final ObjectMapper objectMapper;

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    private final MemberRepository memberRepository;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SimpleWebSocketHandler(objectMapper, memberRepository), "/ws")
            .addInterceptors(jwtHandshakeInterceptor) // ✅ 별도 등록
            .setAllowedOrigins("*");

        // TODO [운영 시 변경]
        // - 특정 프론트 서버만 허용하도록 Origin 제한 필요
        // - wss:// 환경 전환 시 Nginx 프록시 설정 필요
    }

    // 내부 핸들러 정의
    private static class SimpleWebSocketHandler extends TextWebSocketHandler {

//        사용자 세션에 넣기
//        private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

        private final ObjectMapper objectMapper;
        private final MemberRepository memberRepository;

        public SimpleWebSocketHandler(ObjectMapper objectMapper,MemberRepository memberRepository) {
            this.objectMapper = objectMapper;
            this.memberRepository = memberRepository;
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) {
//            사용자 세션에 넣기
//            sessions.add(session);
            log.info("✅ 연결됨: {}", session.getId());

        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {


            Member member =  (Member)session.getAttributes().get("member");

            log.info("hihihii   " + member.getUsername() + " " + member.getRole());



            Member findMember = memberRepository.findByUsername(member.getUsername()).orElseThrow();

            String payload = message.getPayload() + " 지금 로그인중인 유저는 " + findMember.getUsername() + " " +findMember.getRole();
            log.info("📩 받은 메시지: {}", payload);

            String response = "서버 응답: " + payload;


            session.sendMessage(new TextMessage(response));

            log.info("📤 보낸 메시지: {}", response);

//            세션에 유지중인 사용자 모두에게 메시지 전달
//            for (WebSocketSession webSocketSession : sessions) {
//                webSocketSession.sendMessage(new TextMessage(response));
//            }

        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
//            연결종료될 때 제거
//            sessions.remove(session);
            log.info("❌ 연결 종료: {}", session.getId());
        }
    }
}