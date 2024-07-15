package agh.dg.models;

import agh.dg.models.abstracts.World;

import java.util.*;

public class CreepingJungle extends World {
    public CreepingJungle (int width, int height){
        super(width, height);

        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                lessPreferredPositions.add(new Vector2d(i, j));
            }
        }
    }

    @Override
    protected void removeEatenPlant(Plant eatenPlant){
        super.removeEatenPlant(eatenPlant);
        Vector2d availablePosition = eatenPlant.getPosition();

        List<Vector2d> neighbours = this.neighbouringPositions(availablePosition);
        if (!neighbours.stream().filter(plants::containsKey).toList().isEmpty()) {
            preferredPositions.add(availablePosition);
        }else{
            lessPreferredPositions.add(availablePosition);
        }

        List<Vector2d> nonPlantNeighbours = neighbours.stream()
                .filter(neighbour -> !plants.containsKey(neighbour))
                .filter(this::inBounds)
                .toList();

        for (Vector2d neighbour : nonPlantNeighbours) {
            List<Vector2d> potentialPlantPositions = this.neighbouringPositions(neighbour);

            if (potentialPlantPositions.stream().filter(plants::containsKey).toList().isEmpty()) {
                preferredPositions.remove(neighbour);
                lessPreferredPositions.add(neighbour);
            }
        }
    }

    private List<Vector2d> neighbouringPositions(Vector2d position) {
        return List.of(new Vector2d(position.getX() - 1, position.getY()),
                new Vector2d(position.getX() - 1, position.getY() + 1),
                new Vector2d(position.getX(), position.getY() + 1),
                new Vector2d(position.getX() + 1, position. getY() + 1),
                new Vector2d(position.getX() + 1, position.getY()),
                new Vector2d(position.getX() + 1, position.getY() - 1),
                new Vector2d(position.getX(), position.getY() - 1),
                new Vector2d(position.getX() - 1, position.getY() - 1));
    }

    private boolean inBounds(Vector2d neighbour) {
        int upperRightX = this.boundary.topRight().getX();
        int upperRightY = this.boundary.topRight().getY();

        return neighbour.getX() >= 0 && neighbour.getX() < upperRightX
                && neighbour.getY() >= 0 && neighbour.getY() < upperRightY;
    }

    @Override
    public void placeNewPlants(int numberOfPlants){
        List<Vector2d> newPlantPositions = this.findNewPlantPositions(numberOfPlants);

        for (Vector2d plantPosition : newPlantPositions){
            for (Vector2d neighbour : this.neighbouringPositions(plantPosition)) {
                if (!plants.containsKey(neighbour) && this.inBounds(neighbour)) {
                    preferredPositions.add(neighbour);
                    lessPreferredPositions.remove(neighbour);
                }
            }
        }
    }
}
