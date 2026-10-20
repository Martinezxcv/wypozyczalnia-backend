package projekt.wypozyczalnia.repositories;

import projekt.wypozyczalnia.data.entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekt.wypozyczalnia.data.entities.Product;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {

    List<OrderProduct> findById_OrderId(int orderId);
    long countByProductAndOrder_OrderDateBetween(Product product, LocalDate startOfMonth, LocalDate endOfMonth);
}
