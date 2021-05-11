package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class Gear extends FieldAction {

    public final String type;

    public Gear(String type) {
        this.type = type;
    }

    /**
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return
     * @author Thamara Chellakooty, Camilla Boejden, Lauritz Pepke
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player != null & type == "right") {
            player.setHeading(player.getHeading().next());
            return true;

        } else if  (player != null & type == "left") {
            player.setHeading(player.getHeading().prev());
            return true;
        }
        else {
            return false;
        }
    }
}