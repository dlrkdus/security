package securityPractice.domain.user.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @NotBlank(message = "아이디는 필수입니다.")
        @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력해주세요.")
        String userName,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 6, max = 30, message = "비밀번호는 6자 이상 30자 이하로 입력해주세요.")
        String password
) {}