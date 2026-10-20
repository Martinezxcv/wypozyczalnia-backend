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
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressId;

    @Column(name = "City", length = 20, nullable = false)
    private String city;

    @Column(name = "Street", length = 20, nullable = false)
    private String street;

    @Column(name = "Number", length = 7, nullable = false)
    private String number;

    @Column(name = "Postal_Code", length = 6, nullable = false)
    private String postalCode;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "User_id")
    private User user;

}
