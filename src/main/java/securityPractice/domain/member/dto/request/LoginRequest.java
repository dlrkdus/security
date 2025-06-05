package securityPractice.domain.member.dto.request;

public record LoginRequest(
        String username,
        String password
) {
}
