package com.mygdx.game.world.entity.being;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BiographyEvent {

    int minute;
    String worldCode;
    String text;

}
