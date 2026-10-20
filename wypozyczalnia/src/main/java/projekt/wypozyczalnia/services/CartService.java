package projekt.wypozyczalnia.services;

import java.util.List;
import java.util.Map;

public interface CartService {

    String addProductToCart(String productName);
    void deleteProductFromCart(String productName);
    List<Map<String, Object>> showCart();
    void clearCart();

}
