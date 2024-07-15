package agh.dg.models.abstracts;

import agh.dg.Statistics;
import agh.dg.models.Plant;
import agh.dg.models.Vector2d;
import agh.dg.observers.MapChangeListener;
import agh.dg.records.Boundary;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

abstract public class World {

    protected final UUID id = UUID.randomUUID();

    protected final Boundary boundary;

    protected long deadAnimalsLifetimeSum = 0;

    protected int deadAnimalsCount = 0;

    protected final Map<Vector2d, List<Animal>> animals = new ConcurrentHashMap<>();

    protected final Map<Vector2d, Plant> plants = new ConcurrentHashMap<>();

    protected final List<MapChangeListener> observers = new ArrayList<>();

    protected final Set<Vector2d> lessPreferredPositions = new HashSet<>();

    protected final Set<Vector2d> preferredPositions = new HashSet<>();

    public World(int width, int height) {
        boundary = new Boundary(new Vector2d(0, 0), new Vector2d(width, height));
    }

    public UUID getId() {
        return id;
    }

    public Set<Vector2d> getPreferredPositions() {
        return preferredPositions;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public Map<Vector2d, List<Animal>> getAnimals() {
        return animals;
    }

    public List<Animal> getAllAnimals() {
        List<Animal> allAnimals = new ArrayList<>();

        for (List<Animal> animalsTogether : animals.values()) {
            allAnimals.addAll(animalsTogether);
        }

        return allAnimals;
    }

    public Animal getAnimalById(UUID id) {
        List<Animal> allAnimals = getAllAnimals();

        for (Animal animal : allAnimals) {
            if (animal.getId().equals(id)) {
                return animal;
            }
        }

        return null;
    }

    public Map<Vector2d, Plant> getPlants() {
        return plants;
    }

    public int getNumberOfAnimals() {
        return this.getAllAnimals().size();
    }

    public int getNumberOfPlants() {
        return plants.size();
    }

    protected int getNumberOfFreeFields() {
        Set<Vector2d> usedPositions = new HashSet<>(animals.keySet());
        usedPositions.addAll(plants.keySet());
        int width = boundary.topRight().getX();
        int height = boundary.topRight().getY();

        return width * height - usedPositions.size();
    }

    protected List<List<Integer>> getMostPopularGenotype() {
        List<Animal> allAnimals = this.getAllAnimals();
        Map<List<Integer>, Integer> genotypePopularity = new HashMap<>();

        for (Animal animal : allAnimals) {
            if (genotypePopularity.containsKey(animal.genotype)) {
                genotypePopularity.put(animal.genotype, genotypePopularity.get(animal.genotype) + 1);
            } else {
                genotypePopularity.put(animal.genotype, 1);
            }
        }

        return allAnimals.isEmpty() ? new ArrayList<>() : genotypePopularity.keySet()
                .stream()
                .sorted(Comparator.comparingInt(genotypePopularity::get).reversed())
                .toList()
                .stream()
                .limit(3)
                .toList();
    }

    protected double getAverageAliveAnimalsEnergy() {
        List<Animal> allAnimals = this.getAllAnimals();

        return allAnimals.isEmpty() ? 0.0 : (double) allAnimals.stream()
                .map(animal -> animal.energy)
                .reduce(Integer::sum)
                .get() / allAnimals.size();
    }

    protected double getLifeExpectancy() {
        return deadAnimalsCount == 0 ? 0.0 : (double) deadAnimalsLifetimeSum / deadAnimalsCount;
    }

    protected double getAverageAliveAnimalsChildrenCount() {
        List<Animal> allAnimals = this.getAllAnimals();

        return allAnimals.isEmpty() ? 0.0 : (double) allAnimals.stream()
                .map(Animal::getNumberOfChildren)
                .reduce(Integer::sum)
                .get() / allAnimals.size();
    }

    public void setStatistics(Statistics stats, int newDay) {
        stats.setStatisticParameters(this.getNumberOfAnimals(),
                this.getNumberOfPlants(),
                this.getNumberOfFreeFields(),
                this.getMostPopularGenotype(),
                this.getAverageAliveAnimalsEnergy(),
                this.getLifeExpectancy(),
                this.getAverageAliveAnimalsChildrenCount(),
                newDay);
    }

    public void addObserver(MapChangeListener observer) {
        this.observers.add(observer);
    }

    public void mapChanged(Statistics statistics) {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, statistics);
        }
    }

    public void placeNewAnimal(Animal animal) {
        List<Animal> occupyingAnimals = animals.get(animal.getPosition());

        if (occupyingAnimals == null) {
            occupyingAnimals = new ArrayList<>();
        }

        occupyingAnimals.add(animal);
        animals.put(animal.getPosition(), occupyingAnimals);
    }

    public void removeDeadAnimals(int currentDay) {
        for (List<Animal> animalsTogether : animals.values()) {
            Vector2d position = animalsTogether.get(0).getPosition();

            List<Animal> deadAnimals = animalsTogether.stream()
                    .filter(animal -> !animal.isAlive())
                    .toList();

            for (Animal deadAnimal : deadAnimals) {
                deadAnimal.die(currentDay);
                deadAnimalsLifetimeSum += deadAnimal.age;
                deadAnimalsCount++;
            }

            animalsTogether = animalsTogether.stream()
                    .filter(Animal::isAlive)
                    .toList();

            if (animalsTogether.isEmpty()) {
                animals.remove(position);
            } else {
                animals.put(position, animalsTogether);
            }
        }
    }

    public void moveAllAnimals() {
        List<Animal> newAnimalsToPlace = new ArrayList<>();

        for (List<Animal> animalsTogether : animals.values()) {
            Vector2d currentPosition = animalsTogether.get(0).getPosition();

            for (Animal singleAnimal : animalsTogether) {
                singleAnimal.move(this);
                newAnimalsToPlace.add(singleAnimal);
            }

            animals.remove(currentPosition);
        }

        for (Animal animalToPlace : newAnimalsToPlace) {
            placeNewAnimal(animalToPlace);
        }
    }

    public void eatPlants(int plantEnergy) {
        for (List<Animal> animalsTogether : animals.values()) {
            Vector2d currentPosition = animalsTogether.get(0).getPosition();

            if (plants.containsKey(currentPosition)) {
                Animal eatingAnimal = this.dominatingAnimals(animalsTogether, 1).get(0);

                eatingAnimal.eat(plantEnergy);
                this.removeEatenPlant(plants.get(currentPosition));
            }
        }
    }

    protected void removeEatenPlant(Plant eatenPlant) {
        Vector2d availablePosition = eatenPlant.getPosition();
        plants.remove(availablePosition);
    }

    // can be also used to draw the right animal
    // or find animal with most energy
    public List<Animal> dominatingAnimals(List<Animal> allAnimalsConsidered, int howMany) {
        return allAnimalsConsidered.stream()
                .sorted(Comparator.comparing(Animal::getEnergy)
                        .thenComparingInt(Animal::getAge)
                        .thenComparingInt(Animal::getNumberOfChildren)
                        .thenComparing(WorldElement::getId)
                        .reversed())
                .limit(howMany)
                .toList();
    }

    public void reproduceAnimals(int animalEnergyToReproduce, int animalEnergyReproductionDepletion, int minimumMutations, int maximumMutations) {
        for (List<Animal> animalsTogether : animals.values()) {
            if (animalsTogether.size() >= 2) {
                List<Animal> reproducingAnimals = this.dominatingAnimals(animalsTogether, 2);
                Animal dominantAnimal = reproducingAnimals.get(0);
                Animal mateAnimal = reproducingAnimals.get(1);

                dominantAnimal.reproduce(this, mateAnimal, animalEnergyToReproduce, animalEnergyReproductionDepletion, minimumMutations, maximumMutations);
            }
        }
    }

    // common code delegated to this auxiliary function
    protected List<Vector2d> findNewPlantPositions(int numberOfPlants) {
        List<Vector2d> newPlantPositions = new ArrayList<>();
        double preferredPositionProbability = 0.8; // Pareto rule
        Random random = new Random();

        for (int i = 0; i < numberOfPlants; i++) {
            double probability = random.nextDouble();

            if ((probability < preferredPositionProbability && !preferredPositions.isEmpty()) || lessPreferredPositions.isEmpty()) {
                Vector2d plantPosition = preferredPositions.stream().toList().get(random.nextInt(0, preferredPositions.size()));

                preferredPositions.remove(plantPosition);
                newPlantPositions.add(plantPosition);
            } else if (!lessPreferredPositions.isEmpty()) {
                Vector2d plantPosition = lessPreferredPositions.stream().toList().get(random.nextInt(0, lessPreferredPositions.size()));

                lessPreferredPositions.remove(plantPosition);
                newPlantPositions.add(plantPosition);
            }
        }

        for (Vector2d position : newPlantPositions) {
            plants.put(position, new Plant(position));
        }

        return newPlantPositions;
    }

    public void placeNewPlants(int numberOfPlants) {
        this.findNewPlantPositions(numberOfPlants);
    }

    public void aDayPassed(int energyDepletion) {
        for (Animal animal : this.getAllAnimals()) {
            animal.getOlder(energyDepletion);
        }
    }
}
