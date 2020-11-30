package com.epam.esm.service.utils;

import com.epam.esm.dto.CertificateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import java.math.BigDecimal;

@Component
public class UpdatedCertificateValidator implements Validator {
    private static final String DURATION = "duration";
    private static final String PRICE = "price";
    private static final String DURATION_EXCEPTION_KEY = "duration.negative";
    private static final String PRICE_EXCEPTION_KEY = "price.negative";

    @Override
    public boolean supports(Class<?> aClass) {
        return CertificateDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CertificateDto certificateDto = (CertificateDto) o;

        if(certificateDto.getDuration() < 0){
            errors.rejectValue(DURATION, DURATION_EXCEPTION_KEY);
        }

        BigDecimal price = certificateDto.getPrice();
        if(price != null && price.longValue() < 0){
            errors.rejectValue(PRICE, PRICE_EXCEPTION_KEY);
        }
    }
}
