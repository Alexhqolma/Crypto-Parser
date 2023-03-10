package mongo.db.cryptoparser.service.impl;

import java.util.List;
import mongo.db.cryptoparser.model.ReportModel;
import mongo.db.cryptoparser.service.ReportGeneration;

public class ReportGenerationImpl implements ReportGeneration {

    @Override
    public String generateReport(List<ReportModel> list) {
        StringBuilder contentToWrite = new StringBuilder();
        contentToWrite.append("Cryptocurrency Name, Min Price, Max Price")
                .append(System.lineSeparator());
        for (int i = 0; i < list.size(); i++) {
            contentToWrite.append(list.get(i).getCurrencyName())
                    .append(",")
                    .append(list.get(i).getMinValue())
                    .append(",")
                    .append(list.get(i).getMaxValue())
                    .append(System.lineSeparator());
        }
        return contentToWrite.toString();
    }
}
