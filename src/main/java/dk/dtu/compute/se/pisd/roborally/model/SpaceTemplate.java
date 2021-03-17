package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;

import java.util.ArrayList;
import java.util.List;

public class SpaceTemplate {

    public int x;
    public int y;
    public List<Heading> walls = new ArrayList<>();
    public List<FieldAction> actions = new ArrayList<>();

}
