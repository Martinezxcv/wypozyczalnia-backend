package projekt.wypozyczalnia.servicesImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projekt.wypozyczalnia.data.dtos.RoleDTO;
import projekt.wypozyczalnia.data.dtos.UserDTO;
import projekt.wypozyczalnia.data.entities.Cart;
import projekt.wypozyczalnia.data.entities.User;
import projekt.wypozyczalnia.jwt.JwtUtils;
import projekt.wypozyczalnia.jwt.LoginRequest;
import projekt.wypozyczalnia.jwt.LoginResponse;
import projekt.wypozyczalnia.mappers.RoleMapper;
import projekt.wypozyczalnia.mappers.UserMapper;
import projekt.wypozyczalnia.repositories.CartRepository;
import projekt.wypozyczalnia.repositories.RoleRepository;
import projekt.wypozyczalnia.repositories.UserRepository;
import projekt.wypozyczalnia.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CartRepository cartRepository;

    @Override
    public UserDTO getUserInfo(String jwtToken) {
        return userMapper.toDto(userRepository.findByUsername(jwtUtils.getUsernameFromJwtToken(jwtToken.substring(6).trim())));
    }

    @Override
    public String save(UserDTO userDto) {
        User existingUser = userRepository.findByUsername(userDto.getUsername());

        if (existingUser == null) {
            RoleDTO roleDto = roleMapper.toDto(roleRepository.findById(5).get());
            UserDTO newUser = UserDTO.builder()
                    .username(userDto.getUsername())
                    .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                    .email(userDto.getEmail())
                    .firstname(userDto.getFirstname())
                    .lastname(userDto.getLastname())
                    .phone(userDto.getPhone())
                    .roleDto(roleDto)
                    .build();

            userRepository.save(userMapper.toEntity(newUser));
            createCart(userRepository.findByUsername(userDto.getUsername()));
            return "Registration successful";
        }

        return "User already exists";
    }

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

            var authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return new LoginResponse(userDetails.getUsername(), roles, jwtToken);
        } catch (AuthenticationException exception) {
            throw new RuntimeException("Bad credentials", exception);
        }
    }

    @Override
    public void changeDetails(UserDTO userDto) {
        int currentUserId = jwtUtils.getCurrentUserId();
        User user = userRepository.findById(currentUserId).get();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        if(userDto.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        }
        userRepository.save(user);
    }


    private void createCart(User user) {

    Cart cart = new Cart();
    cart.setUser(user);
    cartRepository.save(cart);
}
}
