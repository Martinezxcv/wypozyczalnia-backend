package projekt.wypozyczalnia.data.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCartId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "Product_id", nullable = false)
    private Product productId;

    @ManyToOne
    @JoinColumn(name = "Cart_id", nullable = false)
    private Cart cartId;

}

