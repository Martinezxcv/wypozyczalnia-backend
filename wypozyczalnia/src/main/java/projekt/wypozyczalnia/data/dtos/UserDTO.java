package projekt.wypozyczalnia.data.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private int userId;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private RoleDTO roleDto;
}
