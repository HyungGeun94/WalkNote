package be.stepnote.security;

import io.swagger.v3.oas.annotations.Operation;
import java.util.Collection;
import java.util.Iterator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class MainController {

    @GetMapping("/")
    public String mainP() {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        return "Main Controller : " + name + " " + role;
    }

    @Operation(summary = "웹소켓 연결 안내", description = "프론트엔드는 ws://localhost:8080/ws 로 연결합니다.")
    @GetMapping("/ws-info")
    public String websocketInfo() {
        return "Connect via ws://localhost:8080/ws";
    }
}
