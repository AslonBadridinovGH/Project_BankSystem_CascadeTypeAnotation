package uz.pdp.payload;
import lombok.Data;

import java.util.UUID;

@Data

public class BankDto {

    private String     name;

    private Integer       addressId;

    // private List<UUID> bankATMs;
}
