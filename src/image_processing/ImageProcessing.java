package image_processing;

import main.java.image.Image;

public class ImageProcessing{
  public static Image resize(Image image, int newWidth, int newHeight){
    Image resizedImage = new Image(newWidth, newHeight);

    int oldWidth = image.getWidth();
    int oldHeight = image.getHeight();

    double relativeWidth = (double)(oldWidth) / ((double)(newWidth));
    double relativeHeight = (double)(oldHeight) / ((double)(newHeight));

    for(int x = 0;x < newWidth;x++){
      for(int y = 0;y < newHeight;y++){

        double factorX = x * relativeWidth;
        int oldX = (int)(factorX);
        double deltaX = factorX - oldX;

        double factorY = y * relativeHeight;
        int oldY = (int)(factorY);
        double deltaY = factorY - oldY;

        double newPixelValue = 0;
        if((oldX == oldWidth - 1) && oldY == (oldHeight - 1)){
          newPixelValue = image.getPixel(oldX, oldY) * (1 - deltaX) * (1 - deltaY);
        }
        else if(oldX == oldWidth - 1){
          newPixelValue = image.getPixel(oldX, oldY) * (1 - deltaX) * (1 - deltaY) +
              image.getPixel(oldX, oldY + 1) * (1 - deltaX) * deltaY;
        }
        else if(oldY == oldHeight - 1){
          newPixelValue = image.getPixel(oldX, oldY) * (1 - deltaX) * (1 - deltaY) +
              image.getPixel(oldX + 1, oldY) * deltaX * (1 - deltaY);
        }
        else{
          newPixelValue = image.getPixel(oldX, oldY) * (1 - deltaX) * (1 - deltaY) +
              image.getPixel(oldX + 1, oldY) * deltaX * (1 - deltaY) +
              image.getPixel(oldX, oldY + 1) * (1 - deltaX) * deltaY +
              image.getPixel(oldX + 1, oldY + 1) * deltaX * deltaY;
        }

        resizedImage.setPixel(x, y, (int)(newPixelValue));
      }
    }

    return resizedImage;
  }

}
