package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class Gear extends FieldAction {

    public final int direction;

    public Gear(int type) {
        this.direction = type;
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
        if (player != null & direction == 1 & gameController.board.getGears() != null) {
            player.turnRight(player);
            return true;

            // prev = left
        } else if (player != null & direction == 2 & gameController.board.getGears() != null) {
            player.turnLeft(player);
            return true;
        } else {
            return false;
        }
    }
}
