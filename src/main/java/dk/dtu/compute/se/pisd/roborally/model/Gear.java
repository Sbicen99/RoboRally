package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class Gear extends FieldAction {

    public final  int type;

    public Gear(int type) {
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
        // next = right
        if (player != null & type == 1  & gameController.board.getGears() != null) {
            player.setHeading(player.getHeading().next());
            return true;

        // prev = left
        } else if  (player != null & type == 2 & gameController.board.getGears() != null) {
            player.setHeading(player.getHeading().prev());
            return true;
        }
        else {
            return false;
        }
    }
}
