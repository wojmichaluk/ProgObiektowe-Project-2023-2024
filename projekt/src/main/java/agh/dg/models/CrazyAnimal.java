package agh.dg.models;

import agh.dg.models.abstracts.Animal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CrazyAnimal extends Animal {
    public CrazyAnimal(List<Integer> genotype, int initialEnergy, Vector2d position){
        super(genotype, initialEnergy, position);
    }

    @Override
    protected void updateGenotypeIndex(int currentGenotypeIndex){
        Random random = new Random();
        double probability = random.nextDouble();
        List<Integer> possibleIndexes = new ArrayList<>();

        if (probability < 0.8){
            super.updateGenotypeIndex(currentGenotypeIndex);
        }else {
            for (int i = 0; i < genotype.size(); i++) {
                if (i != currentGenotypeIndex) {
                    possibleIndexes.add(i);
                }
            }

            Collections.shuffle(possibleIndexes);
            genotypeIndex = possibleIndexes.get(0);
        }
    }

    @Override
    protected Animal createAChild(Animal other, int childEnergy, int minimumMutations, int maximumMutations){
        List<Integer> childGenotype = this.crossover(other);

        CrazyAnimal child = new CrazyAnimal(childGenotype, childEnergy, position);
        this.addChild(child);
        CrazyAnimal otherAnimal = (CrazyAnimal) other;
        otherAnimal.addChild(child);
        child.mutate(minimumMutations, maximumMutations);

        return child;
    }
}
