package com.mygdx.game.world.entity.being.params;

import com.mygdx.game.world.enums.character.AttackType;
import com.mygdx.game.world.enums.world.TerrainType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MonsterGenerationParameters {

    private TerrainType terrainType;
    private AttackType attackType;
}
