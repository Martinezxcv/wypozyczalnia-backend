package projekt.wypozyczalnia.data.dtos;

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
public class KitDTO {

    private int kitId;
    private String name;
    private boolean availability;
}
