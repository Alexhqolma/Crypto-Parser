package mongo.db.cryptoparser.service.impl;

import jakarta.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
    private static final String BTC = "BTC";
    private static final String ETH = "ETH";
    private static final String XRP = "XRP";
    private final CurrencyRepository currencyRepository;
    private final HttpClient httpClient;
    private final CurrencyMapper currencyMapper;
    private final FileWriter fileWriter;
    private final ReportGenerator reportGenerator;
    private final ServletContext servletContext;

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
        String currency;
        for (int i = 0; i < 3; i++) {
            currency = (i == 0) ? BTC : (i == 1) ? ETH : XRP;
            ApiCurrencyDto apiCurrencyDto =
                    httpClient.get("https://cex.io/api/last_price/" + currency + "/USD",
                            ApiCurrencyDto.class);
            currencyRepository.save(currencyMapper.toModel(apiCurrencyDto));
        }
    }

    @Override
    public CurrencyDto getMinPrice(String currencyName) {
        currencyName = currencyName.toUpperCase();
        if (currencyName.equals(BTC)
                || currencyName.equals(ETH)
                || currencyName.equals(XRP)) {
            List<CurrencyDto> list = getCurrency(currencyName);
            CurrencyDto api = getBasicObject(list);
            double min = list.get(0).getPrice();
            for (int i = 0; i < list.size(); i++) {
                if (min > list.get(i).getPrice()) {
                    min = list.get(i).getPrice();
                    api.setId(list.get(i).getId());
                    api.setCryptoCurrency(list.get(i).getCryptoCurrency());
                    api.setFiatCurrency(list.get(i).getFiatCurrency());
                    api.setPrice(list.get(i).getPrice());

                }
            }
            return api;
        } else {
            throw new InvalidCurrencyException("Wrong currency");
        }
    }

    @Override
    public CurrencyDto getMaxPrice(String currencyName) {
        currencyName = currencyName.toUpperCase();
        if (currencyName.equals(BTC)
                || currencyName.equals(ETH)
                || currencyName.equals(XRP)) {
            List<CurrencyDto> list = getCurrency(currencyName);
            CurrencyDto api = getBasicObject(list);
            double max = list.get(0).getPrice();
            for (int i = 0; i < list.size(); i++) {
                if (max < list.get(i).getPrice()) {
                    max = list.get(i).getPrice();
                    api.setId(list.get(i).getId());
                    api.setCryptoCurrency(list.get(i).getCryptoCurrency());
                    api.setFiatCurrency(list.get(i).getFiatCurrency());
                    api.setPrice(list.get(i).getPrice());

                }
            }
            return api;
        } else {
            throw new InvalidCurrencyException("Wrong currency");
        }
    }

    @Override
    public List<CurrencyDto> getAll(String currencyName, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Currency> list = currencyRepository
                .findByCryptoCurrencyOrderByPriceAsc(currencyName, pageRequest);
        return list.stream().map(currencyMapper::toDto).toList();
    }

    @Override
    public String createCsvFile() {
        fileWriter.writeDataToFile(reportGenerator.generateReport(getReportList()), WRITE_TO);
        return "Report Done!";
    }

    @Override
    public ResponseEntity<InputStreamResource> createFileReport() throws FileNotFoundException {
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, WRITE_TO);
        File file = new File(WRITE_TO);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(mediaType)
                .contentLength(file.length())
                .body(resource);
    }

    private List<CurrencyDto> getCurrency(String currencyName) {
        return currencyRepository.findByCryptoCurrencyOrderByPriceAsc(currencyName).stream()
                .map(currencyMapper::toDto)
                .toList();
    }

    private CurrencyDto getBasicObject(List<CurrencyDto> list) {
        CurrencyDto api = new CurrencyDto();
        api.setId(list.get(0).getId());
        api.setCryptoCurrency(list.get(0).getCryptoCurrency());
        api.setFiatCurrency(list.get(0).getFiatCurrency());
        api.setPrice(list.get(0).getPrice());
        return api;
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
        list.add(getCurrencyModel(BTC));
        list.add(getCurrencyModel(ETH));
        list.add(getCurrencyModel(XRP));
        return list;
    }
}
