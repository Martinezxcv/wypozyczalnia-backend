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
public class EquipmentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int equipmentTypeId;

    @Column(name = "Name", length = 20, nullable = false)
    private String name;

    /*@OneToMany(mappedBy = "equipmentType")
    private List<Product> products;*/
}
