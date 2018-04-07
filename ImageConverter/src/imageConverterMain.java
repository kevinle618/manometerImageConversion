import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.awt.Color;
import java.awt.image.RescaleOp;

import java.util.Arrays;

import java.lang.NullPointerException;
import java.io.IOException;

public class imageConverterMain {

    public static void main(String[] args) {

        /**
         * The varible brighten factor, is the factor by which you want to contrast the image.
         */
        float brightenFactor = 1.2f;

        /**
         * A try statement is needed here b/c it is possible that there is nothing in the file to be read.
         */
        try {
            File OG_POST_img = new File("OG_POST_DRACS_Camera_20141031_155932.jpg");
            File preimg = new File("PRE_DRACS_Camera_20141031_155932.jpg");
            BufferedImage in = null;
            in = ImageIO.read(imageConverterMain.class.getResource("/resources/PRE_DRACS_Camera_20141031_155932.jpg"));

            /**
             * This try statement converts the image to gray scale.
             * RescaleOP is what creates the contrast after the grayscale has been added.
             */
            try {
                for (int i = 0; i < in.getHeight(); i++) {
                    for (int j = 0; j < in.getWidth(); j++) {
                        float r = new Color(in.getRGB(j, i)).getRed();
                        float g = new Color(in.getRGB(j, i)).getGreen();
                        float b = new Color(in.getRGB(j, i)).getBlue();
                        int grayScaled = (int) (r + g + b) / 3;
                        in.setRGB(j, i, new Color(grayScaled, grayScaled, grayScaled).getRGB());
                    }
                }
                RescaleOp op = new RescaleOp(brightenFactor, 0, null);
                in = op.filter(in, in);
            } catch (NullPointerException e) {
                System.out.println("nullpointer");
            }

            /**
             * This try statement converts the matrix to a string and appends
             * the string to a file to be read outside of IntelliJ.
             */
            try {
                System.out.println("this is the gray scale image");
                int[][] convertedMatrix = matrixConversion(in);
                System.out.println("completed conversion");
                writeMatrixStringFile(toMatrixString(convertedMatrix), "matrixFile.txt");
                File outputfile = new File("savedScaled.jpg");
                ImageIO.write(in, "jpg", outputfile);
            } catch (IOException e) {

            }

        } catch (IOException e) {
            System.out.println("IO EXCPETION");
            e.printStackTrace();
        }


    }

    /**
     * This method converts the image to a 2-d int array.
     *
     * @param input Takes in a buffered image to convert to a matrix.
     * @return A matrix consisting of the gray scale value of each pixel in the image. This simply calls on red b/c R=G=B.
     */
    private static int[][] matrixConversion(BufferedImage input) {
        int[][] grayScaleMatrix = new int[input.getHeight()][input.getWidth()];

        for (int i = 0; i < input.getHeight(); i++) {
            for (int j = 0; j < input.getWidth(); j++) {
                int grayScaleValue = new Color(input.getRGB(j, i)).getRed();
                grayScaleMatrix[i][j] = grayScaleValue;
            }
        }
        return grayScaleMatrix;
    }

    /**
     * This converts a matrix to a string. To either be printed or be written to a file.
     *
     * @param matrix any 2d int matrix.
     * @return String representation of the matrix.
     */
    private static String toMatrixString(int[][] matrix) {
        String matrixString = "";
        for (int i = 0; i < matrix.length; i++) {
            matrixString += Arrays.toString(matrix[i]) + "\n";
//            for (int j=0; j<matrix[0].length; j++){
//                if(j%matrix[0].length-1 != 0) {
//                    matrixString += matrix[i][j] + " ";
//                } else {
//                    matrixString += matrix[i][j] + "\n";
//                }
//            }
        }
        return matrixString;
    }

    /**
     * This method simply saves lines in the main. It creates a text file consisting of the string inputed to the method.
     *
     * @param input    String to be written to a text file.
     * @param fileName Name of the file you want to create.
     */
    private static void writeMatrixStringFile(String input, String fileName) {
        BufferedWriter writer = null;
        try {
            //create a temporary file

            File logFile = new File(fileName);

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(input);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
    }

}
