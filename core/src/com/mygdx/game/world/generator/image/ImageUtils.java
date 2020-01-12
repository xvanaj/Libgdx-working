package com.mygdx.game.world.generator.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.world.entity.universe.world.World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static void setMask(BufferedImage image, BufferedImage mask) {
        final int width = image.getWidth();
        int[] imgData = new int[width];
        int[] maskData = new int[width];

        for (int y = 0; y < image.getHeight(); y++) {
            image.getRGB(0, y, width, 1, imgData, 0, 1);
            mask.getRGB(0, y, width, 1, maskData, 0, 1);

            for (int x = 0; x < width; x++) {
                int color = imgData[x] & 0x00FFFFFF;
                int maskColor = (maskData[x] & 0x00FF0000) << 8;
                color |= maskColor;
                imgData[x] = color;
            }
            //replace the data
            image.setRGB(0, y, width, 1, imgData, 0, 1);
        }
    }

    public static void setPointMask(BufferedImage image, Point point, int i) {
        for (int x = point.x * World.pixelSize; x < (point.x + 1) * World.pixelSize; x++) {
            for (int y = point.y * World.pixelSize; y < (point.y + 1) * World.pixelSize; y++) {
                try {
                    image.setRGB(x, y, image.getRGB(x, y) & 0xffff0000 + i);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(x + y);
                }
            }
        }
    }

    public static BufferedImage useFilter(BufferedImage bufferedImage, RGBImageFilter filter, String fileName) {
        Image img = Toolkit.getDefaultToolkit().createImage(
                new FilteredImageSource(bufferedImage.getSource(),
                        filter));

        BufferedImage newImage = toBufferedImage(img);

        if (fileName != null) {
            saveImage(fileName, newImage, true);
        }

        return newImage;
    }

    public static void saveImage(String filename, BufferedImage bufferedImage, boolean openFile) {
        System.out.printf("Saving TileImage to Disk as %s.png ... \n", filename);
        // Save as PNG
        FileHandle fileHandle = new FileHandle("core/assets/maps/" + filename + ".png");
        if (!fileHandle.parent().exists()) {
            fileHandle = new FileHandle("assets/maps/" + filename + ".png");
        }

        File file = fileHandle.file();
        try {

            ImageIO.write(bufferedImage, "PNG", file);

            if (openFile) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Gdx.app.log("image", "Done!");
    }

    public static void main(String[] args) throws IOException {
        //FileHandle internal = Gdx.files.classpath("core/assets/maps/tilesets/tiles/tile000.png");
    /*    final BufferedImage read = ImageIO.read(new File("c:/_dev/libgdx2/core/assets/maps/tilesets/tiles/tile027.png"));
        final BufferedImage read2 = ImageIO.read(new File("c:/_dev/libgdx2/core/assets/maps/tilesets/tiles/tile023 - copy.png"));
        final BufferedImage read3 = ImageIO.read(new File("c:/_dev/libgdx2/core/assets/maps/tilesets/tiles/tile023 - copy.png"));

        //BufferedImage colored = color(0.40f, 0.65f, 1, read);
        read.getGraphics().drawImage(read2, 0, 32 - read2.getHeight(), null);
        read.getGraphics().drawImage(read3, read2.getWidth(), 32- read3.getHeight(), null);
        ImageIO.write(read, "png", new File("c:/_dev/libgdx2/core/assets/maps/tilesets/tiles/hill-grey-forest.png"));
*/

        BufferedImage bf = new BufferedImage(320, 640, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = bf.createGraphics();

        Color water = Color.cyan;
        Color deepWater = Color.BLUE;
        Color dirt = Color.DARK_GRAY;
        Color grass = Color.green;
        Color snow = Color.white;
        Color sand = Color.yellow;

        setTile(graphics, water, 4, 0);
        setTile(graphics, deepWater, 5, 0);
        setTile(graphics, grass, 0, 0);
        setTile(graphics, dirt, 1, 0);
        setTile(graphics, snow, 2, 0);
        setTile(graphics, sand, 3, 0);

        ImageUtils.saveImage("test_tileset", bf, true);
    }

    private static void setTile(final Graphics2D graphics, final Color color, final int x, final int y) {
        graphics.setColor(color);
        graphics.fillRect(x*32,y*32, 32, 32);
    }


    public static void createTileset() {

    }

    private static BufferedImage color(float r, float g, float b, BufferedImage src) {

        // Copy image ( who made that so complicated :< )
        BufferedImage newImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics2D graphics = newImage.createGraphics();
        graphics.drawImage(src, 0, 0, null);
        graphics.dispose();

        // Color image
        for (int i = 0; i < newImage.getWidth(); i++) {
            for (int j = 0; j < newImage.getHeight(); j++) {
                int ax = newImage.getColorModel().getAlpha(newImage.getRaster().getDataElements(i, j, null));
                int rx = newImage.getColorModel().getRed(newImage.getRaster().getDataElements(i, j, null));
                int gx = newImage.getColorModel().getGreen(newImage.getRaster().getDataElements(i, j, null));
                int bx = newImage.getColorModel().getBlue(newImage.getRaster().getDataElements(i, j, null));
                rx *= r;
                gx *= g;
                bx *= b;
                newImage.setRGB(i, j, (ax << 24) | (rx << 16) | (gx << 8) | (bx << 0));
            }
        }
        return newImage;
    }

    public static void saveScreenshot() {
        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

        // this loop makes sure the whole screenshot is opaque and looks exactly like what the user is seeing
        for(int i = 4; i < pixels.length; i += 4) {
            pixels[i - 1] = (byte) 255;
        }

        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);

        PixmapIO.writePNG(Gdx.files.external("libgdx2/screenshot.png"), pixmap);
        pixmap.dispose();
    }

}
