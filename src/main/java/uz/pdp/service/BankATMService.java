package uz.pdp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.entity.*;
import uz.pdp.repository.*;
import uz.pdp.entity.enumClass.CardType;
import uz.pdp.entity.enums.CardTypeName;
import uz.pdp.payload.BankATMDto;
import uz.pdp.payload.Result;


import java.util.*;

@Service
public class BankATMService {

      @Autowired
      CardRepository cardRepository;

      @Autowired
      CardTypeRepository cardTypeRepository;

      @Autowired
      BanksRepository bankRepository;

      @Autowired
      AddressRepository addressRepository;

      @Autowired
      BankATMRepository bankATMRepository;

      @Autowired
      JavaMailSender javaMailSender;

      @Autowired
      EmployeeRepository employeeRepository;

      @Autowired
      DepositATMRepository depositATMRepository;

      @Autowired
      DepositCardRepository depositCardRepository;

      @Autowired
      WithdrawMoneyRepository withdrawMoneyRepository;



      public Result addBankATM(BankATMDto atmDto) {

            Optional<Banks> byId = bankRepository.findById(atmDto.getBankID());
            if (!byId.isPresent())
                  return new Result("Bank  not found",false);

            Optional<Address> byIdAddress = addressRepository.findById(atmDto.getAddressID());
            if (!byIdAddress.isPresent())
                  return new Result("Address  not found",false);

             Atm bankATM=new Atm();

            bankATM.setBank(byId.get());
            bankATM.setAddress(byIdAddress.get());
            bankATM.setMaxWithdrawLimitDollar(atmDto.getMaxWithdrawLimitDollar());
            bankATM.setMaxWithdrawLimitSum(atmDto.getMaxWithdrawLimitSum());

            bankATM.setCurrentBalanceInSum(atmDto.getCurrentBalanceInSum());
            bankATM.setCurrentBalanceInDollar(atmDto.getCurrentBalanceInDollar());
            bankATM.setMinRestMoneyInSum(atmDto.getMinRestMoneyInSum());
            bankATM.setMinRestMoneyInDollar(atmDto.getMinRestMoneyInDollar());

            bankATM.setCommissionDepositForeignCardInPercent(atmDto.getCommissionDepositForeignCardInPercent());
            bankATM.setCommissionDepositOwnCardInPercent(atmDto.getCommissionDepositOwnCardInPercent());
            bankATM.setCommissionWithdrawForeignCardInPercent(atmDto.getCommissionWithdrawForeignCardInPercent());
            bankATM.setCommissionWithdrawOwnCardInPercent(atmDto.getCommissionWithdrawOwnCardInPercent());
            bankATM.setBudgetFromCommissionSum(atmDto.getBudgetFromCommissionSum());
            bankATM.setBudgetFromCommissionDollar(atmDto.getBudgetFromCommissionDollar());

             Set<CardType> forCardsList = new HashSet<>();
            List<Integer> forCards = atmDto.getCardTypesFor();
            for (Integer forCard : forCards) {
                  if (forCard == 1) {
                        forCardsList.add(cardTypeRepository.findByCardTypeName(CardTypeName.UZCARD));
                  } else if (forCard == 2) {
                        forCardsList.add(cardTypeRepository.findByCardTypeName(CardTypeName.HUMO));
                  } else if (forCard == 3) {
                        forCardsList.add(cardTypeRepository.findByCardTypeName(CardTypeName.VISA));
                  } }
            bankATM.setCardTypesFor(forCardsList);
            bankATMRepository.save(bankATM);
            return new Result("Bank ATM  saved",true);
      }

      public Result getBankATMById(Integer id) {
            Optional<Atm> optionalBankomat = bankATMRepository.findById(id);
            if (!optionalBankomat.isPresent()) {
                return new Result("Bank atm  not found",false);
            }
            return new Result("ATM ",true,optionalBankomat.get());
      }

      public ApiResponse getBankATMBudgetById(Integer id) {

            Optional<Atm> byId = bankATMRepository.findById(id);
            if (!byId.isPresent()) {
                return new ApiResponse("ATM  not found", false);
            }

        // Banknotes placed by an employee at an ATM
       List<DepositInATM> depositInATMList = depositATMRepository.findAllByAtmId(id);
       List<String> depositList = new ArrayList<>();
        for (DepositInATM depositInATM : depositInATMList) {
              depositList.add(depositInATM.getBankNote());
        }
        String [] array=new String[depositInATMList.size()];
        depositList.toArray(array);
        Arrays.sort(array);
        Map<String, Integer> depAtm = new HashMap<>();
        for (int i = 0; i < array.length-1; i++) {
                  int count=1;
                  for (int j = i+1; j < array.length; j++) {
                        if (array[i].equals(array[j])) count++;
                        else break;
                  }
                  if (count>1){
                        depAtm.put("Banknotes deposited by the employee: "+array[i]+" number of banknotes", count);
                        i+=(count-1);
                  }
            }

        // Banknotes placed by the customer in the ATM
       List<DepositInCard> depositInCardList = depositCardRepository.findAllByAtmId(id);
       List<String> depositCardList = new ArrayList<>();
        for (DepositInCard depositInCard : depositInCardList) {
              depositCardList.add(depositInCard.getBankNote());
        }
        String [] arrayCard=new String[depositCardList.size()];
        depositCardList.toArray(arrayCard);
        Arrays.sort(arrayCard);
        Map<String, Integer> depCardMap = new HashMap<>();
        for (int i = 0; i < arrayCard.length-1; i++) {
                  int count=1;
                  for (int j = i+1; j < arrayCard.length; j++) {
                        if (arrayCard[i].equals(arrayCard[j])) count++;
                        else break; }
                  if (count>1){
                        depCardMap.put("Banknotes deposited by the customer: "+arrayCard[i] + " NUMBER of banknotes", count);
                        i+=(count-1);
                  }
            }

         // Banknotes received by the customer from the ATM
        List<WithdrawFromCard> withdrawCard = withdrawMoneyRepository.findAllByAtmId(id);
       List<String> withdrawCarList = new ArrayList<>();
        for (WithdrawFromCard withFCard : withdrawCard) {
              withdrawCarList.add(withFCard.getBankNote());
        }
        String [] arrayWithDr=new String[withdrawCarList.size()];
        withdrawCarList.toArray(arrayWithDr);
        Arrays.sort(arrayWithDr);
        Map<String, Integer> withCardMap = new HashMap<>();
        for (int i = 0; i < arrayWithDr.length-1; i++) {
                  int count=1;
                  for (int j = i+1; j < arrayWithDr.length; j++) {
                        if (arrayWithDr[i].equals(arrayWithDr[j])) count++;
                        else break;

                  }
                  if (count>1){
                   withCardMap.put("Banknotes received by the customer: "+arrayWithDr[i]+ "NUMBER of banknotes: ", count);
                   i+=(count-1);
                  }
            }
       Atm atm = byId.get();
      return new ApiResponse(depAtm, depCardMap, withCardMap, atm.getCurrentBalanceInSum(), atm.getCurrentBalanceInDollar());

}

      public Result editBankATMById(BankATMDto bankomatDto, Integer id) {

            Optional<Atm> byIdBankATM = bankATMRepository.findById(id);
            if (!byIdBankATM.isPresent())
                  return new Result("atm  not found", false);

            Optional<Banks> byId = bankRepository.findById(bankomatDto.getBankID());
            if (!byId.isPresent())
                  return new Result("Bank  not found", false);

            Optional<Address> byIdAddress = addressRepository.findById(bankomatDto.getAddressID());
            if (!byIdAddress.isPresent())
                  return new Result("Address  not found", false);

              Atm bankATM = byIdBankATM.get();
            bankATM.setBank(byId.get());
            bankATM.setAddress(byIdAddress.get());
            bankATM.setMaxWithdrawLimitDollar(bankomatDto.getMaxWithdrawLimitDollar());
            bankATM.setMaxWithdrawLimitSum(bankomatDto.getMaxWithdrawLimitSum());
            bankATM.setCurrentBalanceInSum(bankomatDto.getCurrentBalanceInSum());
            bankATM.setCurrentBalanceInDollar(bankomatDto.getCurrentBalanceInDollar());
            bankATM.setMinRestMoneyInSum(bankomatDto.getMinRestMoneyInSum());
            bankATM.setMinRestMoneyInDollar(bankomatDto.getMinRestMoneyInDollar());

            bankATM.setCommissionDepositForeignCardInPercent(bankomatDto.getCommissionDepositForeignCardInPercent());
            bankATM.setCommissionDepositOwnCardInPercent(bankomatDto.getCommissionDepositOwnCardInPercent());
            bankATM.setCommissionWithdrawForeignCardInPercent(bankomatDto.getCommissionWithdrawForeignCardInPercent());
            bankATM.setCommissionWithdrawOwnCardInPercent(bankomatDto.getCommissionWithdrawOwnCardInPercent());

            bankATM.setBudgetFromCommissionSum(bankomatDto.getBudgetFromCommissionSum());
            bankATM.setBudgetFromCommissionDollar(bankomatDto.getBudgetFromCommissionDollar());


              Set<CardType> forCardsList = new HashSet<>();
            List<Integer> forCards = bankomatDto.getCardTypesFor();

            for (Integer forCard : forCards) {
                  if (forCard == 1) {
                        forCardsList.add(cardTypeRepository.findByCardTypeName(CardTypeName.UZCARD));
                  } else if (forCard == 2) {
                        forCardsList.add(cardTypeRepository.findByCardTypeName(CardTypeName.HUMO));
                  } else if (forCard == 3) {
                        forCardsList.add(cardTypeRepository.findByCardTypeName(CardTypeName.VISA));
                  }
            }   bankATM.setCardTypesFor(forCardsList);

            // IF MONEY IS LOW AT ATM, YOU WILL SEND A SMS.
            Optional<Employee> byIdEmployee = employeeRepository.findById(bankATM.getCreatedBy());
            if (!byIdEmployee.isPresent()) return new Result("Employee  not found",false);

              String email = byIdEmployee.get().getEmail();
            if (bankATM.getMinRestMoneyInSum().equals(bankATM.getCurrentBalanceInSum())){ sendEMail(email);}
            if (bankATM.getMinRestMoneyInDollar().equals(bankATM.getCurrentBalanceInDollar())){ sendEMail(email);}

            bankATMRepository.save(bankATM);
            return new Result("atm  edited", true);
      }

      public Result deleteBankATM(Integer id){
            try {
                  bankATMRepository.deleteById(id);
                  return new Result("atm  deleted",true);
            }catch (Exception e){
                  return new Result("atm  not deleted",false);
            }
      }

      public List<Atm> getBankATMs() {
            return bankATMRepository.findAll();
      }

      public Boolean sendEMail(String sendingEmail){
            try {
                  SimpleMailMessage mailMessage = new SimpleMailMessage();
                  mailMessage.setFrom("aslon.dinov@gmail.com");
                  mailMessage.setTo(sendingEmail);
                  mailMessage.setSubject("minimum level !!!");
                  mailMessage.setText("minimum level of money reached");
                  javaMailSender.send(mailMessage);
                  return true;

            }catch (Exception e){
                  return false;
            }
      }

}

