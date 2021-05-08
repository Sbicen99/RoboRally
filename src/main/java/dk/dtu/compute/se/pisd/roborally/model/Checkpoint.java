package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

/**
 * @author Sercan Bicen
 * Class that extends from FieldAction to handle checkoints on the board.
 */

public class Checkpoint extends FieldAction {

    public final int checkpointnumber;

    public Checkpoint(int checkpointnumber) {
        this.checkpointnumber = checkpointnumber;
    }

    /**
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     */

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player != null) {
            player.setEndCheckpoint(this.checkpointnumber);
            if (player.getEndCheckpoint() >= gameController.board.getCheckpoints().size()) {
                gameController.gameWins(player);
            }
        }
        return true;
    }
}
