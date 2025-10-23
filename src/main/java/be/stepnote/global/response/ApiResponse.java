package be.stepnote.global.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {

    private boolean success;
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .code(HttpStatus.OK.value())
            .message("요청이 성공적으로 처리되었습니다.")
            .data(data)
            .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
            .success(true)
            .code(HttpStatus.OK.value())
            .message(message)
            .data(data)
            .build();
    }

    public static ApiResponse<?> error(HttpStatus status, String message) {
        return ApiResponse.builder()
            .success(false)
            .code(status.value())
            .message(message)
            .data(null)
            .build();
    }
}
