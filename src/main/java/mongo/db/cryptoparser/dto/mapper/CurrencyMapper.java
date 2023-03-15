package mongo.db.cryptoparser.dto.mapper;

import mongo.db.cryptoparser.dto.ApiCurrencyDto;
import mongo.db.cryptoparser.dto.CurrencyDto;
import mongo.db.cryptoparser.model.Currency;
import org.springframework.stereotype.Component;

@Component
public class CurrencyMapper {
    public Currency toModel(ApiCurrencyDto apiCurrencyDto) {
        Currency currency = new Currency();
        currency.setPrice(apiCurrencyDto.getPrice());
        currency.setCryptoCurrency(apiCurrencyDto.getCryptoCurrency());
        currency.setFiatCurrency(apiCurrencyDto.getFiatCurrency());
        return currency;
    }

    public Currency toModel(CurrencyDto currencyDto) {
        Currency currency = new Currency();
        currency.setPrice(currencyDto.getPrice());
        currency.setCryptoCurrency(currencyDto.getCryptoCurrency());
        currency.setFiatCurrency(currencyDto.getFiatCurrency());
        return currency;
    }

    public CurrencyDto toDto(Currency currency) {
        CurrencyDto apiCurrencyResponseDto = new CurrencyDto();
        apiCurrencyResponseDto.setId(currency.getId());
        apiCurrencyResponseDto.setPrice(currency.getPrice());
        apiCurrencyResponseDto.setCryptoCurrency(currency.getCryptoCurrency());
        apiCurrencyResponseDto.setFiatCurrency(currency.getFiatCurrency());
        return apiCurrencyResponseDto;
    }
}
