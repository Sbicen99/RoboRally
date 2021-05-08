package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class Gear extends FieldAction {

    /**
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return
     * @author Thamara Chellakooty, Camilla Boejden, Lauritz Pepke
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player != null) {
            player.setHeading(player.getHeading().next());
            return true;
        } else {
            return false;
        }
    }
}