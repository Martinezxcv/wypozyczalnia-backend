package projekt.wypozyczalnia.servicesImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projekt.wypozyczalnia.data.entities.Cart;
import projekt.wypozyczalnia.data.entities.Product;
import projekt.wypozyczalnia.data.entities.ProductCart;
import projekt.wypozyczalnia.data.entities.ProductCartId;
import projekt.wypozyczalnia.jwt.JwtUtils;
import projekt.wypozyczalnia.repositories.CartRepository;
import projekt.wypozyczalnia.repositories.ProductCartRepository;
import projekt.wypozyczalnia.repositories.ProductRepository;
import projekt.wypozyczalnia.services.CartService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ProductCartRepository productCartRepository;
    private final JwtUtils jwtUtils;

    @Override
    public String addProductToCart(String productName) {

        Cart cart = getOrCreateCart();
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        ProductCart existingCartItem = productCartRepository.findById_CartId_CartIdAndId_ProductId_ProductId(cart.getCartId(), product.getProductId())
                .orElse(null);

        if (existingCartItem != null) {
            return "Item already in cart";

        } else {
            ProductCart cartItem = new ProductCart();
            ProductCartId cartItemId = new ProductCartId();

            cartItemId.setProductId(product);
            cartItemId.setCartId(cart);
            cartItem.setId(cartItemId);

            productCartRepository.save(cartItem);
        }
        return "Item added to cart successfully";
    }

    @Override
    public void deleteProductFromCart(String productName) {

        Cart cart = getOrCreateCart();

        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        ProductCart productCart = productCartRepository.findById_ProductIdAndId_CartId(product, cart)
                .orElseThrow(() -> new EntityNotFoundException("Product not found in the cart"));

        productCartRepository.delete(productCart);
        cartRepository.save(cart);
    }

    @Override
    public List<Map<String, Object>> showCart() {

        Cart clientCart = cartRepository.findByUserUserId(jwtUtils.getCurrentUserId());
        List<Map<String, Object>> elements = new ArrayList<>();
        List<ProductCart> productsInCart = productCartRepository.findById_CartId_CartId(clientCart.getCartId());

        for (ProductCart product : productsInCart) {
            Optional<Product> product1 = productRepository.findById(product.getId().getProductId().getProductId());

            Map<String, Object> item = new HashMap<>();
            item.put("name", product1.get().getName());
            item.put("imageFilepath", product1.get().getImageFilepath());

            elements.add(item);
        }

        return elements;
    }

    private Cart getOrCreateCart() {
        int currentClientId = jwtUtils.getCurrentUserId();

        Optional<Cart> existingCart = Optional.ofNullable(cartRepository.findByUserUserId(currentClientId));

        return existingCart.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(cartRepository.findByUserUserId(currentClientId).getUser());
            return newCart;
        });
    }

    @Override
    public void clearCart() {

        int currentClientId = jwtUtils.getCurrentUserId();
        Cart clientCart = cartRepository.findByUserUserId(currentClientId);

        if (clientCart != null) {

            List<ProductCart> productsInCart = productCartRepository.findById_CartId_CartId(clientCart.getCartId());

            productCartRepository.deleteAll(productsInCart);
        } else {
            throw new EntityNotFoundException("Cart not found");
        }
    }

}
