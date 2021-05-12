package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckpointTest {

    @Test
    void checkpointOneReceived() {
        Board board = new Board(10,10);
        Player player = new Player(board, "blue", "Testplayer");

        assertEquals(0, player.getEndCheckpoint());

        player.setEndCheckpoint(1);

        assertEquals(1, player.getEndCheckpoint());

    }
}