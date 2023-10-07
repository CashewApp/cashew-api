package br.app.cashew.global.hibernate.constraints.uf;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class UFCaseInsensitiveValidator implements ConstraintValidator<ValidUF, String> {

    private static final Pattern UF_PATTERN = Pattern.compile("^(AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO)$", Pattern.CASE_INSENSITIVE);
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && UF_PATTERN.matcher(value).matches();
    }
}
