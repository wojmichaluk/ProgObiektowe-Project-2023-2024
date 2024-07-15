package agh.dg.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Vector2dTest {
    @Test
    public void testMovedBy() {
        Vector2d testVector = new Vector2d(3, 3);

        assertEquals(new Vector2d(2, 4), testVector.movedBy(new Vector2d(-1, 1)));
        assertEquals(new Vector2d(5, 1), testVector.movedBy(new Vector2d(2, -2)));
        assertEquals(new Vector2d(3, 3), testVector.movedBy(new Vector2d(0, 0)));

        assertNotEquals(new Vector2d(0, 0), testVector.movedBy(new Vector2d(-2, -3)));
        assertNotEquals(new Vector2d(2, 6), testVector.movedBy(new Vector2d(-2, 2)));
    }

    @Test
    public void testToString() {
        Vector2d testVector1 = new Vector2d(2, 2);
        Vector2d testVector2 = new Vector2d(4, 3);
        Vector2d testVector3 = new Vector2d(-1, 6);

        assertEquals("(2,2)", testVector1.toString());
        assertEquals("(4,3)", testVector2.toString());
        assertEquals("(-1,6)", testVector3.toString());
    }
}
