package System;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
        List<Node<Point>> landmarks = new ArrayList<>();
        List<Node<Point>> historicLandmarks = new ArrayList<>();
        List<Node<Point>> junctions = new ArrayList<>();
        Controller c;
    @BeforeEach
    void setUp() {
             c = new Controller();
    }



    @Test
    void createLandmarksObjects() {
        assertEquals(c.landmarks.size(),0);
        try{c.createLandmarksObjects();}
        catch (Exception e){
        }
        assertEquals(c.landmarks.size(), 0);
    }

    @Test
    void createJunctionObjects() {
        assertEquals(c.junctions.size(),0);
        try{c.createJunctionObjects();}
        catch (Exception e){
        }
        assertEquals(c.junctions.size(), 0);
    }


    @Test
    void connectJunctions() {
    }

    @Test
    void establishNodesOnMap() {
    }

    @Test
    void connectNodesWithLinks() {
    }

    @Test
    void searchForStartingMatchingPoint() {
    }

    @Test
    void searchForEndingMatchingPoint() {
    }
}