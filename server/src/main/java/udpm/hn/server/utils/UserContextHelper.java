package udpm.hn.server.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import udpm.hn.server.infrastructure.security.user.UserPrincipal;
import java.util.Optional;

@Component
public class UserContextHelper {

    private static Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    public static String getCurrentUserId() {
        return getAuthentication()
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof UserPrincipal)
                .map(principal -> (UserPrincipal) principal)
                .map(UserPrincipal::getId)
                .orElse(null);
    }

    public static String getCurrentUserEmail() {
        return getAuthentication()
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName)
                .orElse(null);
    }
}
