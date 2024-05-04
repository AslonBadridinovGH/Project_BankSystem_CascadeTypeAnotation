package uz.pdp.payload;

import lombok.Data;

@Data
public class DepositATMDto {


    private  Integer   currency;       // 1 va 2 söm dollar
    private  Integer   bankNote;       // 1000, 5000,10 000,50 000,100 000
    private  Integer   amount;
    private  String    date;
    private  Integer atmID;

    private  Integer   fromCardId;
}
