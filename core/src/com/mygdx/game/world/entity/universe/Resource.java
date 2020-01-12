package com.mygdx.game.world.entity.universe;

import com.mygdx.game.world.entity.game.WorldEntity;
import com.mygdx.game.world.enums.world.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

@Getter
@Setter
@NoArgsConstructor
public class Resource extends WorldEntity {

    private ResourceType resourceType;


    public Resource(final int x, final int y, final ResourceType resourceType) {
        this.x = x;
        this.y = y;
        this.resourceType = resourceType;
    }
}
