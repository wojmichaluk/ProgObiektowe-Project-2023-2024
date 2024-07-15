package agh.dg.helpers;

import agh.dg.Statistics;
import agh.dg.enums.GameCommunicat;
import agh.dg.models.abstracts.Animal;

import java.io.PrintWriter;
import java.util.UUID;

public class CsvFileHandler {

    public static void setStatisticsHeader(PrintWriter writer) {
        writer.println("WorldID,Day number,Date,All animals,All plants,All free fields,Most popular genotypes,Average animals energy,Average animals age,Average animals children,AnimalID,Genotype,Energy,Eaten plants,Children,Descendants,Age,Date of death");
    }

    public static void fillStatisticsDay(PrintWriter writer, UUID worldId, Statistics statistics, Animal animal) {
        writer.println(worldId + "," +
                // General statistics
                statistics.getDayCount() + "," +
                GameStatic.convertDaysToDate(statistics.getDayCount()) + "," +
                statistics.getAnimalCount() + "," +
                statistics.getPlantsCount() + "," +
                statistics.getFreeFieldsCount() + "," +
                (!statistics.getMostPopularGenotypes().isEmpty() ? GameStatic.convertGenotypeToString(statistics.getMostPopularGenotypes().get(0)) : GameCommunicat.NO_GENOTYPE.toString()) + " " +
                (statistics.getMostPopularGenotypes().size() > 1 ? GameStatic.convertGenotypeToString(statistics.getMostPopularGenotypes().get(1)) : GameCommunicat.NO_GENOTYPE.toString()) + " " +
                (statistics.getMostPopularGenotypes().size() > 2 ? GameStatic.convertGenotypeToString(statistics.getMostPopularGenotypes().get(2)) : GameCommunicat.NO_GENOTYPE.toString()) + "," +
                GameStatic.convertNumberToString(statistics.getAverageAnimalEnergy()) + "," +
                GameStatic.convertDaysToTime(statistics.getLifeExpectancy()) + "," +
                GameStatic.convertNumberToString(statistics.getAverageChildCount()) + "," +

                // Animal statistics
                (animal != null ? animal.getId() : GameCommunicat.NO_SELECTED_ANIMAL.toString()) + "," +
                (animal != null ? GameStatic.convertGenotypeWithIndexToString(animal.getGenotype(), animal.getGenotypeIndex()) : GameCommunicat.NO_SELECTED_ANIMAL.toString()) + "," +
                (animal != null ? animal.getEnergy() : GameCommunicat.NO_SELECTED_ANIMAL.toString()) + "," +
                (animal != null ? animal.getPlantsEaten() : GameCommunicat.NO_SELECTED_ANIMAL.toString()) + "," +
                (animal != null ? animal.getNumberOfChildren() : GameCommunicat.NO_SELECTED_ANIMAL.toString()) + "," +
                (animal != null ? animal.getNumberOfDescendants() : GameCommunicat.NO_SELECTED_ANIMAL.toString()) + "," +
                (animal != null ? GameStatic.convertDaysToTime(animal.getAge()) : GameCommunicat.NO_SELECTED_ANIMAL.toString()) + "," +
                (animal != null ? GameStatic.convertDaysToDate(animal.getDayOfDeath()) : GameCommunicat.NO_SELECTED_ANIMAL.toString())
        );
    }

    public static void setConfigurationHeader(PrintWriter writer) {
        writer.println("Property,Value");
    }
}
