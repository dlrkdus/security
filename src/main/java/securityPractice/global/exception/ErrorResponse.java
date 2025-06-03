package securityPractice.global.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private String message;
    private int statusCode;
    private String detail;

    public ErrorResponse(ErrorCode code, String detail) {
        this.errorCode = code.errorCode();
        this.message = code.message();
        this.statusCode = code.statusCode();
        this.detail = detail;
    }

    public static ErrorResponse of(ErrorCode code, String detail) {
        return new ErrorResponse(code,detail);
    }

    public static ErrorResponse of(CustomException exception) {
        return new ErrorResponse(
                exception.getErrorCode(),
                exception.getDetails()
        );
    }
}
