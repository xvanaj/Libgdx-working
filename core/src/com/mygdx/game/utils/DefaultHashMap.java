package com.mygdx.game.utils;

import java.util.TreeMap;

/**
 * Created by Jakub Vana on 8.8.2018.
 */
public class DefaultHashMap<K, V> extends TreeMap<K, V> {

    protected V defaultValue;

    public DefaultHashMap() {
    }

    public DefaultHashMap(V defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public V get(Object k) {
        return containsKey(k) ? super.get(k) : defaultValue;
    }

}