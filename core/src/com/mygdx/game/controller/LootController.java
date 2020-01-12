package com.mygdx.game.controller;

import com.mygdx.game.world.entity.being.Being;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Random;

@NoArgsConstructor
public class LootController {

    private Random random = new Random();

    public List<Being> distributeLoot(List<Being> beings) {

        return beings;
    }
}
