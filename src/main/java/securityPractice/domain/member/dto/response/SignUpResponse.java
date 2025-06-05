package securityPractice.domain.member.dto.response;

import lombok.Builder;

@Builder
public record SignUpResponse(
        String userName,
        String name,
        String password
) {}