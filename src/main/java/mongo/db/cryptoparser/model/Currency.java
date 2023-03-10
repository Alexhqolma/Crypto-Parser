package mongo.db.cryptoparser.model;

import java.math.BigInteger;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Currency {
    @Id
    private BigInteger id;
    private double lprice;
    private String curr1;
    private String curr2;
}
