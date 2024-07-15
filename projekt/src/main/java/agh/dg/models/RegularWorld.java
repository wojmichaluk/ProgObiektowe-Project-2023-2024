package agh.dg.models;

import agh.dg.models.abstracts.World;

public class RegularWorld extends World {
    public RegularWorld(int width, int height){
        super(width, height);

        int lowerJungleBoundary = 2 * height / 5;
        int upperJungleBoundary = 3 * height / 5;

        for (int i = 0; i < width; i++){
            for (int j = 0; j < lowerJungleBoundary; j++){
                lessPreferredPositions.add(new Vector2d(i, j));
            }

            for (int j = lowerJungleBoundary; j < upperJungleBoundary; j++){
                preferredPositions.add(new Vector2d(i, j));
            }

            for (int j = upperJungleBoundary; j < height; j++){
                lessPreferredPositions.add(new Vector2d(i, j));
            }
        }
    }

    @Override
    protected void removeEatenPlant(Plant eatenPlant){
        super.removeEatenPlant(eatenPlant);
        Vector2d availablePosition = eatenPlant.getPosition();

        if (this.belongsToJungle(availablePosition)){
            preferredPositions.add(availablePosition);
        }else{
            lessPreferredPositions.add(availablePosition);
        }
    }

    private boolean belongsToJungle(Vector2d position) {
        int height = boundary.topRight().getY();
        int lowerJungleBoundary = 2 * height / 5;
        int upperJungleBoundary = 3 * height / 5;

        return lowerJungleBoundary <= position.getY() && position.getY() < upperJungleBoundary;
    }
}
