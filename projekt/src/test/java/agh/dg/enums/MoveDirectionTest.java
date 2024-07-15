package agh.dg.enums;

import agh.dg.models.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveDirectionTest {
    @Test
    public void changeDirectionTest() {
        assertEquals(MoveDirection.NORTH_EAST, MoveDirection.EAST.changeDirection(7));
        assertEquals(MoveDirection.SOUTH, MoveDirection.SOUTH.changeDirection(0));
        assertEquals(MoveDirection.NORTH_WEST, MoveDirection.SOUTH_WEST.changeDirection(2));
        assertEquals(MoveDirection.WEST, MoveDirection.EAST.changeDirection(4));
        assertEquals(MoveDirection.SOUTH_WEST, MoveDirection.NORTH.changeDirection(5));
        assertEquals(MoveDirection.EAST, MoveDirection.SOUTH.changeDirection(6));
        assertEquals(MoveDirection.NORTH, MoveDirection.NORTH_WEST.changeDirection(1));
        assertEquals(MoveDirection.SOUTH_EAST, MoveDirection.NORTH.changeDirection(3));
    }

    @Test
    public void singleMoveVectorTest() {
        assertEquals(new Vector2d(1, 1), MoveDirection.EAST.changeDirection(7).singleMoveVector());
        assertEquals(new Vector2d(0, -1), MoveDirection.SOUTH.changeDirection(0).singleMoveVector());
        assertEquals(new Vector2d(-1, 1), MoveDirection.SOUTH_WEST.changeDirection(2).singleMoveVector());
        assertEquals(new Vector2d(-1, 0), MoveDirection.EAST.changeDirection(4).singleMoveVector());
        assertEquals(new Vector2d(-1, -1), MoveDirection.NORTH.changeDirection(5).singleMoveVector());
        assertEquals(new Vector2d(1, 0), MoveDirection.SOUTH.changeDirection(6).singleMoveVector());
        assertEquals(new Vector2d(0, 1), MoveDirection.NORTH_WEST.changeDirection(1).singleMoveVector());
        assertEquals(new Vector2d(1, -1), MoveDirection.NORTH.changeDirection(3).singleMoveVector());
    }
}
