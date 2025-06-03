package securityPractice.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import securityPractice.domain.user.constant.Role;
import securityPractice.domain.user.dto.request.SignUpRequest;
import securityPractice.domain.user.dto.response.SignUpResponse;
import securityPractice.domain.user.entity.User;
import securityPractice.domain.user.repository.UserRepository;
import securityPractice.global.exception.CustomException;
import securityPractice.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (userRepository.findByUsername(signUpRequest.userName()).isPresent()) {
            throw new CustomException(ErrorCode.USER_NAME_DUPLICATE);
        }
        String password = passwordEncoder.encode(signUpRequest.password());
        User user = User.builder()
                .name(signUpRequest.name())
                .username(signUpRequest.userName())
                .password(password)
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return SignUpResponse.builder()
                .name(signUpRequest.name())
                .password(password)
                .userName(signUpRequest.userName())
                .build();
    }
}
