package uz.pdp.payload;
import lombok.Data;


@Data
public class WithdrawFromCardDto {


     private  Integer   currency;

     private  Integer   bankNote;

     private  Integer   amount;

     private  String   date;

     private  Integer atmID;

     private  Integer      cardID;

}
