package projekt.wypozyczalnia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekt.wypozyczalnia.data.entities.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String productName);

}
