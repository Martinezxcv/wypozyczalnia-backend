package projekt.wypozyczalnia.data.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Setter
@Getter
public class OrderProductKey implements Serializable {

    @JoinColumn(name = "Product_id")
    private int productId;

    @JoinColumn(name = "Order_id")
    private int orderId;

}

