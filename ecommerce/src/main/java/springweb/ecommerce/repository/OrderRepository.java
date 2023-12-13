package springweb.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springweb.ecommerce.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
