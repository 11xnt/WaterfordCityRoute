package System;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.io.*;
import java.net.URL;

import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;


import java.util.*;


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
    @FXML
    ComboBox comboLandmark;
    @FXML
    public TableView<Point> landmarksTable;
    @FXML
    public TableColumn<Point, String> landmarkColumn;

    static int[] pixel;

    //Waterford Map
    Image cityMap = new Image("System//Waterford.png".toString(),944,580,false,true);
    static Image nonChangedImage;

    private int width = (int) cityMap.getWidth();
    private int height = (int) cityMap.getHeight();

    //background image
    public Image background = new Image("https://res.cloudinary.com/dmepo58r1/image/upload/v1617879106/white-elegant-texture-background-theme_23-2148415644_iymdok.jpg", 1200, 752, false, true);
    public Coordinate startCoord, endCoord;
    public Point startPoint, endPoint;
    Node<?> node;
    Link link;
    public Node<Point> foundStart, foundEnd;
    public Node<Point>[] imageArray = new Node[width*height];

    public List<Node<Point>> landmarks = new ArrayList<>();
    public List<Node<Point>> historicLandmarks = new ArrayList<>();
    public List<Node<Point>> junctions = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        back.setImage(background);
        //nonChangedImage = cityMap;
        mapDisplay.setImage(cityMap);
        pixel = new int[width * height];
        establishNodesOnMap();
        connectNodesWithLinks();
        try {
            createLandmarksObjects();
            createJunctionObjects();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        else if(bfsRadio.isSelected() && shortRadio.isSelected()) {
            bfsRoute();
        }
        else if(djiRadio.isSelected() && shortRadio.isSelected()) {
            djiShortRoute();
        }
        else if(djiRadio.isSelected() && hisRadio.isSelected()) {
            djiHistRoute();
        }
    }

    public void createLandmarksObjects() throws IOException {
        String path = "WaterfordCityRoute/src/System/Coords.csv";
        FileReader file = new FileReader(path);
        BufferedReader csvReader = new BufferedReader(file);
        String line = "";
        while ((line = csvReader.readLine()) !=null) {
            try {
                String[] values = line.split(",");
                Point temp = new Point(values[0],Integer.parseInt(values[1]),Integer.parseInt(values[2]));
                Node<Point> historicLandmark = new Node<>(temp);
                //System.out.println(historicLandmark.data.getType());
                landmarks.add(historicLandmark);
            } catch (Exception ignored) {
            }
        }
    }

    public void createJunctionObjects() throws IOException {
        String path = "WaterfordCityRoute/src/System/Junctions.csv";
        FileReader file = new FileReader(path);
        BufferedReader csvReader = new BufferedReader(file);
        String line = "";
        while ((line = csvReader.readLine()) !=null) {
            try {
                String[] values = line.split(",");
                Point temp = new Point(values[0],Integer.parseInt(values[1]),Integer.parseInt(values[2]));
                Node<Point> junction = new Node<>(temp);
                //System.out.println(junction.data.getType());
                junctions.add(junction);
            } catch (Exception ignored) {
            }
        }
        for(int i =0;i<junctions.size();i++) {
            System.out.println(i + ": " + junctions.get(i).getData().getType());
        }
        showJunctions();
    }
    public void showJunctions() throws IOException {
        if(!junctions.isEmpty()) {
                for (int i = 0; i < junctions.size(); i++) {
                    //System.out.println(landmarks[i].data.getType());
                    rec = new Rectangle(junctions.get(i).getData().getX(), junctions.get(i).getData().getY(), 5, 5);
                    rec.setFill(Color.BLUE);
                    //Hover over landmark
                    rec.setX(junctions.get(i).getData().getX() - 2.5);
                    rec.setY(junctions.get(i).getData().getY() - 2.5);
                    rec.setWidth(3);
                    rec.setHeight(3);
                    rec.setLayoutX(mapDisplay.getLayoutX());
                    rec.setLayoutY(mapDisplay.getLayoutY());
                    ((AnchorPane) mapDisplay.getParent()).getChildren().add(rec);
                    Tooltip.install(rec, new Tooltip(junctions.get(i).getData().getType()));
                    //tils.landmarks[i]
                } choiceBoxLandmarks();
            } else {((AnchorPane) mapDisplay.getParent()).getChildren().removeIf(f->f instanceof Rectangle);}
    }


    // Toggles Landmarks on and off
    public void showLandmarks() throws IOException {
        loadTable();
        if(show.isSelected()) {
            if(!landmarks.isEmpty()) {
                for (int i = 0; i < landmarks.size(); i++) {
                    //System.out.println(landmarks[i].data.getType());
                    rec = new Rectangle(landmarks.get(i).getData().getX(), landmarks.get(i).getData().getY(), 5, 5);
                    rec.setFill(Color.RED);
                    //Hover over landmark
                    rec.setX(landmarks.get(i).getData().getX() - 2.5);
                    rec.setY(landmarks.get(i).getData().getY() - 2.5);
                    rec.setWidth(10);
                    rec.setHeight(10);
                    rec.setLayoutX(mapDisplay.getLayoutX());
                    rec.setLayoutY(mapDisplay.getLayoutY());
                    ((AnchorPane) mapDisplay.getParent()).getChildren().add(rec);
                    Tooltip.install(rec, new Tooltip(landmarks.get(i).getData().getType()));
                    //tils.landmarks[i]
                }
            }
        } else {((AnchorPane) mapDisplay.getParent()).getChildren().removeIf(f->f instanceof Rectangle);}
    }

    //TODO: ADD CHECKBOXES TO THIS TABLE
    public void loadTable() {
        landmarksTable.getItems().clear();
        landmarkColumn.setCellValueFactory(new PropertyValueFactory<Point, String>("type"));
        for (int i = 0; i < landmarks.size()-1;i++) {
            landmarksTable.getItems().add(landmarks.get(i).getData());
        }
        landmarksTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void choiceBoxLandmarks() {
        Node<Point> landmark;
        startBox.getItems().removeAll(startBox.getItems());
        destinationBox.getItems().removeAll(destinationBox.getItems());
        for (int i = 0; i < landmarks.size(); i++) {
            startBox.getItems().add(i, landmarks.get(i).getData().getType());
            destinationBox.getItems().add(i, landmarks.get(i).getData().getType());
        }
    }

    public void updateComboBox(ActionEvent event) {
        comboLandmark.getItems().clear();
        Node<Point> landmark;
        int j = 0;
        if(startBox.getSelectionModel().getSelectedItem()!=null && destinationBox.getSelectionModel().getSelectedItem()!=null) {
            for (int i = 0; i < landmarks.size(); i++) {
                if (!startBox.getSelectionModel().getSelectedItem().equals(landmarks.get(i).getData().getType()) && !startBox.getSelectionModel().getSelectedItem().equals(landmarks.get(i).getData().getType())) {
                    comboLandmark.getItems().add(j, landmarks.get(i).data.getType());
                    j++;
                }
            }
        }
    }




    /**
     * FOR DJI ROUTING
     */

    public Node<Point> matchingNode(Object choiceBox) {
        for(Node<Point> foundLandmark : landmarks) {
            if (choiceBox.equals(foundLandmark.getData().getType())) {
                return foundLandmark;
            }
        } return null;
    }

    public Node<Point> matchingInNode(Node<?> node) {
        for(Node<Point> foundLandmark : landmarks) {
            if (node.getData().equals(foundLandmark.getData())) {
                return foundLandmark;
            }
        } return null;
    }

    public void djiShortRoute() {
        Node<Point> startNode = null;
        Node<Point> endNode = null;
        //connectAllHistoricNodes();
        startNode = matchingNode(startBox.getSelectionModel().getSelectedItem());
        endNode = matchingNode(destinationBox.getSelectionModel().getSelectedItem());
        //System.out.println(startNode + " " + endNode);
        //startNode.connectToNodeUndirected(endNode, Utils.getCostOfPath(startNode,endNode));
        CostedPath cpa = SearchLogic.findCheapestPathDijkstra(startNode, endNode.getData());
        assert cpa != null;
        for (Node<?> n : cpa.getPathList()){
            System.out.println(n.getData());
        }
        drawPath(cpa.getPathList());
        System.out.println("\nThe total path cost is: " + cpa.pathCost);
    }

    // finds the historic route and displays it
    public void djiHistRoute() {
        Node<Point> startNode = null;
        Node<Point> endNode = null;
        connectAllHistoricNodes();
        startNode = matchingNode(startBox.getSelectionModel().getSelectedItem());
        endNode = matchingNode(destinationBox.getSelectionModel().getSelectedItem());

        //CostedPath cpa = SearchLogic.findCheapestPathDijkstra(startNode, )

    }

    public void drawPath(List<Node<?>> pathList) {
        Node<Point> stPos = null;
        Node<Point> enPos = null;
        ((AnchorPane) mapDisplay.getParent()).getChildren().removeIf(f -> f instanceof Line);
        for(int i = 0; i < pathList.size()-1; i++) {
            Line line1 = new Line();
            assert (matchingInNode(pathList.get(i))) != null;
            stPos = matchingInNode(pathList.get(i));
            assert (matchingInNode(pathList.get(i+1))) != null;
            enPos = matchingInNode(pathList.get(i+1));
            line1.setStartX(stPos.getData().getX());
            line1.setStartY(stPos.getData().getY());
            line1.setEndX(enPos.getData().getX());
            line1.setEndY(enPos.getData().getY());
            line1.setLayoutX(mapDisplay.getLayoutX());
            line1.setLayoutY(mapDisplay.getLayoutY());
            ((AnchorPane) mapDisplay.getParent()).getChildren().add(line1);
            line1.toBack();
            mapDisplay.toBack();
            back.toBack();
        }
    }

    public void connectJunctions() {
        //Connects peoples park to P1
        landmarks.get(0).connectToNodeUndirected(junctions.get(0),Utils.getCostOfPath(landmarks.get(0), junctions.get(0)));
        junctions.get(0).connectToNodeUndirected(junctions.get(1), Utils.getCostOfPath(junctions.get(0), junctions.get(1)));
        junctions.get(0).connectToNodeUndirected(junctions.get(3), Utils.getCostOfPath(junctions.get(0), junctions.get(3)));
        junctions.get(1).connectToNodeUndirected(junctions.get(2), Utils.getCostOfPath(junctions.get(1), junctions.get(2)));
        junctions.get(2).connectToNodeUndirected(junctions.get(18), Utils.getCostOfPath(junctions.get(2), junctions.get(18)));
        junctions.get(18).connectToNodeUndirected(junctions.get(17), Utils.getCostOfPath(junctions.get(18), junctions.get(17)));
        //Connects P17 to Waterford Crystal (historic)
        junctions.get(17).connectToNodeUndirected(historicLandmarks.get(1), Utils.getCostOfPath(junctions.get(17), historicLandmarks.get(1)));
        junctions.get(17).connectToNodeUndirected(junctions.get(7), Utils.getCostOfPath(junctions.get(17), junctions.get(7)));
        junctions.get(7).connectToNodeUndirected(junctions.get(4), Utils.getCostOfPath(junctions.get(7), junctions.get(4)));
        junctions.get(4).connectToNodeUndirected(junctions.get(5), Utils.getCostOfPath(junctions.get(4), junctions.get(5)));
        //Connects P5,7,17 to Reginalds Tower
        junctions.get(5).connectToNodeUndirected(historicLandmarks.get(0), Utils.getCostOfPath(junctions.get(5), historicLandmarks.get(0)));
        junctions.get(7).connectToNodeUndirected(historicLandmarks.get(0), Utils.getCostOfPath(junctions.get(7), historicLandmarks.get(0)));
        junctions.get(17).connectToNodeUndirected(historicLandmarks.get(0), Utils.getCostOfPath(junctions.get(17), historicLandmarks.get(0)));
        junctions.get(3).connectToNodeUndirected(historicLandmarks.get(0), Utils.getCostOfPath(junctions.get(3), historicLandmarks.get(0)));

    }

    // separates the historic nodes from the normal landmarks
    public void connectAllHistoricNodes() {
        historicLandmarks.removeAll(landmarks);
        for(int i = 0; i <= landmarks.size()-1; i++) {
            if (Arrays.stream(Utils.historic).anyMatch(landmarks.get(i).getData().getType()::contains)) {
                historicLandmarks.add(landmarks.get(i));
            }
        }
        for(int j = 0; j < historicLandmarks.size();j++) {
            System.out.println(j + ": " + historicLandmarks.get(j).getData().getType());
        }
    }

//    public void connectAllNodes() {
//        for(int i=0; i< landmarks.size()-1;i++) {
//            assert landmarks.get(i+1)!=null;
//            landmarks.get(i).connectToNodeUndirected(landmarks.get(i+1), Utils.getCostOfPath(landmarks.get(i), landmarks.get(i+1)));
//            //System.out.println(landmarks[i] + " " + landmarks[i+1]);
//        }
//    }












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
