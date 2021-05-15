package dk.dtu.compute.se.pisd.roborally.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GearTest {

    Board board = new Board(10,10);
    Player player = new Player(board, "blue", "TestPlayer");

    @Test
    void gearTurnedPlayerRight() {

        //player default heading is south
        assertEquals(Heading.SOUTH, player.getHeading());

        //turn player right once so heading west
        player.turnRight(player);

        assertNotEquals(Heading.SOUTH, player.getHeading());
        assertEquals(Heading.SOUTH.next(), player.getHeading());

        //turn player right once so heading north
        player.turnRight(player);

        assertNotEquals(Heading.WEST, player.getHeading());
        assertEquals(Heading.WEST.next(), player.getHeading());

        //turn player right once so heading east
        player.turnRight(player);

        assertNotEquals(Heading.NORTH, player.getHeading());
        assertEquals(Heading.NORTH.next(), player.getHeading());

        //turn player right once so heading south
        player.turnRight(player);

        assertNotEquals(Heading.EAST, player.getHeading());
        assertEquals(Heading.EAST.next(), player.getHeading());
    }

   @Test
    void gearTurnedPlayerLeft() {

        //player default heading is south
        assertEquals(Heading.SOUTH, player.getHeading());

        //turn player left once so heading east
        player.turnLeft(player);

        assertNotEquals(Heading.SOUTH, player.getHeading());
        assertEquals(Heading.SOUTH.prev(), player.getHeading());

        //turn player left once so heading north
        player.turnLeft(player);

        assertNotEquals(Heading.EAST, player.getHeading());
        assertEquals(Heading.EAST.prev(), player.getHeading());

        //turn player left once so heading west
        player.turnLeft(player);

        assertNotEquals(Heading.NORTH, player.getHeading());
        assertEquals(Heading.NORTH.prev(), player.getHeading());

        //turn player left once so heading south
        player.turnLeft(player);

        assertNotEquals(Heading.WEST, player.getHeading());
        assertEquals(Heading.WEST.prev(), player.getHeading());
    }
}
