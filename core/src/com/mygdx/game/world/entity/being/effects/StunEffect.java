package com.mygdx.game.world.entity.being.effects;

import com.mygdx.game.world.entity.being.Effect;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StunEffect extends Effect {

    public StunEffect(final long duration) {
        super(duration);
        stunned = true;
    }
}
