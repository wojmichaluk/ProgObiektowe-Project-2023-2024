package agh.dg.presenters;

import agh.dg.ExtendedThread;
import agh.dg.Statistics;
import agh.dg.enums.GameCommunicat;
import agh.dg.helpers.CsvFileHandler;
import agh.dg.models.Plant;
import agh.dg.models.Vector2d;
import agh.dg.models.WorldElementBox;
import agh.dg.models.abstracts.Animal;
import agh.dg.models.abstracts.World;
import agh.dg.observers.MapChangeListener;
import agh.dg.helpers.GameStatic;
import agh.dg.records.WorldConfig;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class GamePresenter extends BasePresenter implements MapChangeListener {

    @FXML
    private Button startStopGame;

    // General Statistics fields
    @FXML
    private Label generalStatisticsDays;
    @FXML
    private Label generalStatisticsAnimals;
    @FXML
    private Label generalStatisticsPlants;
    @FXML
    private Label generalStatisticsFreeFields;
    @FXML
    private Label generalStatisticsBestGenotypes1;
    @FXML
    private Label generalStatisticsBestGenotypes2;
    @FXML
    private Label generalStatisticsBestGenotypes3;
    @FXML
    private Label generalStatisticsAverageEnergy;
    @FXML
    private Label generalStatisticsAverageAge;
    @FXML
    private Label generalStatisticsAverageChildren;

    // Animal Statistics fields
    @FXML
    private TextFlow animalStatisticsGenotype;
    @FXML
    private Label animalStatisticsEnergy;
    @FXML
    private Label animalStatisticsPlants;
    @FXML
    private Label animalStatisticsChildren;
    @FXML
    private Label animalStatisticsDescendants;
    @FXML
    private Label animalStatisticsLifeDays;
    @FXML
    private Label animalStatisticsDeathDay;

    // Additional info display
    @FXML
    private Button showAnimalsWithMostPopularGenotypes;
    @FXML
    private Button showPreferredFields;

    private ExtendedThread thread;
    private WorldConfig worldConfig;
    private final Map<Vector2d, Pane> fields = new HashMap<>();
    private Animal selectedAnimal = null;
    private List<Animal> animalsWithMostPopularGenotypes = new ArrayList<>();
    private Set<Vector2d> mostPreferredPositions = new HashSet<>();

    private boolean shouldExportStatistics;
    private boolean showExportStatisticsAlert = true;

    @FXML
    private GridPane generalStatisticsPane;

    @FXML
    private GridPane boardPane;

    private boolean isShowedAnimalsWithMostPopularGenotypes = false;
    private boolean isShowedPreferredFields = false;

    @FXML
    @Deprecated(since = "1.2")
    private void initialize() {
        // Hide animal statistics
        for (Node node : getElementsWithClass("animal-statistic")) {
            node.setVisible(false);
        }

        generalStatisticsPane.setHgap(0);
        generalStatisticsPane.setVgap(0);

        startStopGame.setOnAction(event -> {
            if (thread.isAlive()) {
                startStopGame.getStyleClass().remove("button-red");
                startStopGame.getStyleClass().add("button");
                startStopGame.setText("Start simulation");
                thread.stop();
            } else {
                thread = thread.restart();

                startStopGame.getStyleClass().remove("button");
                startStopGame.getStyleClass().add("button-red");
                startStopGame.setText("Stop simulation");

                showAnimalsWithMostPopularGenotypes.getStyleClass().remove("button-red");
                showAnimalsWithMostPopularGenotypes.getStyleClass().add("button");

                showPreferredFields.getStyleClass().remove("button-red");
                showPreferredFields.getStyleClass().add("button");
            }
        });

        showAnimalsWithMostPopularGenotypes.setOnAction(event -> {
            showAnimalsWithMostPopularGenotypes.getStyleClass().remove("button-red");
            showAnimalsWithMostPopularGenotypes.getStyleClass().add("button");
            if (!thread.isAlive()) {
                if (!isShowedAnimalsWithMostPopularGenotypes) {
                    this.displayMostPopularGenotypes();
                    showAnimalsWithMostPopularGenotypes.getStyleClass().remove("button");
                    showAnimalsWithMostPopularGenotypes.getStyleClass().add("button-red");
                } else {
                    this.hideMostPopularGenotypes();
                }

                isShowedAnimalsWithMostPopularGenotypes = !isShowedAnimalsWithMostPopularGenotypes;
            }
        });

        showPreferredFields.setOnAction(event -> {
            showPreferredFields.getStyleClass().remove("button-red");
            showPreferredFields.getStyleClass().add("button");
            if (!thread.isAlive()) {
                if (!isShowedPreferredFields) {
                    this.displayPreferredFields();
                    showPreferredFields.getStyleClass().remove("button");
                    showPreferredFields.getStyleClass().add("button-red");
                } else {
                    this.hidePreferredFields();
                }

                isShowedPreferredFields = !isShowedPreferredFields;
            }
        });
    }

    private void displayMostPopularGenotypes() {
        for (Animal animal : animalsWithMostPopularGenotypes) {
            Pane pane = fields.get(animal.getPosition());
            Pane displayPane = new Pane();
            displayPane.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
            displayPane.getStyleClass().add("toOptionalDeleteGenotype");
            pane.getChildren().add(displayPane);
            displayPane.toBack();
        }
    }

    private void displayPreferredFields() {
        for (Vector2d position : mostPreferredPositions) {
            Pane pane = fields.get(position);
            Pane displayPane = new Pane();
            displayPane.setBorder(new Border(new javafx.scene.layout.BorderStroke(Color.BLACK,
                    javafx.scene.layout.BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    new BorderWidths(pane.getWidth() / 12.0))));
            displayPane.getStyleClass().add("toOptionalDeleteField");
            pane.getChildren().add(displayPane);
        }
    }

    private void hideMostPopularGenotypes() {
        for (Animal animal : animalsWithMostPopularGenotypes) {
            Pane pane = fields.get(animal.getPosition());

            pane.getChildren().removeIf(child -> child.getStyleClass().contains("toOptionalDeleteGenotype"));
        }
    }

    private void hidePreferredFields() {
        for (Vector2d position : mostPreferredPositions) {
            Pane pane = fields.get(position);

            pane.getChildren().removeIf(child -> child.getStyleClass().contains("toOptionalDeleteField"));
        }
    }

    @Override
    public void mapChanged(World world, Statistics statistics) {
        Platform.runLater(() -> {
            this.animalsWithMostPopularGenotypes =
                    this.findAnimalsWithMostPopularGenotypes(statistics.getMostPopularGenotypes(), world);
            this.mostPreferredPositions = world.getPreferredPositions();
            this.generate(world);
            this.displayGeneralStatistics(statistics);
            this.displayAnimalStatistics();

            if (shouldExportStatistics) {
                exportCsvStatistics(world, statistics);
            }
        });
    }

    private List<Animal> findAnimalsWithMostPopularGenotypes(List<List<Integer>> genotypes, World world) {
        Set<List<Integer>> setGenotypes = new HashSet<>(genotypes);
        return world.getAllAnimals().isEmpty() ? new ArrayList<>() : world.getAllAnimals().stream()
                .filter(animal -> setGenotypes.contains(animal.getGenotype()))
                .filter(animal -> animal == world.dominatingAnimals(world.getAnimals().get(animal.getPosition()), 1).get(0))
                .toList();
    }

    public void setWorldConfig(WorldConfig worldConfig) {
        this.worldConfig = worldConfig;
    }

    public void setShouldExportStatistics(boolean shouldExportStatistics) {
        this.shouldExportStatistics = shouldExportStatistics;
    }

    public void prepare() {
        for (int row = 0; row < worldConfig.mapHeight(); row++) {
            for (int col = 0; col < worldConfig.mapWidth(); col++) {
                Pane pane = new StackPane();
                pane.setPadding(new Insets(0));
                pane.setMinSize((double) 600 / Math.max(worldConfig.mapWidth(), worldConfig.mapHeight()), (double) 600 / Math.max(worldConfig.mapWidth(), worldConfig.mapHeight()));
                pane.setMaxSize((double) 600 / Math.max(worldConfig.mapWidth(), worldConfig.mapHeight()), (double) 600 / Math.max(worldConfig.mapWidth(), worldConfig.mapHeight()));

                fields.put(new Vector2d(col, row), pane);

                boardPane.add(pane, col, row);
            }
        }
    }

    public void generate(World world) {
        // Clear full map to recreate animals
        for (int row = 0; row < worldConfig.mapHeight(); row++) {
            for (int col = 0; col < worldConfig.mapWidth(); col++) {
                Pane pane = fields.get(new Vector2d(col, row));
                pane.setStyle("-fx-background-color: rgba(144, 238, 144, 0.2)");
                pane.getChildren().clear();
            }
        }

        // Put plants
        for (Plant plant : world.getPlants().values()) {
            Pane pane = fields.get(plant.getPosition());
            WorldElementBox plantBox = new WorldElementBox(plant, pane, false);
            Pane plantPane = plantBox.getCellDisplay();
            pane.getChildren().add(plantPane);
        }

        // Find animal with most energy (if exists)
        Animal mostEnergeticAnimal = world.getAllAnimals().isEmpty() ? null :
                world.dominatingAnimals(world.getAllAnimals(), 1).get(0);

        // Put animals
        for (List<Animal> animalsArray : world.getAnimals().values()) {
            Animal animalToShow = world.dominatingAnimals(animalsArray, 1).get(0);
            Pane pane = fields.get(animalToShow.getPosition());
            WorldElementBox animalBox = new WorldElementBox(animalToShow, pane, true);
            Pane animalPane = animalBox.getCellDisplay();
            Circle circle = (Circle) animalPane.getChildren().get(0);
            circle.setFill(GameStatic.getAnimalColorFromEnergy(mostEnergeticAnimal.getEnergy(), animalToShow));

            if (animalToShow.equals(selectedAnimal)) {
                circle.setFill(Color.DEEPSKYBLUE);
            }

            pane.getChildren().add(animalPane);
        }

        // Add event to animals
        for (Node node : getElementsWithClass("animal-pane")) {
            node.setOnMouseClicked(nextEvent -> {
                selectAnimal(world, world.getAnimalById(UUID.fromString(node.getId())));
            });
        }
    }

    public void displayGeneralStatistics(Statistics statistics) {
        generalStatisticsDays.setText(GameStatic.convertDaysToDate(statistics.getDayCount()));
        generalStatisticsAnimals.setText(GameStatic.convertNumberToString(statistics.getAnimalCount()));
        generalStatisticsPlants.setText(GameStatic.convertNumberToString(statistics.getPlantsCount()));
        generalStatisticsFreeFields.setText(GameStatic.convertNumberToString(statistics.getFreeFieldsCount()));
        generalStatisticsAverageEnergy.setText(GameStatic.convertNumberToString(statistics.getAverageAnimalEnergy()));
        generalStatisticsAverageAge.setText(GameStatic.convertDaysToTime(statistics.getLifeExpectancy()));
        generalStatisticsAverageChildren.setText(GameStatic.convertNumberToString(statistics.getAverageChildCount()));

        // Set most popular genotypes
        generalStatisticsBestGenotypes1.setText(
                !statistics.getMostPopularGenotypes().isEmpty() ? GameStatic.convertGenotypeToString(statistics.getMostPopularGenotypes().get(0)) : GameCommunicat.NO_GENOTYPE.toString());
        generalStatisticsBestGenotypes2.setText(
                statistics.getMostPopularGenotypes().size() > 1 ? GameStatic.convertGenotypeToString(statistics.getMostPopularGenotypes().get(1)) : GameCommunicat.NO_GENOTYPE.toString());
        generalStatisticsBestGenotypes3.setText(
                statistics.getMostPopularGenotypes().size() > 2 ? GameStatic.convertGenotypeToString(statistics.getMostPopularGenotypes().get(2)) : GameCommunicat.NO_GENOTYPE.toString());
    }

    public void selectAnimal(World world, Animal animal) {
        if (selectedAnimal != null) {
            Pane lastAnimalPane = (Pane) rootPane.lookup("#" + selectedAnimal.getId());
            Circle lastCircle = (Circle) lastAnimalPane.getChildren().get(0);
            lastCircle.setFill(GameStatic.getAnimalColorFromEnergy(selectedAnimal.getEnergy(), selectedAnimal));
        }

        if (animal.equals(selectedAnimal)) {
            selectedAnimal = null;
        } else {
            selectedAnimal = animal;
            Pane animalPane = (Pane) rootPane.lookup("#" + animal.getId());
            Circle circle = (Circle) animalPane.getChildren().get(0);
            circle.setFill(Color.DEEPSKYBLUE);
        }
        displayAnimalStatistics();
    }

    public void displayAnimalStatistics() {
        if (selectedAnimal != null) {
            for (Node node : getElementsWithClass("animal-statistic")) {
                node.setVisible(true);
            }
            rootPane.lookup(".animal-statistic-header").setVisible(true);

            animalStatisticsEnergy.setText(GameStatic.convertNumberToString(selectedAnimal.getEnergy()));
            animalStatisticsPlants.setText(GameStatic.convertNumberToString(selectedAnimal.getPlantsEaten()));
            animalStatisticsChildren.setText(GameStatic.convertNumberToString(selectedAnimal.getNumberOfChildren()));
            animalStatisticsDescendants.setText(GameStatic.convertNumberToString(selectedAnimal.getNumberOfDescendants()));
            animalStatisticsLifeDays.setText(GameStatic.convertDaysToTime(selectedAnimal.getAge()));
            animalStatisticsDeathDay.setText(GameStatic.convertDaysToDate(selectedAnimal.getDayOfDeath()));

            GameStatic.fillGenotypeWithIndex(
                    animalStatisticsGenotype, selectedAnimal.getGenotype(), selectedAnimal.getGenotypeIndex(), selectedAnimal.getDayOfDeath() == 0
            );
        } else {
            for (Node node : getElementsWithClass("animal-statistic")) {
                node.setVisible(false);
            }
            rootPane.lookup(".animal-statistic-header").setVisible(false);
        }
    }

    public void exportCsvStatistics(World world, Statistics statistics) {
        String projectPath = System.getProperty("user.dir");
        String filename = "World_Statistics_" + world.getId() + ".csv";
        String filePath = projectPath + "/src/main/resources/statistics/" + filename;

        File csvFile = new File(filePath);
        boolean fileExist = csvFile.exists();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            if (!fileExist) {
                CsvFileHandler.setStatisticsHeader(writer);
            }

            CsvFileHandler.fillStatisticsDay(writer, world.getId(), statistics, selectedAnimal);

        } catch (Exception e) {
            if (showExportStatisticsAlert) {
                showExportStatisticsAlert = false;
                showAlert("Error", "Error on saving statistics", "Cannot export file with world and animal statistics", Alert.AlertType.ERROR);
            }
        }
    }

    public void setThread(ExtendedThread thread) {
        this.thread = thread;
    }
}
