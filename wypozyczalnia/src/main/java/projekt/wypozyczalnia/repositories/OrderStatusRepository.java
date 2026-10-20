package projekt.wypozyczalnia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekt.wypozyczalnia.data.entities.OrderStatus;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {

    OrderStatus findByOrderStatusId(int id);
}
