package uz.pdp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.repository.BankATMRepository;
import uz.pdp.repository.CardRepository;
import uz.pdp.repository.DepositATMRepository;
import uz.pdp.entity.Atm;
import uz.pdp.entity.BankCard;
import uz.pdp.entity.DepositInATM;
import uz.pdp.entity.enums.BankNoteName;
import uz.pdp.payload.DepositATMDto;
import uz.pdp.payload.Result;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class DepositInATMService {

      @Autowired
      DepositATMRepository depositATMRepository;

      @Autowired
      BankATMRepository bankATMRepository;

      @Autowired
      CardRepository cardRepository;



      public Result addIncome(DepositATMDto depositDto){

          Optional<Atm> byId = bankATMRepository.findById(depositDto.getAtmID());
          if (!byId.isPresent())  return new Result("ATM not found",false);

          Optional<BankCard> byId1 = cardRepository.findById(depositDto.getFromCardId());
          if (!byId1.isPresent())  return new Result("Card not found",false);


             Atm bankATM = byId.get();

          double balanceInSum = bankATM.getCurrentBalanceInSum();
          double balanceInDollar = bankATM.getCurrentBalanceInDollar();

            DepositInATM depositInATM=new DepositInATM();

              if (depositDto.getBankNote() == 1000)    {
              depositInATM.setBankNote(BankNoteName.THOUSAND_SUM.name());
          }
          else if (depositDto.getBankNote() == 5000)   {
              depositInATM.setBankNote(BankNoteName.FIVE_THOUSAND_SUM.name());
          }
          else if (depositDto.getBankNote() == 10000)  {
              depositInATM.setBankNote(BankNoteName.TEN_THOUSAND_SUM.name());
          }
          else if (depositDto.getBankNote() == 50000)  {
              depositInATM.setBankNote(BankNoteName.FIFTY_THOUSAND_SUM.name());
          }
          else if (depositDto.getBankNote() == 100000) {
              depositInATM.setBankNote(BankNoteName.HUNDRED_THOUSAND_SUM.name());
          }
          else if (depositDto.getBankNote() == 1)      {
              depositInATM.setBankNote(BankNoteName.ONE_DOLLAR.name());
          }
          else if (depositDto.getBankNote() == 5)      {
              depositInATM.setBankNote(BankNoteName.FIVE_DOLLAR.name());
          }
          else if (depositDto.getBankNote() == 10)     {
              depositInATM.setBankNote(BankNoteName.TEN_DOLLAR.name());
          }
          else if (depositDto.getBankNote() == 20)     {
              depositInATM.setBankNote(BankNoteName.TWENTY_DOLLAR.name());
          }
          else if (depositDto.getBankNote() == 50)     {
              depositInATM.setBankNote(BankNoteName.FIFTY_DOLLAR.name());
          }
          else if (depositDto.getBankNote() == 100)    {
              depositInATM.setBankNote(BankNoteName.HUNDRED_DOLLAR.name());
          }
          else {
              return new Result("Banknote not found. Choose 1000,5000,10000,50000,100000 for Sum " +
              "And 1,5,10,20,50,100 for Dollar And Choose 1 for Sum, 2 for Dollar Currency ", false);
          }

          // PUL QÖYISHDA BERILGAN SUMMANI KUPURA/GA BÖLISH YOKI MIJOZ va EMPLOYEE KUPURA TANLAYDI VA NECHTALIGINI
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


               if (depositDto.getCurrency()==1 && a>0) {
             bankATM.setCurrentBalanceInSum(balanceInSum+(depositDto.getAmount()*depositDto.getBankNote()));
          }
          else if (depositDto.getCurrency()==2 && b>0) {
              bankATM.setCurrentBalanceInDollar(balanceInDollar+(depositDto.getAmount()*depositDto.getBankNote()));
          }
          else {
              return new Result("Banknote not found. Choose 1000,5000,10000,50000,100000 for Sum " +
              "And 1,5,10,20,50,100 for Dollar And Choose 1 for Sum, 2 for Dollar Currency ", false);
          }

          depositInATM.setAtm(bankATM);
          depositInATM.setAmount(depositDto.getAmount());
          depositInATM.setBankCard(byId1.get());

          try {
                 depositInATM.setDate(LocalDate.parse(depositDto.getDate()));
             } catch (Exception e) {
                 return new Result("enter  date(yyyy-MM-dd.)",false);
             }
           depositATMRepository.save(depositInATM);
           return new Result("Deposit saved ",true);
      }

      public List<DepositInATM> getIncomes() {
              return depositATMRepository.findAll();
        }



      public List<DepositInATM> getAllFillMoneyByATMId(Integer id) {
          return depositATMRepository.findAllByAtmId(id);
      }

      public Result editIncomeById(DepositATMDto depositDto, Integer id) {

           Optional<DepositInATM> optionalDepositInATM = depositATMRepository.findById(id);
           if (!optionalDepositInATM.isPresent())
               return new Result("DepositInATM not found", false);

           Optional<Atm> byId = bankATMRepository.findById(depositDto.getAtmID());
           if (!byId.isPresent())
               return new Result("ATM not found",false);

          Optional<BankCard> byId1 = cardRepository.findById(depositDto.getFromCardId());
          if (!byId1.isPresent())  return new Result("Card not found",false);

                Atm bankATM = byId.get();
              double balanceInSum = bankATM.getCurrentBalanceInSum();
              double balanceInDollar = bankATM.getCurrentBalanceInDollar();
            DepositInATM depositInATM = optionalDepositInATM.get();

          if (depositDto.getBankNote() == 1000) {
              depositInATM.setBankNote(BankNoteName.THOUSAND_SUM.name());
          }
          else if (depositDto.getBankNote() == 5000) {
              depositInATM.setBankNote(BankNoteName.FIVE_THOUSAND_SUM.name());
          }
          else if (depositDto.getBankNote() == 10000) {
              depositInATM.setBankNote(BankNoteName.TEN_THOUSAND_SUM.name());
          }
          else if (depositDto.getBankNote() == 50000) {
              depositInATM.setBankNote(BankNoteName.FIVE_THOUSAND_SUM.name());
          }
          else if (depositDto.getBankNote() == 100000) {
              depositInATM.setBankNote(BankNoteName.HUNDRED_THOUSAND_SUM.name());
          }
          else if (depositDto.getBankNote() == 1) {
              depositInATM.setBankNote(BankNoteName.ONE_DOLLAR.name());
          }
          else if (depositDto.getBankNote() == 5) {
              depositInATM.setBankNote(BankNoteName.FIVE_DOLLAR.name());
          }
          else if (depositDto.getBankNote() == 10) {
              depositInATM.setBankNote(BankNoteName.TEN_DOLLAR.name());
          }
          else if (depositDto.getBankNote() == 20) {
              depositInATM.setBankNote(BankNoteName.TWENTY_DOLLAR.name());
          }
          else if (depositDto.getBankNote() == 50) {
              depositInATM.setBankNote(BankNoteName.FIFTY_DOLLAR.name());
          }
          else if (depositDto.getBankNote() == 100) {
              depositInATM.setBankNote(BankNoteName.HUNDRED_DOLLAR.name());
          }
          else {
              return new Result("Banknote was not found. Choose 1000,5000,10000,50000,100000 for Sum " +
                      "And 1,5,10,20,50,100 for Dollar And Choose 1 for Sum, 2 for Dollar Currency ", false);
          }

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

                if (depositDto.getCurrency()==1 && a>0) {
               bankATM.setCurrentBalanceInSum(balanceInSum+(depositDto.getAmount()*depositDto.getBankNote()));
           }
           else if (depositDto.getCurrency()==2 && b>0) {
               bankATM.setCurrentBalanceInDollar(balanceInDollar+(depositDto.getAmount()*depositDto.getBankNote()));
           }
           else {
               return new Result("Banknote not found. Choose 1000,5000,10000,50000,100000 for Sum " +
                       "And 1,5,10,20,50,100 for Dollar And Choose 1 for Sum, 2 for Dollar Currency ", false);
           }

           depositInATM.setAtm(byId.get());

           // DIVIDING THE AMOUNT GIVEN IN THE DEPOSIT INTO COUPON OR THE CUSTOMER CHOOSES THE COUPON AND HOW MUCH
           depositInATM.setAmount(depositDto.getAmount());

           try {
               depositInATM.setDate(LocalDate.parse(depositDto.getDate()));
           } catch (Exception e) {
               return new Result("enter  date(yyyy-MM-dd.)",false);
           }
           depositInATM.setBankCard(byId1.get());
           depositATMRepository.save(depositInATM);
           return new Result("Deposit edited ",true);


      }

      public Result deleteIncome(Integer id) {
            try {
                  depositATMRepository.deleteById(id);
                  return new Result("Income deleted", true);
            } catch (Exception e) {
                  return new Result("Income not deleted", false);
            }

      }
}






