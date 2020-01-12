package com.mygdx.game.world.generator.world;

import java.util.Random;

// code by jared
// based from unity map generation
public class WaterGenerator {

    private boolean map[][]; // 2d map
    private int width, height; // width and height determine the dimensions of the grid
    private int fillPercent = 45; // fillPercent specifies how much of the map should be walls
    private boolean useRandomSeed;
    private String seed; // store custom seed here
    private int smoothness = 2;

    public WaterGenerator(int width, int height, boolean useRandomSeed, String seed, int fillPercent, int smoothness) {
        // initialize everything
        this.width = width;
        this.height = height;
        this.useRandomSeed = useRandomSeed;
        this.seed = seed;
        this.fillPercent = fillPercent;
        this.smoothness = smoothness;

        map = new boolean[width][height];
    }

    public boolean[][] getMap() {
        return map;
    }

    public void generateMap() {

        // Generate Map to start things
        fillMap();

        for (int i = 0; i < smoothness; i++) {
            // smoothen the map 2 times
            smoothMap();
        }
    }

    private void fillMap() {
        // fills the map based on fill percent and seed
        if (useRandomSeed)
            seed = String.valueOf(System.currentTimeMillis()); // if true then use time for random seed, else use user-provided seed

        Random pRand = new Random(seed.hashCode()); // create random number generator

        // loop through array and assign either '#'' or ' ' to each index
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // if the generated number is less than the fill percent, then set it to a wall
                // else set as an open area
                map[x][y] = pRand.nextInt(100) < fillPercent ? true : false;

                // if the index is at the edges of the map then set it to a wall
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
                    map[x][y] = false;
            }
        }

        for (int x = 0; x < width; x++) {
            map[x][0] = true;
            map[x][height-1] = true;
        }

        for (int y = 0; y < height; y++) {
            map[0][y] = true;
            map[width-1][y] = true;
        }

    }

    private void smoothMap() {
        for (int x = 0; x < width/2; x++) {
            for (int y = 0; y < height; y++) {
                // loop through each index and get the number of wall blocks around the current index
                int wallCount = getSurroundingWallCount(x, y);

                // if the number of walls is greater than 4 then set the current index to a wall
                // else set as an open area
                if (wallCount > 4)
                    map[x][y] = true;
                else if (wallCount < 4)
                    map[x][y] = false;
            }
        }

        for (int x = width -1; x >= width/2; x--) {
            for (int y = 0; y < height; y++) {
                // loop through each index and get the number of wall blocks around the current index
                int wallCount = getSurroundingWallCount(x, y);

                // if the number of walls is greater than 4 then set the current index to a wall
                // else set as an open area
                if (wallCount > 4)
                    map[x][y] = true;
                else if (wallCount < 4)
                    map[x][y] = false;
            }
        }

    }

    private int getSurroundingWallCount(int x, int y) {
        // get the number of walls surrounding the passed-in index by
        // treating the index as the center of a 3x3 grid array
        int wallCount = 0;

        // each for loop repeats 3 time for a total of 9 loops
        for (int nx = x - 1; nx <= x + 1; nx++) {
            for (int ny = y - 1; ny <= y + 1; ny++) {
                // if the neighbouring tile is an index inside the map, then execute code block
                // else increment wall count by 1
                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    if (nx != x || ny != y) // if the current neighbouring index is not the current index itself then execute code block
                    {
                        // if a the neighbouring index is a wall, add one to wall count
                        wallCount += (map[nx][ny] == true) ? 1 : 0;
                    }
                } else {
                    wallCount++;
                }
            }
        }

        return wallCount;
    }

    public void printMap() {
        // print the map
        System.out.println("Seed: \"" + seed + "\"\t Fill: " + fillPercent + "%");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (map[x][y]) {
                    System.out.print("#");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // the arguments passed here means that I am using a random seed
        // to use your own seed, change the third parameter to false and set your own seed
        // at the fourth parameter; the seed must be a string
        WaterGenerator map = new WaterGenerator(50, 50, true, "seed here", 45, 3);

        map.generateMap();
        map.printMap();
    }
}