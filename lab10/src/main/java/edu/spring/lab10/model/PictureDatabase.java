package edu.spring.lab10.model;

import org.springframework.stereotype.Repository;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Repository
public class PictureDatabase implements PicturesMemory{

    private Map<String, BufferedImage> pictures = new HashMap<>();
    int addedPictures = 1;

    public PictureDatabase() {

    }

    @Override
    public String addPicture(BufferedImage image) {
        String key = "picture" + addedPictures;
        ++addedPictures;
        this.pictures.put(key, image);
        return key;
    }

    @Override
    public BufferedImage getPicture(String key) {
        if(!this.pictures.containsKey(key)) throw new IllegalArgumentException("No image at key: " + key);
        return pictures.get(key);
    }

    @Override
    public BufferedImage getClonedPicture(String key) {
        if(!this.pictures.containsKey(key)) throw new IllegalArgumentException("No image at key: " + key);
        //cloning function
        BufferedImage pattern = pictures.get(key);
        BufferedImage clone = new BufferedImage(pattern.getWidth(),
                pattern.getHeight(), pattern.getType());
        Graphics2D g2d = clone.createGraphics();
        g2d.drawImage(pattern, 0, 0, null);
        g2d.dispose();

        return clone;

    }

    @Override
    public boolean deletePicture(String key) {
        if(!pictures.containsKey(key)) return false;
        pictures.remove(key);
        return true;
    }

    @Override
    public boolean checkIfExists(String key) {
        return pictures.containsKey(key);
    }
}
