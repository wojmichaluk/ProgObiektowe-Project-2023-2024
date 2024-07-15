package agh.dg;

import java.util.ArrayList;
import java.util.List;

public class Statistics {
    private int animalCount = 0;

    private int plantsCount = 0;

    private int freeFieldsCount = 0;

    private List<List<Integer>> mostPopularGenotypes = new ArrayList<>();

    private double averageAnimalEnergy = 0.0;

    private double lifeExpectancy = 0.0;

    private double averageChildCount = 0.0;

    private int dayCount = 0;

    public void setStatisticParameters(int animalCount, int plantsCount, int freeFieldsCount,
                                       List<List<Integer>> mostPopularGenotype, double averageAnimalEnergy,
                                       double lifeExpectancy, double averageChildCount, int dayCount) {
        this.animalCount = animalCount;
        this.plantsCount = plantsCount;
        this.freeFieldsCount = freeFieldsCount;
        this.mostPopularGenotypes = mostPopularGenotype;
        this.averageAnimalEnergy = averageAnimalEnergy;
        this.lifeExpectancy = lifeExpectancy;
        this.averageChildCount = averageChildCount;
        this.dayCount = dayCount;
    }

    public int getAnimalCount() {
        return animalCount;
    }

    public int getPlantsCount() {
        return plantsCount;
    }

    public int getFreeFieldsCount() {
        return freeFieldsCount;
    }

    public List<List<Integer>> getMostPopularGenotypes() {
        return mostPopularGenotypes;
    }

    public double getAverageAnimalEnergy() {
        return averageAnimalEnergy;
    }

    public double getLifeExpectancy() {
        return lifeExpectancy;
    }

    public double getAverageChildCount() {
        return averageChildCount;
    }

    public int getDayCount() {
        return dayCount;
    }
}
