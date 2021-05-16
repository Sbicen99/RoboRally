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
    final private List<String> Player_Board_Option = Arrays.asList("Easy board", "Medium board");
    final private RoboRally roboRally;
    private GameController gameController;
    String boardname;
    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }
    /**
     * This method creates a table with 12 * 12..
     *
     * @author Najib s181663, Camilla Boejden, Thamara Chellakooty, Sercan Bicen.
     */
    public void newGameMediumBoard() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (gameController != null) {
                if (!stopGame()) {
                    return;
                }
            }
            Board board = createMediumBoard();
            gameController = new GameController(board);
            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(13 % board.width, (i * 2)));
                player.setStartpointX(13 % board.width);
                player.setStartpointY(i * 2);
            }
            board.setCurrentPlayer(board.getPlayer(0));
            gameController.startProgrammingPhase();
            roboRally.createBoardView(gameController);
        }
    }
    /**
     * This method of choosing between large or small board and starting the game.
     *
     * @author Najib s181663, Camilla Boejden, Thamara Chellakooty.
     */
    public void newGame() {
        ChoiceDialog<String> dialogg = new ChoiceDialog<>(Player_Board_Option.get(0), Player_Board_Option);
        dialogg.setTitle("Board choice");
        dialogg.setHeaderText("Select board");
        Optional<String> results = dialogg.showAndWait();
        if (results.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }
            String value = dialogg.getSelectedItem();
            if (value.equals("Medium board")) {
                newGameMediumBoard();
            } else if (value.equals("Easy board")) {
                newGameEasyBoard();
            }
        }
    }
    /**
     * This method creates a table with 8*8.
     *
     * @author Najib s181663, Camilla Boejden, Thamara Chellakooty, Sercan Bicen.
     */
    public void newGameEasyBoard() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (gameController != null) {
                if (!stopGame()) {
                    return;
                }
            }
            Board board = createEasyBoard();
            gameController = new GameController(board);
            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(11 % board.width, i));
                player.setStartpointX(11 % board.width);
                player.setStartpointY(i);
            }
            board.setCurrentPlayer(board.getPlayer(0));
            gameController.startProgrammingPhase();
            roboRally.createBoardView(gameController);
        }
    }
    /**
     * @author Najib s181663, Camilla Boejden, Thamara Chellakooty.
     **/
    private Board createMediumBoard() {
        boardname = "mediumboard";
        return LoadBoard.loadBoard("mediumboard");
    }
    /**
     * @return Returns board where different board elements are included on specific fields.
     * @author Camilla Boejden, Thamara Chellakooty, Sercan Bicen  & Lauritz Pepke
     */
    private Board createEasyBoard() {
        boardname = "easyboard";
        return LoadBoard.loadBoard("easyboard");
    }
    /**
     * Method to save games in the database if player want it.
     */
    public void saveGame() {
        if (gameController != null) {
            Board board = gameController.board;
            if (board.getGameId() != null) {
                RepositoryAccess.getRepository().updateGameInDB(board);
            } else {
                RepositoryAccess.getRepository().createGameInDB(board, boardname);
            }
        }
    }
    /**
     * This method load games from the database. Returns messages to the player when it is not possible to retrieve games from the database
     */
    public void loadGame() {
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
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Do you want to save the game?");
            alert.setContentText("Choose your option.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                // Close the game.
                System.exit(-1);
            } else {
                saveGame();
                System.out.println("The game is saved.");
                Alert savedToDB = new Alert(AlertType.INFORMATION);
                savedToDB.setTitle("The game is saved!");
                savedToDB.setHeaderText("You have now saved your game." + "\n" +
                        "The program will close after you press 'OK'..");
                savedToDB.showAndWait();
                System.exit(-1);
            }
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
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }
    public boolean isGameRunning() {
        return gameController != null;
    }
    @Override
    public void update(Subject subject) {
    }
}