package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class Startpoint extends FieldAction{
    @Override
    public boolean doAction(GameController gameController, Space space) {
        return false;
    }
}