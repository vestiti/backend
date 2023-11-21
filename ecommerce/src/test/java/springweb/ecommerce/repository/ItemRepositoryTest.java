package springweb.ecommerce.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import springweb.ecommerce.constant.ItemSellStatus;
import springweb.ecommerce.entity.Item;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Slf4j
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @AfterEach
    public void cleanUp() {
        itemRepository.deleteAll();
    }

    private void createItemList() {
        for (int i = 1; i <= 10; i++) {
            itemRepository.save(Item.builder()
                    .itemName("테스트 상품 " + i)
                    .price(10000 + i)
                    .itemDetail("테스트 상품 설명 " + i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNumber(100)
                    .build());
        }
    }

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        //given
        itemRepository.save(Item.builder().
                itemName("테스트 상품")
                .price(10000)
                .itemDetail("테스트 상품 설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .build());

        //when
        List<Item> itemList = itemRepository.findAll();

        //then
        Item item = itemList.get(0);
        assertThat(item.getItemName()).isEqualTo("테스트 상품");
        assertThat(item.getPrice()).isEqualTo(10000);
        assertThat(item.getItemDetail()).isEqualTo("테스트 상품 설명");
        assertThat(item.getItemSellStatus()).isEqualTo(ItemSellStatus.SELL);
        assertThat(item.getStockNumber()).isEqualTo(100);
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNameTest() {
        //given
        this.createItemList();

        //when
        List<Item> itemList = itemRepository.findByItemName("테스트 상품 1");

        //then
        Item item = itemList.get(0);
        assertThat(item.getItemName()).isEqualTo("테스트 상품 1");
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        //given
        this.createItemList();

        //when
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);

        //then
        for (Item item : itemList) {
            assertThat(item.getPrice()).isLessThan(10005);
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {
        //given
        this.createItemList();

        //when
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);

        //then
        for (Item item : itemList) {
            log.info("item = {}", item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        //given
        this.createItemList();

        //when
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 설명");

        //then
        for (Item item : itemList) {
            assertThat(item.getItemDetail()).contains("테스트 상품 설명");
        }
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findByItemDetailByNativeTest() {
        //given
        this.createItemList();

        //when
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 설명");

        //then
        for (Item item : itemList) {
            assertThat(item.getItemDetail()).contains("테스트 상품 설명");
        }
    }
}