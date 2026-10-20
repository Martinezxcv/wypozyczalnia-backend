package projekt.wypozyczalnia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekt.wypozyczalnia.data.entities.Order;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByCartUserUserIdOrderByOrderDateDesc(int currentClientId);
    List<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
}
