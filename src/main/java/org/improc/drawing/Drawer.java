package org.improc.drawing;

import java.awt.Color;

import org.improc.image.Image;

public class Drawer{
  public static Image drawLine(Image image, int xStart, int yStart, int xEnd, int yEnd,
                               int thickness, byte color){
    if(xStart == xEnd){
      if(yStart < yEnd){
        drawLineBresenham2Vertical(image, xStart, yStart, xEnd, yEnd, color, thickness);
        return image;
      }
      else if(yStart == yEnd){
        image.setPixel(xStart, yStart, color);
        return image;
      }
      else{
        drawLineBresenham6Vertical(image, xStart, yStart, xEnd, yEnd, color, thickness);
        return image;
      }
    }

    double slope = (double)(yEnd - yStart) / ((double)(xEnd - xStart));

    if(slope < -1){
      if(xStart < xEnd){
        drawLineBresenham7(image, xStart, yStart, xEnd, yEnd, color, thickness);
        return image;
      }
      else{
        drawLineBresenham3(image, xStart, yStart, xEnd, yEnd, color, thickness);
        return image;
      }
    }
    else if(-1 <= slope && slope < 0){
      if(xStart < xEnd){
          drawLineBresenham8(image, xStart, yStart, xEnd, yEnd, color, thickness);
        return image;
      }
      else{
          drawLineBresenham4(image, xStart, yStart, xEnd, yEnd, color, thickness);
        return image;
      }
    }
    else if(0 <= slope && slope <= 1){
      if(xStart < xEnd){
        drawLineBresenham1(image, xStart, yStart, xEnd, yEnd, color, thickness);
        return image;
      }
      else{
        drawLineBresenham5(image, xStart, yStart, xEnd, yEnd, color, thickness);
        return image;
      }
    }
    else{ // if(slope > 1)
      if(xStart < xEnd){
        drawLineBresenham2(image, xStart, yStart, xEnd, yEnd, color, thickness);
        return image;
      }
      else{
        drawLineBresenham6(image, xStart, yStart, xEnd, yEnd, color, thickness);
        return image;
      }
    }
  }

  public static void drawLineBresenham1(Image image, int xStart, int yStart, int xEnd, int yEnd,
                                        byte color, int thickness){
    int dx = xEnd - xStart;
    int dy = yEnd - yStart;
    int e = -(dx >> 1);
    int x = xStart;
    int y = yStart;

    while(x <= xEnd){
      for(int offset = -thickness / 2; offset < thickness / 2;offset++) {
        image.setPixel(x, y + offset, color);
      }

      x++;

      e = e + dy;
      if(e >= 0){
        y++;
        e = e - dx;
      }
    }
  }

  public static void drawLineBresenham2(Image image, int xStart, int yStart, int xEnd, int yEnd,
                                        byte color, int thickness){
    int dx = xEnd - xStart;
    int dy = yEnd - yStart;
    int e = -(dy >> 1);
    int x = xStart;
    int y = yStart;

    while(y <= yEnd){
      for(int offset = -thickness / 2; offset < thickness / 2;offset++) {
        image.setPixel(x, y + offset, color);
      }

      y++;

      e = e + dx;
      if(e >= 0){
        x++;
        e = e - dy;
      }
    }
  }

  public static void drawLineBresenham2Vertical(Image image, int xStart, int yStart, int xEnd,
                                                int yEnd, byte color, int thickness){
    int dx = xEnd - xStart;
    int dy = yEnd - yStart;
    int e = -(dy >> 1);
    int x = xStart;
    int y = yStart;

    while(y <= yEnd){
      for(int offset = -thickness / 2; offset < thickness / 2;offset++) {
        image.setPixel(x + offset, y, color);
      }

      y++;

      e = e + dx;
      if(e >= 0){
        x++;
        e = e - dy;
      }
    }
  }

  public static void drawLineBresenham3(Image image, int xStart, int yStart, int xEnd, int yEnd,
                                        byte color, int thickness){
    int dx = xStart - xEnd;
    int dy = yEnd - yStart;
    int e = -(dy >> 1);
    int x = xStart;
    int y = yStart;

    while(y <= yEnd){
      for(int offset = -thickness / 2; offset < thickness / 2;offset++) {
        image.setPixel(x, y + offset, color);
      }

      y++;

      e = e + dx;
      if(e >= 0){
        x--;
        e = e - dy;
      }
    }
  }

  public static void drawLineBresenham4(Image image, int xStart, int yStart, int xEnd, int yEnd,
                                        byte color, int thickness){
    drawLineBresenham8(image, xEnd, yEnd, xStart, yStart, color, thickness);
  }

  public static void drawLineBresenham5(Image image, int xStart, int yStart, int xEnd, int yEnd,
                                        byte color, int thickness){
    drawLineBresenham1(image, xEnd, yEnd, xStart, yStart, color, thickness);
  }

  public static void drawLineBresenham6(Image image, int xStart, int yStart, int xEnd, int yEnd,
                                        byte color, int thickness){
    drawLineBresenham2(image, xEnd, yEnd, xStart, yStart, color, thickness);
  }

  public static void drawLineBresenham6Vertical(Image image, int xStart, int yStart, int xEnd,
                                                int yEnd, byte color, int thickness){
    drawLineBresenham2Vertical(image, xEnd, yEnd, xStart, yStart, color, thickness);
  }

  public static void drawLineBresenham7(Image image, int xStart, int yStart, int xEnd, int yEnd,
                                        byte color, int thickness){
    drawLineBresenham3(image, xEnd, yEnd, xStart, yStart, color, thickness);
  }

  public static void drawLineBresenham8(Image image, int xStart, int yStart, int xEnd, int yEnd,
                                        byte color, int thickness){
    int dx = xEnd - xStart;
    int dy = yStart - yEnd;
    int e = -(dx >> 1);
    int x = xStart;
    int y = yStart;

    while(x <= xEnd){
      for(int offset = -thickness / 2; offset < thickness / 2;offset++) {
        image.setPixel(x, y + offset, color);
      }

      x++;

      e = e + dy;
      if(e >= 0){
        y--;
        e = e - dx;
      }
    }
  }

  public static final byte WHITE = (byte)255;
  public static final byte BLACK = (byte)0;

}
