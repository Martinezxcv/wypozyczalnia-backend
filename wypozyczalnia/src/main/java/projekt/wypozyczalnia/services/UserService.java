package projekt.wypozyczalnia.services;


import projekt.wypozyczalnia.data.dtos.UserDTO;
import projekt.wypozyczalnia.jwt.LoginRequest;
import projekt.wypozyczalnia.jwt.LoginResponse;

public interface UserService {

    UserDTO getUserInfo(String jwtToken);
    String save(UserDTO userDto);
    LoginResponse authenticateUser(LoginRequest loginRequest);
    void changeDetails(UserDTO userDto);
}
