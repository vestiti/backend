package springweb.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springweb.ecommerce.dto.CartItemDto;
import springweb.ecommerce.entity.Cart;
import springweb.ecommerce.entity.CartItem;
import springweb.ecommerce.entity.Item;
import springweb.ecommerce.entity.Member;
import springweb.ecommerce.repository.CartItemRepository;
import springweb.ecommerce.repository.CartRepository;
import springweb.ecommerce.repository.ItemRepository;
import springweb.ecommerce.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email) {
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        if(cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository
                .findByCartIdAndItemId(cart.getId(), item.getId());

        if(savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());

            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);

            return cartItem.getId();
        }
    }
}
