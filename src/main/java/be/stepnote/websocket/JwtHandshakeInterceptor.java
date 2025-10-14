package be.stepnote.websocket;

import be.stepnote.config.security.JWTUtil;
import be.stepnote.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;



@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JWTUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
        ServerHttpResponse response,
        WebSocketHandler wsHandler,
        Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();

            // 1️⃣ URL 쿼리 파라미터로부터 token 추출
            String token = httpRequest.getParameter("token");
            if (token == null) {
                System.out.println("❌ WebSocket token is null");
                return false; // handshake 거부
            }

            // 2️⃣ 토큰 만료 여부 검증
            if (jwtUtil.isExpired(token)) {
                System.out.println("❌ WebSocket token expired");
                return false;
            }

            // 3️⃣ 토큰에서 사용자 정보 추출
            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);

            // 4️⃣ UserEntity 흉내 (JWT에서만 얻은 최소 정보)
            Member member = Member
                .builder()
                .username(username)
                .role(role)
                .password("temppasworddd")
                .nickname(UUID.randomUUID().toString())
                .build();

            // 5️⃣ WebSocket 세션 attributes에 저장
            // (이후 handleTextMessage()에서 session.getAttributes()로 꺼냄)
            attributes.put("member", member);

            System.out.println("✅ WebSocket handshake success for user: " + username);
            return true;
        }

        System.out.println("❌ WebSocket handshake failed (not servlet request)");
        return false;
    }





    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Exception exception) {

    }
}
