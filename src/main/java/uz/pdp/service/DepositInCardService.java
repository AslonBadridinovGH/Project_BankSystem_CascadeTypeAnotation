package uz.pdp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.repository.BankATMRepository;
import uz.pdp.repository.CardRepository;
import uz.pdp.repository.DepositCardRepository;
import uz.pdp.repository.EmployeeRepository;
import uz.pdp.entity.Atm;
import uz.pdp.entity.BankCard;
import uz.pdp.entity.DepositInCard;
import uz.pdp.entity.enums.BankNoteName;
import uz.pdp.payload.DepositInCardDto;
import uz.pdp.payload.Result;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class DepositInCardService {

      @Autowired
      DepositCardRepository depositCardRepository;

      @Autowired
      CardRepository cardRepository;

      @Autowired
      BankATMRepository bankATMRepository;

      @Autowired
      EmployeeRepository employeeRepository;



     public Result addDepositInCard(DepositInCardDto depositDto){

           Optional<BankCard> optionalCard = cardRepository.findById(depositDto.getCardId());
          if (!optionalCard.isPresent()) {
              return new Result("Card not found",false);
          }

          if (!optionalCard.get().isEnabled()) {
              return new Result("Card not enable",false);
          }

          Optional<Atm> byId = bankATMRepository.findById(depositDto.getAtmID());
          if (!byId.isPresent()) {
              return new Result("ATM not found",false);
          }

              DepositInCard depositInCard=new DepositInCard();
            Atm bankATM = byId.get();
            BankCard card = optionalCard.get();

          boolean own = (card.getBankName().getId()).equals(bankATM.getBank().getId());
          double balanceATMInSum =    bankATM.getCurrentBalanceInSum();
          double balanceATMInDollar = bankATM.getCurrentBalanceInDollar();

          double balanceCardInSum = card.getBalanceInSum();
          double balanceCardInDollar = card.getBalanceInDollar();

          double amountDeposit  = depositDto.getAmount() * depositDto.getBankNote();
          double commissionATMOwnCard =     amountDeposit/100 * bankATM.getCommissionDepositOwnCardInPercent();
          double commissionATMForeignCard = amountDeposit/100 * bankATM.getCommissionDepositForeignCardInPercent();

         if (depositDto.getBankNote() == 1000)       {
             depositInCard.setBankNote(BankNoteName.THOUSAND_SUM.name());
         }
         else if (depositDto.getBankNote() == 5000)  {
             depositInCard.setBankNote(BankNoteName.FIVE_THOUSAND_SUM.name());
         }
         else if (depositDto.getBankNote() == 10000) {
             depositInCard.setBankNote(BankNoteName.TEN_THOUSAND_SUM.name());
         }
         else if (depositDto.getBankNote() == 50000) {
             depositInCard.setBankNote(BankNoteName.FIFTY_THOUSAND_SUM.name());
         }
         else if (depositDto.getBankNote() == 100000) {
             depositInCard.setBankNote(BankNoteName.HUNDRED_THOUSAND_SUM.name());
         }
         else if (depositDto.getBankNote() == 1)     {
             depositInCard.setBankNote(BankNoteName.ONE_DOLLAR.name());
         }
         else if (depositDto.getBankNote() == 5)     {
             depositInCard.setBankNote(BankNoteName.FIVE_DOLLAR.name());
         }
         else if (depositDto.getBankNote() == 10)    {
             depositInCard.setBankNote(BankNoteName.TEN_DOLLAR.name());
         }
         else if (depositDto.getBankNote() == 20)    {
             depositInCard.setBankNote(BankNoteName.TWENTY_DOLLAR.name());
         }
         else if (depositDto.getBankNote() == 50)    {
             depositInCard.setBankNote(BankNoteName.FIFTY_DOLLAR.name());
         }
         else if (depositDto.getBankNote() == 100)   {
             depositInCard.setBankNote(BankNoteName.HUNDRED_DOLLAR.name());
         }
         else {
             return new Result("Banknote not found. Choose 1000,5000,10000,50000,100000 for Sum " +
                     "And 1,5,10,20,50,100 for Dollar And Choose 1 for Sum, 2 for Dollar Currency ", false);
         }

          depositInCard.setAtm(bankATM);
          depositInCard.setCard(card);
          depositInCard.setDate(LocalDate.now());
          depositInCard.setAmount(depositDto.getAmount());
          depositInCard.setCurrency(depositDto.getCurrency());

          // SÖM
          int [] arraySum=new int[]{ 1000, 5000, 10000, 50000, 100000 };
          int a=0;
          for (int i : arraySum) {
              if (i==depositDto.getBankNote()){
                  a++;
              }
          }

          // DOLLAR
          int [] arrayDollar=new int[] {1,5,10,20,50,100};
          int b =0;
          for (int i : arrayDollar) {
              if (i==depositDto.getBankNote()){
                  b++;
              }
          }

          if (bankATM.getBudgetFromCommissionSum()==null) bankATM.setBudgetFromCommissionSum(0.00);
          if (bankATM.getBudgetFromCommissionDollar()==null) bankATM.setBudgetFromCommissionDollar(0.00);

               if (depositDto.getCurrency()==1 && a>0 && own) {
              bankATM.setCurrentBalanceInSum(balanceATMInSum + amountDeposit);
              bankATM.setBudgetFromCommissionSum(bankATM.getBudgetFromCommissionSum()+commissionATMOwnCard);
              card.setBalanceInSum(balanceCardInSum + amountDeposit - commissionATMOwnCard);
          }
          else if (depositDto.getCurrency()==1 && a>0 && !own) {
              bankATM.setCurrentBalanceInSum(balanceATMInSum + amountDeposit);
              bankATM.setBudgetFromCommissionSum(bankATM.getBudgetFromCommissionSum()+commissionATMForeignCard);
              card.setBalanceInSum(balanceCardInSum + amountDeposit - commissionATMForeignCard);
          }
          else if (depositDto.getCurrency()==2 && b>0 && own)  {
              bankATM.setCurrentBalanceInDollar(balanceATMInDollar+ amountDeposit);
              bankATM.setBudgetFromCommissionDollar(bankATM.getBudgetFromCommissionDollar()+commissionATMOwnCard);
              card.setBalanceInDollar(balanceCardInDollar+ amountDeposit - commissionATMOwnCard);
          }
          else if (depositDto.getCurrency()==2 && b>0 && !own) {
              bankATM.setCurrentBalanceInDollar(balanceATMInDollar+amountDeposit );
              bankATM.setBudgetFromCommissionDollar(bankATM.getBudgetFromCommissionDollar()+commissionATMForeignCard);
              card.setBalanceInDollar(balanceCardInDollar+amountDeposit- commissionATMForeignCard);
          }
          else {
              return new Result("Banknote not found. Choose 1000,5000,10000,50000,100000 for Sum " +
                      "And 1,5,10,20,50,100 for Dollar And Choose 1 for Sum, 2 for Dollar Currency ", false);
          }

          depositCardRepository.save(depositInCard);
          return new Result("depositCard saved",true);
      }

     public Result editDepositInCardById(DepositInCardDto depositDto, Integer id) {

          Optional<DepositInCard> optionalDepositInCard = depositCardRepository.findById(id);
          if (!optionalDepositInCard.isPresent())
              return new Result("DepositInCard not found",false);

          Optional<BankCard> optionalCard = cardRepository.findById(depositDto.getCardId());
          if (!optionalCard.isPresent())
              return new Result("Card not found",false);

          if (!optionalCard.get().isEnabled())
              return new Result("Card not enable",false);

          Optional<Atm> byId = bankATMRepository.findById(depositDto.getAtmID());
          if (!byId.isPresent())
              return new Result("ATM not found",false);

          DepositInCard depositInCard=optionalDepositInCard.get();
          Atm bankATM = byId.get();
          BankCard card = optionalCard.get();

          boolean own = (card.getBankName().getId()).equals(bankATM.getBank().getId());
          double balanceATMInSum = bankATM.getCurrentBalanceInSum();
          double balanceATMInDollar = bankATM.getCurrentBalanceInDollar();

          double balanceCardInSum = card.getBalanceInSum();
          double balanceCardInDollar = card.getBalanceInDollar();

          double amountDeposit  = depositDto.getAmount() * depositDto.getBankNote();
          double commissionATMOwnCard = bankATM.getCommissionDepositOwnCardInPercent()/100*amountDeposit;
          double commissionATMForeignCard = bankATM.getCommissionDepositForeignCardInPercent()/100*amountDeposit;


         if (depositDto.getBankNote() == 1000) {
             depositInCard.setBankNote(BankNoteName.THOUSAND_SUM.name());
         }
         else if (depositDto.getBankNote() == 5000) {
             depositInCard.setBankNote(BankNoteName.FIVE_THOUSAND_SUM.name());
         }
         else if (depositDto.getBankNote() == 10000) {
             depositInCard.setBankNote(BankNoteName.TEN_THOUSAND_SUM.name());
         }
         else if (depositDto.getBankNote() == 50000) {
             depositInCard.setBankNote(BankNoteName.FIVE_THOUSAND_SUM.name());
         }
         else if (depositDto.getBankNote() == 100000) {
             depositInCard.setBankNote(BankNoteName.HUNDRED_THOUSAND_SUM.name());
         }
         else if (depositDto.getBankNote() == 1) {
             depositInCard.setBankNote(BankNoteName.ONE_DOLLAR.name());
         }
         else if (depositDto.getBankNote() == 5) {
             depositInCard.setBankNote(BankNoteName.FIVE_DOLLAR.name());
         }
         else if (depositDto.getBankNote() == 10) {
             depositInCard.setBankNote(BankNoteName.TEN_DOLLAR.name());
         }
         else if (depositDto.getBankNote() == 20) {
             depositInCard.setBankNote(BankNoteName.TWENTY_DOLLAR.name());
         }
         else if (depositDto.getBankNote() == 50) {
             depositInCard.setBankNote(BankNoteName.FIFTY_DOLLAR.name());
         }
         else if (depositDto.getBankNote() == 100) {
             depositInCard.setBankNote(BankNoteName.HUNDRED_DOLLAR.name());
         }
         else {
             return new Result("Banknote not found. Choose 1000,5000,10000,50000,100000 for Sum " +
                     "And 1,5,10,20,50,100 for Dollar And Choose 1 for Sum, 2 for Dollar Currency ", false);
         }

            depositInCard.setAtm(bankATM);
           depositInCard.setCard(card);

          try {
              depositInCard.setDate(LocalDate.parse(depositDto.getDate()));
          } catch (Exception e) {
              return new Result("enter  date(yyyy-MM-dd.)",false);
          }

          // SPLIT AMOUNT INTO COUPONS OR CUSTOMER CHOOSES COUPON AMOUNT
          depositInCard.setAmount(depositDto.getAmount());
          depositInCard.setCurrency(depositDto.getCurrency());

          // SPLIT AMOUNT INTO COUPONS OR CUSTOMER CHOOSES COUPON AMOUNT
          int [] arraySum=new int[]{ 1000, 5000, 10000, 50000, 100000 };
          int a=0;
          for (int i : arraySum) {
              if (i==depositDto.getBankNote()){
                  a++;
              }
          }
          int [] arrayDollar=new int[] {1,5,10,20,50,100};
          int b =0;
          for (int i : arrayDollar) {
              if (i==depositDto.getBankNote()){
                  b++;
              }
          }

         if (bankATM.getBudgetFromCommissionSum()==null) bankATM.setBudgetFromCommissionSum(0.00);
         if (bankATM.getBudgetFromCommissionDollar()==null) bankATM.setBudgetFromCommissionDollar(0.00);

         if (depositDto.getCurrency()==1 && a>0 && own) {
             bankATM.setCurrentBalanceInSum(balanceATMInSum + amountDeposit);
             bankATM.setBudgetFromCommissionSum(bankATM.getBudgetFromCommissionSum()+commissionATMOwnCard);
             card.setBalanceInSum(balanceCardInSum + amountDeposit - commissionATMOwnCard);
         }
         else if (depositDto.getCurrency()==1 && a>0 && !own) {
             bankATM.setCurrentBalanceInSum(balanceATMInSum + amountDeposit);
             bankATM.setBudgetFromCommissionSum(bankATM.getBudgetFromCommissionSum()+commissionATMForeignCard);
             card.setBalanceInSum(balanceCardInSum + amountDeposit - commissionATMForeignCard);
         }
         else if (depositDto.getCurrency()==2 && b>0 && own)  {
             bankATM.setCurrentBalanceInDollar(balanceATMInDollar+ amountDeposit);
             bankATM.setBudgetFromCommissionDollar(bankATM.getBudgetFromCommissionDollar()+commissionATMOwnCard);
             card.setBalanceInDollar(balanceCardInDollar+ amountDeposit - commissionATMOwnCard);
         }
         else if (depositDto.getCurrency()==2 && b>0 && !own) {
             bankATM.setCurrentBalanceInDollar(balanceATMInDollar+amountDeposit );
             bankATM.setBudgetFromCommissionDollar(bankATM.getBudgetFromCommissionDollar()+commissionATMForeignCard);
             card.setBalanceInDollar(balanceCardInDollar+amountDeposit- commissionATMForeignCard);
         }
          else {
              return new Result("Banknote not found. Choose 1000,5000,10000,50000,100000 for Sum " +
                      "And 1,5,10,20,50,100 for Dollar And Choose 1 for Sum, 2 for Dollar Currency ", false);
          }
          depositCardRepository.save(depositInCard);
         return new Result("DepositInCard edited", true);
      }

     public List<DepositInCard> getDepositInCards()  {
        return depositCardRepository.findAll();
    }

     public DepositInCard getDepositInCardById(Integer id) {
        Optional<DepositInCard> optionalIncome = depositCardRepository.findById(id);
        return optionalIncome.orElse(null);
    }

     public Result deleteDepositInCard(Integer id) {
            try {
                  depositCardRepository.deleteById(id);
                  return new Result("Income deleted", true);
            } catch (Exception e) {
                  return new Result("Income not deleted", false);
            }
      }

     public List<DepositInCard> getDepositInCardByATMId(Integer id) {
        return depositCardRepository.findAllByAtmId(id);
     }

     public ApiResponse getDailyDepositInCardByATMId(Integer id, String day) {
         LocalDate date = null;
         try {
             date=LocalDate.parse(day);
         } catch (Exception e) {
           new Result("enter  date(yyyy-MM-dd.)",false);
         }
         List<DepositInCard> allByAtmIdAndDate = depositCardRepository.findAllByAtmIdAndDate(id, date);
         return new ApiResponse(" ",true, allByAtmIdAndDate);
     }

}

