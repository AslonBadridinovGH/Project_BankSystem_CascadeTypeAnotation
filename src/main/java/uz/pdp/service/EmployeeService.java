
package uz.pdp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.repository.EmployeeRepository;
import uz.pdp.entity.Employee;
import uz.pdp.entity.enums.RoleName;
import uz.pdp.payload.LoginDtoo;
import uz.pdp.payload.RegisterDtoo;
import uz.pdp.repository.RoleRepository;
import uz.pdp.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;


@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JavaMailSender javaMailSender;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;


    public ApiResponse registerEmployee(RegisterDtoo registerDto) {


        boolean existsByEmail = employeeRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail) {
            return new ApiResponse("this Email  already exist", false);
        }
          Employee user = new Employee();
        user.setFirstname(registerDto.getFirstname());
        user.setLastname(registerDto.getLastname());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.BANK_EMPLOYEE)));

        user.setEmailCode(UUID.randomUUID().toString());
        employeeRepository.save(user);

        sendEMail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("User saved. Confirm your email to activate the account", true);
    }

    // SimpleMailMessage Classi orqali Userning Emailiga tasdialash Linkini jönatamiz
    public Boolean sendEMail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("aslon.dinov@gmail.com"); //qaysi emaildan jönatilishi(IXTIYORIY EMAILNI YOZSA BÖLADI)
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Account Confirming");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail/employee?emailCode=" + emailCode + "&email=" + sendingEmail + "'>confirm </a>");
            javaMailSender.send(mailMessage);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {

        Optional<Employee> optionalUser = employeeRepository.findByEmailCodeAndEmail(email, emailCode);
        if (optionalUser.isPresent()) {
            Employee user = optionalUser.get();
            user.setEnabled(true);

            user.setEmailCode(null);
            employeeRepository.save(user);
            return new ApiResponse("Account confirmed", true);
        }
        return new ApiResponse("Account  already exist", false);
    }

    public ApiResponse loginEmployee(LoginDtoo loginDto) {

   try {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        Employee user =(Employee) authentication.getPrincipal();

        String token = JwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
        return new ApiResponse("Token",true, token);

   }catch (BadCredentialsException badCredentialsException){
       return new ApiResponse("password or login error",false);
   }

}

}

