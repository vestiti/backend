package springweb.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;
import springweb.ecommerce.constant.ItemSellStatus;

@Getter
@Setter
public class ItemSearchDto {
    private String searchDateType;

    private ItemSellStatus searchSellStatus;

    private String searchBy;

    private String searchQuery = "";
}
