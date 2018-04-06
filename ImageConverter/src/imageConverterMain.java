import java.lang.NullPointerException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

import java.awt.image.RescaleOp;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class imageConverterMain {

    public static void main(String[] args) {
        float brightenFactor = 1.2f;

        try {
            File OG_POST_img = new File("OG_POST_DRACS_Camera_20141031_155932.jpg");
            File preimg = new File("PRE_DRACS_Camera_20141031_155932.jpg");
            BufferedImage in = null;
            in = ImageIO.read(imageConverterMain.class.getResource("/resources/PRE_DRACS_Camera_20141031_155932.jpg"));

            try {
                for (int i=0; i<in.getHeight(); i++){
                    for (int j=0; j<in.getWidth(); j++){
                        float r = new Color(in.getRGB(j, i)).getRed();
                        float g = new Color(in.getRGB(j, i)).getGreen();
                        float b = new Color(in.getRGB(j, i)).getBlue();
                        int grayScaled = (int)(r+g+b)/3;
                        in.setRGB(j, i, new Color(grayScaled, grayScaled, grayScaled).getRGB());
                    }
                }
                RescaleOp op = new RescaleOp(brightenFactor, 0, null);
                in = op.filter(in, in);
            } catch (NullPointerException e) {
                System.out.println("nullpointer");
            }

            try {
                System.out.println("this is the gray scale image");
                int[][] convertedMatrix = matrixConversion(in);
                System.out.println("completed conversion");
                writeMatrixStringFile(toMatrixString(convertedMatrix));
                File outputfile = new File("savedScaled.jpg");
                ImageIO.write(in, "jpg", outputfile);
            } catch (IOException e) {

            }

        } catch (IOException e) {
            System.out.println("IO EXCPETION");
            e.printStackTrace();
        }


    }

    private static int[][] matrixConversion(BufferedImage input) {
        int[][] grayScaleMatrix = new int[input.getHeight()][input.getWidth()];

        for (int i=0; i<input.getHeight(); i++){
            for (int j=0; j<input.getWidth(); j++){
                int grayScaleValue= new Color(input.getRGB(j, i)).getRed();
                grayScaleMatrix[i][j] = grayScaleValue;
            }
        }
        return grayScaleMatrix;
    }

    private static String toMatrixString(int[][] matrix) {
        String matrixString = "";
        for (int i=0; i < matrix.length; i++){
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

    private static void writeMatrixStringFile(String input) {
        BufferedWriter writer = null;
        try {
            //create a temporary file

            File logFile = new File("matrixFile.txt");

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
