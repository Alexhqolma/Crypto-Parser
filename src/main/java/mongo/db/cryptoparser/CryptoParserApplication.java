package mongo.db.cryptoparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CryptoParserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoParserApplication.class, args);
    }

}
