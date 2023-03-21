package mongo.db.cryptoparser.service;

import java.io.FileNotFoundException;
import java.util.List;
import mongo.db.cryptoparser.model.Currency;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

public interface CurrencyService {

    Currency getMinPrice(String currencyName);

    Currency getMaxPrice(String currencyName);

    List<Currency> getAll(String currencyName, int page, int size);

    String createCsvFile();

    ResponseEntity<InputStreamResource> createFileReport();
}
