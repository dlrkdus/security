package securityPractice.global.exception;

import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException implements Supplier<CustomException> {
    private ErrorCode errorCode;
    private HttpStatus httpStatus;
    private String details;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
        this.details = null;
    }

    @Override
    public CustomException get() {
        return new CustomException(errorCode, httpStatus, details);
    }
}
