package mongo.db.cryptoparser.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrencyTypeValid.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrencyType {
    String message() default "You used wrong currency, should be: BTC, ETH or XRP";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
