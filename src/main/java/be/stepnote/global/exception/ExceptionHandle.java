package be.stepnote.global.exception;

import be.stepnote.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandle {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(error -> error.getDefaultMessage())
            .orElse("잘못된 요청입니다.");

        return ApiResponse.error(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ApiResponse<?> handleEmailDuplicate(EmailAlreadyExistsException e) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, "이메일이 이미존재합니다");
    }

    @ExceptionHandler
    public ApiResponse<?> handleException(Exception e) {
        log.error("❌ 예외 발생", e);  // 전체 스택 트레이스 출력
        return ApiResponse.error(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

    }


}
