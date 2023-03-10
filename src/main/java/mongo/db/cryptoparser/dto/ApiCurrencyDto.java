package mongo.db.cryptoparser.dto;

import java.math.BigInteger;
import lombok.Data;

@Data
public class ApiCurrencyDto {
    private BigInteger id;
    private double lprice;
    private String curr1;
    private String curr2;
}
