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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Space extends Subject {
    private List<Heading> walls = new ArrayList<>();
    private List<FieldAction> actions = new ArrayList<>();

    private List<String> checkpointPassed = new ArrayList<>();

    public final Board board;

    private Heading heading = SOUTH;

    public final int x;
    public final int y;

    private Player player;


    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
    }

    public Player getPlayer() {
        return player;
    }

    //public void wall(){
    //    Space curren = player.getSpace();
    //    if

   // }

    /**
     * BlueConveyorBeltInBoard Move Action.
     * @author Najib s181663
     * @author Sercan, s185040
     */
    /*public void blueConveyorBeltMoving() {
        Space current = player.getSpace();
        if (x == 4 && y == 4 && current != null && player.board == current.board && player != null) {
            Space target = board.getSpace(4, 6);
            if (target != null && target.getPlayer() == null) {
                target.setPlayer(player);
            }
        } else if (x == 6 && y == 6 && current != null && player.board == current.board && player != null) {
            Space target = board.getSpace(8, 6);
            if (target != null && target.getPlayer() == null) {
                target.setPlayer(player);

            }
        }
    }*/


    /**
     * BlueConveyorBeltInBoard Move Action.
     * @author Najib s181663
     * @author Sercan, s185040
     */

    /*public void blueConveyorBeltAction() {
        if (player != null) {
            if (x == 3 && y == 4) {
                Space target = board.getSpace(1, 4);
                if (target != null && target.getPlayer() == null) {
                    target.setPlayer(player);
                }
            }
        }

        if (x == 6 && y == 6) {
            Space target = board.getSpace(6, 8);
            if (target != null && target.getPlayer() == null) {
                target.setPlayer(player);
            }
        }
    }*/

    /**
     * gearTurnRight Move Action
     * @author Lauritz s191179
     * @author Pernille Lyngholm
     */
    /*public void gearTurnRightAction() {
        if (x == 2 && y == 3) {
            player.setHeading(player.getHeading().next());
        }
    }*/

    /**
     * gearTurnLeft Move Action
     * @author Lauritz s191179
     * @author Pernille Lyngholm
     */
    /*public void gearTurnLeftAction() {
        if (x == 5 && y == 7) {
            player.setHeading(player.getHeading().prev());
        }
    }*/

    /*public void checkpointActions() {
        if (x == 5 && y == 2) {
            System.out.println("Passed checkpoint 1..");
            checkpointPassed.add(player.getName());

        } else if (x == 2 && y == 6) {
            System.out.println("Passed checkpoint 2..");
            checkpointPassed.add(player.getName());
        }

        try {
            //checking for duplicates and return it as winner of the game.
            for (int i = 0; i < checkpointPassed.size(); i++) {
                if (checkpointPassed.lastIndexOf(checkpointPassed.get(i)) != i) {
                    //TODO: finding the duplicated player and returns it as winner.
                    System.out.println("The winner is: " + checkpointPassed.get(i));
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }*/


    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }

    }

    public List<Heading> getWalls() {
        return walls;
    }

    public List<FieldAction> getActions() {
        return actions;
    }


    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }

}


