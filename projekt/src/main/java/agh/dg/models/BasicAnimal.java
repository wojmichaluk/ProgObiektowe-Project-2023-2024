package agh.dg.models;

import agh.dg.models.abstracts.Animal;

import java.util.List;

public class BasicAnimal extends Animal {
    public BasicAnimal(List<Integer> genotype, int initialEnergy, Vector2d position){
        super(genotype, initialEnergy, position);
    }

    @Override
    protected Animal createAChild(Animal other, int childEnergy, int minimumMutations, int maximumMutations){
        List<Integer> childGenotype = this.crossover(other);

        BasicAnimal child = new BasicAnimal(childGenotype, childEnergy, position);
        this.addChild(child);
        BasicAnimal otherAnimal = (BasicAnimal) other;
        otherAnimal.addChild(child);
        child.mutate(minimumMutations, maximumMutations);

        return child;
    }
}
