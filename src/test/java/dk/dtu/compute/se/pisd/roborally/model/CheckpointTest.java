package dk.dtu.compute.se.pisd.roborally.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CheckpointTest {

    @Test
    void checkpointOneReceived() {
        Board board = new Board(10,10);
        Player player = new Player(board, "blue", "Testplayer");

        assertEquals(0, player.getEndCheckpoint());

        player.setEndCheckpoint(1);

        assertEquals(1, player.getEndCheckpoint());
    }

    @Test
    void checkpointsOrder() {
        Board board = new Board(10,10);
        Player player = new Player(board, "green", "TestplayerTwo");

        player.setEndCheckpoint(1);

        assertEquals(1, player.getEndCheckpoint());

        player.setEndCheckpoint(2);

        assertEquals(2, player.getEndCheckpoint());

        player.setEndCheckpoint(3);

        assertEquals(3, player.getEndCheckpoint());
    }
}

