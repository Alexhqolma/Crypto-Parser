package mongo.db.cryptoparser.service.impl;

import java.util.List;
import mongo.db.cryptoparser.model.ReportRow;
import mongo.db.cryptoparser.service.ReportGenerator;
import org.springframework.stereotype.Component;

@Component
public class ReportGeneratorImpl implements ReportGenerator {

    @Override
    public String generateReport(List<ReportRow> row) {
        StringBuilder contentToWrite = new StringBuilder();
        contentToWrite.append("Cryptocurrency Name, Min Price, Max Price")
                .append(System.lineSeparator());
        for (int i = 0; i < row.size(); i++) {
            contentToWrite.append(row.get(i).getCurrencyName())
                    .append(",")
                    .append(row.get(i).getMinValue())
                    .append(",")
                    .append(row.get(i).getMaxValue())
                    .append(System.lineSeparator());
        }
        return contentToWrite.toString();
    }
}
