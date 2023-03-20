package mongo.db.cryptoparser.dto;

import java.math.BigInteger;
import lombok.Data;
import mongo.db.cryptoparser.validation.CurrencyType;

@Data
public class CurrencyDto {
    private BigInteger id;
    private double price;
    @CurrencyType
    private String cryptoCurrency;
    private String fiatCurrency;
}
