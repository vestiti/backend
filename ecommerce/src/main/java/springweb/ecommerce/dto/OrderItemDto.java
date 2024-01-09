package springweb.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;
import springweb.ecommerce.entity.OrderItem;

@Getter
@Setter
public class OrderItemDto {
    private String itemName;

    private int count;

    private int orderPrice;

    private String imgUrl;

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemName = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
