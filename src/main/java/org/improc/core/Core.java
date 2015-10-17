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

  public static Image blur(Image image, int[][] kernel, int topOffset, int rightOffset, int bottomOffset, int leftOffset){
    int originalWidth = image.getWidth();
    int originalHeight = image.getHeight();

    int width = originalWidth - leftOffset - rightOffset;
    int height = originalHeight - bottomOffset - topOffset;

    int kernelWidth = kernel.length;
    int halfKernelWidth = kernelWidth >> 1;
    int kernelHeight = kernel[0].length;
    int halfKernelHeight = kernelHeight >> 1;
    double reverseKernelSize = 1 / (double)(kernelWidth * kernelHeight);

    Image blurredImage = new Image(originalWidth, originalHeight);

    /* ===== Calculate horizontal sums ===== */
    int minX = leftOffset - halfKernelWidth;
    int maxX = minX + kernelWidth;
    int minY = bottomOffset - halfKernelHeight;
    int maxY;

    int horizontalSumsHeight = height + (halfKernelHeight << 1);
    int[][] horizontalSums = new int[width][horizontalSumsHeight];

    for(int y = 0;y < horizontalSumsHeight;y++){
      horizontalSums[0][y] = 0;

      for(int x = minX;x < maxX;x++){
        horizontalSums[0][y] += image.getPixel(x, y + minY) & 0xFF;
      }
    }

    int maxIndex = width - 1;
    for(int x = 0;x < maxIndex;x++){
      for(int y = 0;y < horizontalSumsHeight;y++){
        horizontalSums[x + 1][y] = horizontalSums[x][y] - (image.getPixel(minX + x, y + minY) & 0xFF)
                                                        + (image.getPixel(maxX + x, y + minY) & 0xFF);
      }
    }

    /* ===== Calculate vertical sums ===== */
    minX = leftOffset - halfKernelWidth;
    // maxX
    minY = bottomOffset - halfKernelHeight;
    maxY = minY + kernelHeight;

    int verticalSumsWidth = width + (halfKernelWidth << 1);
    int[][] verticalSums = new int[verticalSumsWidth][height];

    for(int x = 0;x < verticalSumsWidth;x++){
      verticalSums[x][0] = 0;

      for(int y = minY;y < maxY;y++){
        verticalSums[x][0] += image.getPixel(x + minX, y) & 0xFF;
      }
    }

    /*minY--;
    maxY--;
    for(int y = 1;y < height;y++){
      for(int x = 0; x < verticalSumsWidth;x++){
        verticalSums[x][y] = verticalSums[x][y - 1] - image.getPixel(x, y + minY) & 0xFF
                                                    + image.getPixel(x, y + maxY) & 0xFF;
      }
    }*/

    /* ===== Calculate convolution ===== */
    minX = leftOffset - halfKernelWidth;
    maxX = minX + kernelWidth;

    int startingAccumulator = 0;
    for(int x = minX;x < maxX;x++){
      startingAccumulator += verticalSums[x][0];
    }
    int accumulator = startingAccumulator;

    maxX = leftOffset + width;
    maxY = bottomOffset + height;
    for(int x = leftOffset;x < maxX - 1;x++){
      for(int y = bottomOffset;y < maxY - 1;y++){
        blurredImage.setPixel(x, y, (byte)(accumulator * reverseKernelSize));

        accumulator = accumulator - horizontalSums[x - leftOffset][y - bottomOffset]
                                  + horizontalSums[x - leftOffset][y - bottomOffset + kernelHeight];
      }
      blurredImage.setPixel(x, maxY - 1, (byte)(accumulator * reverseKernelSize));

      startingAccumulator = startingAccumulator - verticalSums[x - leftOffset][0]
                                                + verticalSums[x - leftOffset + kernelWidth][0];
      accumulator = startingAccumulator;
    }
    for(int y = bottomOffset;y < maxY - 1;y++){
      blurredImage.setPixel(maxX - 1, y, (byte)(accumulator * reverseKernelSize));

      accumulator = accumulator - horizontalSums[maxX - 1 - leftOffset][y - bottomOffset]
          + horizontalSums[maxX - 1 - leftOffset][y - bottomOffset + kernelHeight];
    }
    blurredImage.setPixel(maxX - 1, maxY - 1, (byte)(accumulator * reverseKernelSize));

    return blurredImage;
  }

  public static Image addBorder(Image image, int topBorder, int rightBorder, int bottomBorder, int leftBorder){
    int oldWidth = image.getWidth();
    int oldHeight = image.getHeight();
    int newWidth = oldWidth + leftBorder + rightBorder;
    int newHeight = oldHeight + topBorder + bottomBorder;

    Image borderedImage = new Image(newWidth, newHeight);

    for(int x = 0;x < oldWidth;x++){
      for(int y = 0;y < oldHeight;y++){
        borderedImage.setPixel(leftBorder + x, bottomBorder + y, image.getPixel(x, y));
      }
    }

    return borderedImage;
  }

}
