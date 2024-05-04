package uz.pdp.payload;

import lombok.Data;

@Data
public class DepositInCardDto {

    private  Integer   currency;       // 1:söm va 2:dollar
    private  Integer   bankNote;
    private  Integer   amount;
    private  Integer atmID;
    private  Integer      cardId;
    private  String    date;

}
