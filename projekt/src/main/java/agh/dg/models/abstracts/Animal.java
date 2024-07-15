package agh.dg.models.abstracts;

import agh.dg.enums.MoveDirection;
import agh.dg.models.Vector2d;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

abstract public class Animal extends WorldElement {

    protected final List<Integer> genotype;

    protected MoveDirection moveDirection;

    protected int energy;

    protected int age = 0;

    protected int genotypeIndex;

    protected int plantsEaten = 0;

    protected int dayOfDeath;

    protected List<Animal> children = new ArrayList<>();

    public Animal(List<Integer> genotype, int initialEnergy, Vector2d position) {
        super(position);

        Random random = new Random();
        this.genotype = genotype;
        genotypeIndex = random.nextInt(0, genotype.size());
        this.moveDirection = MoveDirection.values()[random.nextInt(0, MoveDirection.values().length)];
        energy = initialEnergy;
    }

    public void setMoveDirection(int value) {
        moveDirection = MoveDirection.values()[value];
    }

    public int getEnergy() {
        return energy;
    }

    public int getAge(){
        return age;
    }

    public int getNumberOfChildren(){
        return children.size();
    }

    public int getNumberOfDescendants(){
        return getDescendants().size();
    }

    public List<Integer> getGenotype() {
        return genotype;
    }

    public int getGenotypeIndex() {
        return genotypeIndex;
    }

    public int getPlantsEaten() {
        return plantsEaten;
    }

    public int getDayOfDeath() {
        return dayOfDeath;
    }

    public Node getGraphicalRepresentation(Pane pane) {
        return new Circle(pane.getWidth()/ 2);
    }

    public boolean isAlive(){
        return energy > 0;
    }

    protected void move(World world) {
        moveDirection = moveDirection.changeDirection(genotype.get(genotypeIndex));
        this.updateGenotypeIndex(genotypeIndex);

        Vector2d possibleNextPosition = position.movedBy(moveDirection.singleMoveVector());
        int width = world.getBoundary().topRight().getX();
        int height = world.getBoundary().topRight().getY();

        if (possibleNextPosition.getY() == -1 || possibleNextPosition.getY() == height) {
            moveDirection = moveDirection.changeDirection(MoveDirection.values().length / 2);
        } else {
            position = possibleNextPosition;

            if (position.getX() == -1){
                position = new Vector2d(width - 1, position.getY());
            }else if (position.getX() == width){
                position = new Vector2d(0, position.getY());
            }
        }
    }

    protected void updateGenotypeIndex(int currentGenotypeIndex) {
        genotypeIndex = (currentGenotypeIndex + 1) % genotype.size();
    }

    protected void eat(int energy){
        plantsEaten++;
        this.energy += energy;
    }

    protected void reproduce(World world, Animal mate, int animalEnergyToReproduce, int animalEnergyReproductionDepletion, int minimumMutations, int maximumMutations){
        if (Math.min(energy, mate.energy) < animalEnergyToReproduce){
            return;
        }

        Animal child = this.createAChild(mate, 2 * animalEnergyReproductionDepletion, minimumMutations, maximumMutations);
        world.placeNewAnimal(child);

        this.energy -= animalEnergyReproductionDepletion;
        mate.energy -= animalEnergyReproductionDepletion;
    }

    // common code delegated to this auxiliary function
    protected List<Integer> crossover (Animal other) {
        double dominantPart = (double) energy / (energy + other.energy);
        Random random = new Random();
        int dominantSide = random.nextInt(2);
        List<Integer> childGenotype = new ArrayList<>();

        if (dominantSide == 0){ //left side
            int genotypeDivider = (int) Math.round(genotype.size() * dominantPart);

            for (int i = 0; i < genotypeDivider; i++){
                childGenotype.add(genotype.get(i));
            }

            for (int i = genotypeDivider; i < genotype.size(); i++){
                childGenotype.add(other.genotype.get(i));
            }
        }else{ //right side
            int genotypeDivider = (int) Math.round(genotype.size() * (1 - dominantPart));

            for (int i = 0; i < genotypeDivider; i++){
                childGenotype.add(other.genotype.get(i));
            }

            for (int i = genotypeDivider; i < genotype.size(); i++){
                childGenotype.add(genotype.get(i));
            }
        }

        return childGenotype;
    }

    abstract protected Animal createAChild (Animal other, int childEnergy, int minimumMutations, int maximumMutations);

    protected void addChild(Animal child) {
        children.add(child);
    }

    protected void mutate(int minimumMutations, int maximumMutations){
        Random random = new Random();
        int mutations = random.nextInt(minimumMutations, maximumMutations + 1);
        List<Integer> mutatedIndexes = this.randomIndexes(mutations);

        for (int index : mutatedIndexes){
            this.genotype.set(index, random.nextInt(0, MoveDirection.values().length));
        }
    }

    protected List<Integer> randomIndexes(int mutations){
        List<Integer> possibleIndexes = new ArrayList<>();

        for (int i = 0; i < genotype.size(); i++){
            possibleIndexes.add(i);
        }

        Collections.shuffle(possibleIndexes);
        return possibleIndexes.subList(0, mutations);
    }

    protected void getOlder(int energyDailyDepletion){
        age++;
        energy -= energyDailyDepletion;
    }

    protected List<Animal> getDescendants() {
        List<Animal> descendants = new ArrayList<>(children);

        for (Animal child : children) {
            List<Animal> childDescendants = child.getDescendants();
            descendants.addAll(childDescendants);
        }

        return descendants.stream()
                .distinct()
                .toList();
    }

    protected void die(int dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
    }
}
