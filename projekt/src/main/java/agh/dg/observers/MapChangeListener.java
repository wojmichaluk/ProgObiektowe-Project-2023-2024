package agh.dg.observers;

import agh.dg.Statistics;
import agh.dg.models.abstracts.World;

public interface MapChangeListener {
    void mapChanged(World world, Statistics statistics);
}
