package com.mygdx.game.world.builder;

import com.mygdx.game.asset.GameAssetManager;
import com.mygdx.game.name.MonsterNameGenerator;
import com.mygdx.game.world.entity.being.ElderBeing;
import com.mygdx.game.world.entity.being.params.BeingCreationParameters;
import com.mygdx.game.world.entity.being.params.ElderBeingCreationParameters;
import com.mygdx.game.world.enums.being.Size;
import com.mygdx.game.world.enums.character.Alignment;

public class ElderBeingBuilder extends BeingBuilder<ElderBeingBuilder> {

    private ElderBeing being;

    public ElderBeingBuilder() {
        this.being = new ElderBeing();
    }

    public ElderBeing build() {
        being.calculateStats();

        being.setHp(being.getHpMax());
        being.setMp(being.getMpMax());
        return being;
    }

    public ElderBeingBuilder createFeatures(BeingCreationParameters beingCreationParameters) {
        ElderBeingCreationParameters params = beingCreationParameters.getElderBeingCreationParameters();

        being.setSpeciesType(beingCreationParameters.getRaceType().getSpeciesType());
        being.setHeadType(beingCreationParameters.getRaceType().getHeadType());
        being.setSkinColor(beingCreationParameters.getRaceType().getSkinColor());

        being.setSize(beingCreationParameters.getRaceType().getSize());
        being.setAlignment(beingCreationParameters.getRaceType().getAlignment());

/*        being.setSpeciesType(Randomizer.get(ElderBeing.SpeciesType.values()));
        being.setHeadType(Randomizer.get(ElderBeing.HeadType.values()));
        being.setSkinColor(Randomizer.get(ElderBeing.SkinColor.values()));
        being.setBodyMaterial(Randomizer.get(ElderBeing.BodyMaterial.values()));

        being.setSize(Randomizer.get(Size.values()));
        being.setAlignment(Randomizer.get(Alignment.values()));*/

        return this;
    }

    public ElderBeingBuilder createBeing(BeingCreationParameters parameters) {
        generateMonster(parameters);

        //todo
        //being.setIconName(Randomizer.get(GameResources.getPortraits().keySet()));
        being.calculateStats();

        return this;
    }

    public ElderBeingBuilder createTypeName() {
        Size weight = being.getSize();
        StringBuilder sb = new StringBuilder();

        //size prefix
        if (weight.equals(Size.GARGANTUAN)) {
            sb.append("gargantuan ");
        } else if (weight.equals(Size.HUGE)) {
            sb.append("huge ");
        } else if (weight.equals(Size.TINY)) {
            sb.append("tiny ");
        }

        //head
        if (being.getHeadType().getModificationName() != null && !being.getHeadType().getModificationName().equals("")) {
            sb.append(being.getHeadType().getModificationName() + " ");
        }

        //body
        //sb.append(being.getBodyMaterial().getModificationName() + " ");

        //alignment
        if (being.getAlignment() != Alignment.NEUTRAL) {
            sb.append(being.getAlignment().name().toLowerCase() + " ");
        }

        //species
        sb.append(being.getSpeciesType().name().toLowerCase() + " ");

        being.setTypeName(sb.toString().trim());

        return this;
    }

    public ElderBeingBuilder createName(final GameAssetManager gameAssetManager) {
        being.setName(MonsterNameGenerator.generateElderName(being.getAlignment().name(), gameAssetManager));

        return this;
    }
}
    
