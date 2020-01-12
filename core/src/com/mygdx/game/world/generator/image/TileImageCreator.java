package com.mygdx.game.world.generator.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.world.entity.universe.world.World;
import com.mygdx.game.world.entity.game.WorldEntity;
import com.mygdx.game.world.enums.world.TerrainType;
import com.mygdx.game.utils.Randomizer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jakub Vana on 29.8.2018.
 */
public class TileImageCreator implements IImageCreator {

    private final int PIXEL_SCALE = World.pixelSize;
    private Map<String, BufferedImage> mapImages = new HashMap<>();

    /**
     * Private Method to create a Buffered Image and save the result in a file.
     *
     * @param filename
     */
    @Override
    public void visualizeFromEnum(World world, String filename, boolean openFile) {
        BufferedImage bufferedImage = getImage(world);
        world.setImageFileName(filename);

        ImageUtils.saveImage(filename, bufferedImage, openFile);
    }

    public BufferedImage getImage(World world) {
        if (mapImages.get(world.getName()) != null) {
            return mapImages.get(world.getName());
        }

        int IMAGE_WIDTH = world.getWidth() * PIXEL_SCALE;
        int IMAGE_HEIGHT = world.getHeight() * PIXEL_SCALE;

        // Constructs a BufferedImage of one of the predefined image types.
        BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        // Create a graphics which can be used to draw into the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();

        // fill all the image with white
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        final BufferedImage tilesetImage = getTilesetImage("tilesets/world001_tileset.png");

        for (int x = 0; x < world.getHeight(); x++) {
            for (int y = 0; y < world.getWidth(); y++) {

                final int pixelSize = World.getPixelSize();
                final int terrainTypeOrdinal = world.getTerrainLayer()[x][y];

                final BufferedImage tileImage = tilesetImage.getSubimage(TerrainType.values()[terrainTypeOrdinal].getPoint().x * pixelSize, TerrainType.values()[terrainTypeOrdinal].getPoint().y * pixelSize, pixelSize, pixelSize);
                g2d.drawImage(tileImage, null, x * PIXEL_SCALE, y * PIXEL_SCALE);

                if (false) {
                    if (TerrainType.values()[terrainTypeOrdinal].isPassable() && world.getMonsterLevelLayer()[x][y] != -1) {
                        g2d.drawString(String.valueOf(world.getMonsterLevelLayer()[x][y]), x * PIXEL_SCALE, (y + 1) * PIXEL_SCALE);
                    }
                }

                if (true) {
                    if (world.getRegionsLayer()[x][y] > 0) {
                        g2d.drawString(String.valueOf(world.getRegionsLayer()[x][y]), x * PIXEL_SCALE, (y + 1) * PIXEL_SCALE);
                    }
                }
            }
        }

        //smoothenMap(world, bufferedImage);

        g2d.dispose();
        mapImages.put(world.getName(), bufferedImage);

        return bufferedImage;
    }

    private void smoothenMap(World world, BufferedImage bufferedImage) {
        for (int x = 31; x < world.getWidth() * 32 - 1; x = x + 32) {
            for (int y = 31; y < world.getHeight() * 32 - 1; y = y + 1) {
                if (bufferedImage.getRGB(x, y) != bufferedImage.getRGB(x + 1, y)) {
                    if (Randomizer.get(0, 1) == 0) {
                        bufferedImage.setRGB(x, y, bufferedImage.getRGB(x + 1, y));
                    }

                }
                if (bufferedImage.getRGB(x, y) != bufferedImage.getRGB(x, y + 1)) {
                    if (Randomizer.get(0, 1) == 0) {
                        bufferedImage.setRGB(x, y, bufferedImage.getRGB(x, y + 1));
                    }

                }
            }
        }

        for (int y = 31; y < world.getHeight() * 32 - 1; y = y + 32) {
            for (int x = 31; x < world.getWidth() * 32 - 1; x = x + 1) {
                if (bufferedImage.getRGB(x, y) != bufferedImage.getRGB(x + 1, y)) {
                    if (Randomizer.get(0, 1) == 0) {
                        bufferedImage.setRGB(x, y, bufferedImage.getRGB(x + 1, y));
                    }

                }
                if (bufferedImage.getRGB(x, y) != bufferedImage.getRGB(x, y + 1)) {
                    if (Randomizer.get(0, 1) == 0) {
                        bufferedImage.setRGB(x, y, bufferedImage.getRGB(x, y + 1));
                    }

                }
            }
        }
    }

    private BufferedImage getTilesetImage(String imagePath) {
        BufferedImage tempImage;
        try {
            FileHandle internal = Gdx.files.classpath("core/assets/maps/" + imagePath.substring(0, imagePath.length() - 4) + ".png");
            if (!internal.file().exists()) {
                internal = Gdx.files.classpath("assets/maps/" + imagePath.substring(0, imagePath.length() - 4) + ".png");
            }

            tempImage = ImageIO.read(internal.file());
        } catch (Exception e) {
            tempImage = null;
            e.printStackTrace();
        }
        return tempImage;
    }


    public void createSpritesLayer(Graphics2D graphics, World world) {
/*        BufferedImage bufferedImage1 = new BufferedImage(width, height, TYPE_BYTE_INDEXED);
        Graphics2D g = bufferedImage1.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g.fillRect(0,0, width, height);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));*/
        /*world.getGates().stream().forEach(sprite -> {
            BufferedImage image = sprite.getMapImage();
            graphics.drawImage(image, null, sprite.getMapPosition().x * PIXEL_SCALE, sprite.getMapPosition().y * PIXEL_SCALE);
        });

        world.getTowns().values().stream().forEach(sprite -> {
            BufferedImage image = sprite.getMapImage();
            graphics.drawImage(image, null, sprite.getMapPosition().x * PIXEL_SCALE, sprite.getMapPosition().y * PIXEL_SCALE);
            drawAdditionalInfo(graphics, sprite.getMapInfo(), sprite.getMapPosition());
        });

        world.getAncients().stream().forEach(sprite -> {
            BufferedImage image = sprite.getMapImage();
            graphics.drawImage(image, null, sprite.getMapPosition().x * PIXEL_SCALE, sprite.getMapPosition().y * PIXEL_SCALE);
            drawAdditionalInfo(graphics, sprite.getMapInfo(), sprite.getMapPosition());
        });

        world.getResources().stream().forEach(sprite -> {
            BufferedImage image = sprite.getMapImage();
            //resources are half size of the pixel
            graphics.drawImage(image, sprite.getMapPosition().x * PIXEL_SCALE, sprite.getMapPosition().y * PIXEL_SCALE, PIXEL_SCALE / 2, PIXEL_SCALE / 2, null);
        });

        world.getPlayers().stream().forEach(sprite -> {
            BufferedImage image = sprite.getMapImage();
            graphics.drawImage(image, null, sprite.getMapPosition().x * PIXEL_SCALE, sprite.getMapPosition().y * PIXEL_SCALE);
            drawAdditionalInfo(graphics, sprite.getMapInfo(), sprite.getMapPosition());
        });*/
    }

    private void drawAdditionalInfo(Graphics2D graphics, String mapInfo, Point mapPosition) {
        if (mapInfo != null) {
            graphics.drawString(mapInfo, mapPosition.x * PIXEL_SCALE, mapPosition.y * PIXEL_SCALE);
        }
    }


    private void visualizeSprites(Graphics2D g2d, List<WorldEntity> worldEntities) {
       /* worldEntities.stream().forEach(sprite -> {
            BufferedImage image = sprite.getMapImage();
            g2d.drawImage(image, null, sprite.getMapPosition().x * PIXEL_SCALE, sprite.getMapPosition().y * PIXEL_SCALE);
        });*/
    }

    private void visualizeAncients(Graphics2D g2d, World world) {
        world.getAncients().stream().forEach(ancientBeing -> {
            //BufferedImage image = GameResources.getTileImage(ancientBeing.getAncientMonsterType().getTilesetImageIndex());
            BufferedImage image = null;

            g2d.drawImage(image, null, ancientBeing.getMapPosition().x * PIXEL_SCALE, ancientBeing.getMapPosition().y * PIXEL_SCALE);
            g2d.drawString(ancientBeing.getAncientMonsterType().getName(), ancientBeing.getMapPosition().x * PIXEL_SCALE, ancientBeing.getMapPosition().y * PIXEL_SCALE);
        });
    }

    private void visualizeResources(Graphics2D g2d, World world) {
        world.getResources().stream().forEach(resource -> {
            g2d.setColor(resource.getResourceType().getColor());
            g2d.fillRect(resource.getMapPosition().x * PIXEL_SCALE, resource.getMapPosition().y * PIXEL_SCALE + PIXEL_SCALE - 8, 8, 8);
        });
    }


    @Override
    public void visualizeTowns(Graphics2D g2d, World world) {
        //BufferedImage image = GameResources.getBufferedImage("town");
        BufferedImage image = null;

        world.getTowns().values().stream().forEach(town -> {
            g2d.drawImage(image, null, town.getMapPosition().x * PIXEL_SCALE, town.getMapPosition().y * PIXEL_SCALE);
            g2d.drawString(town.getName(), town.getMapPosition().x * PIXEL_SCALE, town.getMapPosition().y * PIXEL_SCALE);
        });
    }


}
