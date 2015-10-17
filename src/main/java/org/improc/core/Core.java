package org.improc.core;

import org.improc.image.Image;

/**
 *  @class This class implements some basic, image processing methods.
 *
 *  The indexing of the images starts from the bottom left corner and is done in a cartesian basis with zero(0) as the
 *  first index, rather than in a row-column one. Concretely, given the following four by four(4x4) image:
 *
 *  03 13 23 33
 *  02 12 22 32
 *  01 11 21 31
 *  00 10 20 30
 *
 *  the pixel with index (0, 0) would have a value of 00. Accordingly, the pixel with index (2, 3) would have a value of
 *  23, etc...
 */
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

  /**
   * @brief This method implements the blurring of a given image by applying the convolution between it and a
   *        <B>specific<B/> kernel.
   *
   *        The resulting image has the same size with the one given as input. The kernel is a two(2) dimensional, square
   *        matrix with every value equal to one(1). The provided offsets, should be integers bigger than the half of the
   *        kernel edge size.
   *
   * @param image The image to convolve with the specific kernel.
   * @param kernelEdge The size of the edge of the two(2) dimensional, square matrix which implements the kernel.
   * @param topOffset Number of pixels, counting from the top of the image, to be excluded from the convolution.
   * @param rightOffset Number of pixels, counting from the right of the image, to be excluded from the convolution.
   * @param bottomOffset Number of pixels, counting from the bottom of the image, to be excluded from the convolution.
   * @param leftOffset Number of pixels, counting from the left of the image, to be excluded from the convolution.
   *
   * @return Returns the result of the convolution between the given image and the kernel. The resulting image is of the
   *         same width and height with the given one.
   */
  public static Image blur(Image image, int kernelEdge, int topOffset, int rightOffset, int bottomOffset, int leftOffset){
    // Full image width and height. This is the width and the height of the image without excluding the offset pixels.
    int originalWidth = image.getWidth();
    int originalHeight = image.getHeight();

    // Image width and height after excluding the offset pixels.
    int width = originalWidth - leftOffset - rightOffset;
    int height = originalHeight - bottomOffset - topOffset;

    // Calculate  some values that are usually used, to save execution time.
    int kernelWidth = kernelEdge;
    int halfKernelWidth = kernelWidth >> 1;
    int kernelHeight = kernelEdge;
    int halfKernelHeight = kernelHeight >> 1;
    double reverseKernelSize = 1 / (double)(kernelWidth * kernelHeight);

    Image blurredImage = new Image(originalWidth, originalHeight);

    /*
    The implementation of the convolution is split into three(3) parts. The first part, calculates the horizontal sums of
    the given image. Concretely, in the following, four by four(4x4) image with an offset of four(4) pixels in each side:

    00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 03 13 23 33 00 00 00 00
    00 00 00 00 02 12 22 32 00 00 00 00
    00 00 00 00 01 11 21 31 00 00 00 00
    00 00 00 00 00 10 20 30 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00

    a kernel with an edge size equal to three(3), would result in the calculation of the following horizontal sums:

            horizontalSum[0][0]                  horizontalSum[0][1]
    00 00 00 00 00 00 00 00 00 00 00 00  00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00  00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00  00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00  00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 03 13 23 33 00 00 00 00  00 00 00 00 03 13 23 33 00 00 00 00
    00 00 00 00 02 12 22 32 00 00 00 00  00 00 00 00 02 12 22 32 00 00 00 00
    00 00 00 00 01 11 21 31 00 00 00 00  00 00 00 00 01 11 21 31 00 00 00 00
    00 00 00 00 00 10 20 30 00 00 00 00  00 00 00 [0 00 10] 20 30 00 00 00 00
    00 00 00 [0 00 0] 00 00 00 00 00 00  00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00  00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00  00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00  00 00 00 00 00 00 00 00 00 00 00 00

            horizontalSum[1][0]
    00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 03 13 23 33 00 00 00 00
    00 00 00 00 02 12 22 32 00 00 00 00
    00 00 00 00 01 11 21 31 00 00 00 00
    00 00 00 00 00 10 20 30 00 00 00 00
    00 00 00 00 [0 00 0] 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00
     */

    /* ===== Calculate horizontal sums ===== */
    int minX = leftOffset - halfKernelWidth;
    int maxX = minX + kernelWidth;
    int minY = bottomOffset - halfKernelHeight;
    int maxY;

    int horizontalSumsHeight = height + (halfKernelHeight << 1);
    int[][] horizontalSums = new int[width][horizontalSumsHeight];

    /*
    Calculate the first column of horizontal sums. For the rest columns, each horizontal sum is calculated using the
    horizontal sum to its left, by removing the value of a pixel and adding the value of another. The pixel with the
    value to be removed is the one that belongs only to the left horizontal sum. The pixel with the value to be added
    is the one that belongs to the right horizontal sum.

    For example, given the horizontal sum in position (x, y),

    hs1: [00 10 11] 12

    the horizontal sum in position (x + 1, y) would be

    hs2: 00 [10 11 12]

    and the value of hs2 would be value(hs2) = value(hs1) - value(pixel(0, 0)) + value(pixel(1, 2)) .
    */
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

    /*
    The second part, calculates the vertical sums. Due to the way the convolution is calculated, only the first row of
    vertical sums is needed. The way they are calculated is the same with the way the horizontal sums are calculated.
     */

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

    /*
    The third part is the actual calculation of the convolution. This is done using the definition of the convolution,
    which is to slide the kernel over the image placing the center of the kernel above every pixel. The new value of the
    pixel on which the center of the kernel is placed, is the sum of the values of the pixels under the kernel, divided
    by the square of the kernel's edge.

    The kernel slides over the image by column, starting from the bottom left corner. That is, the new values of the pixels
    are calculated with the following sequence:

    00, 01, 02, 03, ...,
    10, 11, 12, 13, ...
     */

    /* ===== Calculate convolution ===== */
    minX = leftOffset - halfKernelWidth;
    maxX = minX + kernelWidth;

    int startingAccumulator = 0;
    for(int x = minX;x < maxX;x++){
      startingAccumulator += verticalSums[x][0];
    }
    int accumulator = startingAccumulator;

    maxX = leftOffset + width - 1;
    maxY = bottomOffset + height - 1;
    for(int x = leftOffset;x < maxX;x++){
      for(int y = bottomOffset;y < maxY;y++){
        blurredImage.setPixel(x, y, (byte)(accumulator * reverseKernelSize));

        accumulator = accumulator - horizontalSums[x - leftOffset][y - bottomOffset]
                                  + horizontalSums[x - leftOffset][y - bottomOffset + kernelHeight];
      }
      // The value of the last pixel of this column is calculated outside of the `for` loop. This is done because, in each
      // loop, the value of the `accumulator` of the next loop is calculated. When the final loop is executed, the value
      // of the accumulator for the next loop is not needed, and also leads to an `IndexOutOfBounds` exception.
      blurredImage.setPixel(x, maxY, (byte)(accumulator * reverseKernelSize));

      startingAccumulator = startingAccumulator - verticalSums[x - leftOffset][0]
                                                + verticalSums[x - leftOffset + kernelWidth][0];
      accumulator = startingAccumulator;
    }

    // The values of the last column of pixels is calculated outside of the `for` loop. This is done because, in each loop,
    // the value of the `startingAccumulator` of the next loop is calculated. When the final loop is executed, the value
    // of the `startingAccumulator` for the next loop is not needed, and also leads to an `IndexOutOfBounds` exception.
    for(int y = bottomOffset;y < maxY;y++){
      blurredImage.setPixel(maxX, y, (byte)(accumulator * reverseKernelSize));

      accumulator = accumulator - horizontalSums[maxX - leftOffset][y - bottomOffset]
          + horizontalSums[maxX - leftOffset][y - bottomOffset + kernelHeight];
    }
    blurredImage.setPixel(maxX, maxY, (byte)(accumulator * reverseKernelSize));

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
