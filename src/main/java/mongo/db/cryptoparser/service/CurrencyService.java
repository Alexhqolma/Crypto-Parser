package mongo.db.cryptoparser.service;

import java.io.FileNotFoundException;
import java.util.List;
import mongo.db.cryptoparser.dto.CurrencyDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

public interface CurrencyService {

    CurrencyDto getMinPrice(String currencyName);

    CurrencyDto getMaxPrice(String currencyName);

    List<CurrencyDto> getAll(String currencyName, int page, int size);

    String createCsvFile();

    ResponseEntity<InputStreamResource> createFileReport() throws FileNotFoundException;
}
