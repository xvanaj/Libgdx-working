package com.mygdx.game.world.entity.being;

import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.entity.game.WorldEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvasionArmy extends WorldEntity {

    @Override
    public String getMapInfo(){
        return "INVASION";
    }
}
