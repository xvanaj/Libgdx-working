package com.mygdx.game.world.entity.being;

import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.entity.being.params.AncientMonsterGenerationParameters;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AncientBeing extends Being {

    private transient World world;
    private AncientMonsterGenerationParameters.AncientMonsterType ancientMonsterType;

    @Override
    public String getMapInfo() {
        return getAncientMonsterType().getName();
    }
}
