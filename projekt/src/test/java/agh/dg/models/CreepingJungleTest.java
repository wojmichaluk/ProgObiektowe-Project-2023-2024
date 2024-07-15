package agh.dg.models;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CreepingJungleTest {
    @Test
    public void integrationTest() {
        //given
        CreepingJungle creepingJungle = new CreepingJungle(5, 5);
        BasicAnimal animal1 = new BasicAnimal(List.of(1, 1, 1), 51, new Vector2d(2, 2));
        BasicAnimal animal2 = new BasicAnimal(List.of(1, 2, 3), 50, new Vector2d(2, 2));

        //then
        assertEquals(new Vector2d(5, 5), creepingJungle.getBoundary().topRight());
        assertTrue(creepingJungle.getPreferredPositions().isEmpty());


        //when
        creepingJungle.placeNewAnimal(animal1);
        creepingJungle.placeNewAnimal(animal2);

        //then
        assertEquals(2, creepingJungle.getNumberOfAnimals());
        assertEquals(0, creepingJungle.getNumberOfPlants());


        //when
        creepingJungle.reproduceAnimals(20, 10, 1, 2);

        //then
        assertEquals(3, creepingJungle.getNumberOfAnimals());


        //when
        for (int i = 0; i < 5; i++) {
            creepingJungle.aDayPassed(5);
        }
        creepingJungle.removeDeadAnimals(5);

        //then
        assertEquals(2, creepingJungle.getNumberOfAnimals());


        //given
        BasicAnimal animal3 = new BasicAnimal(List.of(1, 4, 7), 100, new Vector2d(4, 4));

        //when
        creepingJungle.placeNewAnimal(animal3);
        creepingJungle.placeNewPlants(25);
        creepingJungle.eatPlants(20);

        //then
        assertEquals(23, creepingJungle.getNumberOfPlants());
        assertEquals(36, animal1.getEnergy());
        assertNotEquals(35, animal2.getEnergy());
        assertEquals(120, animal3.getEnergy());
        assertEquals(2, creepingJungle.getPreferredPositions().size());


        //given
        List<Integer> genotype1 = List.of(0, 0, 0, 0);
        BasicAnimal basicAnimal1 = new BasicAnimal(genotype1, 100, new Vector2d(2, 4));
        basicAnimal1.setMoveDirection(0);
        List<Integer> genotype2 = List.of(0, 0, 0, 0);
        BasicAnimal basicAnimal2 = new BasicAnimal(genotype2, 100, new Vector2d(4, 2));
        basicAnimal2.setMoveDirection(2);

        //when
        creepingJungle.placeNewAnimal(basicAnimal1);
        creepingJungle.placeNewAnimal(basicAnimal2);
        creepingJungle.moveAllAnimals();

        //then
        assertEquals(new Vector2d(2, 4), basicAnimal1.getPosition());
        assertEquals(new Vector2d(0, 2), basicAnimal2.getPosition());

        //when
        creepingJungle.moveAllAnimals();
        creepingJungle.moveAllAnimals();

        //then
        assertEquals(new Vector2d(2, 2), basicAnimal1.getPosition());


        //given
        BasicAnimal basicAnimal3 = new BasicAnimal(List.of(1, 2, 3, 4), 110, new Vector2d(2, 2));
        BasicAnimal basicAnimal4 = new BasicAnimal(List.of(1, 2, 3, 4), 120, new Vector2d(2, 2));

        //when
        creepingJungle.placeNewAnimal(basicAnimal3);
        creepingJungle.placeNewAnimal(basicAnimal4);

        //then
        assertEquals(Set.of(basicAnimal3, basicAnimal4),
                new HashSet<>(creepingJungle.dominatingAnimals(creepingJungle.getAnimals()
                        .get(basicAnimal3.getPosition()), 2)));
    }
}
