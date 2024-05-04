package uz.pdp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.payload.AddressDto;
import uz.pdp.payload.Result;
import uz.pdp.service.AddressService;


@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @PreAuthorize(value = "hasRole('BANK_DIRECTOR')")
    @PostMapping
    public HttpEntity<?> addAddress(@RequestBody AddressDto addressDto) {
        Result result = addressService.addAddress(addressDto);
        return ResponseEntity.status(201).body(result);
    }

    @PreAuthorize(value = "hasRole('BANK_DIRECTOR')")
    @PutMapping("/{id}")
    public HttpEntity<?> editAddress(@RequestBody AddressDto addressDto, @PathVariable Integer id) {
        Result result = addressService.editAddressById(addressDto, id);
        return ResponseEntity.ok(result);
    }

}



