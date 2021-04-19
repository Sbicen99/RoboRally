package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

/**
 * Gameboard element that can push the player one or two spaces forward.
 * @author Thamara Chellakooty
 */
public class ConveyorBelt extends FieldAction{

private Heading heading;
public final int type;


    public ConveyorBelt(int type) {
        this.type = type;
    }
    public Heading getHeading(){
        return heading;
    }
    public void setHeading ( Heading heading) {
        this.heading = heading;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player != null & type == 1 ){
            player.moveForward(player);
            return true;
        }
        else if (player != null & type == 2 ) {
            player.moveForward(player);
            player.moveForward(player);
        }
        return true;
    }

    public int getType(){
        return this.type;
    }
}
