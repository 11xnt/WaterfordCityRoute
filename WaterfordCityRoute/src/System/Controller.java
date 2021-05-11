package System;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
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
    public ImageView back, mapDisplay, startBimg, endBimg, searchAnimation, refreshGif;
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
    public TableView<Point> landmarksTable, landmarksAvoidTable;
    @FXML
    public TableColumn<Point, String> landmarkColumn, landmarkColumn1;
    @FXML
    TextField numOfRoutes;

    static int[] pixel;

    //Waterford Map
    Image cityMap = new Image("System//Waterford.png".toString(),944,580,false,true);
    Image nonChangedImage = cityMap;

    public int width;
    public int height;

    //background image
    //public Image background = new Image("https://res.cloudinary.com/dmepo58r1/image/upload/v1617879106/white-elegant-texture-background-theme_23-2148415644_iymdok.jpg", 1200, 752, false, true);
    Image startImg = new Image("System/iconfinder_gpsmapicons07_68010.png",48,48,false,true);
    Image endImg = new Image("System/finish-line-flag.png",48,48,false,true);
    Image searchAni = new Image("System/icons8-search.gif",48,48,false,true);
    Image refreshAni = new Image("System/refresh.gif",48,48,false,true);
    public Coordinate startCoord, endCoord;
    public Point startPoint, endPoint;
    Node<?> node;
    Link link;
    public Node<Point> foundStart, foundEnd;

    public ArrayList<Integer> imageAgendaArray;
    public ArrayList<Integer> imageLocationArray;

    public List<Node<Point>> landmarks = new ArrayList<>();
    public List<Node<Point>> historicLandmarks = new ArrayList<>();
    public List<Node<Point>> junctions = new ArrayList<>();
    public List<Node<Point>> allPoints = new ArrayList<>();
    public ArrayList<String> encountered = new ArrayList<>();
    public int[] imageArray;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //back.setImage(background);
        //nonChangedImage = cityMap;
        startBimg.setImage(startImg);
        searchAnimation.setImage(searchAni);
        refreshGif.setImage(refreshAni);
        endBimg.setImage(endImg);
        mapDisplay.setImage(cityMap);
        //pixel = new int[width * height];
        int width = (int)cityMap.getWidth();
        int height = (int)mapDisplay.getImage().getHeight();
        imageArray = new int[(int)mapDisplay.getImage().getWidth()*(int)mapDisplay.getImage().getHeight()];
        imageLocationArray = new ArrayList<>();
        //connectNodesWithLinks();
        try {
            createLandmarksObjects();
            createJunctionObjects();
        } catch (IOException e) {
            e.printStackTrace();
        }
        establishNodesOnMap();
        }

    public void startingLandmark(ActionEvent actionEvent) {
        imageLocationArray = new ArrayList<>();
        mapDisplay.setOnMousePressed(null);
        coordStartLocator();
    }

    public void destinationLandmark(ActionEvent actionEvent) {
        mapDisplay.setOnMousePressed(null);
        coordEndLocator();
    }

    public void runSearch(ActionEvent actionEvent) {
        if(bfsRadio.isSelected() && shortRadio.isSelected()) {
            bfsShortRoute();
        }
        else if(bfsRadio.isSelected() && shortRadio.isSelected()) {
            //bfsRoute();
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
        for (int i =0; i < landmarks.size();i++) {
            System.out.println(i + ": " + landmarks.get(i).getData().getType());
        }
        createHistoricNodesSublist();
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
                junctions.add(junction);
            } catch (Exception ignored) {
            }
        }
        for(int i =0;i<junctions.size();i++) {
            System.out.println(i + ": " + junctions.get(i).getData().getType());
        }
        choiceBoxLandmarks();
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
                    rec.setWidth(5);
                    rec.setHeight(5);
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
//                        connectJunctions();
//                        for(int j = 0 ; j < allPoints.size(); j++) {
//                            System.out.println(j + ": " + allPoints.get(j).getData().getType());
//                        }
                    }
                }
            if(allPoints.isEmpty()) {
                allPoints.addAll(landmarks);
                allPoints.addAll(historicLandmarks);
                allPoints.addAll(junctions);
                connectJunctions();
                connectAllJunctions();
            }
        } else {((AnchorPane) mapDisplay.getParent()).getChildren().removeIf(f->f instanceof Rectangle);}
    }

    public void loadTable() {
        landmarksTable.getItems().clear();
        landmarksAvoidTable.getItems().clear();
        landmarkColumn.setCellValueFactory(new PropertyValueFactory<Point, String>("type"));
        landmarkColumn1.setCellValueFactory(new PropertyValueFactory<Point, String>("type"));
        for (int i = 0; i < landmarks.size();i++) {
            landmarksTable.getItems().add(landmarks.get(i).getData());
            landmarksAvoidTable.getItems().add(landmarks.get(i).getData());
        }
        landmarksTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        landmarksAvoidTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
        ((AnchorPane)mapDisplay.getParent()).getChildren().removeIf(f->f instanceof Circle);
        ((AnchorPane)mapDisplay.getParent()).getChildren().removeIf(f->f instanceof Rectangle);
        imageLocationArray.clear();
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

    public Node<Point> matchingAllNode(Node<?> node) {
        for(Node<Point> foundLandmark : allPoints) {
            if (node.getData().equals(foundLandmark.getData())) {
                return foundLandmark;
            }
        } return null;
    }

    public Node<Point> matchingHistoricNode(Node<?> node) {

        for(Node<Point> foundLandmark : allPoints) {
            if (node.getData().equals(foundLandmark.getData())) {
                return foundLandmark;
            }
        } return null;
    }

    public void djiShortRoute() {
        ((AnchorPane) mapDisplay.getParent()).getChildren().removeIf(f -> f instanceof Line);
        Node<Point> startNode = null;
        Node<Point> endNode = null;
        Node<Point> waypoint = null;
        //connectAllHistoricNodes();
        startNode = matchingNode(startBox.getSelectionModel().getSelectedItem());
        endNode = matchingNode(destinationBox.getSelectionModel().getSelectedItem());
        //System.out.println(startNode + " " + endNode);
        //startNode.connectToNodeUndirected(endNode, Utils.getCostOfPath(startNode,endNode));
        for(int i = 0; i < landmarks.size();i++) {
            if (landmarksTable.getSelectionModel().isSelected(landmarks.indexOf(landmarks.get(i)))) {
                waypoint = matchingNode(landmarksTable.getSelectionModel().getSelectedItem().getType());
            }
        }
        for(int j = 0; j < landmarks.size();j++) {
            if (landmarksAvoidTable.getSelectionModel().isSelected(landmarks.indexOf(landmarks.get(j)))) {
                for(int k = 0; k < landmarks.get(j).getAdjList().size(); k++) {
                    //Node<?> tempLand = landmarks.get(j);
                    landmarks.get(j).getAdjList().remove(k);
                    //tempLand.getAdjList().get(tempLand.getAdjList().indexOf(tempDest)).setCost(Integer.MAX_VALUE);
                }

            }
        }
        if(waypoint != null) {
            CostedPath cpa = SearchLogic.findCheapestPathDijkstra(startNode, waypoint.getData());
            CostedPath cpa1 = SearchLogic.findCheapestPathDijkstra(waypoint, endNode.getData());
            for (Node<?> n : cpa.getPathList()) {
                System.out.println(n.getData());
            }
            drawPath(cpa.getPathList());
            drawPath(cpa1.getPathList());
            System.out.println("\nThe total path cost is: " + cpa.pathCost);
        } else {
            CostedPath cpa = SearchLogic.findCheapestPathDijkstra(startNode, endNode.getData());
            for (Node<?> n : cpa.getPathList()) {
                System.out.println(n.getData());
            }
            drawPath(cpa.getPathList());
            System.out.println("\nThe total path cost is: " + cpa.pathCost);
        }
    }

    // finds the historic route and displays it
    public void djiHistRoute() {
        ((AnchorPane) mapDisplay.getParent()).getChildren().removeIf(f -> f instanceof Line);
        encountered.clear();
        Node<Point> startNode;
        Node<Point> endNode;
        Node<Point> closestHistoric;
        Node<Point> after;
        Node<Point> waypoint = null;
        CostedPath cpa = null,cpa1,cpa2 = null;
        startNode = matchingNode(startBox.getSelectionModel().getSelectedItem());
        endNode = matchingNode(destinationBox.getSelectionModel().getSelectedItem());
        // at the start we connect all the junctions together
            // if they are already connected, then start finding the historic route
        for(int i = 0; i < landmarks.size();i++) {
            if (landmarksTable.getSelectionModel().isSelected(landmarks.indexOf(landmarks.get(i)))) {
                waypoint = matchingNode(landmarksTable.getSelectionModel().getSelectedItem().getType());
            }
        }
        for(int j = 0; j < landmarks.size();j++) {
            if (landmarksAvoidTable.getSelectionModel().isSelected(landmarks.indexOf(landmarks.get(j)))) {
                encountered.add(landmarks.get(j).getData().getType());
            }
        }
        if(waypoint != null) {
            cpa1 = SearchLogic.findCheapestPathDijkstra(startNode, waypoint.getData());
            drawHistoricPath(cpa1.getPathList());
            System.out.println("first");
            for (Node<?> n : cpa1.getPathList()){
                System.out.println(n.getData() + "->" + matchingAllNode(n).getData().getType());
            }
            //cpa.getPathList().add(cpa1.getPathList().);
            // adds the starting node to the encountered so it will not travel back to it again
            encountered.add(startNode.getData().getType());
            startNode = waypoint;
        } else {
            closestHistoric = findClosestHistoricLandmark(startNode);
            // creates the shortest path from that starting node to the closest historic node
            cpa1 = SearchLogic.findCheapestPathDijkstra(startNode, closestHistoric.getData());
            // draws the shortest path
            drawHistoricPath(cpa1.getPathList());
            System.out.println("first");
            for (Node<?> n : cpa1.getPathList()){
                System.out.println(n.getData() + "->" + matchingAllNode(n).getData().getType());
            }
            //cpa.getPathList().add(cpa1.getPathList().);
            // adds the starting node to the encountered so it will not travel back to it again
            encountered.add(startNode.getData().getType());
            startNode = closestHistoric;

        }

            // returns the closest historic landmark from the starting node once.
            //closestHistoric = findClosestHistoricLandmark(startNode);
            // creates the shortest path from that starting node to the closest historic node
            // draws the shortest path

            while (startNode != null) {
                // if the next historic node isn't null (end, continue on creating a path
                if (findClosestHistoricLandmark(startNode) != null) {
                    //cpa2 = null;
                    // returns the closest historic landmark
                    closestHistoric = findClosestHistoricLandmark(startNode);
                    // draws the shortest path
                    cpa2 = SearchLogic.findCheapestPathDijkstra(startNode, closestHistoric.getData());
                    drawHistoricPath(cpa2.getPathList());
                    System.out.println("Inbetween nodes");
                    for (Node<?> n : cpa2.getPathList()) {
                        System.out.println(n.getData() + "->" + matchingAllNode(n).getData().getType());
                    }
                    // adds the historic node to the encountered so it will not travel back to it again
                    //encountered.clear();
                    encountered.add(startNode.getData().getType());
                    // assign the after node as the historic node
                    startNode = closestHistoric;
                }
                if (findClosestHistoricLandmark(startNode) == null) {
                    // once all nodes have been reached, find the cheapest path from the node to the end/destination node
                    cpa = SearchLogic.findCheapestPathDijkstra(startNode, endNode.getData());
                    drawHistoricPath(cpa.getPathList());
                    System.out.println("last");
                    for (Node<?> n : cpa.getPathList()) {
                        System.out.println(n.getData() + "->" + matchingAllNode(n).getData().getType());
                    }
                    // after last path is drawn, break the loop
                    break;
                }
            }


    }

    public Node<Point> findClosestHistoricLandmark(Node<Point> startNode) {
        Node<Point> foundClosest = null;
        Node<Point> temp3 = null;
        // initially sets the temp to the max value
        int temp = Integer.MAX_VALUE;
        for(int i = 0; i < historicLandmarks.size(); i++) {
            int temp2 = Utils.getCostOfPath(startNode,historicLandmarks.get(i));
            // if temp2 is smaller than temp, then reassign the value
            if(temp2 < temp && !encountered.toString().contains(historicLandmarks.get(i).getData().getType()) && !startNode.getData().getType().equals(historicLandmarks.get(i).getData().getType())) {
                temp = temp2;
                temp3 = historicLandmarks.get(i);
                //encountered.clear();
            } else if(temp2 < temp && encountered.toString().contains(historicLandmarks.get(i).getData().getType()) && startNode.getData().getType().equals(historicLandmarks.get(i).getData().getType())) {
                temp = temp2;
                temp3 = historicLandmarks.get(i+1);
                //encountered.clear();
            }
        }
        foundClosest = temp3;
        return foundClosest;
    }

    public void drawHistoricPath(List<Node<?>> pathList) {
        for(int i = 0; i < pathList.size()-1; i++) {
            Line line1 = new Line();
            Node<Point> stPos = matchingHistoricNode(pathList.get(i));
            Node<Point> enPos = matchingHistoricNode(pathList.get(i+1));
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

    public void drawPath(List<Node<?>> pathList) {

        for(int i = 0; i < pathList.size()-1; i++) {
            Line line1 = new Line();
            Node<Point> stPos = matchingAllNode(pathList.get(i));
            Node<Point> enPos = matchingAllNode(pathList.get(i+1));
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
        junctions.get(2).connectToNodeUndirected(junctions.get(43), Utils.getCostOfPath(junctions.get(2), junctions.get(43)));
        junctions.get(18).connectToNodeUndirected(junctions.get(17), Utils.getCostOfPath(junctions.get(18), junctions.get(17)));
        //Connects P17 to Waterford Crystal (historic)

        junctions.get(17).connectToNodeUndirected(junctions.get(7), Utils.getCostOfPath(junctions.get(17), junctions.get(7)));
        junctions.get(7).connectToNodeUndirected(junctions.get(4), Utils.getCostOfPath(junctions.get(7), junctions.get(4)));
        junctions.get(4).connectToNodeUndirected(junctions.get(5), Utils.getCostOfPath(junctions.get(4), junctions.get(5)));
        //Connects P5,7,17 to Reginalds Tower

        // historic landmark 0 = Reginald tower P5,P7,P17
        junctions.get(5).connectToNodeUndirected(historicLandmarks.get(0), Utils.getCostOfPath(junctions.get(5), historicLandmarks.get(0)));
        junctions.get(7).connectToNodeUndirected(historicLandmarks.get(0), Utils.getCostOfPath(junctions.get(7), historicLandmarks.get(0)));
        junctions.get(17).connectToNodeUndirected(historicLandmarks.get(0), Utils.getCostOfPath(junctions.get(17), historicLandmarks.get(0)));
        junctions.get(3).connectToNodeUndirected(historicLandmarks.get(0), Utils.getCostOfPath(junctions.get(3), historicLandmarks.get(0)));
        junctions.get(5).connectToNodeUndirected(landmarks.get(1), Utils.getCostOfPath(junctions.get(5), landmarks.get(1)));
        junctions.get(7).connectToNodeUndirected(landmarks.get(1), Utils.getCostOfPath(junctions.get(7), landmarks.get(1)));
        junctions.get(17).connectToNodeUndirected(landmarks.get(1), Utils.getCostOfPath(junctions.get(17), landmarks.get(1)));
        junctions.get(3).connectToNodeUndirected(landmarks.get(1), Utils.getCostOfPath(junctions.get(3), landmarks.get(1)));
        // historic landmark 1 = Waterford Crystal P17,P18
        junctions.get(17).connectToNodeUndirected(historicLandmarks.get(1), Utils.getCostOfPath(junctions.get(17), historicLandmarks.get(1)));
        junctions.get(18).connectToNodeUndirected(historicLandmarks.get(1), Utils.getCostOfPath(junctions.get(18), historicLandmarks.get(1)));
        junctions.get(17).connectToNodeUndirected(landmarks.get(3), Utils.getCostOfPath(junctions.get(17), landmarks.get(3)));
        junctions.get(18).connectToNodeUndirected(landmarks.get(3), Utils.getCostOfPath(junctions.get(18), landmarks.get(3)));
        // historic landmark 2 = Bull Post P33,P30,P37
        junctions.get(30).connectToNodeUndirected(historicLandmarks.get(2), Utils.getCostOfPath(junctions.get(30), historicLandmarks.get(2)));
        junctions.get(33).connectToNodeUndirected(historicLandmarks.get(2), Utils.getCostOfPath(junctions.get(33), historicLandmarks.get(2)));
        junctions.get(37).connectToNodeUndirected(historicLandmarks.get(2), Utils.getCostOfPath(junctions.get(37), historicLandmarks.get(2)));
        junctions.get(30).connectToNodeUndirected(landmarks.get(6), Utils.getCostOfPath(junctions.get(30), landmarks.get(6)));
        junctions.get(33).connectToNodeUndirected(landmarks.get(6), Utils.getCostOfPath(junctions.get(33), landmarks.get(6)));
        junctions.get(37).connectToNodeUndirected(landmarks.get(6), Utils.getCostOfPath(junctions.get(37), landmarks.get(6)));
        // historic landmark 3 = Double Tower P21, WIT
        landmarks.get(5).connectToNodeUndirected(historicLandmarks.get(3), Utils.getCostOfPath(landmarks.get(5),historicLandmarks.get(3)));
        junctions.get(21).connectToNodeUndirected(historicLandmarks.get(3), Utils.getCostOfPath(junctions.get(21),historicLandmarks.get(3)));
        landmarks.get(5).connectToNodeUndirected(landmarks.get(7), Utils.getCostOfPath(landmarks.get(5),landmarks.get(7)));
        junctions.get(21).connectToNodeUndirected(landmarks.get(7), Utils.getCostOfPath(junctions.get(21),landmarks.get(7)));
        // historic landmark 4 = Clock Tower P10,P41
        //junctions.get(10).connectToNodeUndirected(historicLandmarks.get(4), Utils.getCostOfPath(junctions.get(10), historicLandmarks.get(4)));
        historicLandmarks.get(4).connectToNodeUndirected(junctions.get(41), Utils.getCostOfPath(historicLandmarks.get(4), junctions.get(41)));
        historicLandmarks.get(4).connectToNodeUndirected(junctions.get(10), Utils.getCostOfPath(historicLandmarks.get(4), junctions.get(10)));
        landmarks.get(8).connectToNodeUndirected(junctions.get(41), Utils.getCostOfPath(landmarks.get(8), junctions.get(41)));
        landmarks.get(8).connectToNodeUndirected(junctions.get(10), Utils.getCostOfPath(landmarks.get(8), junctions.get(10)));

        junctions.get(6).connectToNodeUndirected(junctions.get(8), Utils.getCostOfPath(junctions.get(6), junctions.get(8)));
        junctions.get(6).connectToNodeUndirected(junctions.get(41), Utils.getCostOfPath(junctions.get(6), junctions.get(41)));
        junctions.get(28).connectToNodeUndirected(junctions.get(6), Utils.getCostOfPath(junctions.get(28), junctions.get(6)));
        junctions.get(10).connectToNodeUndirected(junctions.get(11), Utils.getCostOfPath(junctions.get(10), junctions.get(11)));
        junctions.get(11).connectToNodeUndirected(junctions.get(12), Utils.getCostOfPath(junctions.get(11), junctions.get(12)));
        junctions.get(12).connectToNodeUndirected(junctions.get(13), Utils.getCostOfPath(junctions.get(12), junctions.get(13)));
        junctions.get(13).connectToNodeUndirected(junctions.get(14), Utils.getCostOfPath(junctions.get(13), junctions.get(14)));
        junctions.get(14).connectToNodeUndirected(junctions.get(34), Utils.getCostOfPath(junctions.get(14), junctions.get(34)));
        junctions.get(34).connectToNodeUndirected(junctions.get(35), Utils.getCostOfPath(junctions.get(34), junctions.get(35)));
        junctions.get(34).connectToNodeUndirected(junctions.get(32), Utils.getCostOfPath(junctions.get(34), junctions.get(32)));
        junctions.get(32).connectToNodeUndirected(junctions.get(16), Utils.getCostOfPath(junctions.get(32), junctions.get(16)));
        junctions.get(16).connectToNodeUndirected(junctions.get(31), Utils.getCostOfPath(junctions.get(16), junctions.get(31)));
        junctions.get(35).connectToNodeUndirected(junctions.get(36), Utils.getCostOfPath(junctions.get(35), junctions.get(36)));
        junctions.get(36).connectToNodeUndirected(junctions.get(37), Utils.getCostOfPath(junctions.get(36), junctions.get(37)));
        junctions.get(28).connectToNodeUndirected(junctions.get(29), Utils.getCostOfPath(junctions.get(28), junctions.get(29)));
        junctions.get(29).connectToNodeUndirected(junctions.get(30), Utils.getCostOfPath(junctions.get(29), junctions.get(30)));
        junctions.get(31).connectToNodeUndirected(junctions.get(30), Utils.getCostOfPath(junctions.get(31), junctions.get(30)));
        junctions.get(33).connectToNodeUndirected(junctions.get(38), Utils.getCostOfPath(junctions.get(33), junctions.get(38)));
        junctions.get(38).connectToNodeUndirected(junctions.get(39), Utils.getCostOfPath(junctions.get(38), junctions.get(39)));
        junctions.get(39).connectToNodeUndirected(junctions.get(23), Utils.getCostOfPath(junctions.get(39), junctions.get(23)));
        junctions.get(23).connectToNodeUndirected(junctions.get(24), Utils.getCostOfPath(junctions.get(23), junctions.get(24)));
        junctions.get(24).connectToNodeUndirected(junctions.get(40), Utils.getCostOfPath(junctions.get(24), junctions.get(40)));
        junctions.get(23).connectToNodeUndirected(junctions.get(25), Utils.getCostOfPath(junctions.get(23), junctions.get(25)));
        junctions.get(25).connectToNodeUndirected(junctions.get(26), Utils.getCostOfPath(junctions.get(25), junctions.get(26)));
        junctions.get(26).connectToNodeUndirected(junctions.get(27), Utils.getCostOfPath(junctions.get(26), junctions.get(27)));
        junctions.get(26).connectToNodeUndirected(junctions.get(29), Utils.getCostOfPath(junctions.get(26), junctions.get(29)));
        junctions.get(27).connectToNodeUndirected(junctions.get(28), Utils.getCostOfPath(junctions.get(27), junctions.get(28)));
        junctions.get(8).connectToNodeUndirected(junctions.get(18), Utils.getCostOfPath(junctions.get(8), junctions.get(18)));
        junctions.get(18).connectToNodeUndirected(junctions.get(19), Utils.getCostOfPath(junctions.get(18), junctions.get(19)));
        junctions.get(19).connectToNodeUndirected(junctions.get(20), Utils.getCostOfPath(junctions.get(19), junctions.get(20)));
        junctions.get(20).connectToNodeUndirected(junctions.get(21), Utils.getCostOfPath(junctions.get(20), junctions.get(21)));
        junctions.get(42).connectToNodeUndirected(junctions.get(4), Utils.getCostOfPath(junctions.get(42), junctions.get(4)));
        junctions.get(42).connectToNodeUndirected(junctions.get(8), Utils.getCostOfPath(junctions.get(42), junctions.get(8)));
        junctions.get(42).connectToNodeUndirected(junctions.get(41), Utils.getCostOfPath(junctions.get(42), junctions.get(41)));
        junctions.get(43).connectToNodeUndirected(junctions.get(18), Utils.getCostOfPath(junctions.get(43), junctions.get(18)));

       // 5=wit,4=applemarket,2=waterforddis
        landmarks.get(5).connectToNodeUndirected(junctions.get(40), Utils.getCostOfPath(landmarks.get(5),junctions.get(40)));

        //landmarks.get(7).connectToNodeUndirected(junctions.get(21), Utils.getCostOfPath(landmarks.get(7),junctions.get(21)));
        landmarks.get(4).connectToNodeUndirected(junctions.get(19), Utils.getCostOfPath(landmarks.get(4),junctions.get(19)));
        landmarks.get(4).connectToNodeUndirected(junctions.get(27), Utils.getCostOfPath(landmarks.get(4),junctions.get(27)));
        landmarks.get(4).connectToNodeUndirected(junctions.get(20), Utils.getCostOfPath(landmarks.get(4),junctions.get(20)));
        landmarks.get(2).connectToNodeUndirected(junctions.get(13), Utils.getCostOfPath(landmarks.get(2),junctions.get(13)));
    }

    public void connectAllJunctions() {
        for (int i = 0; i < allPoints.size(); i++) {
            System.out.println(i + ": " + allPoints.get(i).getData().getType());
        }
    }

    // separates the historic nodes from the normal landmarks
    public void createHistoricNodesSublist() {
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

    public void bfsRadio(ActionEvent event) {
        //imageLocationArray = null;

    }


    public void coordStartLocator() {
        mapDisplay.setOnMousePressed(e -> {
            startCoord = new Coordinate((int)e.getX(),(int)e.getY());
            //startLandmark(startCoord);
            if(startCoord.getX() != 0 || startCoord.getY() != 0) {
                if(imageArray[(startCoord.getY() * (int) mapDisplay.getImage().getWidth()) + startCoord.getX()] != -1) {
                    //imageArray[(startCoord.getY() * (int) mapDisplay.getImage().getWidth()) + startCoord.getX()] = 1;
                    //imageLocationArray.add(((startCoord.getY()*(int)mapDisplay.getImage().getWidth())+startCoord.getX()));
                    startLandmark(startCoord);
                }
            }
        });
    }

    public void coordEndLocator() {
        mapDisplay.setOnMousePressed(e -> {
            endCoord = new Coordinate((int)e.getX(),(int)e.getY());
            if(endCoord.getX() != 0 || endCoord.getY() != 0) {
                // gets the end array position
                if(imageArray[(endCoord.getY() * (int) mapDisplay.getImage().getWidth()) + endCoord.getX()] != -1) {
                   //imageArray[(endCoord.getY() * (int) mapDisplay.getImage().getWidth()) + endCoord.getX()] = 1;
                    imageLocationArray.add(((endCoord.getY() * (int) mapDisplay.getImage().getWidth()) + endCoord.getX()));
                    endLandmark(endCoord);
                }
            }
        });
    }


    public void startLandmark(Coordinate startCoord) {
        // ((AnchorPane)mapDisplay.getParent()).getChildren().removeAll(circle);
        //coordStartLocator();
        if(startCoord != null) {
            Circle circle = new Circle();
            //startPoint = new Point("start", startCoord.getX(), startCoord.getY());
            circle.setCenterX(startCoord.getX());
            circle.setCenterY(startCoord.getY());
            circle.setRadius(5);
            circle.setFill(Color.RED);
            circle.setLayoutX(mapDisplay.getLayoutX());
            circle.setLayoutY(mapDisplay.getLayoutY());
            ((AnchorPane) mapDisplay.getParent()).getChildren().removeIf(f -> f instanceof Circle);
            ((AnchorPane) mapDisplay.getParent()).getChildren().add(circle);
            circle.toFront();
        }
    }

    private void endLandmark(Coordinate endCoord) {
        //coordEndLocator();
        if(endCoord != null) {
            Rectangle rect = new Rectangle();
            //endPoint = new Point("end", endCoord.getX(), endCoord.getY());
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


    public void bfsShortRoute() {
        connectNodesWithLinks();
        //

    }

    // scans through all pixels on the map, if the pixel is not white then its not made a node.
    public void establishNodesOnMap() {
        PixelReader pixelReader = mapDisplay.getImage().getPixelReader();
        for (int i = 0; i < mapDisplay.getImage().getHeight(); i++) {
            for (int j = 0; j < mapDisplay.getImage().getWidth(); j++) {
                Color getColor = pixelReader.getColor(j, i);
                if (getColor.getBrightness() > .92 || getColor.equals(Color.WHITE)) {
                    // white pixels get the value of 0
                    imageArray[(i * (int) mapDisplay.getImage().getWidth()) + j] = 0;
                } else if(getColor.getBrightness()<.92 || !getColor.equals(Color.WHITE)) {
                    // rest get the value of -1
                    imageArray[(i * (int) mapDisplay.getImage().getWidth()) + j] = -1;
                }
            }
        }
    }


    // connects the stand-alone nodes with another if they are next to another.
    // array starts at the top left of the map and works its to the bottom right corner.
    public void connectNodesWithLinks() {
        //imageLocationArray = new ArrayList<>();
        imageAgendaArray = new ArrayList<>();
        imageLocationArray = new ArrayList<>();
        imageLocationArray.add(((startCoord.getY()*(int)mapDisplay.getImage().getWidth())+startCoord.getX()));
        imageLocationArray.add(((endCoord.getY()*(int)mapDisplay.getImage().getWidth())+endCoord.getX()));
        //process through the imageArray length
        int startPixel = imageLocationArray.get(0); // gets the position of the start node in the imageArray
        int endPixel = imageLocationArray.get(1);   // gets the position of the end node in the imageArray
        int currentPixel;
        // adds the starting pixel to the start of the BFS to begin the search
        imageAgendaArray.add(startPixel);

        // sets the starting pixel to a value of 1
        imageArray[imageLocationArray.get(0)] = 1;
        //Integer left, right= 0, up= 0, down= 0;
        //Integer left, right, up, down;
        while (imageLocationArray.get(0)-1 != imageLocationArray.get(1) || imageLocationArray.get(0)+1 != imageLocationArray.get(1) || imageLocationArray.get(0)+(int)mapDisplay.getImage().getWidth() != imageLocationArray.get(1) || imageLocationArray.get(0)-(int)mapDisplay.getImage().getWidth() != imageLocationArray.get(1)) {
            //Integer left = 0, right= 0, up= 0, down= 0;
            // gets the first pixel in queue (in the agenda) and searched all possible routes around that
            //TODO
            currentPixel = imageAgendaArray.get(0);
            // directions surrounding the pixel
//            if(currentPixel+1 < imageArray.length-1) right = imageArray[currentPixel+1];
//            if(currentPixel-1 < 0) left = imageArray[currentPixel-1];
//            if(currentPixel+(int)mapDisplay.getImage().getWidth() < imageArray.length-1) down = imageArray[currentPixel+(int)mapDisplay.getImage().getWidth()];
//            if(currentPixel-(int)mapDisplay.getImage().getWidth() >= 0) up = imageArray[currentPixel-(int)mapDisplay.getImage().getWidth()];
//            // removes the pixel being dealt with from the agenda array
            imageAgendaArray.remove(0);

            int currentCost = imageArray[currentPixel];

            // checks if all directions are possible
            // checks if right is possible
            if(imageArray[currentPixel+1] == 0 && currentPixel+1 < imageArray.length-1) {
               // if (imageArray[currentPixel + 1] < imageArray.length && imageArray[currentPixel + 1] == 0) {
                    //right = imageArray[currentPixel+1];
                imageAgendaArray.add(currentPixel+1);
                imageArray[currentPixel+1] = currentCost+1;

            }
            // checks if left is possible
            if(imageArray[currentPixel-1] == 0 && currentPixel-1 >= 0) {
               // if (imageArray[currentPixel - 1] < imageArray.length && imageArray[currentPixel - 1] == 0) {
                    //left = imageArray[currentPixel - 1];
                    imageAgendaArray.add(currentPixel-1);
                imageArray[currentPixel-1] = currentCost+1;

            }
            // checks if down is possible
            if(((currentPixel+(int)mapDisplay.getImage().getWidth()) < imageArray.length-1) && (imageArray[currentPixel+(int)mapDisplay.getImage().getWidth()] == 0)) {
                    //if (imageArray[currentPixel + (int) mapDisplay.getImage().getWidth()] < imageArray.length && imageArray[currentPixel + (int) mapDisplay.getImage().getWidth()] == 0) {
                    //down = imageArray[currentPixel + (int) mapDisplay.getImage().getWidth()];
                imageAgendaArray.add(currentPixel + (int) mapDisplay.getImage().getWidth());
                imageArray[currentPixel + (int) mapDisplay.getImage().getWidth()] = currentCost + 1;
            }

            // checks if up is possible
            if((currentPixel-(int)mapDisplay.getImage().getWidth()) >= 0 && (imageArray[currentPixel-(int)mapDisplay.getImage().getWidth()] == 0)) {
                //if (imageArray[currentPixel - (int) mapDisplay.getImage().getWidth()] < imageArray.length && imageArray[currentPixel - (int) mapDisplay.getImage().getWidth()] == 0) {
                    //up = imageArray[currentPixel - (int) mapDisplay.getImage().getWidth()];
                imageAgendaArray.add(currentPixel-(int)mapDisplay.getImage().getWidth());
                imageArray[currentPixel-(int)mapDisplay.getImage().getWidth()] = currentCost+1;

            }

            // checks if all directions have either white or not white pixels.
//
//                // if down is white set it to V+1 and add to imageAgenda
//                if (down == 0) {
//                    imageArray[down] = currentPixel+1;
//                    imageAgendaArray.add(imageArray[down]);
//                }
//                // if right is white set it to V+1 and add to imageAgenda
//                if (right == 0) {
//                    imageArray[right] = currentPixel+1;
//                    imageAgendaArray.add(imageArray[right]);
//                }
//                // if up is white set it to V+1 and add to imageAgenda
//                if (up == 0) {
//                    imageArray[up] = currentPixel+1;
//                    imageAgendaArray.add(imageArray[up]);
//                }
//                // if left is white set it to V+1 and add to imageAgenda
//                if (left == 0) {
//                    imageArray[left] = currentPixel+1;
//                    imageAgendaArray.add(imageArray[left]);
//                }
//            }
            if(currentPixel==endPixel) {
                buildPath(); // sends the indexes to build the path
                break;
            }

            if (imageAgendaArray.isEmpty()) {
                System.out.println("Completed");
            }
        }

        for(int i = 0; i<imageArray.length;i++) {
           if(imageArray[i] > 1) {
                System.out.println("Index: " + i + " = " + imageArray[i]);
            }
        }
        findBFSShort(endPixel);
    }

    public void buildPath() {
        //PixelReader pixelReader = mapDisplay.getImage().getPixelReader();
        WritableImage outputImage = new WritableImage((int)mapDisplay.getImage().getWidth(), (int) mapDisplay.getImage().getHeight());
        PixelWriter writer = outputImage.getPixelWriter();

        for (int i = 0; i < mapDisplay.getImage().getHeight(); i++) {
            for (int j = 0; j < mapDisplay.getImage().getWidth(); j++) {
                if (imageArray[i*(int)mapDisplay.getImage().getWidth() + j] >= 1) {
                    // white pixels get the value of 0
                    writer.setColor(j,i,Color.GREEN);
                } else {
                    writer.setColor(j,i, mapDisplay.getImage().getPixelReader().getColor(j,i));
                }
            }
        }
        mapDisplay.setImage(outputImage);
    }

    public void findBFSShort(int start) {
        PixelReader pixelReader = mapDisplay.getImage().getPixelReader();
        WritableImage outputImage = new WritableImage((int)mapDisplay.getImage().getWidth(), (int) mapDisplay.getImage().getHeight());

        PixelWriter writer = outputImage.getPixelWriter();

        mapDisplay.setImage(nonChangedImage);
        // add to path list
        ArrayList<Integer> shortestPath = new ArrayList<>();        // creates an arraylist to create the shortest path
        int currentPixel = start;                     // retrieves the end's value
        shortestPath.add(start);// adds the end to the start of the arraylist
        int currentCost = imageArray[currentPixel];
        int counter = 0;
        while(currentCost != 1) {
            //check left (-1)
            if(currentPixel-1 >= 0 && imageArray[currentPixel-1] == currentCost-1) {
                shortestPath.add(currentPixel-1);
                currentCost = currentCost-1;
                currentPixel = currentPixel-1;
                counter++;
            }
            //check right (+1)
            else if(currentPixel+1 < imageArray.length-1 && imageArray[currentPixel+1] == currentCost-1) {
                shortestPath.add(currentPixel+1);
                currentCost = currentCost-1;
                currentPixel = currentPixel+1;
                counter++;
            }
            //check up (- width)
            else if((currentPixel-(int)mapDisplay.getImage().getWidth()) >= 0 && imageArray[currentPixel-(int)mapDisplay.getImage().getWidth()] == currentCost-1) {
                shortestPath.add(currentPixel-(int)mapDisplay.getImage().getWidth());
                currentCost = currentCost-1;
                currentPixel = currentPixel-(int)mapDisplay.getImage().getWidth();
                counter++;
            }
            //check down (+ width)
            else if((currentPixel+(int)mapDisplay.getImage().getWidth()) < imageArray.length && imageArray[currentPixel+(int)mapDisplay.getImage().getWidth()] == currentCost-1) {
                shortestPath.add((currentPixel+(int)mapDisplay.getImage().getWidth()));
                currentCost = currentCost-1;
                currentPixel = currentPixel+(int)mapDisplay.getImage().getWidth();
                counter++;
            }

            if(counter == 0) {
                int x = shortestPath.get(counter) % (int) mapDisplay.getImage().getWidth();
                int y = shortestPath.get(counter) / (int) mapDisplay.getImage().getWidth();
                writer.setColor(x, y, Color.PURPLE);
            } else if (counter >= 1){
                int x = shortestPath.get(counter-1) % (int) mapDisplay.getImage().getWidth();
                int y = shortestPath.get(counter-1) / (int) mapDisplay.getImage().getWidth();
                writer.setColor(x, y, Color.PURPLE);
            }
        }
        mapDisplay.setImage(outputImage);
        for (int i = 0; i < mapDisplay.getImage().getHeight(); i++) {
            for (int j = 0; j < mapDisplay.getImage().getWidth(); j++) {
                if (!outputImage.getPixelReader().getColor(j,i).equals(Color.PURPLE)) {
                    // white pixels get the value of 0
                    writer.setColor(j,i, nonChangedImage.getPixelReader().getColor(j,i));
                }
            }
        }
        mapDisplay.setImage(outputImage);
    }

}
