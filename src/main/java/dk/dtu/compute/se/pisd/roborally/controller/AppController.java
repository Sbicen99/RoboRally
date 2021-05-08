/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.dal.GameInDB;
import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    final private RoboRally roboRally;

    private GameController gameController;

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }


    /**
     * Creating a new game where player choose how many player should be on the board.
     */

    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }
            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.
            Board board = createBoard();
            gameController = new GameController(board);
            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i % board.width + 1, 1));
            }
            board.setCurrentPlayer(board.getPlayer(0));
            gameController.startProgrammingPhase();
            RepositoryAccess.getRepository().createGameInDB(board);
            roboRally.createBoardView(gameController);
        }
    }


    /**
     * @return Returns board where different board elements are included on specific fields.
     * @author Camilla Boejden, Thamara Chellakooty, Sercan Bicen & Lauritz Pepke
     */
    private Board createBoard() {
        Board board = LoadBoard.loadBoard("easyboard");
        return board;


        /*Board board = new Board(10,10);
        Gear gear = new Gear();
        Checkpoint firstCheckpoint = new Checkpoint(1);
        Checkpoint secondCheckpoint = new Checkpoint(2);
        ConveyorBelt conveyorBelt1 = new ConveyorBelt(1, Heading.SOUTH);
        ConveyorBelt conveyorBelt2 = new ConveyorBelt(2, Heading.NORTH);
        ConveyorBelt conveyorBelt3 = new ConveyorBelt(1, Heading.EAST);
        ConveyorBelt conveyorBelt4 = new ConveyorBelt(2, Heading.WEST);

        // adding checkpoints to the board.
        board.getSpace(6,7).getActions().add(firstCheckpoint);
        board.getSpace(4, 5).getActions().add(secondCheckpoint);


        board.getSpace(4,9).getActions().add(conveyorBelt1);
        board.getSpace(5,0).getActions().add(conveyorBelt2);
        board.getSpace(0,5).getActions().add(conveyorBelt3);
        board.getSpace(9,4).getActions().add(conveyorBelt4);


        board.getSpace(5,3).getActions().add(gear);
        board.getSpace(6,6).getActions().add(gear);

        // adding walls to the board.
        board.getSpace(2,3).getWalls().add(Heading.EAST);
        board.getSpace(2,7).getWalls().add(Heading.SOUTH);
        board.getSpace(8,3).getWalls().add(Heading.NORTH);
        board.getSpace(8,7).getWalls().add(Heading.EAST);
        return board;*/
    }


    /**
     * Automatically saves games in the database when player start game.
     */
    public void saveGame() {
        // XXX needs to be implemented eventually
        if (gameController != null) {
            Board board = gameController.board;
            if (board.getGameId() != null) {
                RepositoryAccess.getRepository().updateGameInDB(board);
            }
        }
    }


    public void loadGameFromFile() {
        // TODO: need to be implemented.
    }


    /**
     * This method load games from the database. Returns messages to the player when it is not possible to retrieve games from the database
     */
    public void loadGame() {

        // XXX needs to be implemented eventually

        List<GameInDB> games = RepositoryAccess.getRepository().getGames();
        if (!games.isEmpty()) {
            ChoiceDialog<GameInDB> dialog = new ChoiceDialog<>(games.get(games.size() - 1), games);
            dialog.setTitle("Select game");
            dialog.setHeaderText("Select a game number");
            Optional<GameInDB> result = dialog.showAndWait();

            if (result.isPresent()) {
                Board board = RepositoryAccess.getRepository().loadGameFromDB(result.get().id);
                if (board != null) {
                    gameController = new GameController(board);
                    roboRally.createBoardView(gameController);
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Couldn't load the game");
                    alert.setHeaderText("There was a problem");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No games avaible!");
            alert.setHeaderText("There are no games in the db!");
            alert.showAndWait();
        }
    }


    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {
            /*Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Do you want to save the game?");
            alert.setContentText("Choose your option.");

            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent() || result.get() != ButtonType.OK) {
                System.exit(-1);
            } else {
                saveGame();
            }*/

            // here we save the game (without asking the user).
            saveGame();

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }


    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

}