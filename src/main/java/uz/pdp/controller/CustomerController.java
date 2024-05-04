

package uz.pdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.payload.LoginDtoo;
import uz.pdp.payload.RegisterDtoo;
import uz.pdp.security.JwtProvider;
import uz.pdp.service.ApiResponse;
import uz.pdp.service.CustomerService;


@RestController
@RequestMapping("/api/auth")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping(value = "/register/customer")
    public HttpEntity<?>registerUser(@RequestBody RegisterDtoo registerDto){
        ApiResponse apiResponse = customerService.registerCustomer(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }


    @GetMapping( value = "/verifyEmail/customer")
    public HttpEntity<?>verifyEmail(@RequestParam String emailCode, @RequestParam String email){
        ApiResponse apiResponse=customerService.verifyEmail(emailCode,email);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PostMapping(value = "/login/customer")
    public HttpEntity<?>login(@RequestBody LoginDtoo loginDto){
        ApiResponse apiResponse=customerService.loginCustomer(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }

}


