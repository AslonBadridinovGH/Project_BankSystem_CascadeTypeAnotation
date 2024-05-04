package uz.pdp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.service.DirectorService;
import uz.pdp.payload.LoginDtoo;
import uz.pdp.payload.RegisterDtoo;
import uz.pdp.service.ApiResponse;


@RestController
@RequestMapping("/api/auth")
public class DirectorController {

    @Autowired
    DirectorService directorService;



    @PostMapping(value = "/register/director")
    public HttpEntity<?>registerDirector(@RequestBody RegisterDtoo registerDto){
        ApiResponse apiResponse = directorService.registerDirector(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }


    @GetMapping( value = "/verifyEmail/director")
    public HttpEntity<?>verifyEmail(@RequestParam String emailCode, @RequestParam String email){
        ApiResponse apiResponse=directorService.verifyEmail(emailCode,email);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PostMapping(value = "/login/director")
    public HttpEntity<?>loginDirector(@RequestBody LoginDtoo loginDto){
        ApiResponse apiResponse=directorService.loginDirector(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }


}


