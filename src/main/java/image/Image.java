package main.java.image;

public class Image{
  public Image(int numberOfRows, int numberOfColumns){
    numberOfRows_ = numberOfRows;
    numberOfColumns_ = numberOfColumns;

    pixels_ = new double[numberOfRows_][];
    for(int i = 0;i < numberOfRows_;i++){
      pixels_[i] = new double[numberOfColumns_];

      for(int j = 0;j < numberOfColumns_;j++){
        pixels_[i][j] = 0;
      }
    }
  }

  private int numberOfRows_;
  private int numberOfColumns_;

  private double[][] pixels_;

}
