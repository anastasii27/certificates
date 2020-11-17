package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import java.util.List;
import java.util.StringJoiner;

public class SelectMostUsedTagsQuery {
    private static final String PART1 = "SELECT t.tagId as id, t.`name`\n" +
            "FROM (\n" +
            "\tSELECT tagId, `name`, count(tagId) as tagFrequency FROM `gift-certificates`.`tag_m2m_gift_certificate`\n" +
            "\tJOIN `gift-certificates`.tag ON tagId = tag.id\n" +
            "\tWHERE ";
    private static final String PART2 = "\tGROUP BY tagId) t \n" +
            "WHERE t.tagFrequency = (\n" +
            "\tSELECT max(t1.tagFrequency)\n" +
            "    FROM (\n" +
            "\t\tSELECT tagId, `name`, count(tagId) as tagFrequency FROM `gift-certificates`.`tag_m2m_gift_certificate`\n" +
            "\t\tJOIN `gift-certificates`.tag ON tagId = tag.id\n" +
            "\t\tWHERE ";
    private static final String PART3 ="\tGROUP BY tagId) t1)";
    private static final String OR = " OR ";
    private static final String GIFT_CERTIFICATE_ID = "giftCertificateId=";

    public String createQuery(List<Certificate> certificates){
        String whereCondition = getWhereCondition(certificates);
        StringBuilder query =  new StringBuilder();

        return query.append(PART1)
                .append(whereCondition)
                .append(PART2)
                .append(whereCondition)
                .append(PART3).toString();
    }

    private String getWhereCondition(List<Certificate> certificates) {
        StringJoiner joiner = new StringJoiner(OR);

        for (Certificate certificate : certificates) {
            long id = certificate.getId();
            joiner.add(GIFT_CERTIFICATE_ID + id);
        }
        return joiner.toString();
    }
}
