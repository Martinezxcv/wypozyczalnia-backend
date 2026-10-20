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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @ManyToOne()
    @JoinColumn(name = "Category_id", nullable = false)
    private Category category;

    @ManyToOne()
    @JoinColumn(name = "Skill_Level_id", nullable = false)
    private SkillLevel skillLevel;

    @ManyToOne()
    @JoinColumn(name = "Equipment_Type_id", nullable = false)
    private EquipmentType equipmentType;

    @Column(name = "Model", length = 30, nullable = false)
    private String model;

    @Column(name = "Brand", length = 30, nullable = false)
    private String brand;

    @Column(name = "Name", length = 20, nullable = false)
    private String name;

    @Column(name = "Availability", nullable = false)
    private boolean available;

    @Column(name = "Size", length = 2, nullable = false)
    private String size;

    @Column(name = "Weight", nullable = false)
    private float weight;

    @Column(name = "Price",length = 8, precision = 2, nullable = false)
    private float price;

    @Column(name = "Description", nullable = false)
    private String description;

    @Column(name = "Image_Filepath", nullable = false)
    private String imageFilepath;

}
