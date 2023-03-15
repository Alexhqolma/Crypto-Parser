package mongo.db.cryptoparser.service;

import java.util.List;
import mongo.db.cryptoparser.model.ReportRow;

public interface ReportGenerator {
    String generateReport(List<ReportRow> row);
}
