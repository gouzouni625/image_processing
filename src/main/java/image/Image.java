package main.java.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image{
  public Image(int width, int height){
    width_ = width;
    height_ = height;

    pixels_ = new int[width_][];
    for(int i = 0;i < width_;i++){
      pixels_[i] = new int[height_];

      for(int j = 0;j < height_;j++){
        pixels_[i][j] = 0;
      }
    }
  }

  public void setPixel(int x, int y, int value){
    pixels_[x][y] = value;
  }

  public int getPixel(int x, int y){
    return pixels_[x][y];
  }

  public void save(String path) throws IOException{
    BufferedImage bufferedImage = new BufferedImage(width_, height_, BufferedImage.TYPE_INT_RGB);
    for(int x = 0;x < width_;x++){
      for(int y = 0;y < height_;y++){
        bufferedImage.setRGB(x, height_ - y - 1, pixels_[x][y]);
      }
    }

    File outputFile = new File(path);
    ImageIO.write(bufferedImage, "bmp", outputFile);
  }

  private int width_;
  private int height_;

  // Pixels begin counting from the bottom left with the first dimensional being the horizontal.
  private int[][] pixels_;

}
