package agh.dg.presenters;

import agh.dg.ExtendedThread;
import agh.dg.Simulation;
import agh.dg.exceptions.InvalidGameParamsException;
import agh.dg.helpers.CsvFileHandler;
import agh.dg.models.abstracts.World;
import agh.dg.records.Boundary;
import agh.dg.records.WorldConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MenuPresenter extends BasePresenter {

    @FXML
    private TextField formMapHeight;

    @FXML
    private TextField formMapWidth;

    @FXML
    private TextField formPlantStart;

    @FXML
    private TextField formPlantDaily;

    @FXML
    private TextField formPlantEnergy;

    @FXML
    private TextField formAnimalStart;

    @FXML
    private TextField formAnimalStartEnergy;

    @FXML
    private TextField formAnimalEnergyReproductionDepletion;

    @FXML
    private TextField formAnimalEnergyDailyDepletion;

    @FXML
    private TextField formAnimalEnergyToReproduce;

    @FXML
    private TextField formAnimalMutationMinimum;

    @FXML
    private TextField formAnimalMutationMaximum;

    @FXML
    private TextField formAnimalGenotypeLength;

    @FXML
    private CheckBox formVariantMap;

    @FXML
    private CheckBox formVariantAnimal;
    
    @FXML
    private CheckBox formExportStatistics;

    @FXML
    private Button createGame;

    @FXML
    private Button exportConfiguration;

    @FXML
    private Button importConfiguration;

    @FXML
    private void initialize() throws Exception {
        createGame.setOnAction(event -> {
            try {
                WorldConfig worldConfig = getWorldConfigFromParams();
                validateStartParameters(worldConfig);
                createNewGame(worldConfig);
            } catch (Exception e) {
                showAlert("Error", "Invalid data in game parameters", "Cannot start the Darwin World", Alert.AlertType.ERROR);
            }
        });

        exportConfiguration.setOnAction(event -> {
            try {
                exportConfigurationFile();
            } catch (Exception e) {
                showAlert("Error", "Error on saving configuration", "Cannot export the configuration file", Alert.AlertType.ERROR);
            }
        });

        importConfiguration.setOnAction(event -> {
            try {
                importConfigurationFile();
            } catch (Exception e) {
                showAlert("Error", "Error on importing configuration", "Cannot load the configuration file", Alert.AlertType.ERROR);
            }
        });
    }

    private void createNewGame(WorldConfig worldConfig) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();

        loader.setLocation(getClass().getClassLoader().getResource("game.fxml"));
        GridPane view = loader.load();
        GamePresenter presenter = loader.getController();
        presenter.setWorldConfig(worldConfig);
        presenter.setShouldExportStatistics(formExportStatistics.isSelected());
        presenter.prepare();

        Simulation simulation = new Simulation(worldConfig, presenter);
        ExtendedThread thread = new ExtendedThread(simulation);
        thread.start();

        stage.setOnCloseRequest(event -> {
            simulation.stopRunning();
        });

        presenter.setThread(thread);

        stage.setTitle("Darwin World - World " + simulation.getWorld().getId());
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png")));
        stage.getIcons().add(icon);
        stage.setScene(new Scene(view));
        stage.setMaximized(true);
        stage.show();
    }

    private void exportConfigurationFile() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File resourcesDirectory = new File("src/main/resources/configs/");
        fileChooser.setInitialDirectory(resourcesDirectory);

        Stage stage = (Stage) exportConfiguration.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            PrintWriter writer = new PrintWriter(new FileWriter(file));

            CsvFileHandler.setConfigurationHeader(writer);
            writer.println("MapHeight," + formMapHeight.getText());
            writer.println("MapWidth," + formMapWidth.getText());
            writer.println("PlantStart," + formPlantStart.getText());
            writer.println("PlantDaily," + formPlantDaily.getText());
            writer.println("PlantEnergy," + formPlantEnergy.getText());
            writer.println("AnimalStart," + formAnimalStart.getText());
            writer.println("AnimalStartEnergy," + formAnimalStartEnergy.getText());
            writer.println("AnimalEnergyReproductionDepletion," + formAnimalEnergyReproductionDepletion.getText());
            writer.println("AnimalEnergyDailyDepletion," + formAnimalEnergyDailyDepletion.getText());
            writer.println("AnimalEnergyToReproduce," + formAnimalEnergyToReproduce.getText());
            writer.println("AnimalMutationMinimum," + formAnimalMutationMinimum.getText());
            writer.println("AnimalMutationMaximum," + formAnimalMutationMaximum.getText());
            writer.println("AnimalGenotypeLength," + formAnimalGenotypeLength.getText());
            writer.println("VariantMap," + formVariantMap.isSelected());
            writer.println("VariantAnimal," + formVariantAnimal.isSelected());

            writer.close();
        }
    }

    private void importConfigurationFile() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File resourcesDirectory = new File("src/main/resources/configs/");
        fileChooser.setInitialDirectory(resourcesDirectory);

        Stage stage = (Stage) importConfiguration.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        HashMap<String, String> formValues = new HashMap<>();

        if (file != null) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(",");
                if (args.length == 2) {
                    String key = args[0].trim();
                    String value = args[1].trim();

                    formValues.put("form" + key, value);
                }
            }

            fillFormWorldConfigParams(formValues);
        }

    }

    private void validateStartParameters(WorldConfig worldConfig) throws Exception {
        if (worldConfig.mapWidth() <= 0 || worldConfig.mapHeight() <= 0 || worldConfig.mapWidth() > 200 || worldConfig.mapHeight() > 200) {
            throw new InvalidGameParamsException();
        }

        if (worldConfig.plantStart() < 0 || worldConfig.plantDaily() < 0 || worldConfig.plantEnergy() < 0) {
            throw new InvalidGameParamsException();
        }

        if (worldConfig.animalStart() <= 0 || worldConfig.animalStartEnergy() <= 0 || worldConfig.animalEnergyToReproduce() <= 0) {
            throw new InvalidGameParamsException();
        }

        if (worldConfig.animalEnergyReproductionDepletion() <= 0 || worldConfig.animalEnergyDailyDepletion() <= 0) {
            throw new InvalidGameParamsException();
        }

        if (worldConfig.animalMutationMinimum() < 0 || worldConfig.animalMutationMaximum() <= 0 || worldConfig.animalGenotypeLength() <= 0) {
            throw new InvalidGameParamsException();
        }

        if (worldConfig.animalMutationMinimum() > worldConfig.animalMutationMaximum()) {
            throw new InvalidGameParamsException();
        }

        if (worldConfig.animalMutationMaximum() > worldConfig.animalGenotypeLength()) {
            throw new InvalidGameParamsException();
        }
    }

    private WorldConfig getWorldConfigFromParams() {
        return new WorldConfig(
                Integer.parseInt(formMapHeight.getText()),
                Integer.parseInt(formMapWidth.getText()),
                Integer.parseInt(formPlantStart.getText()),
                Integer.parseInt(formPlantDaily.getText()),
                Integer.parseInt(formPlantEnergy.getText()),
                Integer.parseInt(formAnimalStart.getText()),
                Integer.parseInt(formAnimalStartEnergy.getText()),
                Integer.parseInt(formAnimalEnergyReproductionDepletion.getText()),
                Integer.parseInt(formAnimalEnergyDailyDepletion.getText()),
                Integer.parseInt(formAnimalEnergyToReproduce.getText()),
                Integer.parseInt(formAnimalMutationMinimum.getText()),
                Integer.parseInt(formAnimalMutationMaximum.getText()),
                Integer.parseInt(formAnimalGenotypeLength.getText()),
                formVariantMap.isSelected(),
                formVariantAnimal.isSelected()
        );
    }


    private void fillFormWorldConfigParams(HashMap<String, String> formValues) throws InvalidKeyException {
        for (Map.Entry<String, String> value : formValues.entrySet()) {
            switch (value.getKey()) {
                case "Property":
                    break;
                case "formMapHeight":
                    formMapHeight.setText(value.getValue());
                    break;
                case "formMapWidth":
                    formMapWidth.setText(value.getValue());
                    break;
                case "formPlantStart":
                    formPlantStart.setText(value.getValue());
                    break;
                case "formPlantDaily":
                    formPlantDaily.setText(value.getValue());
                    break;
                case "formPlantEnergy":
                    formPlantEnergy.setText(value.getValue());
                    break;
                case "formAnimalStart":
                    formAnimalStart.setText(value.getValue());
                    break;
                case "formAnimalStartEnergy":
                    formAnimalStartEnergy.setText(value.getValue());
                    break;
                case "formAnimalEnergyReproductionDepletion":
                    formAnimalEnergyReproductionDepletion.setText(value.getValue());
                    break;
                case "formAnimalEnergyDailyDepletion":
                    formAnimalEnergyDailyDepletion.setText(value.getValue());
                    break;
                case "formAnimalEnergyToReproduce":
                    formAnimalEnergyToReproduce.setText(value.getValue());
                    break;
                case "formAnimalMutationMinimum":
                    formAnimalMutationMinimum.setText(value.getValue());
                    break;
                case "formAnimalMutationMaximum":
                    formAnimalMutationMaximum.setText(value.getValue());
                    break;
                case "formAnimalGenotypeLength":
                    formAnimalGenotypeLength.setText(value.getValue());
                    break;
                case "formVariantMap":
                    formVariantMap.setSelected(Boolean.parseBoolean(value.getValue()));
                    break;
                case "formVariantAnimal":
                    formVariantAnimal.setSelected(Boolean.parseBoolean(value.getValue()));
                    break;
                default:
                    throw new InvalidKeyException();
            }
        }
    }
}
