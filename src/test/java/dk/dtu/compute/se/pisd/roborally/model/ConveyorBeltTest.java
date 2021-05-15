package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConveyorBeltTest {

    @Test
    void getType() {
        ConveyorBelt conveyorBelt = new ConveyorBelt(1, Heading.EAST);
        assertEquals(1, conveyorBelt.getType());
    }


    @Test
    void getHeading() {
        ConveyorBelt conveyorBelt = new ConveyorBelt(2, Heading.WEST);
        assertEquals(Heading.WEST, conveyorBelt.getHeading());
    }


    @Test
    void doAction() {
        ConveyorBelt conveyorBelt = new ConveyorBelt(1, Heading.WEST);
        Board board = new Board(10,10);
        GameController gameController = new GameController(board);
        Player player = new Player(board, "red", "TestPlayer");
        Space space1 = new Space(board, 3,3);
        player.setSpace(space1);
        Space space2 = new Space(board,2,3);
        conveyorBelt.doAction(gameController,space1);
        assertEquals(space2, player.getSpace());
    }
}