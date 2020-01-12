package com.mygdx.game.world.enums.world;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum MaskType {

    REAL_TEMPERATURE_ONE_POLE,
    REAL_TEMPERATURE_TWO_POLES,

    EDGE,
    RADIAL,
    CELLULAR,
    CIRCLE,
    ASTERISK,
    SHEET;

    private static final Set<MaskType> temperatureMasks = new HashSet<MaskType>(){{this.add(MaskType.REAL_TEMPERATURE_ONE_POLE); this.add(REAL_TEMPERATURE_TWO_POLES);}};

    public static List<MaskType> valuesBasicLayers(){
        final List<MaskType> collect = Arrays.stream(MaskType.values()).filter(item -> !temperatureMasks.contains(item)).collect(Collectors.toList());

        return collect;
    }

    public static List<MaskType> valuesTemperatureLayers(){
        final List<MaskType> collect = Arrays.stream(MaskType.values()).filter(item -> temperatureMasks.contains(item)).collect(Collectors.toList());

        return collect;
    }
}