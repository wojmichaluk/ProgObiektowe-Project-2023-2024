package agh.dg.models;

import agh.dg.models.abstracts.WorldElement;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class Plant extends WorldElement {
    public Plant(Vector2d position) {
        super(position);
    }

    @Override
    public Node getGraphicalRepresentation(Pane pane) {
        Pane plantPane = new Pane();
        plantPane.setMaxSize(pane.getWidth(), pane.getHeight());
        plantPane.setStyle("-fx-background-color: green");
        return plantPane;
    }
}
