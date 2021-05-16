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

    Board board = new Board(10,10);
    ConveyorBelt conveyorBelt = new ConveyorBelt(1, Heading.WEST);

    @Test

    /**
    * testing that the players heading will be the same heading as conveyerbelt.
    * */
    void testPlayerHeading() {
        ConveyorBelt conveyorBelt = new ConveyorBelt(2, Heading.WEST);
        Heading heading =  conveyorBelt.getHeading();
        Player player1 = new Player(board,"blue", "test ");
        player1.setHeading(heading);
        assertEquals(player1.getHeading(), conveyorBelt.getHeading());
    }

}