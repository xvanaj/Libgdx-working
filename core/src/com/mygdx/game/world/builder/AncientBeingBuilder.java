package com.mygdx.game.world.builder;

import com.mygdx.game.asset.GameAssetManager;
import com.mygdx.game.name.MonsterNameGenerator;
import com.mygdx.game.world.entity.being.AncientBeing;
import com.mygdx.game.world.entity.being.params.AncientMonsterGenerationParameters;
import com.mygdx.game.world.entity.being.params.BeingCreationParameters;
import com.mygdx.game.utils.Randomizer;

public class AncientBeingBuilder extends BeingBuilder<AncientBeingBuilder> {

    private AncientBeing being;

    public AncientBeingBuilder() {
        this.being = new AncientBeing();
    }

    @Override
    public AncientBeingBuilder createBeing(BeingCreationParameters parameters) {
        super.createBeing(parameters);
        setAncientBeingType(parameters);

        return this;
    }


    @Override
    protected void generateMonster(BeingCreationParameters parameters) {
        this.generateLevel(parameters)
                .generateMonsterType(parameters)
                .generateAttributes(parameters.getAttributesGenerationParameters())
                .generateName(parameters)
                .generateItems(parameters)
                .generateSpells(parameters)
                .setAncientBeingType(parameters);
    }

    private AncientBeingBuilder setAncientBeingType(BeingCreationParameters parameters) {
        if (parameters.getAncientMonsterGenerationParameters().getAncientMonsterType() != null) {
            being.setAncientMonsterType(parameters.getAncientMonsterGenerationParameters().getAncientMonsterType());
        } else {
            being.setAncientMonsterType(Randomizer.get(AncientMonsterGenerationParameters.AncientMonsterType.values()));
        }

        return this;
    }

    @Override
    public AncientBeing build(GameAssetManager gameAssetManager) {
        being.calculateStats();
        if (being.getName() == null) {  //todo delete
            //being.setName(new NameGenerator().generateMonsterName((int) being.getLevel(), gameAssetManager));
            being.setName(MonsterNameGenerator.generateElderName(being.getAlignment().name(), gameAssetManager));
        }

        being.setHp(being.getHpMax());
        being.setMp(being.getMpMax());
        return being;
    }

}

