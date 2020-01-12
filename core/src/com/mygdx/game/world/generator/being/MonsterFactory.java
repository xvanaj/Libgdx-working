package com.mygdx.game.world.generator.being;


import com.mygdx.game.asset.GameAssetManager;
import com.mygdx.game.asset.Utility;
import com.mygdx.game.world.builder.AncientBeingBuilder;
import com.mygdx.game.world.builder.BeingBuilder;
import com.mygdx.game.world.entity.being.AncientBeing;
import com.mygdx.game.world.entity.being.Being;
import com.mygdx.game.world.entity.being.params.AncientMonsterGenerationParameters;
import com.mygdx.game.world.entity.being.params.AttributesGenerationParameters;
import com.mygdx.game.world.entity.being.params.BeingCreationParameters;
import com.mygdx.game.world.entity.being.params.MonsterGenerationParameters;
import com.mygdx.game.world.enums.being.BeingType;
import com.mygdx.game.world.enums.world.TerrainType;

import java.util.ArrayList;
import java.util.List;

public class MonsterFactory {

    public static AncientBeing generateAncientMonster(int level, TerrainType terrainType) {
        BeingCreationParameters params = createDefaultParameters(level, terrainType);
        params.setBeingType(BeingType.ANCIENT_MONSTER);
        params.setAncientMonsterGenerationParameters(new AncientMonsterGenerationParameters());

        return new AncientBeingBuilder().createBeing(params).build(Utility.assetManager);
    }


    public Being generateMonster(int level, TerrainType terrainType, GameAssetManager gameAssetManager) {
        BeingCreationParameters params = createDefaultParameters(level, terrainType);
        params.setBeingType(BeingType.MONSTER);

        return new BeingBuilder().createBeing(params).build(gameAssetManager);
    }

    private static BeingCreationParameters createDefaultParameters(int level, TerrainType terrainType) {
        BeingCreationParameters params = new BeingCreationParameters();
        params.setMinLevel(level);
        params.setMaxLevel(level);
        params.setMonsterGenerationParameters(new MonsterGenerationParameters());
        params.getMonsterGenerationParameters().setTerrainType(terrainType);

        params.setAttributesGenerationParameters(new AttributesGenerationParameters());
        params.getAttributesGenerationParameters().setLevelBasedValue(true);
        params.getAttributesGenerationParameters().setLevelBasedValueVariance(10);

        return params;
    }

    public List<Being> generateMonsters(int level, TerrainType terrainType, int count, GameAssetManager gameAssetManager) {
        List<Being> monsters = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            monsters.add(generateMonster(level, terrainType, gameAssetManager));
        }

        return monsters;
    }
}
