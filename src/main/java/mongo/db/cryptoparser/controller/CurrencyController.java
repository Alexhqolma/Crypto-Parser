package mongo.db.cryptoparser.controller;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import mongo.db.cryptoparser.dto.CurrencyDto;
import mongo.db.cryptoparser.dto.mapper.CurrencyMapper;
import mongo.db.cryptoparser.service.CurrencyService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cryptocurrencies")
@RestController
public class CurrencyController {
    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

    public CurrencyController(CurrencyService currencyService,
                              CurrencyMapper currencyMapper) {
        this.currencyService = currencyService;
        this.currencyMapper = currencyMapper;
    }

    @GetMapping("/minprice")
    public CurrencyDto getMinPrice(@Valid @RequestParam("name") String currencyName) {
        return currencyMapper.toDto(currencyService.getMinPrice(currencyName));
    }

    @GetMapping("/maxprice")
    public CurrencyDto getMaxPrice(@Valid @RequestParam("name") String currencyName) {
        return currencyMapper.toDto(currencyService.getMaxPrice(currencyName));
    }

    @GetMapping
    public List<CurrencyDto> getAll(
            @RequestParam("name") String currencyName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return currencyService.getAll(currencyName, page, size)
                .stream()
                .map(currencyMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/csv")
    public ResponseEntity<InputStreamResource> createFileReport() throws FileNotFoundException {
        currencyService.createCsvFile();
        return currencyService.createFileReport();
    }
}
