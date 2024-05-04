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
public class DirectorService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JavaMailSender javaMailSender;


    public ApiResponse registerDirector(RegisterDtoo registerDto) {


        boolean existsByEmail = employeeRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail) {
            return new ApiResponse("this Email  already exist", false);
        }
            Employee user = new Employee();
        user.setFirstname(registerDto.getFirstname());
        user.setLastname(registerDto.getLastname());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Collections.singleton - return object as Collection.
        // Set<> inherits from Collection, so we give Collection instead of Set
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.BANK_DIRECTOR)));
        user.setEmailCode(UUID.randomUUID().toString());
        employeeRepository.save(user);

        sendEMail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("User saved. Confirm your email to activate the account", true);
    }


    public Boolean sendEMail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("aslon.dinov@gmail.com");  // qaysi emaildan jönatilishi(IXTIYORIY EMAILNI YOZSA BÖLADI)
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Confirm Account");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail/director?emailCode=" + emailCode + "&email=" + sendingEmail + "'>Confirm Account </a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {

        Optional<Employee> optionalUser = employeeRepository.findByEmailCodeAndEmail(emailCode, email);
        if (optionalUser.isPresent()) {
            Employee director = optionalUser.get();

            director.setEnabled(true);
            // When he clicks the link 2nd time, it falls to else(!optionalUser.isPresent()).If the EmailCode from the link
            // is not found in the DB, it should return the message "Account already verified".
            director.setEmailCode(null);
            employeeRepository.save(director);
            return new ApiResponse("Account confirmed", true);
        }
        return new ApiResponse("Account already confirmed", false);
    }

    public ApiResponse loginDirector(LoginDtoo loginDto) {

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

