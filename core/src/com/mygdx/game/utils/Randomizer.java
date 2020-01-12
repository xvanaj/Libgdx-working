package com.mygdx.game.utils;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {
    public static final Random RANDOM = ThreadLocalRandom.current();

    public static <E> E get(E... e) {
        return e[RANDOM.nextInt(e.length)];
    }

    public static <E> E get(List<E> e) {
        return e.get(RANDOM.nextInt(e.size()));
    }


    public static <E> E get(Set<E> e) {
        int size = e.size();
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for (E obj : e) {
            if (i == item) {
                return obj;
            }
            i++;
        }
        return null;
    }

    public static double generate(double minimum, double maximum) {
        return ThreadLocalRandom.current().nextDouble(minimum, maximum);
    }

    public static float range(float minimum, float maximum) {
        return (float) ThreadLocalRandom.current().nextDouble(minimum, maximum);
    }

    public static int generate(int minimum, int maximum) {
        return RANDOM.nextInt(maximum - minimum) + minimum;
    }

    static public int zeroToUpperBound(int upper) {
        ThreadLocalRandom generator = ThreadLocalRandom.current();
        return generator.nextInt(upper);
    }

    static public int range(int lower, int upper) {
        //note the static factory method for getting an object; there's no constructor,
        //and there's no need to pass a seed
        ThreadLocalRandom generator = ThreadLocalRandom.current();
        if (lower == upper) {
            return lower;
        }
        return generator.nextInt(lower, upper);

    }

    static public double gaussian(double mean, double variance) {
        //log("Generating " + MAX + " random doubles in a gaussian distribution.");
        //log("Mean: " + mean + " Variance:" + variance);
        ThreadLocalRandom generator = ThreadLocalRandom.current();
        return mean + generator.nextGaussian() * variance;
    }
}