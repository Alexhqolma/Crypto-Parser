package mongo.db.cryptoparser.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ReportRow {
    private String currencyName;
    private BigDecimal maxValue;
    private BigDecimal minValue;
}
