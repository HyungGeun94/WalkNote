package be.stepnote.member.exception;

public class MemberException extends RuntimeException {

    private final MemberErrorCode errorCode;

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode.getMessage()); // RuntimeException의 message에도 넣어둔다 (로그용)
        this.errorCode = errorCode;
    }

    public MemberErrorCode getErrorCode() {
        return errorCode;
    }
}