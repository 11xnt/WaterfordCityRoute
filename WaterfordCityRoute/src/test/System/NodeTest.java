package test.System;//package System;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class NodeTest {
//
//    Node <Integer> nodeA;
//    Node <Integer> nodeB;
//
//    @BeforeEach
//    void setUp() {
//        nodeA = new Node<>(100);
//        nodeB = new Node<>(200);
//    }
//    @Test
//    void constructorTest(){
//        assertEquals(100, nodeA.getData());
//
//    }
//
//    @Test
//    void correctSettingXCoordinate(){
//        nodeA.setX(100);
//        assertEquals(100, nodeA.getX());
//    }
//
//    @Test
//    void InCorrectSettingXCoordinate(){
//        nodeA.setX(-10);
//        assertEquals(0, nodeA.getX());
//    }
//
//    @Test
//    void correctSettingYCoordinate(){
//        nodeA.setY(100);
//        assertEquals(100, nodeA.getY());
//    }
//
//    @Test
//    void InCorrectSettingYCoordinate(){
//        nodeA.setY(-10);
//        assertEquals(0, nodeA.getY());
//    }
//
////    @Test
//////    void connectNodeDirectedTest() {
//////        nodeA.connectToNodeDirected(nodeB, 100);
//////
//////        assertEquals(nodeA.getAdjList().get(0).getDestinationNode(), nodeB);
//////    }
//////
//////    @Test
//////    void connectNodeUnDirectedTest() {
//////        nodeA.connectToNodeUndirected(nodeB, 100);
//////
//////        assertEquals(nodeB.getAdjList().get(0).getDestinationNode(), nodeA);
//////    }
//}