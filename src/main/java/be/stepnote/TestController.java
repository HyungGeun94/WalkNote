package be.stepnote;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Test", description = "프론트 통신 test")
public class TestController {

    @Operation(summary = " get Test", description = "get요청을 하면 test 문자가 (text/plain) 반환됩니다")
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @Operation(summary = " post Test", description = "name과 age를 담아 요청을 하면 id :1(고정), name, age가 반환됩니다.")
    @PostMapping("/userTest")
    public TestUser testUser(@RequestBody TestUserForm testUserForm) {

        return new TestUser(testUserForm.getName(), testUserForm.getAge());

    }



    @Data
    public static class TestUser {

        Integer id=1;
        String name;
        Integer age;

        public TestUser(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }

    @Data
    public static class TestUserForm {

        String name;
        Integer age;
        public TestUserForm(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

    }
}
