package projekt.wypozyczalnia.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import projekt.wypozyczalnia.data.dtos.UserDTO;
import projekt.wypozyczalnia.data.entities.User;
import projekt.wypozyczalnia.jwt.JwtUtils;
import projekt.wypozyczalnia.jwt.LoginRequest;
import projekt.wypozyczalnia.jwt.LoginResponse;
import projekt.wypozyczalnia.mappers.UserMapper;
import projekt.wypozyczalnia.repositories.UserRepository;
import projekt.wypozyczalnia.servicesImpl.UserServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeAll
    static void setUpClass() {
        MockitoAnnotations.openMocks(UserServiceTest.class);
    }

    @Test
    void shouldSaveUser() {
        // given
        User user = new User();
        user.setUsername("test");
        user.setPassword("Test1234");
        user.setEmail("test@wp.pl");
        user.setFirstname("Test");
        user.setLastname("Test");
        user.setPhone("123456789");

        // when
        when(userRepository.save(any(User.class))).thenReturn(user);
        userRepository.save(user);

        // then
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldGetUserInfo() {
        // given
        String jwtToken = "Bearer sampleJwtToken";
        String username = "testUser";

        User user = new User();
        user.setUsername(username);
        user.setEmail("test@example.com");

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setEmail("test@example.com");

        // when
        when(jwtUtils.getUsernameFromJwtToken("sampleJwtToken")).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserInfo(jwtToken);

        // then
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("test@example.com", result.getEmail());

        verify(jwtUtils, times(1)).getUsernameFromJwtToken("sampleJwtToken");
        verify(userRepository, times(1)).findByUsername(username);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void shouldAuthenticateUser() {
        // given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        UserDetails mockUserDetails = org.springframework.security.core.userdetails.User.builder()
                .username("testUser")
                .password("testPassword")
                .authorities(List.of((GrantedAuthority) () -> "ROLE_USER"))
                .build();

        Authentication mockAuthentication = mock(Authentication.class);
        String mockJwtToken = "mockJwtToken";

        // when
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(jwtUtils.generateTokenFromUsername(mockUserDetails)).thenReturn(mockJwtToken);

        LoginResponse response = userService.authenticateUser(loginRequest);

        // then
        assertNotNull(response);
        assertEquals("testUser", response.getUsername());
        assertEquals(1, response.getRoles().size());
        assertTrue(response.getRoles().contains("ROLE_USER"));
        assertEquals(mockJwtToken, response.getJwtToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateTokenFromUsername(mockUserDetails);
    }

    @Test
    void shouldChangeDetails() {
        // given
        UserDTO userDto = new UserDTO();
        userDto.setFirstname("NewFirstName");
        userDto.setLastname("NewLastName");
        userDto.setUsername("newUsername");
        userDto.setEmail("newEmail@example.com");
        userDto.setPhone("987654321");
        userDto.setPassword("newPassword");

        int mockUserId = 1;
        User mockUser = new User();
        mockUser.setUserId(mockUserId);
        mockUser.setFirstname("OldFirstName");
        mockUser.setLastname("OldLastName");
        mockUser.setUsername("oldUsername");
        mockUser.setEmail("oldEmail@example.com");
        mockUser.setPhone("123456789");

        // when
        when(jwtUtils.getCurrentUserId()).thenReturn(mockUserId);
        when(userRepository.findById(mockUserId)).thenReturn(java.util.Optional.of(mockUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        userService.changeDetails(userDto);

        // then
        assertEquals("NewFirstName", mockUser.getFirstname());
        assertEquals("NewLastName", mockUser.getLastname());
        assertEquals("newUsername", mockUser.getUsername());
        assertEquals("newEmail@example.com", mockUser.getEmail());
        assertEquals("987654321", mockUser.getPhone());
        assertEquals("encodedPassword", mockUser.getPassword());

        verify(jwtUtils, times(1)).getCurrentUserId();
        verify(userRepository, times(1)).findById(mockUserId);
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(mockUser);
    }
}