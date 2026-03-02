package se.sali.webbapplikation.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import se.sali.webbapplikation.model.User;
import se.sali.webbapplikation.repository.IUserRepository;

@Component
public class AuthUtils {

    private final IUserRepository userRepository;

    public AuthUtils(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser(Authentication auth) {
        Object principal = auth.getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        }

        if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) principal;
            String username = oauth2User.getAttribute("login");
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        throw new RuntimeException("Unknown authentication type");
    }
}
