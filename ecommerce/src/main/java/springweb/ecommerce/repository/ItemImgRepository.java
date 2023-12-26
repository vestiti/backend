package springweb.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springweb.ecommerce.entity.ItemImg;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}
