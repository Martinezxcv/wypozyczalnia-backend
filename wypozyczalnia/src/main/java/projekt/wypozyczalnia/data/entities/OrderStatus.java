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
    public class OrderStatus {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int orderStatusId;

        @Column(name = "Status", length = 15, nullable = false)
        private String status;

        @OneToMany(mappedBy = "orderStatus")
        private List<Order> orders;
    }
