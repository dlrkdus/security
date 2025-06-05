package securityPractice.domain.member.service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import securityPractice.domain.member.constant.Role;
import securityPractice.domain.member.dto.request.LoginRequest;
import securityPractice.domain.member.dto.request.SignUpRequest;
import securityPractice.domain.member.dto.response.SignUpResponse;
import securityPractice.domain.member.dto.response.TokenResponse;
import securityPractice.domain.member.entity.Member;
import securityPractice.domain.member.repository.UserRepository;
import securityPractice.domain.member.util.JwtTokenProvider;
import securityPractice.global.exception.CustomException;
import securityPractice.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (userRepository.findByUsername(signUpRequest.userName()).isPresent()) {
            throw new CustomException(ErrorCode.USER_NAME_DUPLICATE);
        }
        String password = passwordEncoder.encode(signUpRequest.password());
        Member member = Member.builder()
                .name(signUpRequest.name())
                .username(signUpRequest.userName())
                .password(password)
                .role(Role.USER)
                .build();
        userRepository.save(member);
        return SignUpResponse.builder()
                .name(signUpRequest.name())
                .password(password)
                .userName(signUpRequest.userName())
                .build();
    }


    public TokenResponse login(LoginRequest request) {
        try {
            // 사용자 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(), request.password())
            );

            // 인증 성공 → 토큰 발급
            String accessToken = jwtTokenProvider.generateAccessToken(
                    request.username(), getRoles(authentication));
            String refreshToken = jwtTokenProvider.generateRefreshToken();

            // 중복 로그인 방지
            if (isTokenExists(request.username())) {
                throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ATTEMPT);
            }

            // Refresh Token 저장
            saveRefreshToken(refreshToken, request.username());

            return new TokenResponse(accessToken, refreshToken);

        } catch (AuthenticationException e) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    private void saveRefreshToken(String refreshToken, String username) {
        redisTemplate.opsForValue().set(
                username + ":refresh_token",  // 키
                refreshToken,  // 값
                30,  // 30일 동안 유효
                TimeUnit.DAYS  // 기간 단위: 일
        );
    }

    // Redis 에 해당 키가 존재하는지 확인
    private boolean isTokenExists(String username) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(username + ":refresh_token"));
    }

    private List<String> getRoles(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

}
