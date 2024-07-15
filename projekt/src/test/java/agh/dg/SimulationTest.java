package agh.dg;

import agh.dg.models.*;
import agh.dg.models.abstracts.Animal;
import agh.dg.models.abstracts.World;
import agh.dg.records.WorldConfig;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//simulation is not created literally, but we are testing all important
//methods included in Simulation constructor and run() method
public class SimulationTest {
    @Test
    public void testVariantOne() {
        WorldConfig worldConfig = new WorldConfig(5, 5, 0, 0,
                10, 2, 100, 30,
                10, 90, 1,
                2, 4, false, false);
        int dayCount = 0;
        World regularWorld = worldConfig.variantMap() ?
                new CreepingJungle(worldConfig.mapWidth(), worldConfig.mapHeight()) :
                new RegularWorld(worldConfig.mapWidth(), worldConfig.mapHeight());

        List<Vector2d> animalPositions = List.of(new Vector2d(2, 2), new Vector2d(4, 4));
        List<List<Integer>> genotypes = List.of(List.of(1, 1, 1, 1), List.of(5, 5, 5, 5));
        List<Animal> animals = new ArrayList<>();

        for (int i = 0; i < worldConfig.animalStart(); i++) {
            Animal animal = worldConfig.variantAnimal() ?
                    new CrazyAnimal(genotypes.get(i), worldConfig.animalStartEnergy(), animalPositions.get(i)) :
                    new BasicAnimal(genotypes.get(i), worldConfig.animalStartEnergy(), animalPositions.get(i));
            animals.add(animal);

            animal.setMoveDirection(0);
            regularWorld.placeNewAnimal(animal);
        }

        regularWorld.placeNewPlants(worldConfig.plantStart());

        //now imitate run() method for 5 days
        while (dayCount < 5) {
            dayCount++;
            regularWorld.removeDeadAnimals(dayCount);
            regularWorld.moveAllAnimals();
            regularWorld.eatPlants(worldConfig.plantEnergy());
            regularWorld.reproduceAnimals(worldConfig.animalEnergyToReproduce(), worldConfig.animalEnergyReproductionDepletion(),
                    worldConfig.animalMutationMinimum(), worldConfig.animalMutationMaximum());

            if (dayCount == 1) {
                BasicAnimal child = (BasicAnimal) regularWorld.getAllAnimals().stream()
                        .filter(animal -> !(animal == animals.get(0) || animal == animals.get(1)))
                        .toList().get(0);
                animals.add(child);
            }

            regularWorld.placeNewPlants(worldConfig.plantDaily());
            regularWorld.aDayPassed(worldConfig.animalEnergyDailyDepletion());
        }

        //animals should reproduce once and be alive along with the child
        assertTrue(animals.get(0).isAlive());
        assertTrue(animals.get(1).isAlive());
        assertTrue(animals.get(2).isAlive());
        assertEquals(3, regularWorld.getAllAnimals().size());
    }

    @Test
    public void testVariantTwo() {
        WorldConfig worldConfig = new WorldConfig(5, 5, 0, 0,
                10, 2, 100, 20,
                10, 90, 1,
                2, 4, true, false);
        int dayCount = 0;
        World creepingJungle = worldConfig.variantMap() ?
                new CreepingJungle(worldConfig.mapWidth(), worldConfig.mapHeight()) :
                new RegularWorld(worldConfig.mapWidth(), worldConfig.mapHeight());

        List<Vector2d> animalPositions = List.of(new Vector2d(2, 2), new Vector2d(4, 4));
        List<List<Integer>> genotypes = List.of(List.of(1, 1, 1, 1), List.of(5, 5, 5, 5));
        List<Animal> animals = new ArrayList<>();

        for (int i = 0; i < worldConfig.animalStart(); i++) {
            Animal animal = worldConfig.variantAnimal() ?
                    new CrazyAnimal(genotypes.get(i), worldConfig.animalStartEnergy(), animalPositions.get(i)) :
                    new BasicAnimal(genotypes.get(i), worldConfig.animalStartEnergy(), animalPositions.get(i));
            animals.add(animal);

            animal.setMoveDirection(0);
            creepingJungle.placeNewAnimal(animal);
        }

        creepingJungle.placeNewPlants(worldConfig.plantStart());

        //now imitate run() method for 5 days
        while (dayCount < 5) {
            dayCount++;
            creepingJungle.removeDeadAnimals(dayCount);
            creepingJungle.moveAllAnimals();
            creepingJungle.eatPlants(worldConfig.plantEnergy());
            creepingJungle.reproduceAnimals(worldConfig.animalEnergyToReproduce(), worldConfig.animalEnergyReproductionDepletion(),
                    worldConfig.animalMutationMinimum(), worldConfig.animalMutationMaximum());

            if (dayCount == 1) {
                //animals should reproduce once ...
                assertEquals(3, creepingJungle.getAllAnimals().size());

                BasicAnimal child = (BasicAnimal) creepingJungle.getAllAnimals().stream()
                        .filter(animal -> !(animal == animals.get(0) || animal == animals.get(1)))
                        .toList().get(0);
                animals.add(child);
            }

            creepingJungle.placeNewPlants(worldConfig.plantDaily());
            creepingJungle.aDayPassed(worldConfig.animalEnergyDailyDepletion());
        }

        //... and be alive, but their child not
        assertTrue(animals.get(0).isAlive());
        assertTrue(animals.get(1).isAlive());
        assertFalse(animals.get(2).isAlive());
        assertEquals(2, creepingJungle.getAllAnimals().size());
    }

    @Test
    public void testVariantThree() {
        WorldConfig worldConfig = new WorldConfig(5, 5, 0, 0,
                10, 2, 40, 20,
                10, 35, 1,
                2, 4, false, true);
        int dayCount = 0;
        World regularWorld = worldConfig.variantMap() ?
                new CreepingJungle(worldConfig.mapWidth(), worldConfig.mapHeight()) :
                new RegularWorld(worldConfig.mapWidth(), worldConfig.mapHeight());

        List<Vector2d> animalPositions = List.of(new Vector2d(0, 0), new Vector2d(4, 4));
        List<List<Integer>> genotypes = List.of(List.of(1, 1, 1, 1), List.of(5, 5, 5, 5));
        List<Animal> animals = new ArrayList<>();

        for (int i = 0; i < worldConfig.animalStart(); i++) {
            Animal animal = worldConfig.variantAnimal() ?
                    new CrazyAnimal(genotypes.get(i), worldConfig.animalStartEnergy(), animalPositions.get(i)) :
                    new BasicAnimal(genotypes.get(i), worldConfig.animalStartEnergy(), animalPositions.get(i));
            animals.add(animal);

            animal.setMoveDirection(0);
            regularWorld.placeNewAnimal(animal);
        }

        regularWorld.placeNewPlants(worldConfig.plantStart());

        //now imitate run() method for 5 days
        while (dayCount < 5) {
            dayCount++;
            regularWorld.removeDeadAnimals(dayCount);
            regularWorld.moveAllAnimals();
            regularWorld.eatPlants(worldConfig.plantEnergy());
            regularWorld.reproduceAnimals(worldConfig.animalEnergyToReproduce(), worldConfig.animalEnergyReproductionDepletion(),
                    worldConfig.animalMutationMinimum(), worldConfig.animalMutationMaximum());

            if (dayCount == 2) {
                //no reproduction should occur
                assertEquals(2, regularWorld.getAllAnimals().size());
            }

            regularWorld.placeNewPlants(worldConfig.plantDaily());
            regularWorld.aDayPassed(worldConfig.animalEnergyDailyDepletion());
        }

        //all animals should be dead ( :( ) by day 5
        assertFalse(animals.get(0).isAlive());
        assertFalse(animals.get(1).isAlive());
        assertTrue(regularWorld.getAllAnimals().isEmpty());
    }

    @Test
    public void testVariantFour() {
        WorldConfig worldConfig = new WorldConfig(6, 5, 30, 5,
                20, 5, 100, 20,
                10, 90, 1,
                2, 4, true, true);
        int dayCount = 0;
        World creepingJungle = worldConfig.variantMap() ?
                new CreepingJungle(worldConfig.mapWidth(), worldConfig.mapHeight()) :
                new RegularWorld(worldConfig.mapWidth(), worldConfig.mapHeight());

        List<Vector2d> animalPositions = List.of(new Vector2d(0, 0), new Vector2d(1, 5),
                new Vector2d(2, 0), new Vector2d(3, 5), new Vector2d(4, 0));
        List<List<Integer>> genotypes = List.of(List.of(0, 0, 0, 0), List.of(4, 4, 4, 4),
                List.of(0, 0, 0, 0), List.of(4, 4, 4, 4), List.of(0, 0, 0, 0));
        List<Animal> animals = new ArrayList<>();

        for (int i = 0; i < worldConfig.animalStart(); i++) {
            Animal animal = worldConfig.variantAnimal() ?
                    new CrazyAnimal(genotypes.get(i), worldConfig.animalStartEnergy(), animalPositions.get(i)) :
                    new BasicAnimal(genotypes.get(i), worldConfig.animalStartEnergy(), animalPositions.get(i));
            animals.add(animal);

            animal.setMoveDirection(0);
            creepingJungle.placeNewAnimal(animal);
        }

        creepingJungle.placeNewPlants(worldConfig.plantStart());

        //now imitate run() method for 5 days
        while (dayCount < 5) {
            dayCount++;
            creepingJungle.removeDeadAnimals(dayCount);
            creepingJungle.moveAllAnimals();
            creepingJungle.eatPlants(worldConfig.plantEnergy());
            creepingJungle.reproduceAnimals(worldConfig.animalEnergyToReproduce(), worldConfig.animalEnergyReproductionDepletion(),
                    worldConfig.animalMutationMinimum(), worldConfig.animalMutationMaximum());
            creepingJungle.placeNewPlants(worldConfig.plantDaily());
            creepingJungle.aDayPassed(worldConfig.animalEnergyDailyDepletion());

            //animals should eat everyday
            for (Animal animal : animals) {
                assertEquals(worldConfig.animalStartEnergy() + (worldConfig.plantEnergy() -
                        worldConfig.animalEnergyDailyDepletion()) * dayCount, animal.getEnergy());
            }
        }

        //all animals should be alive and well fed
        assertTrue(animals.get(0).isAlive());
        assertTrue(animals.get(1).isAlive());
        assertTrue(animals.get(2).isAlive());
        assertTrue(animals.get(3).isAlive());
        assertTrue(animals.get(4).isAlive());
        assertEquals(5, creepingJungle.getAllAnimals().size());
    }
}
