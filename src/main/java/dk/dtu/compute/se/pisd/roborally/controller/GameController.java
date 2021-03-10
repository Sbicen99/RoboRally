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

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {

    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * Moves the player and changes to another player.
     * Checks if the field is empty.
     * @author Sercan Bicen, s185040
     * @author Najib Hebrawi s181663
     * @implNote ( && space == board.getSpace(0,0)) do, we cant click in the board with the mouse.
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space)  {
        if (space.getPlayer() == null && space.board == board){
            Player currentPlayer = board.getCurrentPlayer();
            currentPlayer.setSpace(space);
            board.setNotEmpty("");
            int number = board.getPlayerNumber(currentPlayer);
            Player nextPlayer = board.getPlayer((number + 1) % board.getPlayersNumber());
            board.setCurrentPlayer(nextPlayer);
            board.setCount(board.getCount() + 1);

        } else {
            board.setNotEmpty("The field isn't empty");

        }
    }


    // XXX: V2
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX: V2
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    /**
     * Once the player has completed programming his robot.
     */
    // XXX: V2
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    // XXX: V2
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX: V2
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /**
     * Executing command cards for all players.
     */

    // XXX: V2
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    /**
     * Executing command cards for current player.
     */

    // XXX: V2
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // XXX: V2
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    // XXX: V2
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    if (command.isInteractive()){
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                    executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }


    /**
     * Executing command with option on turn right and turn left and continue.
     * @author Najib, s181663
     * @param option
     */
    public void executeCommandOptionAndContinue(@NotNull Command option){
        Player currentPlayer = board.getCurrentPlayer();
        if (currentPlayer != null && board.getPhase() == Phase.PLAYER_INTERACTION && option != null){
            board.setPhase(Phase.ACTIVATION);
            executeCommand(currentPlayer, option);

            int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
            if (nextPlayerNumber < board.getPlayersNumber()) {
                board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
            } else {
                int step = board.getStep() + 1;
                if (step < Player.NO_REGISTERS) {
                    makeProgramFieldsVisible(step);
                    board.setStep(step);
                    board.setCurrentPlayer(board.getPlayer(0));
                } else {
                    startProgrammingPhase();
                }
            }
        }
    }

    // XXX: V2
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD:
                    this.moveForward(player);
                    break;
                case MoveBack:
                    this.Moveback(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD1:
                    this.fastForward1(player);
                    break;
                case FAST_FORWARD2:
                    this.fastForward2(player);
                    break;
                case U_Turn:
                    this.UTurn(player);
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    /**
     * Moving the player one field forward.
     * @author Najib s181663
     * @param player
     */

    // TODO Assignment V2
    public void moveForward(@NotNull Player player) {
        if (player.board == board && player != null) {
            Space space = player.getSpace();
            Heading heading = player.getHeading();
            Space target = board.getNeighbour(space, heading);
            if (target != null ) {
                try {
                    moveToSpace(player, target, heading);
                } catch (ImpossibleMoveException e) {
// we don't do anything here for now;
// we just catch the exception so that
// we do no pass it on to the caller
// (which would be very bad style).
                }
            }
        }
    }

    /**
     * @author Najib Hebrawi s181663
     * @param player
     * @param space
     * @param heading
     * @throws ImpossibleMoveException
     */
    private void moveToSpace(
            @NotNull Player player,
            @NotNull Space space,
            @NotNull Heading heading) throws ImpossibleMoveException {
        Player other = space.getPlayer();
        if (other != null){
            Space target = board.getNeighbour(space, heading);
            if (target != null) {
// XXX Note that there might be additional problems
// with infinite recursion here!
                moveToSpace(other, target, heading);
            } else {
                throw new ImpossibleMoveException(player, space, heading);
            }
        }
        player.setSpace(space);
    }


    /**
     * Moving the player one field back( without changing the direction).
     * @author Najib s181663
     * @param player
     */
    public void Moveback(@NotNull Player player){
        Space current = player.getSpace();
        if (current != null && player.board == current.board && player != null){
            Space target = board.getNeighbourr(current, player.getHeading());
            if (target != null && target.getPlayer() == null){
                target.setPlayer(player);
            }
        }
    }

    /**
     * Moving the player two fields forward.
     * Calling the moveForward method two times
     * @author Sercan Bicen, s185040
     * @author Najib Hebrawi, s181663
     * @param player
     */

    // TODO Assignment V2
    public void fastForward1(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }

    /**
     * Moving the player three field forward.
     * @author Najib s181663
     * @param player
     */
    public void fastForward2(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
        moveForward(player);
    }


    /**
     * Turning the player to the right.
     * @author Sercan, s185040
     * @author Najib Hebrawi, s181663
     * @param player
     */
    // TODO Assignment V2
    public void turnRight(@NotNull Player player) {
        if (player != null && player.board == board){
            player.setHeading(player.getHeading().next());
        }
    }



    /**
     * Turning the player to the left.
     * @author Sercan, s185040
     * @author Najib Hebrawi, s181663
     * @param player
     */
    // TODO Assignment V2
    public void turnLeft(@NotNull Player player) {
        if (player != null && player.board == board){
            player.setHeading(player.getHeading().prev());
        }

    }

    /**
     * Turning the player U-Turn.
     * @author Najib Hebrawi, s181663
     * @param player
     */
    public void UTurn(@NotNull Player player){
        if (player !=null && player.board == board){
            player.setHeading(player.getHeading().BackUTurn());
        }
    }

    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

}