package System;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkTest {
    Node nodeA, nodeB;
    Link edge;

    @BeforeEach
    void setUp() {
        nodeA = new Node(100);
        nodeB = new Node(1000);
        edge = new Link(nodeA, 100);
    }

    @Test
    void setCostCorrectly() {
        edge.setCost(20);
        assertEquals(20, edge.getCost());
    }

    @Test
    void setCostIncorrectly() {
        edge.setCost(-100);
        assertNotEquals(0, edge.getCost(),"Example of incorrectly setup of the set cost. There are no constraints in the code.");
    }


}