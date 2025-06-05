package securityPractice.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import securityPractice.domain.member.entity.Member;
import securityPractice.domain.member.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 데이터베이스에서 사용자 정보 조회
        Member member = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 사용자 정보를 기반으로 UserDetails 객체 생성
        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())  // 비밀번호는 암호화된 상태로 가져와야 함
                .roles(member.getRole().name())  // 역할(roles)은 배열로 변환
                .build();
    }
}
