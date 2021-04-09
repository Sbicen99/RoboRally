package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class Gear extends FieldAction {

    /**
     * @author Thamara Chellakooty
     * @author Camilla Boejden
     * @return
     */
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player != null){
            player.setHeading(player.getHeading().next());
            return true;
        } else {
            return false;
        }
    }
}