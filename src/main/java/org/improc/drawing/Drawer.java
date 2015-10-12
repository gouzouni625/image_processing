package org.improc.drawing;

import java.awt.Color;

import org.improc.image.Image;

public class Drawer{
  public static Image drawLine(Image image, int xStart, int yStart, int xEnd, int yEnd, int thickness, int color){
    if(xStart == xEnd){
      if(yStart < yEnd){
        for(int offset = -thickness / 2; offset < thickness / 2;offset++){
          drawLineBresenham_2(image, xStart + offset, yStart, xEnd + offset, yEnd, color);
        }
        return image;
      }
      else if(yStart == yEnd){
        image.setPixel(xStart, yStart, color);
        return image;
      }
      else{
        for(int offset = -thickness / 2; offset < thickness / 2;offset++){
          drawLineBresenham_6(image, xStart + offset, yStart, xEnd + offset, yEnd, color);
        }
        return image;
      }
    }

    double slope = (double)(yEnd - yStart) / ((double)(xEnd - xStart));

    if(slope < -1){
      if(xStart < xEnd){
        for(int offset = -thickness / 2; offset < thickness / 2;offset++){
          drawLineBresenham_7(image, xStart, yStart + offset, xEnd, yEnd + offset, color);
        }
        return image;
      }
      else{
        for(int offset = -thickness / 2; offset < thickness / 2;offset++){
          drawLineBresenham_3(image, xStart, yStart + offset, xEnd, yEnd + offset, color);
        }
        return image;
      }
    }
    else if(-1 <= slope && slope < 0){
      if(xStart < xEnd){
        for(int offset = -thickness / 2; offset < thickness / 2;offset++){
          drawLineBresenham_8(image, xStart, yStart + offset, xEnd, yEnd + offset, color);
        }
        return image;
      }
      else{
        for(int offset = -thickness / 2; offset < thickness / 2;offset++){
          drawLineBresenham_4(image, xStart, yStart + offset, xEnd, yEnd + offset, color);
        }
        return image;
      }
    }
    else if(0 <= slope && slope <= 1){
      if(xStart < xEnd){
        //for(int offset = -thickness / 2; offset < thickness / 2;offset++){
          drawLineBresenham_1(image, xStart, yStart/* + offset*/, xEnd, yEnd/* + offset*/, color);
        //}
        return image;
      }
      else{
        for(int offset = -thickness / 2; offset < thickness / 2;offset++){
          drawLineBresenham_5(image, xStart, yStart + offset, xEnd, yEnd + offset, color);
        }
        return image;
      }
    }
    else{ // if(slope > 1)
      if(xStart < xEnd){
        for(int offset = -thickness / 2; offset < thickness / 2;offset++){
          drawLineBresenham_2(image, xStart, yStart + offset, xEnd, yEnd + offset, color);
        }
        return image;
      }
      else{
        for(int offset = -thickness / 2; offset < thickness / 2;offset++){
          drawLineBresenham_6(image, xStart, yStart + offset, xEnd, yEnd + offset, color);
        }
        return image;
      }
    }
  }

  public static void drawLineBresenham_1(Image image, int xStart, int yStart, int xEnd, int yEnd, int color){
    int dx = xEnd - xStart;
    int dy = yEnd - yStart;
    int e = -(dx >> 1);
    int x = xStart;
    int y = yStart;

    while(x <= xEnd){
      for(int offset = -10 / 2; offset < 10 / 2;offset++) {
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

  public static void drawLineBresenham_2(Image image, int xStart, int yStart, int xEnd, int yEnd, int color){
    int dx = xEnd - xStart;
    int dy = yEnd - yStart;
    int e = -(dy >> 1);
    int x = xStart;
    int y = yStart;

    while(y <= yEnd){
      image.setPixel(x, y, color);

      y++;

      e = e + dx;
      if(e >= 0){
        x++;
        e = e - dy;
      }
    }
  }

  public static void drawLineBresenham_3(Image image, int xStart, int yStart, int xEnd, int yEnd, int color){
    int dx = xStart - xEnd;
    int dy = yEnd - yStart;
    int e = -(dy >> 1);
    int x = xStart;
    int y = yStart;

    while(y <= yEnd){
      image.setPixel(x, y, color);

      y++;

      e = e + dx;
      if(e >= 0){
        x--;
        e = e - dy;
      }
    }
  }

  public static void drawLineBresenham_4(Image image, int xStart, int yStart, int xEnd, int yEnd, int color){
    drawLineBresenham_8(image, xEnd, yEnd, xStart, yStart, color);
  }

  public static void drawLineBresenham_5(Image image, int xStart, int yStart, int xEnd, int yEnd, int color){
    drawLineBresenham_1(image, xEnd, yEnd, xStart, yStart, color);
  }

  public static void drawLineBresenham_6(Image image, int xStart, int yStart, int xEnd, int yEnd, int color){
    drawLineBresenham_2(image, xEnd, yEnd, xStart, yStart, color);
  }

  public static void drawLineBresenham_7(Image image, int xStart, int yStart, int xEnd, int yEnd, int color){
    drawLineBresenham_3(image, xEnd, yEnd, xStart, yStart, color);
  }

  public static void drawLineBresenham_8(Image image, int xStart, int yStart, int xEnd, int yEnd, int color){
    int dx = xEnd - xStart;
    int dy = yStart - yEnd;
    int e = -(dx >> 1);
    int x = xStart;
    int y = yStart;

    while(x <= xEnd){
      image.setPixel(x, y, color);

      x++;

      e = e + dy;
      if(e >= 0){
        y--;
        e = e - dx;
      }
    }
  }

  public static final int WHITE = Color.WHITE.getRGB();

}
