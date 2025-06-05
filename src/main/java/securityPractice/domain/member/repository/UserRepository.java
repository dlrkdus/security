package securityPractice.domain.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import securityPractice.domain.member.entity.Member;

public interface UserRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByUsername(String username);
}
