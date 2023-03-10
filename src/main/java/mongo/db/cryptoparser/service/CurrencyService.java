package mongo.db.cryptoparser.service;

import java.util.List;
import mongo.db.cryptoparser.dto.ApiCurrencyResponseDto;

public interface CurrencyService {

    void syncExternalCurrencyBtc();

    void syncExternalCurrencyEth();

    void syncExternalCurrencyXrp();

    ApiCurrencyResponseDto getMinPrice(String currencyName);

    ApiCurrencyResponseDto getMaxPrice(String currencyName);

    List<ApiCurrencyResponseDto> getAllCurrency(String currencyName, int page, int size);

    String createCsvFile();
}
