package projekt.wypozyczalnia.repositories;

import projekt.wypozyczalnia.data.entities.Cart;
import projekt.wypozyczalnia.data.entities.Product;
import projekt.wypozyczalnia.data.entities.ProductCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCartRepository extends JpaRepository<ProductCart, Integer> {

    Optional<ProductCart> findById_ProductIdAndId_CartId(Product product, Cart cart);
    List<ProductCart> findById_CartId_CartId(int cartId);
    Optional<ProductCart> findById_CartId_CartIdAndId_ProductId_ProductId(int cartId, int productId);
}
