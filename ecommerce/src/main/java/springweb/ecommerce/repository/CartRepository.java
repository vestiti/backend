package springweb.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springweb.ecommerce.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByMemberId(Long memberId);
}
