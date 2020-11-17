package com.epam.esm.repository;

import com.epam.esm.model.FilterParam;
import org.springframework.stereotype.Component;
import static com.epam.esm.model.FilterParam.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class SelectFilteredQuery {
     private static final String BASE_QUERY = "SELECT c FROM Certificate c LEFT JOIN c.tags t ";
     private static final String ADDITIONAL_QUERY1 = " t.name in (";
     private static final String ADDITIONAL_QUERY2 = ") GROUP BY c HAVING count(c)=";
     private static final String WHERE = " WHERE ";
     private static final String LIKE = " LIKE '%";
     private static final String END_OF_LIKE = "%' ";
     private static final String ORDER_BY = " ORDER BY ";
     private static final String SPACE = " ";
     private static final String AND = " AND ";
     private static final String QUOTE = "'";
     private static final String COMMA = ",";
     private final List<String> illegalSearchParam = Arrays.asList(DIRECTION.name().toLowerCase(),
             FIELD.name().toLowerCase(), TAG.name().toLowerCase());

     public String createQuery(Map<String, String> filterParam){
        StringBuilder sqlBuilder = new StringBuilder(BASE_QUERY);
        String searchQuery = searchingParamsIntoHqlQuery(filterParam);
        String sortQuery = sortingParamsIntoHqlQuery(filterParam);
        String tagsQuery = tagParamsIntoHqlQuery(filterParam);

        if(!searchQuery.trim().isEmpty()){
            sqlBuilder.append(WHERE).append(searchQuery);
        }

        if(!searchQuery.trim().isEmpty() & !tagsQuery.trim().isEmpty()){
            sqlBuilder.append(AND).append(tagsQuery);
        }else if(searchQuery.trim().isEmpty() & !tagsQuery.trim().isEmpty()){
            sqlBuilder.append(WHERE).append(tagsQuery);
        }

        sqlBuilder.append(sortQuery);

        return sqlBuilder.toString();
     }

     private String tagParamsIntoHqlQuery(Map<String, String> filterParam){
         StringJoiner joiner = new StringJoiner(COMMA);

         filterParam.entrySet().stream()
                 .filter(e -> e.getKey().contains(TAG.name().toLowerCase()))
                 .forEach(e -> joiner.add(QUOTE + e.getValue()+ QUOTE));

         if(joiner.length() != 0){
            return ADDITIONAL_QUERY1 + joiner.toString() + ADDITIONAL_QUERY2 + countTags(filterParam);
         }
         return SPACE;
     }

     private int countTags(Map<String, String> filterParam){
         int i=0;
         for (String key: filterParam.keySet()){
             if(key.contains(TAG.name().toLowerCase())){
                 i++;
             }
         }
         return i;
     }

     private String searchingParamsIntoHqlQuery(Map<String, String> filterParam){
        StringJoiner joiner = new StringJoiner(AND);
        filterParam.entrySet().stream()
                .filter(e -> illegalSearchParam.stream().noneMatch(s-> e.getKey().contains(s)))
                .forEach(e ->{
                        String value = e.getValue();
                        String correctKey = getCorrectQueryParamName(e.getKey());

                        joiner.add(getWhereConditionForSearchParam(value, correctKey));
                });

        return joiner.toString();
    }

    private String sortingParamsIntoHqlQuery(Map<String, String> filterParam){
        StringBuilder query = new StringBuilder();
        String sortDirection = filterParam.get(DIRECTION.getParam());
        String sortField = filterParam.get(FIELD.getParam());

        if(sortField != null && sortDirection != null){
            query.append(ORDER_BY)
                    .append(valueOf(sortField.toUpperCase()).getParam())
                    .append(SPACE)
                    .append(sortDirection);
        }
        return query.toString();
    }

    private String getCorrectQueryParamName(String mapKey){
        return FilterParam.stream()
                .map(e->e.name().toLowerCase())
                .filter(mapKey::contains)
                .findFirst().orElse(SPACE);
    }

    private String getWhereConditionForSearchParam(String value, String paramName){
        return new StringBuilder().append(valueOf(paramName.toUpperCase()).getParam())
                .append(LIKE)
                .append(value)
                .append(END_OF_LIKE).toString();
    }
}
