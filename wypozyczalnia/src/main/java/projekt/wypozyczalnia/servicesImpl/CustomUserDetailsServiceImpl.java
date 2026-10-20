package projekt.wypozyczalnia.servicesImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import projekt.wypozyczalnia.data.entities.User;
import projekt.wypozyczalnia.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        System.out.println("Found user: " + user.getUsername());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,
                true, true, true,
                AuthorityUtils.createAuthorityList(user.getRole().getType())
        );
    }
}
