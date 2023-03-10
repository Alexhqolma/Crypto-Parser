package mongo.db.cryptoparser.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import mongo.db.cryptoparser.dto.ApiCurrencyDto;
import mongo.db.cryptoparser.dto.ApiCurrencyResponseDto;
import mongo.db.cryptoparser.dto.mapper.CurrencyMapper;
import mongo.db.cryptoparser.exception.InvalidCurrencyException;
import mongo.db.cryptoparser.model.Currency;
import mongo.db.cryptoparser.model.ReportModel;
import mongo.db.cryptoparser.repository.CurrencyRepository;
import mongo.db.cryptoparser.service.CurrencyService;
import mongo.db.cryptoparser.service.FileWriter;
import mongo.db.cryptoparser.service.HttpClient;
import mongo.db.cryptoparser.service.ReportGeneration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final HttpClient httpClient;
    private final CurrencyMapper currencyMapper;
    private final String writeTo = "src/main/resources/result.csv";

    public CurrencyServiceImpl(CurrencyRepository currencyRepository,
                               HttpClient httpClient,
                               CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.httpClient = httpClient;
        this.currencyMapper = currencyMapper;
    }

    @Scheduled(cron = "*/10 * * * * ?")
    @Override
    public void syncExternalCurrencyBtc() {
        ApiCurrencyDto apiCurrencyDto =
                httpClient.get("https://cex.io/api/last_price/BTC/USD",
                        ApiCurrencyDto.class);
        currencyRepository.save(currencyMapper.toModel(apiCurrencyDto));
    }

    @Scheduled(cron = "*/10 * * * * ?")
    @Override
    public void syncExternalCurrencyEth() {
        ApiCurrencyDto apiCurrencyDto =
                httpClient.get("https://cex.io/api/last_price/ETH/USD",
                        ApiCurrencyDto.class);
        currencyRepository.save(currencyMapper.toModel(apiCurrencyDto));
    }

    @Scheduled(cron = "*/10 * * * * ?")
    @Override
    public void syncExternalCurrencyXrp() {
        ApiCurrencyDto apiCurrencyDto =
                httpClient.get("https://cex.io/api/last_price/XRP/USD",
                        ApiCurrencyDto.class);
        currencyRepository.save(currencyMapper.toModel(apiCurrencyDto));
    }

    @Override
    public ApiCurrencyResponseDto getMinPrice(String currencyName) {
        currencyName = currencyName.toUpperCase();
        if (currencyName.equals("BTC")
                || currencyName.equals("ETH")
                || currencyName.equals("XRP")) {
            List<ApiCurrencyResponseDto> list = getCurrency(currencyName);
            ApiCurrencyResponseDto api = getBasicObject(list);
            double min = list.get(0).getLprice();
            for (int i = 0; i < list.size(); i++) {
                if (min > list.get(i).getLprice()) {
                    min = list.get(i).getLprice();
                    api.setId(list.get(i).getId());
                    api.setCurr1(list.get(i).getCurr1());
                    api.setCurr2(list.get(i).getCurr2());
                    api.setLprice(list.get(i).getLprice());

                }
            }
            return api;
        } else {
            throw new InvalidCurrencyException("Wrong currency");
        }
    }

    @Override
    public ApiCurrencyResponseDto getMaxPrice(String currencyName) {
        currencyName = currencyName.toUpperCase();
        if (currencyName.equals("BTC")
                || currencyName.equals("ETH")
                || currencyName.equals("XRP")) {
            List<ApiCurrencyResponseDto> list = getCurrency(currencyName);
            ApiCurrencyResponseDto api = getBasicObject(list);
            double max = list.get(0).getLprice();
            for (int i = 0; i < list.size(); i++) {
                if (max < list.get(i).getLprice()) {
                    max = list.get(i).getLprice();
                    api.setId(list.get(i).getId());
                    api.setCurr1(list.get(i).getCurr1());
                    api.setCurr2(list.get(i).getCurr2());
                    api.setLprice(list.get(i).getLprice());

                }
            }
            return api;
        } else {
            throw new InvalidCurrencyException("Wrong currency");
        }
    }

    @Override
    public List<ApiCurrencyResponseDto> getAllCurrency(String currencyName, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Currency> list = currencyRepository.findByCurr1(currencyName, pageRequest);
        return list.stream().map(currencyMapper::toDto).toList();
    }

    @Override
    public String createCsvFile() {
        ReportModel reportModelBtc = new ReportModel();
        reportModelBtc.setCurrencyName("BTC");
        reportModelBtc.setMaxValue(BigDecimal.valueOf(getMaxPrice("BTC").getLprice()));
        reportModelBtc.setMinValue(BigDecimal.valueOf(getMinPrice("BTC").getLprice()));
        ReportModel reportModelEth = new ReportModel();
        reportModelEth.setCurrencyName("ETH");
        reportModelEth.setMaxValue(BigDecimal.valueOf(getMaxPrice("ETH").getLprice()));
        reportModelEth.setMinValue(BigDecimal.valueOf(getMinPrice("ETH").getLprice()));
        ReportModel reportModelXrp = new ReportModel();
        reportModelXrp.setCurrencyName("XRP");
        reportModelXrp.setMaxValue(BigDecimal.valueOf(getMaxPrice("XRP").getLprice()));
        reportModelXrp.setMinValue(BigDecimal.valueOf(getMinPrice("XRP").getLprice()));
        List<ReportModel> list = new ArrayList<>();
        list.add(reportModelBtc);
        list.add(reportModelEth);
        list.add(reportModelXrp);
        ReportGeneration reportGeneration = new ReportGenerationImpl();
        FileWriter fileWriter = new FileWriterImpl();
        fileWriter.writeDataToFile(reportGeneration.generateReport(list), writeTo);
        return "Report Done!";
    }

    private List<ApiCurrencyResponseDto> getCurrency(String currencyName) {
        return currencyRepository.findByCurr1(currencyName).stream()
                .map(currencyMapper::toDto)
                .toList();
    }

    private ApiCurrencyResponseDto getBasicObject(List<ApiCurrencyResponseDto> list) {
        ApiCurrencyResponseDto api = new ApiCurrencyResponseDto();
        api.setId(list.get(0).getId());
        api.setCurr1(list.get(0).getCurr1());
        api.setCurr2(list.get(0).getCurr2());
        api.setLprice(list.get(0).getLprice());
        return api;
    }
}
