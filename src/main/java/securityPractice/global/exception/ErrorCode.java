package securityPractice.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    USER_NAME_DUPLICATE(HttpStatus.CONFLICT,40900, "USER001","이미 존재하는 아이디입니다.");
    private final HttpStatus httpStatus;
    private final int statusCode;
    private final String errorCode;
    private final String message;

    public int statusCode() {return this.statusCode;}

    public String errorCode() {
        return this.errorCode;
    }

    public String message() {
        return this.message;
    }
}
