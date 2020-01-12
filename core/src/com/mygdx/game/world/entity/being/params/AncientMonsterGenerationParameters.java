package com.mygdx.game.world.entity.being.params;

import com.mygdx.game.utils.Randomizer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class AncientMonsterGenerationParameters {

    AncientMonsterType ancientMonsterType = Randomizer.get(AncientMonsterType.values());

    @Getter
    @AllArgsConstructor
    public enum AncientMonsterType{
        DRAGON("Ancient dragon", "ancientDragon", new Point(14, 60)),
        ARCH_DEMON("Arch demon", "archDemon", new Point(14, 61)),
        ;
        private String name;
        private String mapImage;
        private Point tilesetImageIndex;
    }
}
