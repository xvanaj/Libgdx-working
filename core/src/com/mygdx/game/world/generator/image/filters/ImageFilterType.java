package com.mygdx.game.world.generator.image.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
enum ImageFilterType {
    red(0xffff0000),
    blue(0xFF00FF00),
    green(0xFF0000FF);

    int filter;
}
