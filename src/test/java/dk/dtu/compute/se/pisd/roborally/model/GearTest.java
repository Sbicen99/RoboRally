package dk.dtu.compute.se.pisd.roborally.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GearTest {

    @Test
    void gearTurnedPlayerRight() {
        Board board = new Board(10,10);
        Player player = new Player(board, "blue", "TestPlayer");

        //player default heading is south
        assertEquals(Heading.SOUTH, player.getHeading());

        //turn player right so heading west
        player.setHeading(player.getHeading().next());

        assertNotEquals(Heading.SOUTH, player.getHeading());
        assertEquals(player.getHeading(), Heading.SOUTH.next());
        assertEquals(Heading.WEST, player.getHeading());

        //turn player right so heading north
        player.setHeading(player.getHeading().next());

        assertNotEquals(Heading.WEST, player.getHeading());
        assertEquals(player.getHeading(), Heading.WEST.next());
        assertEquals(Heading.NORTH, player.getHeading());

        //turn player right so heading east
        player.setHeading(player.getHeading().next());

        assertNotEquals(Heading.NORTH, player.getHeading());
        assertEquals(player.getHeading(), Heading.NORTH.next());
        assertEquals(Heading.EAST, player.getHeading());

        //turn player right so heading south
        player.setHeading(player.getHeading().next());

        assertNotEquals(Heading.EAST, player.getHeading());
        assertEquals(player.getHeading(), Heading.EAST.next());
        assertEquals(Heading.SOUTH, player.getHeading());
    }

    @Test
    void gearTurnedPlayerLeft() {
        Board board = new Board(10,10);
        Player player = new Player(board, "red", "TestPlayerTwo");

        //player default heading is south
        assertEquals(Heading.SOUTH, player.getHeading());

        //turn player left so heading east
        player.setHeading(player.getHeading().prev());

        assertNotEquals(Heading.SOUTH, player.getHeading());
        assertEquals(player.getHeading(), Heading.SOUTH.prev());
        assertEquals(Heading.EAST, player.getHeading());

        //turn player left so heading north
        player.setHeading(player.getHeading().prev());

        assertNotEquals(Heading.EAST, player.getHeading());
        assertEquals(player.getHeading(), Heading.EAST.prev());
        assertEquals(Heading.NORTH, player.getHeading());

        //turn player left so heading west
        player.setHeading(player.getHeading().prev());

        assertNotEquals(Heading.NORTH, player.getHeading());
        assertEquals(player.getHeading(), Heading.NORTH.prev());
        assertEquals(Heading.WEST, player.getHeading());

        //turn player left so heading south
        player.setHeading(player.getHeading().prev());

        assertNotEquals(Heading.WEST, player.getHeading());
        assertEquals(player.getHeading(), Heading.WEST.prev());
        assertEquals(Heading.SOUTH, player.getHeading());
    }
}