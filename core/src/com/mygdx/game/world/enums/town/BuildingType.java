package com.mygdx.game.world.enums.town;

import com.mygdx.game.world.entity.universe.IEncyclopediaItem;
import com.mygdx.game.utils.Hotkeys;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum BuildingType implements ITreeNode, IIconified, IEncyclopediaItem {

   /* SMALL_HOUSE("small house", Hotkeys.ARENA, true, false, 10, 0.1, BuildingPurpose.HOUSING, TownSize.ISOLATED_DWELLING, 10, 0),
    HOUSE("house", Hotkeys.ARENA, true, false, 50, 0.5, BuildingPurpose.HOUSING, TownSize.ISOLATED_DWELLING, 30, 0),
    LARGE_HOUSE("large house", Hotkeys.ARENA, true, false, 250, 2.5, BuildingPurpose.HOUSING, TownSize.ISOLATED_DWELLING, 150, 0),*/
    TEMPLE("temple", Hotkeys.TEMPLE, true, false, 60000, 0, BuildingPurpose.COMMON, TownSize.ISOLATED_DWELLING, 0, 0),
    GUILD_FIGHTER("fighters guild", Hotkeys.TRAINING_GROUNDS, true, false, 1500, 0, BuildingPurpose.GUILD, TownSize.ISOLATED_DWELLING, 0, 0),
    PUB("pub", Hotkeys.PUB, true, false, 1500, 0, BuildingPurpose.COMMON, TownSize.ISOLATED_DWELLING, 0, 0),
    SHOP("shop", Hotkeys.SHOP, true, false, 1500, 0, BuildingPurpose.COMMON, TownSize.ISOLATED_DWELLING, 0, 0),
    LIBRARY("book shop", Hotkeys.SHOP, true, false, 1500, 0, BuildingPurpose.GUILD, TownSize.ISOLATED_DWELLING, 0, 0),
    ARENA("arena", Hotkeys.ARENA, true, false, 120000, 0, BuildingPurpose.COMMON, TownSize.CITY, 0, 0),
    COLISEUM("coliseum", Hotkeys.ARENA, true, false, 1000000, 0, BuildingPurpose.COMMON, TownSize.LARGE_CITY, 0, 0),
    GROCERY_STORE("grocery store", Hotkeys.GROCERY_STORE, true, true, 100, 2, BuildingPurpose.INCOME, TownSize.ISOLATED_DWELLING, 0, 0),
    /*FISHING_HUT("fishing hut", Hotkeys.FISHING_HUT, true, true, 100, 2, BuildingPurpose.FOOD, TownSize.ISOLATED_DWELLING, 0, 50),
    FARM("farm", Hotkeys.FISHING_HUT, true, true, 100, 2, BuildingPurpose.FOOD, TownSize.ISOLATED_DWELLING, 0, 100),
    QUARY("quary", Hotkeys.QUARY, true, true, 100, 2, BuildingPurpose.INCOME, TownSize.HAMLET, 0, 0),
    WOOD_CUTTER("wood cutter", Hotkeys.WOOD_CUTTER, true, true, 1000, 10, BuildingPurpose.INCOME, TownSize.HAMLET, 0, 0),
    IRON_MINE("iron mine", Hotkeys.IRON_MINE, true, true, 1000, 10, BuildingPurpose.INCOME, TownSize.TOWN, 0, 0),
    SILVER_MINE("silver mine", Hotkeys.SILVER_MINE, true, true, 1000, 10, BuildingPurpose.INCOME, TownSize.CITY, 0, 0),
    GOLD_MINE("gold mine", Hotkeys.GOLD_MINE, true, true, 10000, 100, BuildingPurpose.INCOME, TownSize.CITY, 0, 0)*/;

    static {
        COLISEUM.setPrerequisities(ARENA);
    }

    private String name;
    private String hotkey;
    private boolean levelable;
    private boolean unlimitedCount;
    private float cost;
    private float income;
    private BuildingPurpose buildingType;
    private TownSize minimumTownSize;
    private int residents;
    private int foodProduced;

    BuildingType(String name, String hotkey, boolean levelable, boolean unlimitedCount, float cost, float income, BuildingPurpose buildingType, TownSize minimumTownSize, int residents, int foodProduced) {
        this.name = name;
        this.hotkey = hotkey;
        this.levelable = levelable;
        this.unlimitedCount = unlimitedCount;
        this.cost = cost;
        this.income = income;
        this.buildingType = buildingType;
        this.minimumTownSize = minimumTownSize;
        this.residents = residents;
        this.foodProduced = foodProduced;
    }

    private Set<ITreeNode> prerequisities;

    @Override
    public Set<ITreeNode> getPrerequisities() {
        return prerequisities;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public void setPrerequisities(ITreeNode... prerequisities) {
        this.prerequisities = Stream.of(prerequisities).collect(Collectors.toSet());
    }

    @Override
    public String getIconName() {
            return /*GamResources.getIcons().get("")*/ null;
    }

    @Override
    public String getEncyclopediaDescription() {
        return toString();
    }

    @Override
    public String getEncyclopediaIconName() {
        return "HOUSE";
    }

    public enum BuildingPurpose implements IEncyclopediaItem {
        COMMON,
        FOOD,
        GUILD,
        HOUSING,
        INCOME,
        ;

        @Override
        public String getName() {
            return name();
        }

        @Override
        public String getEncyclopediaDescription() {
            return name();
        }

        @Override
        public String getEncyclopediaIconName() {
            return "town";
        }
    }
}