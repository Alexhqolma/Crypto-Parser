package mongo.db.cryptoparser.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ReportModel {
    private String currencyName;
    private BigDecimal maxValue;
    private BigDecimal minValue;
}
