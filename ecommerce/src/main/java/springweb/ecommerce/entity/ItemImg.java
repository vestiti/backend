package springweb.ecommerce.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "item_img")
@Getter
@Setter
public class ItemImg extends BaseEntity {
    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName;

    private String originalImgName;

    private String imgUrl;

    private String representImgYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImage(String originalImgName, String imgName, String imgUrl) {
        this.originalImgName = originalImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
