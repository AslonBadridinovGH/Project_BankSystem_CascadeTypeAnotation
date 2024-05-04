package uz.pdp.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.payload.Result;
import uz.pdp.repository.AddressRepository;
import uz.pdp.entity.Address;
import uz.pdp.payload.AddressDto;

import java.util.Optional;


@Service
public class AddressService {

      @Autowired
      AddressRepository addressRepository;


      public Result addAddress(AddressDto addressDto){

             Address address=new Address();
            address.setHouse(addressDto.getHouse());
            address.setStreet(addressDto.getStreet());
            address.setCity(addressDto.getCity());
            addressRepository.save(address);
            return new Result("Address  added",true);
      }

      public Result editAddressById(AddressDto addressDto, Integer id){

            Optional<Address> byId = addressRepository.findById(id);
            if (!byId.isPresent())
                  return new Result("Address  not found",false);
              Address address = byId.get();
            address.setHouse(addressDto.getHouse());
            address.setStreet(addressDto.getStreet());
            address.setCity(addressDto.getCity());
            addressRepository.save(address);
            return new Result("Address  edited",true);
      }

}

