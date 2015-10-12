package org.improc.core;

import org.improc.image.Image;

public class Core {

  public static Image resize(Image image, int newWidth, int newHeight) {
    int oldWidth = image.getWidth();
    int oldHeight = image.getHeight();

    Image resizedImage = new Image(newWidth, newHeight);

    int relativeWidth = (oldWidth << 16) / newWidth;
    int relativeHeight = (oldHeight << 16) / newHeight;

    int oldX = 0;
    int factorX = relativeWidth;
    for(int x = 0;x < newWidth;x++) {

      int oldY = 0;
      int factorY = relativeHeight;
      for(int y = 0;y < newHeight;y++) {
        resizedImage.setPixel(x, y, image.getPixel(oldX, oldY));

        oldY = factorY >> 16;
        factorY += relativeHeight;
      }

      oldX = factorX >> 16;
      factorX += relativeWidth;
    }

    return resizedImage;
  }

}
