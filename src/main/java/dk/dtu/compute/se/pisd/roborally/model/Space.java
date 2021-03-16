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
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;

import java.util.ArrayList;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Space extends Subject {
    private List<Heading> walls = new ArrayList<>();
    private List<FieldAction> actions = new ArrayList<>();

    public final Board board;

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



    /**
     * BlueConveyorBeltInBoard Move Action.
     * @author Najib s181663
     * @author Sercan, s185040
     */
    public void blueConveyorBeltMoving(){
        Space current = player.getSpace();
        if (x== 1 && y ==0 && current != null && player.board == current.board && player != null){
            Space target = board.getSpace(1, 2);
            if (target != null && target.getPlayer() == null) {
                target.setPlayer(player);
            }
        }

        else if (x== 0 && y ==8 && current != null && player.board == current.board && player != null){
            Space target = board.getSpace(2,8);
            if (target != null && target.getPlayer()  == null){
                target.setPlayer(player);

            }

        }else if (x== 9 && y ==1 && current != null && player.board == current.board && player != null){
            Space target = board.getSpace(7,1);
            if (target != null && target.getPlayer()  == null){
                target.setPlayer(player);

            }

        }else if (x== 8 && y ==9 && current != null && player.board == current.board && player != null){
            Space target = board.getSpace(8,7);
            if (target != null && target.getPlayer()  == null){
                target.setPlayer(player);

            }

        }else if (x== 3 && y ==3 && current != null && player.board == current.board && player != null){
            Space target = board.getSpace(5,3);
            if (target != null && target.getPlayer()  == null){
                target.setPlayer(player);

            }

        }else if (x== 4 && y ==3 && current != null && player.board == current.board && player != null){
            Space target = board.getSpace(6,3);
            if (target != null && target.getPlayer()  == null){
                target.setPlayer(player);

            }

        }else if (x== 5 && y ==3 && current != null && player.board == current.board && player != null){
            Space target = board.getSpace(7,3);
            if (target != null && target.getPlayer()  == null){
                target.setPlayer(player);

            }

        }else if (x== 6 && y ==3 && current != null && player.board == current.board && player != null){
            Space target = board.getSpace(8,3);
            if (target != null && target.getPlayer()  == null){
                target.setPlayer(player);

            }

        }
    }

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
