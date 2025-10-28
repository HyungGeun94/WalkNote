package be.stepnote.global.exception;

import be.stepnote.global.response.ApiResponse;
import be.stepnote.member.exception.MemberErrorCode;
import be.stepnote.member.exception.MemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandle {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(error -> error.getDefaultMessage())
            .orElse("잘못된 요청입니다.");

        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) status = HttpStatus.BAD_REQUEST;

        // 3. ApiResponse 생성
        ApiResponse<?> body = ApiResponse.error(status, message);

        // 4. ResponseEntity로 래핑하여 상태 코드 명시
        return ResponseEntity
            .status(status)
            .body(body);
    }

    // 1) Member 도메인에서 던진 예외 처리
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ApiResponse<?>> handleMemberException(MemberException ex) {
        MemberErrorCode code = ex.getErrorCode();

        ApiResponse<?> body = ApiResponse.error(
            code.getHttpStatus(),          // 예: "EMAIL_DUPLICATE"
            code.getMessage()     // 예: "이미 사용 중인 이메일입니다."
        );

        return ResponseEntity
            .status(code.getHttpStatus())
            .body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntime(RuntimeException ex) {
        log.error("❌ 예외 발생", ex);  // 전체 스택 트레이스 출력

        ApiResponse<?> body = ApiResponse.error(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "서버 내부 오류가 발생했습니다."
        );

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(body);
    }


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleEmailDuplicate(EmailAlreadyExistsException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponse<?> body = ApiResponse.error(status, "이메일이 이미 존재합니다.");

        return ResponseEntity
            .status(status)
            .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error(" 예외 발생", e); // 전체 스택 트레이스 로깅

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponse<?> body = ApiResponse.error(status, "잘못된 요청입니다.");

        return ResponseEntity
            .status(status)
            .body(body);
    }


}
