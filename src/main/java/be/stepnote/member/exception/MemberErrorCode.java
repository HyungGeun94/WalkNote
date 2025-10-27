package be.stepnote.member.exception;

import org.springframework.http.HttpStatus;

public enum MemberErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."),
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "해당 리소스에 접근할 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    MemberErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
