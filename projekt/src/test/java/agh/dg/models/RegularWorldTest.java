package agh.dg.models;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RegularWorldTest {
    @Test
    public void integrationTest() {
        //given
        RegularWorld regularWorld = new RegularWorld(5, 5);
        CrazyAnimal animal1 = new CrazyAnimal(List.of(1, 1, 1), 51, new Vector2d(2, 2));
        CrazyAnimal animal2 = new CrazyAnimal(List.of(1, 2, 3), 50, new Vector2d(2, 2));

        //then
        assertEquals(new Vector2d(5, 5), regularWorld.getBoundary().topRight());
        assertEquals(Set.of(new Vector2d(0, 2), new Vector2d(1, 2), new Vector2d(2, 2),
                new Vector2d(3, 2), new Vector2d(4, 2)), regularWorld.getPreferredPositions());


        //when
        regularWorld.placeNewAnimal(animal1);
        regularWorld.placeNewAnimal(animal2);

        //then
        assertEquals(2, regularWorld.getNumberOfAnimals());
        assertEquals(0, regularWorld.getNumberOfPlants());


        //when
        regularWorld.reproduceAnimals(20, 10, 1, 2);

        //then
        assertEquals(3, regularWorld.getNumberOfAnimals());


        //when
        for (int i = 0; i < 5; i++) {
            regularWorld.aDayPassed(5);
        }
        regularWorld.removeDeadAnimals(5);

        //then
        assertEquals(2, regularWorld.getNumberOfAnimals());


        //given
        CrazyAnimal animal3 = new CrazyAnimal(List.of(1, 4, 7), 100, new Vector2d(4, 4));

        //when
        regularWorld.placeNewAnimal(animal3);
        regularWorld.placeNewPlants(25);
        regularWorld.eatPlants(20);

        //then
        assertEquals(23, regularWorld.getNumberOfPlants());
        assertEquals(36, animal1.getEnergy());
        assertNotEquals(35, animal2.getEnergy());
        assertEquals(120, animal3.getEnergy());


        //given
        List<Integer> genotype1 = List.of(0, 0, 0, 0);
        CrazyAnimal crazyAnimal1 = new CrazyAnimal(genotype1, 100, new Vector2d(2, 4));
        crazyAnimal1.setMoveDirection(0);
        List<Integer> genotype2 = List.of(0, 0, 0, 0);
        CrazyAnimal crazyAnimal2 = new CrazyAnimal(genotype2, 100, new Vector2d(4, 2));
        crazyAnimal2.setMoveDirection(2);

        //when
        regularWorld.placeNewAnimal(crazyAnimal1);
        regularWorld.placeNewAnimal(crazyAnimal2);
        regularWorld.moveAllAnimals();

        //then
        assertEquals(new Vector2d(2, 4), crazyAnimal1.getPosition());
        assertEquals(new Vector2d(0, 2), crazyAnimal2.getPosition());

        //when
        regularWorld.moveAllAnimals();
        regularWorld.moveAllAnimals();

        //then
        assertEquals(new Vector2d(2, 2), crazyAnimal1.getPosition());


        //given
        CrazyAnimal crazyAnimal3 = new CrazyAnimal(List.of(1, 2, 3, 4), 110, new Vector2d(2, 2));
        CrazyAnimal crazyAnimal4 = new CrazyAnimal(List.of(1, 2, 3, 4), 120, new Vector2d(2, 2));

        //when
        regularWorld.placeNewAnimal(crazyAnimal3);
        regularWorld.placeNewAnimal(crazyAnimal4);

        //then
        assertEquals(Set.of(crazyAnimal3, crazyAnimal4),
                new HashSet<>(regularWorld.dominatingAnimals(regularWorld.getAnimals()
                        .get(crazyAnimal3.getPosition()), 2)));
    }
}
