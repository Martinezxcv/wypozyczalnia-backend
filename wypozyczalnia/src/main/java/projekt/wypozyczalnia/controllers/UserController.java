package projekt.wypozyczalnia.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projekt.wypozyczalnia.data.dtos.UserDTO;
import projekt.wypozyczalnia.jwt.LoginRequest;
import projekt.wypozyczalnia.jwt.LoginResponse;
import projekt.wypozyczalnia.services.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO) {
        String result = userService.save(userDTO);

        if (result.equals("Registration successful")) {
            return ResponseEntity.ok("{\"message\": \"Registration successful\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\": \"User already exists\"}");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = userService.authenticateUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", exception.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasAnyAuthority('Client')")
    @GetMapping("/info")
    public ResponseEntity<UserDTO> getUserInfo(@RequestHeader("Authorization") String jwtToken) {
        return ResponseEntity.ok(userService.getUserInfo(jwtToken));
    }

    @PreAuthorize("hasAnyAuthority('Client')")
    @PutMapping("/change")
    public ResponseEntity<String> changeDetails(@RequestBody UserDTO userDTO) {
        userService.changeDetails(userDTO);
        return ResponseEntity.ok("{\"message\": \"Details changed successfully\"}");
    }
}
