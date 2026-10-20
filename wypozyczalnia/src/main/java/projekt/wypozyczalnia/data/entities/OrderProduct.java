package projekt.wypozyczalnia.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderProduct {

    @EmbeddedId
    private OrderProductKey id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "Product_id")
    private Product product;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "Order_id")
    private Order order;

}
