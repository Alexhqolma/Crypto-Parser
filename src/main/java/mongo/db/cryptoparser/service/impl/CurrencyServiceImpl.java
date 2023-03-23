package mongo.db.cryptoparser.service.impl;

import jakarta.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mongo.db.cryptoparser.dto.ApiCurrencyDto;
import mongo.db.cryptoparser.dto.CurrencyDto;
import mongo.db.cryptoparser.dto.mapper.CurrencyMapper;
import mongo.db.cryptoparser.exception.InvalidCurrencyException;
import mongo.db.cryptoparser.model.Currency;
import mongo.db.cryptoparser.model.ReportRow;
import mongo.db.cryptoparser.repository.CurrencyRepository;
import mongo.db.cryptoparser.service.CurrencyService;
import mongo.db.cryptoparser.service.FileWriter;
import mongo.db.cryptoparser.service.HttpClient;
import mongo.db.cryptoparser.service.MediaTypeUtils;
import mongo.db.cryptoparser.service.ReportGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private static final String WRITE_TO = "src/main/resources/result.csv";
    private static final Set<String> currencies = Set.of("BTC", "ETH", "XRP");
    private final CurrencyRepository currencyRepository;
    private final HttpClient httpClient;
    private final CurrencyMapper currencyMapper;
    private final FileWriter fileWriter;
    private final ReportGenerator reportGenerator;
    private final ServletContext servletContext;
    @Value("${api.url}")
    private String apiUrl;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository,
                               HttpClient httpClient,
                               CurrencyMapper currencyMapper,
                               FileWriter fileWriter,
                               ReportGenerator reportGenerator,
                               ServletContext servletContext) {
        this.currencyRepository = currencyRepository;
        this.httpClient = httpClient;
        this.currencyMapper = currencyMapper;
        this.fileWriter = fileWriter;
        this.reportGenerator = reportGenerator;
        this.servletContext = servletContext;
    }

    @Scheduled(cron = "*/10 * * * * ?")
    public void syncExternalCurrencyBtc() {
        for (String currency: currencies) {
            ApiCurrencyDto apiCurrencyDto =
                    httpClient.get(apiUrl + currency + "/USD",
                            ApiCurrencyDto.class);
            currencyRepository.save(currencyMapper.toModel(apiCurrencyDto));
        }
    }

    @Override
    public Currency getMinPrice(String currencyName) {
        currencyName = currencyName.toUpperCase();
        if (currencies.contains(currencyName)) {
            List<Currency> list = getCurrency(currencyName);
            Currency api = getBasicObject(list);
            double min = list.get(0).getPrice();
            for (Currency currency : list) {
                if (min > currency.getPrice()) {
                    min = currency.getPrice();
                    api.setId(currency.getId());
                    api.setCryptoCurrency(currency.getCryptoCurrency());
                    api.setFiatCurrency(currency.getFiatCurrency());
                    api.setPrice(currency.getPrice());

                }
            }
            return api;
        } else {
            throw new InvalidCurrencyException("Wrong currency");
        }
    }

    @Override
    public Currency getMaxPrice(String currencyName) {
        currencyName = currencyName.toUpperCase();
        if (currencies.contains(currencyName)) {
            List<Currency> list = getCurrency(currencyName);
            Currency api = getBasicObject(list);
            double max = list.get(0).getPrice();
            for (Currency currency : list) {
                if (max < currency.getPrice()) {
                    max = currency.getPrice();
                    api.setId(currency.getId());
                    api.setCryptoCurrency(currency.getCryptoCurrency());
                    api.setFiatCurrency(currency.getFiatCurrency());
                    api.setPrice(currency.getPrice());

                }
            }
            return api;
        } else {
            throw new InvalidCurrencyException("Wrong currency");
        }
    }

    @Override
    public List<Currency> getAll(String currencyName, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Currency> list = currencyRepository
                .findByCryptoCurrencyOrderByPriceAsc(currencyName, pageRequest);
        return list.stream().map(currencyMapper::toDto).toList()
                .stream().map(currencyMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public String createCsvFile() {
        fileWriter.writeDataToFile(reportGenerator.generateReport(getReportList()), WRITE_TO);
        return "Report Done!";
    }

    @Override
    public ResponseEntity<InputStreamResource> createFileReport() {
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, WRITE_TO);
        File file = new File(WRITE_TO);
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Can't find file ", e);
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(mediaType)
                .contentLength(file.length())
                .body(resource);
    }

    private List<Currency> getCurrency(String currencyName) {
        return currencyRepository.findByCryptoCurrencyOrderByPriceAsc(currencyName);
    }

    private Currency getBasicObject(List<Currency> list) {
        CurrencyDto api = new CurrencyDto();
        api.setId(list.get(0).getId());
        api.setCryptoCurrency(list.get(0).getCryptoCurrency());
        api.setFiatCurrency(list.get(0).getFiatCurrency());
        api.setPrice(list.get(0).getPrice());
        return currencyMapper.toModel(api);
    }

    private ReportRow getCurrencyModel(String currency) {
        ReportRow currencyRow = new ReportRow();
        currencyRow.setCurrencyName(currency);
        currencyRow.setMaxValue(BigDecimal.valueOf(getMaxPrice(currency).getPrice()));
        currencyRow.setMinValue(BigDecimal.valueOf(getMinPrice(currency).getPrice()));
        return currencyRow;
    }

    private List<ReportRow> getReportList() {
        List<ReportRow> list = new ArrayList<>();
        for (String currency : currencies) {
            list.add(getCurrencyModel(currency));
        }
        return list;
    }
}
