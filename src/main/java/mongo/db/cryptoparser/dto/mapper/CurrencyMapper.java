package mongo.db.cryptoparser.dto.mapper;

import mongo.db.cryptoparser.dto.ApiCurrencyDto;
import mongo.db.cryptoparser.dto.ApiCurrencyResponseDto;
import mongo.db.cryptoparser.model.Currency;
import org.springframework.stereotype.Component;

@Component
public class CurrencyMapper {
    public Currency toModel(ApiCurrencyDto apiCurrencyDto) {
        Currency currency = new Currency();
        currency.setLprice(apiCurrencyDto.getLprice());
        currency.setCurr1(apiCurrencyDto.getCurr1());
        currency.setCurr2(apiCurrencyDto.getCurr2());
        return currency;
    }

    public ApiCurrencyResponseDto toDto(Currency currency) {
        ApiCurrencyResponseDto apiCurrencyResponseDto = new ApiCurrencyResponseDto();
        apiCurrencyResponseDto.setId(currency.getId());
        apiCurrencyResponseDto.setLprice(currency.getLprice());
        apiCurrencyResponseDto.setCurr1(currency.getCurr1());
        apiCurrencyResponseDto.setCurr2(currency.getCurr2());
        return apiCurrencyResponseDto;
    }
}
