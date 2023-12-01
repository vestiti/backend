package springweb.ecommerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import springweb.ecommerce.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
}
