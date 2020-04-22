package core.squid;

import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidgrid.mapping.IDungeonGenerator;

public class SquidDungeonTest {

    public static final int SIZE = 512;

    public static void main(String[] args) {
/*
        IDungeonGenerator dungeonGenerator = new ClassicRogueMapGenerator(SIZE * SIZE / 50,SIZE * SIZE / 50,
                SIZE,SIZE,5,8,5,8);

        printDungeon(new OrganicMapGenerator(SIZE,SIZE));
        printDungeon(new ConnectingMapGenerator(SIZE, SIZE, new RNG()));
        printDungeon(new DenseRoomMapGenerator(SIZE,SIZE));
        printDungeon(new DungeonGenerator(SIZE, SIZE));
        printDungeon(new FlowingCaveGenerator(SIZE, SIZE));
        printDungeon(new LanesMapGenerator(SIZE,SIZE, new RNG(), 8));
        printDungeon(new MixedGenerator(SIZE, SIZE, new RNG()));
        printDungeon(new ModularMapGenerator(SIZE, SIZE));
        printDungeon(new SectionDungeonGenerator(SIZE, SIZE, new RNG()));
        printDungeon(new SerpentMapGenerator(SIZE, SIZE, new RNG()));
        printDungeon(new SymmetryDungeonGenerator(SIZE, SIZE, new RNG()));
        printDungeon(new ThinDungeonGenerator(SIZE, SIZE, new RNG()));

        DungeonBoneGen dungeonBoneGen = new DungeonBoneGen();
        //DungeonUtility.debugPrint(dungeonBoneGen.generate(TilesetType.ROOMS_AND_CORRIDORS_B, 4000, 4000));

        SerpentDeepMapGenerator serpentDeepMapGenerator = new SerpentDeepMapGenerator(SIZE, SIZE, 3, new RNG());
        System.out.println(serpentDeepMapGenerator.getClass());
        DungeonUtility.debugPrint(serpentDeepMapGenerator.generate()[0]);
        DungeonUtility.debugPrint(serpentDeepMapGenerator.generate()[1]);
        DungeonUtility.debugPrint(serpentDeepMapGenerator.generate()[2]);

*/
    }

    private static void printDungeon(IDungeonGenerator dungeonGenerator) {
        System.out.println(dungeonGenerator.getClass());
        DungeonUtility.debugPrint(dungeonGenerator.generate());
    }

    @Override
    public String toString() {

        return super.toString();
    }
}
