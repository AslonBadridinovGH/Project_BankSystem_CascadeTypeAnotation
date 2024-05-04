package uz.pdp.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.repository.EmployeeRepository;


@Service
public class AuthService implements UserDetailsService {


    @Autowired
    EmployeeRepository employeeRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      return employeeRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username+" not found"));
    }

}

