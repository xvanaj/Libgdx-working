package com.mygdx.game.world.generator.image;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.enums.world.TerrainType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Jakub Vana on 29.8.2018.
 */
public class PixelImageCreator implements IImageCreator {

    private final int PIXEL_SCALE = 5;

    /**
     * Private Method to create a Buffered Image and save the result in a file.
     *
     * @param array
     * @param filename
     */
    private void visualizeFromDoubleArray(double[][] array, String filename) {

        System.out.println("Creating PixelImageCreator, please wait...");

        int IMAGE_HEIGHT = array.length * PIXEL_SCALE;
        int IMAGE_WIDTH = array[0].length * PIXEL_SCALE;

        System.out.println("Image Width: " + IMAGE_WIDTH + "px");
        System.out.println("Image Height: " + IMAGE_HEIGHT + "px");

        // Constructs a BufferedImage of one of the predefined image types.
        BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        // Create a graphics which can be used to draw into the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();

        // fill all the image with white
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        int middle = array[0].length;

        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {

                //Defining coloring rules for each value
                //You may also use enums with switch case here
                if (array[x][y] < 0.2) { // if value equals 0, fill with water
                    g2d.setColor(Color.BLUE);
                    g2d.fillRect(y * PIXEL_SCALE, x * PIXEL_SCALE, PIXEL_SCALE, PIXEL_SCALE);
                } else if (array[x][y] < 0.25) { // if value equals 1, fill with land
                    g2d.setColor(Color.YELLOW);
                    g2d.fillRect(y * PIXEL_SCALE, x * PIXEL_SCALE, PIXEL_SCALE, PIXEL_SCALE);
                } else if (array[x][y] < 0.7) { // if value equals 1, fill with land
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(y * PIXEL_SCALE, x * PIXEL_SCALE, PIXEL_SCALE, PIXEL_SCALE);
                } else if (array[x][y] < 0.92) { // if value equals 1, fill with land
                    g2d.setColor(Color.lightGray);
                    g2d.fillRect(y * PIXEL_SCALE, x * PIXEL_SCALE, PIXEL_SCALE, PIXEL_SCALE);
                } else if (array[x][y] <= 1) { // if value equals 1, fill with land
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(y * PIXEL_SCALE, x * PIXEL_SCALE, PIXEL_SCALE, PIXEL_SCALE);
                }
            }
        }
        // Disposes of this graphics context and releases any system resources
        // that it is using.
        g2d.dispose();

        saveImage(filename, bufferedImage);
    }

    /**
     * Private Method to create a Buffered Image and save the result in a file.
     *
     * @param filename
     */
    @Override
    public void visualizeFromEnum(World world, String filename, boolean openFile) {

        //System.out.println("Creating PixelImageCreator, please wait...");

        int IMAGE_HEIGHT = world.getHeight() * PIXEL_SCALE;
        int IMAGE_WIDTH = world.getWidth() * PIXEL_SCALE;

        //System.out.println("Image Width: " + IMAGE_WIDTH + "px");
        //System.out.println("Image Height: " + IMAGE_HEIGHT + "px");

        // Constructs a BufferedImage of one of the predefined image types.
        BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        // Create a graphics which can be used to draw into the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();

        // fill all the image with white
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                //Defining coloring rules for each value
                //You may also use enums with switch case here
                g2d.setColor(TerrainType.values()[world.getTerrainLayer()[x][y]].getColor());
                g2d.fillRect(y * PIXEL_SCALE, x * PIXEL_SCALE, PIXEL_SCALE, PIXEL_SCALE);
            }
        }
        // Disposes of this graphics context and releases any system resources
        // that it is using.

        g2d.dispose();

        saveImage(filename, bufferedImage);
    }

    @Override
    public void visualizeTowns(Graphics2D g2d, World world) {
        world.getTowns().values().stream().forEach(town -> {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(town.getY(), town.getX(), PIXEL_SCALE, PIXEL_SCALE);
        });
    }


    private void saveImage(String filename, BufferedImage bufferedImage) {
        Gdx.app.log("imageCreator", "Saving PixelImageCreator to Disk as " + filename + ".png ... \n");
        // Save as PNG
        File file = new File(filename + ".png");
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gdx.app.log("imageCreator", "Done!");
    }

}
