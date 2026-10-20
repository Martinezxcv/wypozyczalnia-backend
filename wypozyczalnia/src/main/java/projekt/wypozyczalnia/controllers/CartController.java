package projekt.wypozyczalnia.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projekt.wypozyczalnia.services.CartService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/addProduct")
    public ResponseEntity<String> addProductToCart(
            @RequestParam(name = "productName") String productName
    ) {
        try {
            String result = cartService.addProductToCart(productName);

            if ("Item already in cart".equals(result)) {
                return new ResponseEntity<>("Product is already in the cart", HttpStatus.CONFLICT);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding product to cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteProduct")
    public ResponseEntity<String> deleteProductFromCart(
            @RequestParam(name = "productName") String productName
    ) {
        try {
            cartService.deleteProductFromCart(productName);
            return new ResponseEntity<>("Product deleted from cart successfully", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding product to cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearUserCart() {
        try {
            cartService.clearCart();
            return new ResponseEntity<>("Cart cleared succesfully", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding product to cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    List<Map<String, Object>> getClientCart(){
       return cartService.showCart();
    }

}
