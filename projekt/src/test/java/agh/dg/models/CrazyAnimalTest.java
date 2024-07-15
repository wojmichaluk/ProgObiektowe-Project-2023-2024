package agh.dg.models;

import agh.dg.models.abstracts.World;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class CrazyAnimalTest {
    @Test
    public void integrationTest() {
        //given
        List<Integer> genotype = List.of(5, 5, 5, 5);
        CrazyAnimal crazyAnimal = new CrazyAnimal(genotype, 101, new Vector2d(5, 5));
        crazyAnimal.setMoveDirection(0);
        World regularWorld = new RegularWorld(10, 10);

        //then
        assertTrue(crazyAnimal.isAlive());


        //when
        regularWorld.placeNewAnimal(crazyAnimal);
        regularWorld.moveAllAnimals();

        //then
        assertEquals(new Vector2d(4, 4), crazyAnimal.getPosition());


        //given
        List<Integer> mateGenotype = List.of(6, 6, 6, 6);
        CrazyAnimal mateAnimal = new CrazyAnimal(mateGenotype, 100, new Vector2d(4, 4));

        //when
        regularWorld.placeNewAnimal(mateAnimal);
        regularWorld.reproduceAnimals(50, 20, 0, 0);

        //then
        CrazyAnimal child = (CrazyAnimal) regularWorld.getAllAnimals().stream()
                .filter(animal -> !(animal == crazyAnimal || animal == mateAnimal))
                .toList().get(0);
        assertEquals(81, crazyAnimal.getEnergy());
        assertEquals(80, mateAnimal.getEnergy());
        assertEquals(40, child.getEnergy());
        assertTrue(Objects.equals(child.getGenotype(), List.of(5, 5, 6, 6)) || Objects.equals(child.getGenotype(), List.of(6, 6, 5, 5)));
        assertEquals(1, crazyAnimal.getNumberOfDescendants());
        assertEquals(1, mateAnimal.getNumberOfChildren());


        //when
        for (int i = 0; i < 5; i++) {
            regularWorld.aDayPassed(10);
        }
        regularWorld.removeDeadAnimals(5);

        //then
        assertFalse(child.isAlive());
        assertEquals(5, crazyAnimal.getAge());
        assertNotEquals(4, mateAnimal.getAge());
        assertEquals(5, child.getDayOfDeath());
        assertTrue(crazyAnimal.isAlive());
        assertEquals(30, mateAnimal.getEnergy());


        //when
        regularWorld.placeNewPlants(100);
        regularWorld.eatPlants(20);

        //then
        assertEquals(51, crazyAnimal.getEnergy());
        assertEquals(30, mateAnimal.getEnergy());
    }
}

