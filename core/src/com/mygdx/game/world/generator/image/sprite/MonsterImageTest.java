package com.mygdx.game.world.generator.image.sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class MonsterImageTest {



    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(100, 100);
        frame.setLocation(300, 300);

        JPanel jPanel = new JPanel() {
            private Point headPos = new Point(16,6);
            private Dimension headSize = new Dimension(6, 10);
            private Dimension imgSize = new Dimension(32, 32);

            private Point torsoPos = new Point(16 , 16);
            private Dimension torsoSize = new Dimension(8, 10);

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                AffineTransform at = new AffineTransform();
                //at.translate(16, 16);
                at.scale(2, 2);
                //at.translate(-100, -100);

                g2.setTransform(at);

                g2.clearRect(0, 0, (int) imgSize.getWidth(), (int) imgSize.getHeight());
                g2.setColor(Color.RED);
                g2.drawRect(0, 0, 32, 32);
              /*  BufferedImage headImage = GameResources.getBufferedImage("head");
                headImage = ImageUtils.useFilter(headImage, new ThresholdImageFilter(155, 155, 0), null);
                BufferedImage torsoImage = GameResources.getBufferedImage("torso");
                BufferedImage armlImage = GameResources.getBufferedImage("arml");
                BufferedImage armrImage = GameResources.getBufferedImage("armr");


                int headX = (32 - headImage.getWidth()) / 2;
                int headY = 0;
                g2.drawImage(headImage, headX, headY, null);


                int torsoX = (32 - torsoImage.getWidth()) / 2;
                int torsoY = headImage.getHeight();
                g2.drawImage(torsoImage, torsoX, torsoY, null);
                g2.drawImage(armlImage, torsoX - armlImage.getWidth(), torsoY, null);
                g2.drawImage(armrImage, torsoImage.getWidth() + torsoX, torsoY, null);*/


                g2.dispose();

            }
        };
        frame.add(jPanel);





    }
}
