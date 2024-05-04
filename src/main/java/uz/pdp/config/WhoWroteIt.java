package uz.pdp.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.pdp.entity.Employee;

import java.util.Optional;


// the User's UUID number is displayed in the createdBy column of the Product Entity in the DATA BASE.
public class WhoWroteIt implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {

        // USER LOGGED IN TO THE SYSTEM - NOT null, NOT Authenticated, NOT anonymousUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                  if (authentication != null
                && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser"))
        {
            // WE RETURN THE ID (UUID) OF THE USER WHO ENTERED THE SYSTEM
            Employee user = (Employee) authentication.getPrincipal();
            return Optional.of(user.getId());
        }
        return Optional.empty();
    }


}
