package agh.dg;

import agh.dg.models.BasicAnimal;
import agh.dg.models.CrazyAnimal;
import agh.dg.models.RegularWorld;
import agh.dg.models.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StatisticsTest {
    @Test
    public void integrationTest() {
        //given
        Statistics statistics = new Statistics();
        RegularWorld regularWorld = new RegularWorld(5, 5);
        BasicAnimal animal1 = new BasicAnimal(List.of(1, 1, 1), 62, new Vector2d(2, 2));
        BasicAnimal animal2 = new BasicAnimal(List.of(1, 2, 3), 58, new Vector2d(2, 2));

        //when
        regularWorld.placeNewAnimal(animal1);
        regularWorld.placeNewAnimal(animal2);
        regularWorld.setStatistics(statistics, 1);

        //then
        assertEquals(2, statistics.getAnimalCount());
        assertEquals(0, statistics.getPlantsCount());
        assertEquals(24, statistics.getFreeFieldsCount());
        assertEquals(Set.of(List.of(1, 1, 1), List.of(1, 2, 3)), new HashSet<>(statistics.getMostPopularGenotypes()));
        assertEquals(60.0, statistics.getAverageAnimalEnergy());
        assertEquals(0.0, statistics.getLifeExpectancy());
        assertEquals(0.0, statistics.getAverageChildCount());
        assertEquals(1, statistics.getDayCount());


        //when
        regularWorld.reproduceAnimals(20, 10, 1, 2);
        regularWorld.setStatistics(statistics, 2);

        //then
        assertEquals(3, statistics.getAnimalCount());
        assertEquals(40.0, statistics.getAverageAnimalEnergy());
        assertEquals((double) 2 / 3, statistics.getAverageChildCount());
        assertEquals(2, statistics.getDayCount());


        //when
        for (int i = 0; i < 5; i++) {
            regularWorld.aDayPassed(5);
        }
        regularWorld.removeDeadAnimals(7);
        regularWorld.setStatistics(statistics, 7);

        //then
        assertNotEquals(3, statistics.getAnimalCount());
        assertEquals(5.0, statistics.getLifeExpectancy());
        assertEquals(1.0, statistics.getAverageChildCount());
        assertEquals(25.0, statistics.getAverageAnimalEnergy());
        assertEquals(7, statistics.getDayCount());


        //given
        BasicAnimal animal3 = new BasicAnimal(List.of(1, 4, 7), 100, new Vector2d(4, 4));

        //when
        regularWorld.placeNewAnimal(animal3);
        regularWorld.placeNewPlants(25);
        regularWorld.eatPlants(20);
        regularWorld.setStatistics(statistics, 8);

        //then
        assertEquals(23, statistics.getPlantsCount());
        assertEquals(0, statistics.getFreeFieldsCount());
        assertEquals(3, statistics.getAnimalCount());
        assertEquals((double) 2 / 3, statistics.getAverageChildCount());
        assertEquals((double) 190 / 3, statistics.getAverageAnimalEnergy());
        assertEquals(8, statistics.getDayCount());


        //given
        BasicAnimal basicAnimal1 = new BasicAnimal(List.of(1, 2, 3, 4), 100, new Vector2d(1, 3));
        BasicAnimal basicAnimal2 = new BasicAnimal(List.of(1, 3, 5, 7), 110, new Vector2d(2, 4));

        //when
        regularWorld.placeNewAnimal(basicAnimal1);
        regularWorld.placeNewAnimal(basicAnimal2);
        regularWorld.setStatistics(statistics, 9);

        //then
        assertEquals(5, statistics.getAnimalCount());
        assertEquals(80.0, statistics.getAverageAnimalEnergy());
        assertEquals(0.4, statistics.getAverageChildCount());
        assertEquals(9, statistics.getDayCount());
    }
}
