package mongo.db.cryptoparser.controller;

import java.util.List;
import mongo.db.cryptoparser.dto.ApiCurrencyResponseDto;
import mongo.db.cryptoparser.service.CurrencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cryptocurrencies")
@RestController
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/minprice")
    public ApiCurrencyResponseDto minPrice(@RequestParam("name") String currencyName) {
        return currencyService.getMinPrice(currencyName);
    }

    @GetMapping("/maxprice")
    public ApiCurrencyResponseDto maxPrice(@RequestParam("name") String currencyName) {
        return currencyService.getMaxPrice(currencyName);
    }

    @GetMapping
    public List<ApiCurrencyResponseDto> getAllCurrency(
            @RequestParam("name") String currencyName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return currencyService.getAllCurrency(currencyName, page, size);
    }

    @GetMapping("/csv")
    public String createCsvFile() {
        currencyService.createCsvFile();
        return "Created!";
    }
}
