
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
public class CustomerService {

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


      public ApiResponse registerCustomer(RegisterDtoo registerDto) {


      boolean existsByEmail = employeeRepository.existsByEmail(registerDto.getEmail());
      if (existsByEmail) {
      return new ApiResponse("this Email  already exist", false);
      }
      Employee user = new Employee();
      user.setFirstname(registerDto.getFirstname());
      user.setLastname(registerDto.getLastname());
      user.setEmail(registerDto.getEmail());
      user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
      user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.CUSTOMER)));

      user.setEmailCode(UUID.randomUUID().toString());
      employeeRepository.save(user);

      sendEMail(user.getEmail(), user.getEmailCode());
      return new ApiResponse("User saved. Confirm your email to activate the account", true);
      }


      public Boolean sendEMail(String sendingEmail, String emailCode) {
      try {
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setFrom("aslon.dinov@gmail.com");
      mailMessage.setTo(sendingEmail);
      mailMessage.setSubject("Confirm Account");
      mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail/customer?emailCode=" + emailCode + "&email=" + sendingEmail + "'>Confirm Account </a>");
      javaMailSender.send(mailMessage);
      return true;

      } catch (Exception e) {
      return false;
      }
      }

      // THIS METHOD WORKS WHEN THE USER OPENS HIS EMAIL AND CLICKS ON THE VERIFICATION LINK. It separates the email and emailCode from the LINK.
      public ApiResponse verifyEmail(String emailCode, String email) {

      Optional<Employee> optionalUser = employeeRepository.findByEmailCodeAndEmail(email, emailCode);
      if (optionalUser.isPresent()) {
      Employee user = optionalUser.get();

      user.setEnabled(true);
            // When he clicks the link 2nd time, it falls to else(!optionalUser.isPresent()).If the EmailCode from the link
            // is not found in the DB, it should return the message "Account already verified".
      user.setEmailCode(null);
      employeeRepository.save(user);
      return new ApiResponse("Account confirmed", true);
      }
      return new ApiResponse("Account  already exist", false);
      }

      public ApiResponse loginCustomer(LoginDtoo loginDto) {

      try {

      Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
      loginDto.getUsername(), loginDto.getPassword()));

      Employee user =(Employee) authentication.getPrincipal();
      // RETURN USERNAME TOKEN WITH ROLE; THE NEXT TIME THE USER LOGINS WITH THIS TOKEN OR ACCESSES THE CLOSED ROAD:
      String token = JwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
      return new ApiResponse("Token",true, token);

      }catch (BadCredentialsException badCredentialsException){
      return new ApiResponse("password or login error",false);
      }

      }


    }

