package projekt.wypozyczalnia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekt.wypozyczalnia.data.entities.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByUserUserId(int userId);
}
