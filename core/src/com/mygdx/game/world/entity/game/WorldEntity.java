package com.mygdx.game.world.entity.game;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.mygdx.game.world.entity.universe.GameEntity;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public abstract class WorldEntity extends GameEntity {

    protected int gid;
    protected int id;
    protected int x,y;
    protected String worldCode;
    protected String name;
    protected transient TextureMapObject textureMapObject;

    protected String getMapInfo() {
        return null;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point getMapPosition() {
        return new Point(x,y);
    }

    public Point getTilePosition() {
        return new Point(x/32,y/32);
    }

    public int getTileX(){
        return x/32;
    }

    public int getTileY(){
        return y/32;
    }
}
