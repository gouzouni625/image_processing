package main.java.drawing;

import java.awt.Color;

import main.java.image.Image;

public class Drawer{
  public static Image drawLine(Image image, double xStart, double yStart, double xEnd, double yEnd, int thickness){
    if(xStart == xEnd){
      if(yStart == yEnd){

      }
    }

    return image;
  }

  public static void drawLineBresenham_1(Image image, int xStart, int yStart, int xEnd, int yEnd){
    int dx = xEnd - xStart;
    int dy = yEnd - yStart;
    int e = -(dx >> 1);
    int x = xStart;
    int y = yStart;

    while(x <= xEnd){
      image.setPixel(x, y, WHITE);

      x++;

      e = e + dy;
      if(e >= 0){
        y++;
        e = e - dx;
      }
    }
  }

  public static void drawLineBresenham_2(Image image, int xStart, int yStart, int xEnd, int yEnd){
    int dx = xEnd - xStart;
    int dy = yEnd - yStart;
    int e = -(dy >> 1);
    int x = xStart;
    int y = yStart;

    while(y <= yEnd){
      image.setPixel(x, y, WHITE);

      y++;

      e = e + dx;
      if(e >= 0){
        x++;
        e = e - dy;
      }
    }
  }

  public static void drawLineBresenham_3(Image image, int xStart, int yStart, int xEnd, int yEnd){
    int dx = xStart - xEnd;
    int dy = yEnd - yStart;
    int e = -(dy >> 1);
    int x = xStart;
    int y = yStart;

    while(y <= yEnd){
      image.setPixel(x, y, WHITE);

      y++;

      e = e + dx;
      if(e >= 0){
        x--;
        e = e - dy;
      }
    }
  }

  public static void drawLineBresenham_4(Image image, int xStart, int yStart, int xEnd, int yEnd){
    drawLineBresenham_8(image, xEnd, yEnd, xStart, yStart);
  }

  public static void drawLineBresenham_5(Image image, int xStart, int yStart, int xEnd, int yEnd){
    drawLineBresenham_1(image, xEnd, yEnd, xStart, yStart);
  }

  public static void drawLineBresenham_6(Image image, int xStart, int yStart, int xEnd, int yEnd){
    drawLineBresenham_2(image, xEnd, yEnd, xStart, yStart);
  }

  public static void drawLineBresenham_7(Image image, int xStart, int yStart, int xEnd, int yEnd){
    drawLineBresenham_3(image, xEnd, yEnd, xStart, yStart);
  }

  public static void drawLineBresenham_8(Image image, int xStart, int yStart, int xEnd, int yEnd){
    int dx = xEnd - xStart;
    int dy = yStart - yEnd;
    int e = -(dx >> 1);
    int x = xStart;
    int y = yStart;

    while(x <= xEnd){
      image.setPixel(x, y, WHITE);

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
