package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class Gear extends FieldAction {


    /**
     * @author Thamara Chellakooty, Camilla Boejden, Lauritz Pepke
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return
     */
    @Override
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