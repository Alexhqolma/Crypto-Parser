package mongo.db.cryptoparser.dto;

import java.math.BigInteger;
import lombok.Data;

@Data
public class CurrencyDto {
    private BigInteger id;
    private double price;
    private String cryptoCurrency;
    private String fiatCurrency;
}
