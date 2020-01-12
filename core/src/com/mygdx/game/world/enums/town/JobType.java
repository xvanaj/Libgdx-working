package com.mygdx.game.world.enums.town;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobType {

    FOOD(CitizenType.FARMER),
    PRODUCTIVITY(CitizenType.BUILDER),
    SOCIAL(CitizenType.SOCIAL),

    HOUSING(CitizenType.COMMONER),
    ;

    CitizenType workerType;
}
