package uz.pdp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.repository.AddressRepository;
import uz.pdp.repository.BankATMRepository;
import uz.pdp.repository.BanksRepository;
import uz.pdp.entity.Address;
import uz.pdp.entity.Banks;
import uz.pdp.payload.BankDto;
import uz.pdp.payload.Result;

import java.util.Optional;

@Service
public class BankService {


      @Autowired
      BanksRepository bankRepository;

      @Autowired
      AddressRepository addressRepository;

      @Autowired
      BankATMRepository bankATMRepository;


      public Result addBank(BankDto bankDto) {

            Optional<Address> bankAddress = addressRepository.findById(bankDto.getAddressId());
            if (!bankAddress.isPresent()) return new Result("Address  not found",false);

               Banks bank=new Banks();
            bank.setAddress(bankAddress.get());
            bank.setName(bankDto.getName());
            bankRepository.save(bank);
            return new Result("Bank  saved",true);
      }

      public Result editBankById(BankDto bankDto, Integer id) {

            Optional<Banks> byIdBank = bankRepository.findById(id);
            if (!byIdBank.isPresent())
                  return new Result("Bank  not found",false);

            Optional<Address> bankAddress = addressRepository.findById(bankDto.getAddressId());
            if (!bankAddress.isPresent()) return new Result("Address  not found",false);

              Banks bank=byIdBank.get();
            bank.setAddress(bankAddress.get());
            bank.setName(bankDto.getName());
            bankRepository.save(bank);
            return new Result("Bank  edited",true);
      }

}


