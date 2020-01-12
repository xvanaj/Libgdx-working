package com.mygdx.game.world.generator;

import com.mygdx.game.world.builder.RaceTypeBuilder;
import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.entity.universe.world.World;

public class RaceTypeFactory {

    public static RaceType createRandomRaceType(World world) {
        final RaceTypeBuilder raceTypeBuilder = new RaceTypeBuilder();

        final RaceType raceType = raceTypeBuilder.generate().build();
        if (world != null) {
            world.getRaces().add(raceType);
        }

        return raceType;
    }

    public static void main(String[] args) {
        final RaceType raceType = createRandomRaceType(null);

        System.out.println(raceType);
    }
}
