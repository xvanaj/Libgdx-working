package com.mygdx.game.world.params;

import com.mygdx.game.world.enums.world.MaskType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaskCreationInputParameters {

    public MaskCreationInputParameters(final MaskType maskType) {
        this.maskType = maskType;
    }

    private MaskType maskType = MaskType.CELLULAR;
    private MaskType temperatureMaskType = MaskType.REAL_TEMPERATURE_TWO_POLES;
    private float edgeFade = 0.2f;
    private int fillPercentCellularMask = 45;
    private int smoothFactorCellularMask = 2;

}
