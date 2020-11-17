package com.epam.esm.converter;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import javax.persistence.AttributeConverter;
import java.time.ZoneId;

public class ZoneIdConverter implements AttributeConverter<ZoneId, String> {
    @Override
    public String convertToDatabaseColumn(ZoneId zoneId) {
        if(zoneId != null) {
            return zoneId.getId();
        }
        return EMPTY;
    }

    @Override
    public ZoneId convertToEntityAttribute(String s) {
        return ZoneId.of(s);
    }
}
