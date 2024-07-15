package agh.dg.models.abstracts;

import agh.dg.models.Vector2d;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.UUID;

abstract public class WorldElement {

    protected final UUID id = UUID.randomUUID();

    protected Vector2d position;

    public WorldElement(Vector2d position) {
        this.position = position;
    }

    public UUID getId() {
        return id;
    }

    public Vector2d getPosition() {
        return position;
    }

    abstract public Node getGraphicalRepresentation(Pane pane);
}
