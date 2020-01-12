package com.mygdx.game.asset;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@Getter
public class TextFileContent {

    private Set<String> availableKeys = new HashSet<>();
    private List<TextFileLine> lines = new ArrayList<>();

    @Getter
    public static class TextFileLine {
        private Map<FileKeyType, String> values = new HashMap<>();
    }

    @AllArgsConstructor
    @Getter
    public enum FileKeyType{
        ALIGNMENT(FilterType.EQUAL),
        NAME(FilterType.EQUAL),
        TYPE(FilterType.EQUAL),
        GENDER(FilterType.EQUAL),
        RACE(FilterType.EQUAL),
        MINLEVEL(FilterType.LET),
        MAXLEVEL(FilterType.GET),
        ATTACKTYPE(FilterType.EQUAL),
        ;

        private FilterType filterType;
    }

    public enum FilterType {
        EQUAL,
        LET,
        GET,
    }
}
