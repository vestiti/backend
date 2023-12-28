package springweb.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import springweb.ecommerce.entity.ItemImg;
import springweb.ecommerce.repository.ItemImgRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String originalImageName = itemImgFile.getOriginalFilename();
        String imageName = "";
        String imageUrl = "";

        if (!StringUtils.isEmpty(originalImageName)) {
            imageName = fileService.uploadFile(itemImgLocation, originalImageName,
                    itemImgFile.getBytes());

            imageUrl = "/images/item/" + imageName;
        }

        itemImg.updateItemImage(originalImageName, imageName, imageUrl);
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if(!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            if(!StringUtils.isEmpty(savedItemImg.getImageName())) {
                fileService.deleteFile(itemImgLocation + "/"
                        + savedItemImg.getImageName());
            }

            String originalImageName = itemImgFile.getOriginalFilename();
            String imageName = fileService.uploadFile(itemImgLocation, originalImageName,
                    itemImgFile.getBytes());
            String imageUrl = "/images/item/" + imageName;
            savedItemImg.updateItemImage(originalImageName, imageName, imageUrl);
        }
    }
}
