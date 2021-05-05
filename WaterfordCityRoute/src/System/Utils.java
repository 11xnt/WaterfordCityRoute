package System;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Utils {

/*    Node<String>[] landmarks1(){
        Node<String> peoples = new Node ("Peoples Park")
    }*/

    static String[] landmarks = {"Peoples Park", "Reginald Tower", "Waterford Distillery", "Waterford Crystal", "Apple Market", "The Wyse Park",
    "Double Tower", "Waterford HSE", "WIT", "Church of Sacred Heart", "Abbey Road Gardens", "Ferry Bank Park"
    };

    public static Image imageToBW(Image image, double threshold) {

        WritableImage writableImage = new WritableImage(
                image.getPixelReader(),
                (int) image.getWidth(),
                (int) image.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        PixelReader pixelReader = writableImage.getPixelReader();
        for (int i = 0; i < writableImage.getWidth(); i++) {
            for (int j = 0; j < writableImage.getHeight(); j++) {
                Color c = pixelReader.getColor(i, j);
                double colourSum = c.getRed() + c.getBlue() + c.getGreen();
                if (colourSum > threshold) {
                    pixelWriter.setColor(i, j, Color.WHITE);
                } else {
                    pixelWriter.setColor(i, j, Color.BLACK);
                }
            }
        }
        return writableImage;
    }

//    public static Node<Color> aquireCoordFromMousePosition(Image image, int x, int y, Node<Color>[] nodes) {
//
//        // gets the node that should have that x and y
//        return nodes[(int) ((y * image.getWidth()) + x)];
//    }

}
