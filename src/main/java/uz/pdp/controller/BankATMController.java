package uz.pdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.entity.Atm;
import uz.pdp.payload.BankATMDto;
import uz.pdp.payload.Result;
import uz.pdp.service.ApiResponse;
import uz.pdp.service.BankATMService;

import java.util.List;


@RestController
@RequestMapping("/bankATM")
public class BankATMController {

    @Autowired
    BankATMService atmService;


    @PreAuthorize(value = "hasRole('BANK_DIRECTOR')")
    @PostMapping
    public HttpEntity<?> addBankATM(@RequestBody BankATMDto bankomatDto) {
        Result result = atmService.addBankATM(bankomatDto);
        return ResponseEntity.status(201).body(result);
    }


    @PreAuthorize(value = "hasRole('BANK_DIRECTOR')")
    @GetMapping("/{id}")
    public HttpEntity<?> getBankATMById(@PathVariable Integer id) {
        Result cardById = atmService.getBankATMById(id);
        return ResponseEntity.status(cardById != null ? HttpStatus.OK : HttpStatus.CONFLICT).body(cardById);
    }


     @PreAuthorize(value = "hasRole('BANK_DIRECTOR')")
     @GetMapping("/budgetById/{id}")
     public HttpEntity<?> getBankATMBudgetById(@PathVariable Integer id) {
         ApiResponse cardById = atmService.getBankATMBudgetById(id);
         return ResponseEntity.status(cardById != null ? HttpStatus.OK : HttpStatus.CONFLICT).body(cardById);
     }


    @PreAuthorize(value = "hasRole('BANK_DIRECTOR')")
    @GetMapping
    public HttpEntity<?> getBankATM() {
        List<Atm> bankATMS = atmService.getBankATMs();
        return ResponseEntity.ok(bankATMS);
    }


    @PreAuthorize(value = "hasRole('BANK_DIRECTOR')")
    @PutMapping("/{id}")
    public HttpEntity<?> editBankATM(@RequestBody BankATMDto bankomatDto, @PathVariable Integer id) {
        Result result = atmService.editBankATMById(bankomatDto, id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize(value = "hasRole('BANK_DIRECTOR')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteBankATM(@PathVariable Integer id) {
        Result result = atmService.deleteBankATM(id);
        return ResponseEntity.ok(result);
    }

}



