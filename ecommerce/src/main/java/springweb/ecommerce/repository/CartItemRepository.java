package springweb.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springweb.ecommerce.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);
}
