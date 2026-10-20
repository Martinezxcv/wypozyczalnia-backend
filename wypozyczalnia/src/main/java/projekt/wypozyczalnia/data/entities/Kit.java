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
public class Kit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int kitId;

    @Column(name = "Name", length = 40, nullable = false)
    private String name;

    @Column(name = "Availability", nullable = false)
    private boolean availability;

}
