package projekt.wypozyczalnia.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projekt.wypozyczalnia.data.entities.Cart;
import projekt.wypozyczalnia.data.entities.Product;
import projekt.wypozyczalnia.data.entities.ProductCart;
import projekt.wypozyczalnia.data.entities.ProductCartId;
import projekt.wypozyczalnia.jwt.JwtUtils;
import projekt.wypozyczalnia.repositories.CartRepository;
import projekt.wypozyczalnia.repositories.ProductCartRepository;
import projekt.wypozyczalnia.repositories.ProductRepository;
import projekt.wypozyczalnia.servicesImpl.CartServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCartRepository productCartRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void shouldThrowProductAlreadyInCart() {
        // given
        String productName = "Test Product";
        Cart cart = new Cart();
        cart.setCartId(1);

        Product product = new Product();
        product.setProductId(1);
        product.setName(productName);

        ProductCart existingCartItem = new ProductCart();

        // when
        when(jwtUtils.getCurrentUserId()).thenReturn(1);
        when(cartRepository.findByUserUserId(1)).thenReturn(cart);
        when(productRepository.findByName(productName)).thenReturn(Optional.of(product));
        when(productCartRepository.findById_CartId_CartIdAndId_ProductId_ProductId(cart.getCartId(), product.getProductId()))
                .thenReturn(Optional.of(existingCartItem));

        String result = cartService.addProductToCart(productName);

        // then
        assertEquals("Item already in cart", result);

        verify(productRepository, times(1)).findByName(productName);
        verify(productCartRepository, times(1))
                .findById_CartId_CartIdAndId_ProductId_ProductId(cart.getCartId(), product.getProductId());
    }

    @Test
    void shouldAddProductToCart() {
        // given
        String productName = "Test Product";
        Cart cart = new Cart();
        cart.setCartId(1);

        Product product = new Product();
        product.setProductId(1);
        product.setName(productName);

        // when
        when(jwtUtils.getCurrentUserId()).thenReturn(1);
        when(cartRepository.findByUserUserId(1)).thenReturn(cart);
        when(productRepository.findByName(productName)).thenReturn(Optional.of(product));
        when(productCartRepository.findById_CartId_CartIdAndId_ProductId_ProductId(cart.getCartId(), product.getProductId()))
                .thenReturn(Optional.empty());

        String result = cartService.addProductToCart(productName);

        // then
        assertEquals("Item added to cart successfully", result);

        verify(productRepository, times(1)).findByName(productName);
        verify(productCartRepository, times(1))
                .findById_CartId_CartIdAndId_ProductId_ProductId(cart.getCartId(), product.getProductId());
        verify(productCartRepository, times(1)).save(any(ProductCart.class));
    }

    @Test
    void shouldDeleteProductFromCart() {
        // given
        String productName = "Test Product";
        Cart cart = new Cart();
        cart.setCartId(1);

        Product product = new Product();
        product.setProductId(1);
        product.setName(productName);

        ProductCart productCart = new ProductCart();
        ProductCartId productCartId = new ProductCartId();
        productCartId.setProductId(product);
        productCartId.setCartId(cart);
        productCart.setId(productCartId);

        // when
        when(jwtUtils.getCurrentUserId()).thenReturn(1);
        when(cartRepository.findByUserUserId(1)).thenReturn(cart);
        when(productRepository.findByName(productName)).thenReturn(Optional.of(product));
        when(productCartRepository.findById_ProductIdAndId_CartId(product, cart)).thenReturn(Optional.of(productCart));

        cartService.deleteProductFromCart(productName);
        List<ProductCart> remainingItems = productCartRepository.findById_CartId_CartId(cart.getCartId());

        // then
        assertTrue(remainingItems.isEmpty(), "Cart should be empty after product deletion");

        verify(productRepository, times(1)).findByName(productName);
        verify(productCartRepository, times(1)).findById_ProductIdAndId_CartId(product, cart);
        verify(productCartRepository, times(1)).delete(productCart);
        verify(cartRepository, times(1)).save(cart);
    }


    @Test
    void shouldShowCart() {
        // given
        Cart cart = new Cart();
        cart.setCartId(1);

        Product product = new Product();
        product.setProductId(1);
        product.setName("Test Product");
        product.setImageFilepath("/path/to/image");

        ProductCart productCart = new ProductCart();
        ProductCartId cartId = new ProductCartId();
        cartId.setProductId(product);
        productCart.setId(cartId);

        // when
        when(jwtUtils.getCurrentUserId()).thenReturn(1);
        when(cartRepository.findByUserUserId(1)).thenReturn(cart);
        when(productCartRepository.findById_CartId_CartId(cart.getCartId())).thenReturn(List.of(productCart));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        List<Map<String, Object>> result = cartService.showCart();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).get("name"));
        assertEquals("/path/to/image", result.get(0).get("imageFilepath"));

        verify(cartRepository, times(1)).findByUserUserId(1);
        verify(productCartRepository, times(1)).findById_CartId_CartId(cart.getCartId());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void shouldClearCart() {
        // given
        Cart cart = new Cart();
        cart.setCartId(1);

        ProductCart productCart = new ProductCart();

        // when
        when(jwtUtils.getCurrentUserId()).thenReturn(1);
        when(cartRepository.findByUserUserId(1)).thenReturn(cart);
        when(productCartRepository.findById_CartId_CartId(cart.getCartId())).thenReturn(List.of(productCart));

        cartService.clearCart();

        // then
        verify(productCartRepository, times(1)).deleteAll(List.of(productCart));
    }

}
