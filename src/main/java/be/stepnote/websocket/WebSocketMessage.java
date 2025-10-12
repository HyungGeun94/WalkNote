package be.stepnote.websocket;

import be.stepnote.security.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SimpleWebSocketHandler(), "/ws")
            .addInterceptors(jwtHandshakeInterceptor) // ✅ 별도 등록
            .setAllowedOrigins("*");

        // TODO [운영 시 변경]
        // - 특정 프론트 서버만 허용하도록 Origin 제한 필요
        // - wss:// 환경 전환 시 Nginx 프록시 설정 필요
    }

    // 내부 핸들러 정의
    private static class SimpleWebSocketHandler extends TextWebSocketHandler {
        @Override
        public void afterConnectionEstablished(WebSocketSession session) {
            log.info("✅ 연결됨: {}", session.getId());

        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {


            UserEntity user =  (UserEntity)session.getAttributes().get("user");

            log.info("hihihii   " + user.getUsername() + " " + user.getRole());



            // TODO [역직렬화]
            // - ObjectMapper를 사용해 JSON → DTO 변환
            // - LocationUpdateDto dto = objectMapper.readValue(payload, LocationUpdateDto.class);
            // - type 값에 따라 로직 분기 (location_update, ping, etc.)


            // TODO [비즈니스 로직]
            // - 서비스 계층으로 던져서 DB/Redis에 위치 저장
            // - executorService.submit()으로 비동기 처리
            /*

                반환값 -> 주변 댓글데이터 ,
                주변 1km 내의 저장된 1분 내에 사용자 정보?

               // 정훈님이 말씀하신 웹소켓 3개 열고 들어가는거
               // 내가 말한 웹소켓1개에 반환값 다 넣는거 어떤ㄱ ㅔ 좋을지 고려해보기.

            username,password ( 소셜로그인 )

            로그인 한 다음 엑세스,리프레시
            http 통신은 헤더에 Authorization -> Bearer
            ws 통신은 http request -> 파라미터
            http 먼저 되는거 확인하고 시큐리티(보안)


            {
                "type": "location_update",
                "userId": 123,
                "lat": 37.5012,
                "lon": 127.0256,
                "timestamp": 1696926000
            }
             */

            String payload = message.getPayload() + " 지금 로그인중인 유저는 " + user.getUsername() + " " +user.getRole();
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