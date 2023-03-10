package mongo.db.cryptoparser.repository;

import java.util.List;
import mongo.db.cryptoparser.model.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CurrencyRepository extends MongoRepository<Currency, Long> {
    List<Currency> findByCurr1(String curr1);

    Page<Currency> findByCurr1(String curr1, Pageable pageable);
}
