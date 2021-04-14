package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class Checkpoint extends FieldAction {

    public final int checkpointnumber;

    public Checkpoint(int checkpointnumber) {
        this.checkpointnumber = checkpointnumber;
    }


    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player != null) {
            player.setEndCheckpoint(this.checkpointnumber);
            if (player.getEndCheckpoint() >= gameController.board.getCheckpoints().size()) {
                //TODO: handle a win here.
                gameController.gameWins(player);
            }
        }
        return true;
    }
}
