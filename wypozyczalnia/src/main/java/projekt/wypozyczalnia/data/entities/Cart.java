package projekt.wypozyczalnia.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    @ManyToOne
    @JoinColumn(name = "User_id", nullable = false)
    private User user;

    /*@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<Order> orders;*/

}
