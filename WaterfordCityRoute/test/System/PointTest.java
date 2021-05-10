package System;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    Point goodPoint;

    @BeforeEach
    void setUp() {
    goodPoint = new Point("landmark",22,44);
    }

    @Test
    void getType() {
    assertEquals("landmark", goodPoint.getType());
    }

    @Test
    void setType() {
        goodPoint.setType("historic");
        assertEquals("historic",goodPoint.getType());
    }

    @Test
    void getX() {
        assertEquals(22, goodPoint.getX());
    }

    @Test
    void setX() {
        goodPoint.setX(30);
        assertEquals(30, goodPoint.getX());
    }

    @Test
    void getY() {
        assertEquals(44, goodPoint.getY());
    }

    @Test
    void setY() {
        goodPoint.setY(100);
        assertEquals(100, goodPoint.getY());
    }
}