package springweb.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import springweb.ecommerce.entity.ItemImg;

@Getter
@Setter
public class ItemImgDto {
    private Long id;

    private String imageName;

    private String originalImageName;

    private String imageUrl;

    private String representImageYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
