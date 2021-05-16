package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.jetbrains.annotations.NotNull;

/**
 * Gameboard element that can push the player one or two spaces forward.
 *
 * @author Thamara Chellakooty, Camilla Boejden.
 */
public class ConveyorBelt extends FieldAction {

    private Heading heading;
    public final int type;

    public ConveyorBelt(int type, @NotNull Heading heading) {
        this.type = type;
        this.heading = heading;
    }

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        player.setHeading(heading);
        if (player != null & type == 1 & gameController.board.getConveyerBelt() != null) {
            player.moveForward(player);
            return true;
        } else if (player != null & type == 2 & gameController.board.getConveyerBelt() != null) {
            player.moveForward(player);
            player.moveForward(player);
            return true;
        } else if (player != null & type == 3 & gameController.board.getConveyerBelt() != null) {
            player.turnLeft(player);
            player.moveForward(player);
            return true;
        } else if (player != null & type == 4 & gameController.board.getConveyerBelt() != null) {
            player.turnRight(player);
            player.moveForward(player);
            player.moveForward(player);
    }
        return true;
    }

    public int getType() {
        return this.type;
    }
}
