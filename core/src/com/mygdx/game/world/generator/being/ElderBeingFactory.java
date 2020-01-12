package com.mygdx.game.world.generator.being;

import com.mygdx.game.asset.GameAssetManager;
import com.mygdx.game.asset.Utility;
import com.mygdx.game.world.generator.RaceTypeFactory;
import com.mygdx.game.world.builder.ElderBeingBuilder;
import com.mygdx.game.world.entity.being.ElderBeing;
import com.mygdx.game.world.entity.being.params.BeingCreationParameters;
import com.mygdx.game.world.entity.universe.RaceType;
import com.mygdx.game.world.enums.being.BeingType;

public class ElderBeingFactory {

    public static ElderBeing createRandomElderBeing(final RaceType raceType, final GameAssetManager gameAssetManager){
        BeingCreationParameters beingCreationParameters = new BeingCreationParameters();
        beingCreationParameters.setMinLevel(120);
        beingCreationParameters.setMaxLevel(150);
        beingCreationParameters.setBeingType(BeingType.ELDER);
        beingCreationParameters.setRaceType(raceType);

        return new ElderBeingBuilder()
                .createFeatures(beingCreationParameters)
                .createTypeName()
                .createName(gameAssetManager)
                .createBeing(beingCreationParameters)
                .build();
    }

    public static void main(String[] args) {
        System.out.println(createRandomElderBeing(RaceTypeFactory.createRandomRaceType(null), Utility.assetManager).toShortString());

    }
}
