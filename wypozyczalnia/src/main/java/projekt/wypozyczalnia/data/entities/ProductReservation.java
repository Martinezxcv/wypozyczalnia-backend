package projekt.wypozyczalnia.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "productreservation")
public class ProductReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "ReservationStartDate", nullable = false)
    private LocalDate reservationStartDate;

    @Column(name = "ReservationEndDate", nullable = false)
    private LocalDate reservationEndDate;

}