package projekt.wypozyczalnia.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "Order_Status_id", nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "Address_id", nullable = false)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "Cart_id", nullable = false)
    private Cart cart;

    @Column(name = "Order_Date", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate orderDate;

    @Column(name = "Rental_Date_Start", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate rentalDateStart;

    @Column(name = "Rental_Date_End", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate rentalDateEnd;

    @Column(name = "Total_Cost", precision = 2, length = 8)
    private float totalCost;

}
