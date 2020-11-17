package com.epam.esm.model;

import java.util.stream.Stream;

public enum FilterParam {
    NAME("c.name"),
    DESCRIPTION("c.description"),
    DATE("c.createDate"),
    TAG("t.name"),
    FIELD("field"),
    DIRECTION("direction");

    private String param;

    private FilterParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    public static Stream<FilterParam> stream() {
        return Stream.of(FilterParam.values());
    }
}
