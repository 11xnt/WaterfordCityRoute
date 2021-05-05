package System;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.io.*;
import java.net.URL;
import javafx.scene.shape.Rectangle;


import java.util.Arrays;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    public ImageView back, mapDisplay;
    @FXML
    public Button startBut;
    @FXML
    Pane pane;
    @FXML
    ChoiceBox startBox, destinationBox;
    @FXML
    CheckBox show;
    @FXML
    Rectangle rec;
    @FXML
    RadioButton bfsRadio, djiRadio, hisRadio, shortRadio;


    static int[] pixel;

    //Waterford Map
    Image cityMap = new Image("System//Waterford.png".toString(),944,580,false,true);
    static Image nonChangedImage;

    private int width = (int) cityMap.getWidth();
    private int height = (int) cityMap.getHeight();

    //background image
    public Image background = new Image("https://res.cloudinary.com/dmepo58r1/image/upload/v1617879106/white-elegant-texture-background-theme_23-2148415644_iymdok.jpg", 1200, 752, false, true);
    public Node<Point>[] landmarks = new Node[8];
    public Coordinate startCoord, endCoord;
    public Point startPoint, endPoint;
    Node<?> node;
    Link link;
    public Node<Point> foundStart, foundEnd;
    public Node<Point>[] imageArray = new Node[width*height];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        back.setImage(background);
        //nonChangedImage = cityMap;
        mapDisplay.setImage(cityMap);
        pixel = new int[width * height];
        establishNodesOnMap();
        connectNodesWithLinks();
    }

    public void startingLandmark(ActionEvent actionEvent) {
        coordStartLocator();
    }

    public void destinationLandmark(ActionEvent actionEvent) {
        mapDisplay.setOnMousePressed(null);
        coordEndLocator();
    }

    public void runSearch(ActionEvent actionEvent) {
        if(bfsRadio.isSelected() && shortRadio.isSelected()) {
            bfsRoute();
        }
        else if(djiRadio.isSelected() && shortRadio.isSelected()) {
            djiRoute(foundStart, foundEnd);
        }
    }

    public void createHistoricObjects() throws IOException {
        String path = "src/System/Coords.csv";
        FileReader file = new FileReader(path);
        BufferedReader csvReader = new BufferedReader(file);
        String line = "";
        while ((line = csvReader.readLine()) !=null) {
            try {
                String[] values = line.split(",");
                Point temp = new Point(values[0],Integer.parseInt(values[1]),Integer.parseInt(values[2]));
                Node<Point> historicLandmark = new Node<>(temp);
                System.out.println(historicLandmark.data.getType());
                addNodesToLandmarksList(historicLandmark);
            } catch (Exception ignored) {
            }
        }
    }

    // this is really scuffed but idk it works
    public void addNodesToLandmarksList(Node<Point> historicLandmark) {
        int i=0;
        while(landmarks[i]!=null) {
            i++;
            if(i == landmarks.length-1) {
                break;
            }
        }
        if(landmarks[i]==null) {
            landmarks[i]=historicLandmark;
        }
    }

    // Toggles Landmarks on and off
    public void showLandmarks() throws IOException {
        if(show.isSelected()) {
            createHistoricObjects();
            if(landmarks != null) {
                for (int i = 0; i < landmarks.length; i++) {
                    System.out.println(landmarks[i].data.getType());
                    rec = new Rectangle(landmarks[i].data.getX(), landmarks[i].data.getY(), 5, 5);
                    rec.setFill(Color.RED);
                    //Hover over landmark
                    rec.setX(landmarks[i].data.getX() - 2.5);
                    rec.setY(landmarks[i].data.getY() - 2.5);
                    rec.setWidth(10);
                    rec.setHeight(10);
                    rec.setLayoutX(mapDisplay.getLayoutX());
                    rec.setLayoutY(mapDisplay.getLayoutY());
                    ((AnchorPane) mapDisplay.getParent()).getChildren().add(rec);
                    String empty = "";
                    Tooltip tt = new Tooltip();
                    Tooltip.install(rec, new Tooltip(empty+(landmarks[i].data)));
                    tt.setText(empty+(landmarks[i].data));

                }
            }
        } else ((AnchorPane) mapDisplay.getParent()).getChildren().removeIf(f->f instanceof Rectangle);
    }


    /**
     * FOR DJI ROUTING
     * @param foundStart
     * @param foundEnd
     */

    public void djiRoute(Node<Point> foundStart, Node<Point> foundEnd) {
//        Node2<Point> a = new Node2<>(startPoint);
//        Node2<Point> b = new Node2<>(endPoint);
//        a.connectToNodeUndirected(b,  (int)(java.lang.Math.sqrt(((endPoint.x-startPoint.x)*(endPoint.x-startPoint.x))+((endPoint.y-startPoint.y)*(endPoint.y-startPoint.y)))));
//       // CostedPath cpa = SearchLogic.findCheapestPathDijkstra(foundStart, foundEnd.data);
//        for(Node<?> n : cpa.pathList)
//            System.out.println(n.data);
//        System.out.println("\nThe total path cost is: "+cpa.pathCost);
//        Line line1 = new Line();
//        line1.setStartX(startPoint.x);
//        line1.setStartY(startPoint.y);
//        line1.setEndX(endPoint.x);
//        line1.setEndY(endPoint.y+2.5);
//        line1.setFill(Color.CORAL);
//        line1.setLayoutX(mapDisplay.getLayoutX());
//        line1.setLayoutY(mapDisplay.getLayoutY());
//        ((AnchorPane) mapDisplay.getParent()).getChildren().removeIf(f -> f instanceof Line);
//        ((AnchorPane) mapDisplay.getParent()).getChildren().add(line1);
//        line1.toBack();
//        mapDisplay.toBack();
//        back.toBack();
    }


    /***********
     * for BFS *
     ***********/

    public void coordStartLocator() {
        mapDisplay.setOnMousePressed(e -> {
            startCoord = new Coordinate((int)e.getX(),(int)e.getY());
        });

        if(startCoord!=null) {
            Node<Point> temp = searchForStartingMatchingPoint(imageArray);
            startLandmark(temp);
        }
    }

    public void coordEndLocator() {
        mapDisplay.setOnMousePressed(e -> {
            endCoord = new Coordinate((int)e.getX(),(int)e.getY());
        });
        if(endCoord!=null) {
            Node<Point> temp = searchForEndingMatchingPoint(imageArray);
            endLandmark(temp);
        }
    }


    public void startLandmark(Node<Point> foundStart) {
        // ((AnchorPane)mapDisplay.getParent()).getChildren().removeAll(circle);
        //coordStartLocator();
        Circle circle = new Circle();
        //startPoint = new Point("start", startCoord.getX(), startCoord.getY());
        circle.setCenterX(foundStart.data.getX());
        circle.setCenterY(foundStart.data.getY());
        circle.setRadius(5);
        circle.setFill(Color.RED);
        circle.setLayoutX(mapDisplay.getLayoutX());
        circle.setLayoutY(mapDisplay.getLayoutY());
        ((AnchorPane) mapDisplay.getParent()).getChildren().removeIf(f -> f instanceof Circle);
        ((AnchorPane) mapDisplay.getParent()).getChildren().add(circle);

    }

    private void endLandmark(Node<Point> foundEnd) {
        //coordEndLocator();
        if(endCoord != null) {
            Rectangle rect = new Rectangle();
            endPoint = new Point("end", endCoord.getX(), endCoord.getY());
            rect.setX(endCoord.getX() - 2.5);
            rect.setY(endCoord.getY() - 2.5);
            rect.setWidth(10);
            rect.setHeight(10);
            rect.setFill(Color.GREEN);
            rect.setLayoutX(mapDisplay.getLayoutX());
            rect.setLayoutY(mapDisplay.getLayoutY());
            ((AnchorPane) mapDisplay.getParent()).getChildren().removeIf(f -> f instanceof Rectangle);
            ((AnchorPane) mapDisplay.getParent()).getChildren().add(rect);
        }
    }


    // scans through all pixels on the map, if the pixel is not white then its not made a node.
    public void establishNodesOnMap() {
        PixelReader pixelReader = mapDisplay.getImage().getPixelReader();
        for (int i = 0; i < mapDisplay.getImage().getHeight(); i++) {
            for (int j = 0; j < mapDisplay.getImage().getWidth(); j++) {
                Color getColor = pixelReader.getColor(j, i);
                if (getColor.equals(Color.WHITE)) {
                    Point pixelPoint = new Point("white pixel:" + j + "x" + i, j, i);
                    Node<Point> pixelNode = new Node<>(pixelPoint);
                    imageArray[(i * (int) mapDisplay.getImage().getWidth()) + j] = pixelNode;
                } else {
                    imageArray[(i * (int) mapDisplay.getImage().getWidth()) + j]=null;
                }
            }
        }
    }



    public void bfsRoute() {

    }

    // connects the stand-alone nodes with another if they are next to another.
    // array starts at the top left of the map and works its to the bottom right corner.
    public void connectNodesWithLinks() {
        //process through the imageArray length
        for(int i = 0; i < imageArray.length; i++) {
            int right;
            int down;

            if((i+1) < imageArray.length) {
                right = i + 1;

            } else right = imageArray.length-1;

            if((i + (int) mapDisplay.getImage().getWidth()) < imageArray.length) {
                down = i + (int) mapDisplay.getImage().getWidth();
            } else down = imageArray.length-1;

            if(imageArray[i] != null) {
                if(down < imageArray.length && imageArray[down] != null) {
                    imageArray[i].connectToNodeUndirected(imageArray[down],1);
                }
                if(right < imageArray.length && imageArray[right] != null) {
                    imageArray[i].connectToNodeUndirected(imageArray[right],1);
                }
            }

        }
    }

    public Node<Point> searchForStartingMatchingPoint(Node<Point>[] imageArray) {
        for(int i = 0; i < imageArray.length; i++) {
            if (imageArray[i].data.getX() == startCoord.getX()) {
                if (imageArray[i].data.getY() == startCoord.getY()) {
                    foundStart = imageArray[i];
                }
            }
        } return foundStart;
    }

    public Node<Point> searchForEndingMatchingPoint(Node<Point>[] imageArray) {
        for(int i = 0; i < imageArray.length; i++) {
            if (imageArray[i].data.getX() == endCoord.getX()) {
                if (imageArray[i].data.getY() == endCoord.getY()) {
                    foundEnd = imageArray[i];
                }
            }
        } return foundEnd;
    }



}
