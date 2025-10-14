package be.stepnote.websocket;

import be.stepnote.location.LocationService;
import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import be.stepnote.walk.WalkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
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

    private final WalkService walkService;

    private final LocationService locationService;

    private final MemberRepository memberRepository;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SimpleWebSocketHandler(objectMapper,walkService,locationService,
                memberRepository), "/ws")
            .addInterceptors(jwtHandshakeInterceptor) // ✅ 별도 등록
            .setAllowedOrigins("*");

        // TODO [운영 시 변경]
        // - 특정 프론트 서버만 허용하도록 Origin 제한 필요
        // - wss:// 환경 전환 시 Nginx 프록시 설정 필요
    }

    // 내부 핸들러 정의
    private static class SimpleWebSocketHandler extends TextWebSocketHandler {

        private final ObjectMapper objectMapper;
        private final WalkService walkService;
        private final LocationService locationService;
        private final MemberRepository memberRepository;

        public SimpleWebSocketHandler(ObjectMapper objectMapper,WalkService walkService,
            LocationService locationService, MemberRepository memberRepository) {
            this.objectMapper = objectMapper;
            this.walkService = walkService;
            this.locationService = locationService;
            this.memberRepository = memberRepository;
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) {
            log.info("✅ 연결됨: {}", session.getId());

        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {


            Member member =  (Member)session.getAttributes().get("member");

            log.info("hihihii   " + member.getUsername() + " " + member.getRole());

            WebSocketRequestDTO webSocketMessageDTO = objectMapper.readValue(message.getPayload(),
                WebSocketRequestDTO.class);

            log.info("hihihii   " + webSocketMessageDTO);

            Member findMember = memberRepository.findByUsername(member.getUsername()).orElseThrow();

            // 모든 좌표는 기록 (actionType 관계없이)
            locationService.save(findMember, webSocketMessageDTO.getLatitude(), webSocketMessageDTO.getLongitude());

            // WalkActionType 에 따라 분기
            switch (webSocketMessageDTO.getActionType()) {
                case START_WALK -> walkService.startWalk(findMember, webSocketMessageDTO);
                case RECORD_LOCATION -> walkService.recordWalk(findMember, webSocketMessageDTO);
                case STOP_WALK -> walkService.stopWalk(findMember, webSocketMessageDTO);
                case NONE -> { /* 단순 위치 업데이트만 */ }
            }

//            UserPrivacySetting setting = privacySettingService.getOrDefault(user);
//
//            if (setting.isShowUsers()) {
//                users = userService.findNearby(dto.getLatitude(), dto.getLongitude());
//            }
//            if (setting.isShowPosts()) {
//                posts = postService.findNearby(dto.getLatitude(), dto.getLongitude());
//            }


            // 주변 정보 응답
//            WebSocketResponse response = nearbyService.buildNearbyResponse(user, dto);
//            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));


            // TODO [비즈니스 로직]
            // - 서비스 계층으로 던져서 DB/Redis에 위치 저장
            // - executorService.submit()으로 비동기 처리

            WebSocketResponse webSocketResponse = new WebSocketResponse(new ArrayList<>(),
                new ArrayList<>());

            String payload = "프론트에서 받아야 할 정보는 주변 사용자 정보 리스트 + 마킹글 리스트 ";
            log.info("📩 받은 메시지: {}", payload);

            String response = "서버 응답: " + payload;
            session.sendMessage(new TextMessage(response));

            log.info("📤 보낸 메시지: {}", response);

            // TODO [에러 처리]
            // - JSON 파싱 실패, null 필드 등 Validation 처리
            // - Exception 발생 시 session.sendMessage(new TextMessage("ERROR: ..."));
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
            log.info("❌ 연결 종료: {}", session.getId());
        }
    }
}