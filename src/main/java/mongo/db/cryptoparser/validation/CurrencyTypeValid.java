package mongo.db.cryptoparser.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CurrencyTypeValid implements ConstraintValidator<CurrencyType, String> {
    private static final String LATIN_REGEX = "^[A-Za-z]$";

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext constraintValidatorContext) {
        return userName.matches(LATIN_REGEX);
    }
}
