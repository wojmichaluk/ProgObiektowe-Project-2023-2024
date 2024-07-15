package agh.dg.records;

public record WorldConfig(int mapHeight,
                          int mapWidth,
                          int plantStart,
                          int plantDaily,
                          int plantEnergy,
                          int animalStart,
                          int animalStartEnergy,
                          int animalEnergyReproductionDepletion,
                          int animalEnergyDailyDepletion,
                          int animalEnergyToReproduce,
                          int animalMutationMinimum,
                          int animalMutationMaximum,
                          int animalGenotypeLength,
                          boolean variantMap,
                          boolean variantAnimal) {
}
