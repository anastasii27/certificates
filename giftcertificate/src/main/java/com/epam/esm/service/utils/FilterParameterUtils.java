package com.epam.esm.service.utils;

import com.epam.esm.model.FilterParam;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FilterParameterUtils {
    private static final List<String> possibleSortDirectionParams = Arrays.asList("asc", "desc");
    private static final String DIRECTION = "direction";
    private static final String TAG = "tag";
    private static final String PERCENT = "%";

    public static void removeIllegalSearchParams(Map<String, String> filterParams){
        filterParams.entrySet().removeIf(entry ->
                FilterParam.stream().noneMatch(e-> entry.getKey().contains(e.name().toLowerCase())));
    }

    public static void removeIllegalSortDirectionParams(Map<String, String> filterParams){
        filterParams.entrySet().removeIf(entry ->
                possibleSortDirectionParams.stream().noneMatch(s -> {
                    if(entry.getKey().equals(DIRECTION)){
                        return entry.getValue().equals(s);
                    }
                    return true;
                })
        );
    }

    public static void modifyParamsValuesForDatabase(Map<String, String> filterParams){
        for(Map.Entry<String, String> e : filterParams.entrySet()){
            if(!e.getKey().contains(TAG)){
                e.setValue(PERCENT + e.getValue()+PERCENT);
            }
        }
    }
}
