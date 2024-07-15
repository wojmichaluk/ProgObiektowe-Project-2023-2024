package agh.dg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import agh.dg.enums.MoveDirection;
import agh.dg.models.*;
import agh.dg.models.abstracts.Animal;
import agh.dg.models.abstracts.World;
import agh.dg.presenters.GamePresenter;
import agh.dg.records.WorldConfig;

public class Simulation implements Runnable {

    private final World world;
    private final WorldConfig worldConfig;
    private boolean running = true;

    private final Statistics statistics = new Statistics();
    private int dayCount = 0;

    public Simulation(WorldConfig worldConfig, GamePresenter presenter) {
        this.worldConfig = worldConfig;
        this.world = worldConfig.variantMap() ?
                new CreepingJungle(worldConfig.mapWidth(), worldConfig.mapHeight()) :
                new RegularWorld(worldConfig.mapWidth(), worldConfig.mapHeight());
        this.world.addObserver(presenter);

        List<Vector2d> randomAnimalPositions = this.generateRandomAnimalPositions();

        for (int i = 0; i < worldConfig.animalStart(); i++) {
            List<Integer> genotype = this.generateRandomGenotype();
            Animal animal = worldConfig.variantAnimal() ?
                    new CrazyAnimal(genotype, worldConfig.animalStartEnergy(), randomAnimalPositions.get(i)) :
                    new BasicAnimal(genotype, worldConfig.animalStartEnergy(), randomAnimalPositions.get(i));

            world.placeNewAnimal(animal);
        }

        world.placeNewPlants(worldConfig.plantStart());

        world.mapChanged(statistics);
    }

    public World getWorld() {
        return world;
    }

    private List<Vector2d> generateRandomAnimalPositions() {
        int maxX = worldConfig.mapWidth();
        int maxY = worldConfig.mapHeight();
        Random random = new Random();
        List<Vector2d> positions = new ArrayList<>();

        for (int i = 0; i < worldConfig.animalStart(); i++) {
            positions.add(new Vector2d(random.nextInt(0, maxX), random.nextInt(0, maxY)));
        }

        return positions;
    }

    private List<Integer> generateRandomGenotype() {
        int maxGene = MoveDirection.values().length;
        Random random = new Random();
        List<Integer> genotype = new ArrayList<>();

        for (int j = 0; j < worldConfig.animalGenotypeLength(); j++) {
            genotype.add(random.nextInt(0, maxGene));
        }

        return genotype;
    }

    @Override
    public void run() {
        while (running) {
            dayCount++;
            world.removeDeadAnimals(dayCount);
            world.moveAllAnimals();
            world.eatPlants(worldConfig.plantEnergy());
            world.reproduceAnimals(worldConfig.animalEnergyToReproduce(), worldConfig.animalEnergyReproductionDepletion(),
                    worldConfig.animalMutationMinimum(), worldConfig.animalMutationMaximum());
            world.placeNewPlants(worldConfig.plantDaily());
            world.setStatistics(statistics, dayCount);

            try {
                world.mapChanged(statistics);
                Thread.sleep(200);
            }catch(InterruptedException e){
                System.out.println(e.getMessage());
            }finally{
                world.aDayPassed(worldConfig.animalEnergyDailyDepletion());
            }
        }
    }

    public void stopRunning() {
        running = false;
    }
}

