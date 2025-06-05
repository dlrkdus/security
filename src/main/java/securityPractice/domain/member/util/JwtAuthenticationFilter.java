package securityPractice.domain.member.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 	1.	사용자가 JWT Access Token 을 Authorization 헤더에 담아 요청
     * 	2.	필터가 그 헤더에서 토큰 추출
     * 	3.	토큰을 검증 (서명 확인, 만료 여부 등)
     * 	4.	토큰에 담긴 사용자 정보를 기반으로 Authentication 객체 생성
     * 	5.	SecurityContext 에 저장함으로써 인증 완료
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. 토큰 추출
        String token = jwtTokenProvider.resolveToken(request);

        // 2. 토큰이 유효하다면
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 3. 사용자 인증 정보 생성
            Authentication auth = jwtTokenProvider.getAuthentication(token);

            // 4. SecurityContext 에 저장
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 5. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}