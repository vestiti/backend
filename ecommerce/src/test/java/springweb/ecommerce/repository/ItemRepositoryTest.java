package springweb.ecommerce.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;
import springweb.ecommerce.constant.ItemSellStatus;
import springweb.ecommerce.entity.Item;
import springweb.ecommerce.entity.QItem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Slf4j
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

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

    private void createItemList2() {
        for (int i = 1; i <= 5; i++) {
            itemRepository.save(Item.builder()
                    .itemName("테스트 상품 " + i)
                    .price(10000 + i)
                    .itemDetail("테스트 상품 설명 " + i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNumber(100)
                    .build());
        }

        for (int i = 6; i <= 10; i++) {
            itemRepository.save(Item.builder()
                    .itemName("테스트 상품 " + i)
                    .price(10000 + i)
                    .itemDetail("테스트 상품 설명 " + i)
                    .itemSellStatus(ItemSellStatus.SOLD_OUT)
                    .stockNumber(0)
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
            System.out.println(item.toString());
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

    @Test
    @DisplayName("QueryDsl 조회 테스트1")
    public void queryDslTest() {
        //given
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 설명" + "%"))
                .orderBy(qItem.price.desc());

        //when
        List<Item> itemList = query.fetch();

        //then
        for(Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2() {
        //given
        this.createItemList2();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 설명";
        int price = 10003;
        String itemSellStat = "SELL";


        //when
        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        //then
        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPageResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPageResult.getTotalElements());

        List<Item> resultItemList = itemPageResult.getContent();
        assertThat(resultItemList.size()).isEqualTo(2);
    }
}