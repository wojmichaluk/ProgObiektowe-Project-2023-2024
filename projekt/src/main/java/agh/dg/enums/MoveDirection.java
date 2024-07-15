package agh.dg.enums;

import agh.dg.models.Vector2d;

public enum MoveDirection {
    NORTH(0),
    NORTH_EAST(1),
    EAST(2),
    SOUTH_EAST(3),
    SOUTH(4),
    SOUTH_WEST(5),
    WEST(6),
    NORTH_WEST(7);

    private final int directionValue;

    MoveDirection(int directionValue) {
        this.directionValue = directionValue;
    }

    public MoveDirection changeDirection(int genotype) {
        int newDirectionValue = (directionValue + genotype) % values().length;
                
        return switch (newDirectionValue) {
            case 0 -> NORTH;
            case 1 -> NORTH_EAST;
            case 2 -> EAST;
            case 3 -> SOUTH_EAST;
            case 4 -> SOUTH;
            case 5 -> SOUTH_WEST;
            case 6 -> WEST;
            case 7 -> NORTH_WEST;
            default -> throw new IllegalStateException();
        };
    }

    public Vector2d singleMoveVector(){
        return switch(this.directionValue){
            case 0 -> new Vector2d(0, 1);
            case 1 -> new Vector2d(1, 1);
            case 2 -> new Vector2d(1, 0);
            case 3 -> new Vector2d(1, -1);
            case 4 -> new Vector2d(0, -1);
            case 5 -> new Vector2d(-1, -1);
            case 6 -> new Vector2d(-1, 0);
            case 7 -> new Vector2d(-1, 1);
            default -> throw new IllegalStateException();
        };
    }
}
