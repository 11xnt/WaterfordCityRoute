package System;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    Node<Integer> nodeA;
    Node<Integer> nodeB;

    @BeforeEach
    void setUp() {

        nodeA = new Node<>(100);
        nodeB = new Node<>(200);
    }

    @Test
    void connectToNodeDirected() {

    }

    @Test
    void connectNodeDirectedTest() {
        nodeA.connectToNodeDirected(nodeB, 100);

        assertEquals(nodeA.getAdjList().get(0).getDestNode(), nodeB);
    }

    @Test
    void getData() {
        assertEquals(100, nodeA.getData());
    }

    @Test
    void setData() {
        nodeA.setData(300);
        assertEquals(300, nodeA.getData());
    }

    @Test
    void getNodeValue() {
        assertEquals(2147483647,nodeA.getNodeValue());
    }

    @Test
    void setNodeValue() {
        nodeA.setNodeValue(2344333);
        assertEquals(2344333, nodeA.getNodeValue());
    }

    @Test
    void getAdjList() {
        assertEquals(nodeB.getAdjList(),nodeA.getAdjList());
    }
}