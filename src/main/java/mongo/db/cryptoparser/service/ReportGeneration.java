package mongo.db.cryptoparser.service;

import java.util.List;
import mongo.db.cryptoparser.model.ReportModel;

public interface ReportGeneration {
    String generateReport(List<ReportModel> list);
}
