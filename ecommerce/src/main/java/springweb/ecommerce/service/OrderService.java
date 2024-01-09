package springweb.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springweb.ecommerce.dto.ItemImgDto;
import springweb.ecommerce.dto.OrderDto;
import springweb.ecommerce.dto.OrderHistDto;
import springweb.ecommerce.dto.OrderItemDto;
import springweb.ecommerce.entity.*;
import springweb.ecommerce.repository.ItemImgRepository;
import springweb.ecommerce.repository.ItemRepository;
import springweb.ecommerce.repository.MemberRepository;
import springweb.ecommerce.repository.OrderRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtoList = new ArrayList<>();

        for(Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();

            for(OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository
                        .findByItemIdAndRepresentImageYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImageUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtoList.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtoList, pageable, totalCount);
    }
}
