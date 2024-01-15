package springweb.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import springweb.ecommerce.dto.CartDetailDto;
import springweb.ecommerce.dto.CartItemDto;
import springweb.ecommerce.entity.CartItem;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new springweb.ecommerce.dto.CartDetailDto(ci.id, i.itemName, i.price, " +
            "ci.count, im.imageUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.item.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.representImageYn = 'Y' " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
