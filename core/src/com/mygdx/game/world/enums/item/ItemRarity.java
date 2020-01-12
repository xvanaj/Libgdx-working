package com.mygdx.game.world.enums.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemRarity {

	COMMON("Common", 50, 1, 1.0),
	UNCOMMON("Uncommon", 25, 5, 1.2),
	RARE("Rare", 12, 10, 1.4),
	EPIC("Epic", 3, 15, 1.6),
	LEGENDARY("Legendary", 1, 20, 1.8),
	;

	private String name;
	private int probability;
	private int minLevel;
	private double modifier;

}
