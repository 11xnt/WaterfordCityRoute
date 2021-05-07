package System;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.List;

public class Utils {


    static String[] historic = {"Reginald Tower", "Waterford Crystal", "Bull Post", "Double Tower","Clock Tower"
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

    public static int getCostOfPath(Node<Point> startNode, Node<Point> endNode) {

//        if(path.size() <= 1) return 0;
//
//        int cost = 0;
//
//        for(int i = 0; i < path.size() - 1; i++) {
//            Node<?> currentNode = path.get(i);
//            Node<?> nextNode = path.get(i + 1);
//
//            for(Link adjEdge : currentNode.getAdjList()){
//                if(adjEdge.getDestNode().equals(nextNode))
//                    cost += adjEdge.getCost();
//            }
//
//        }
//
//        return cost;
        int pathCost = (int)(java.lang.Math.sqrt(((endNode.getData().getX()-startNode.getData().getX())*(endNode.getData().getX() - startNode.getData().getX()))+
                ((endNode.getData().getY() - startNode.getData().getY()) * (endNode.getData().getY() - startNode.getData().getY()))));

        if(pathCost < 0) {
            pathCost = pathCost*-1;
        }
        return pathCost;

    }

}
