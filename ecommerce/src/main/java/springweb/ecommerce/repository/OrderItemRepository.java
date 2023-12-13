package springweb.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springweb.ecommerce.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
