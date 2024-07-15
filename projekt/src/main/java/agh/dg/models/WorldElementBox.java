package agh.dg.models;

import agh.dg.models.abstracts.WorldElement;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class WorldElementBox {
    private final Pane cellDisplay = new StackPane();

    public WorldElementBox(WorldElement worldElement, Pane node, boolean isAnimal) {
        Node worldElementNode = worldElement.getGraphicalRepresentation(node);
        Label label = new Label(worldElement.getPosition().toString());
        label.setFont(new Font(node.getWidth() / 4.5));
        cellDisplay.setId(worldElement.getId().toString());
        if (isAnimal) {
            cellDisplay.getStyleClass().add("animal-pane");
        }
        cellDisplay.getChildren().add(worldElementNode);
        cellDisplay.getChildren().add(label);
    }

    public Pane getCellDisplay() {
        return cellDisplay;
    }
}
