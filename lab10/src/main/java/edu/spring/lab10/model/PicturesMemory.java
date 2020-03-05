package edu.spring.lab10.model;

import java.awt.image.BufferedImage;

public interface PicturesMemory {
    String addPicture(BufferedImage image);
    BufferedImage getPicture(String key);
    BufferedImage getClonedPicture(String key);
    boolean deletePicture(String key);
    boolean checkIfExists(String key);


}
