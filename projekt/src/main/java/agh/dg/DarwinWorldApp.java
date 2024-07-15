package agh.dg;

import agh.dg.presenters.MenuPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Objects;

public class DarwinWorldApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("menu.fxml"));
        GridPane viewRoot = loader.load();
        MenuPresenter presenter = loader.getController();

        stage.setTitle("Darwin World - Menu");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png")));
        stage.getIcons().add(icon);
        stage.setMaximized(true);
        stage.setScene(new Scene(viewRoot));
        stage.show();
    }
}
