package agh.dg.helpers;

import agh.dg.models.abstracts.Animal;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class GameStatic {

    static final int YEAR_DAYS = 365;

    public static Color getAnimalColorFromEnergy(int maximumEnergy, Animal activeAnimal) {
        Color startColor = Color.RED;
        Color endColor = Color.YELLOW;
        double value = maximumEnergy == 0 ? 0.0 : (double) activeAnimal.getEnergy() / maximumEnergy;

        double red = startColor.getRed() + (endColor.getRed() - startColor.getRed()) * value;
        double green = startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * value;
        double blue = startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * value;

        return new Color(red, green, blue, 1.0);
    }

    public static String convertGenotypeToString(List<Integer> genotype) {
        StringBuilder sb = new StringBuilder("(");

        for (Integer integer : genotype) {
            sb.append(integer).append(" ");
        }

        return sb.deleteCharAt(sb.length() - 1).append(")").toString();
    }

    public static String convertGenotypeWithIndexToString(List<Integer> genotype, int index) {
        StringBuilder sb = new StringBuilder("( ");

        for (int i = 0; i < genotype.size(); i++) {
            if (i == index) {
                sb.append("[");
            }
            sb.append(genotype.get(i)).append(" ");
            if (i == index) {
                sb.deleteCharAt(sb.length() - 1).append("] ");
            }
        }

        return sb.deleteCharAt(sb.length() - 1).append(" )").toString();
    }

    public static void fillGenotypeWithIndex(TextFlow textFlow, List<Integer> genotype, int index, boolean isAnimalAlive) {
        textFlow.getChildren().clear();

        textFlow.getChildren().add(new Text("( "));

        for (int i = 0; i < genotype.size(); i++) {
            String textValue = genotype.get(i).toString();
            Text text = new Text(textValue);

            if (i == index) {
                text.setStyle("-fx-font-weight: bold; -fx-fill: " + (isAnimalAlive ? "#4CAF50" : "#e74c3c") + ";");
            }

            textFlow.getChildren().add(text);

            if (i < genotype.size() - 1) {
                textFlow.getChildren().add(new Text(" "));
            }
        }

        textFlow.getChildren().add(new Text(" )"));
    }

    public static String convertDaysToDate(int days) {
        int dateYear = days / YEAR_DAYS;
        int dateDay = days % YEAR_DAYS;

        return "Year: " + removeTrailingZeros(dateYear) + " Day: " + removeTrailingZeros(dateDay);
    }

    public static String convertDaysToTime(double days) {
        int timeYears = (int) days / YEAR_DAYS;
        double timeDays = (double) Math.round(days % YEAR_DAYS * 100) / 100;

        return removeTrailingZeros(timeYears) + " years " + removeTrailingZeros(timeDays) + " days";
    }

    public static String convertNumberToString(double number) {
        double parsedNumber = (double) Math.round(number * 1000) / 1000;
        return removeTrailingZeros(parsedNumber);
    }

    private static String removeTrailingZeros(double number) {
        return new BigDecimal(String.valueOf(number)).stripTrailingZeros().toPlainString();
    }
}
