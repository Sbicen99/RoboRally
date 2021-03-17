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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    /**
     * Change screen resolution.
     */
    final public static int SPACE_HEIGHT = 70; // 60; // 75;
    final public static int SPACE_WIDTH = 70;  // 60; // 75;

    public final Space space;

    public SpaceView(@NotNull Space space) {

        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);


        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }

        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);

        setBlueConveyorBeltInBoard();
        setwallOnBoard();
    }


    /**
     * setBlueConveyorBeltInBoard with different directions.
     * @author Najib s181663
     * @author Sercan, s185040
     */
    private void setBlueConveyorBeltInBoard() {
        if (space.x == 1 && space.y == 0) {
            blueConveyorBelt(180,1,-5);

        } else if (space.x == 3 && space.y == 4 || space.x == 3 && space.y == 5) {
            blueConveyorBelt(0,0,0);

        } else if (space.x == 6 && space.y == 4 || space.x == 6 && space.y == 5) {
            blueConveyorBelt(180, 0, 0);

        } else if (space.x == 0 && space.y == 8) {
            blueConveyorBelt(90, 15, -15);

        } else if (space.x == 9 && space.y == 1) {
            blueConveyorBelt(270, 15, -15);
        }

        else if (space.x == 8 && space.y == 9) {
            blueConveyorBelt(0,0,0);
        }

        for (int i = 3; i < 7; i++) {
            for (int j = 3; j < 7; j++) {
                if (space.x == j && space.y == 6) {
                    blueConveyorBelt(-90, 15, -15);
                }
            }

            if (space.x == i && space.y == 3){
                blueConveyorBelt(90, 15, -15);
            }
        }

    }


    /**
     * Placing walls on board
     * @author Thamara Chellakooty & Camilla Boejden
     */
    public void setwallOnBoard () {
    for (int i = 0; i <= space.x; i++) {
        for (int j = 0; j <= space.y; j++) {
            if (space.x == 1 && space.y == 1) {
                addingWallsWithCanvas();
            }else if (space.x == 7 && space.y == 2) {
                addingVerticalWallWithCanvas();
            }else if (space.x == 0 && space.y == 7) {
                    addingVerticalWallWithCanvas();
            } else if (space.x == 7 && space.y == 7) {
            addingWallsWithCanvas();
            }
        }
    }
}
    /**
     * Adding a wall to the board with Pane
     * @author Sercan Bicen
     */

    /*private void addingWallsWithPane() {
        Pane pane = new Pane();
        Rectangle rectangle = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);
        rectangle.setFill(Color.TRANSPARENT);
        pane.getChildren().add(rectangle);

        Line line = new Line(2, SPACE_WIDTH-2, SPACE_HEIGHT-2, SPACE_HEIGHT-2);
        line.setStroke(Color.RED);
        line.setStrokeWidth(5);
        pane.getChildren().add(line);
        this.getChildren().add(pane);
    }*/


    /**
     * Adding a wall to the board with Canvas.
     * @author Sercan Bicen
     */
    protected void addingWallsWithCanvas() {
        Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(5);
        graphicsContext.setLineCap(StrokeLineCap.ROUND);

        graphicsContext.strokeLine(2, SPACE_HEIGHT-2, SPACE_WIDTH-2, SPACE_HEIGHT-2);
        this.getChildren().add(canvas);
    }

    /**
     * Adding vertical walls to the boards with Canvas.
     * @author Thamara Chellakooty & Camilla Boejden
     */
    protected void addingVerticalWallWithCanvas() {
        Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(5);
        graphicsContext.setLineCap(StrokeLineCap.ROUND);

        graphicsContext.strokeLine(SPACE_HEIGHT-2, 2, SPACE_WIDTH-2, SPACE_WIDTH-2);
        this.getChildren().add(canvas);
    }


    /**
     * setBlueConveyorBeltInBoard
     * @author Najib s181663
     * @author Sercan, s185040
     */
    protected void blueConveyorBelt(double rotation, double addingValueToHeight, double addingValueToWidth) {
        Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);

        //creating the image object
        try {
            InputStream stream = new FileInputStream("src/main/resources/images/976.png");
            Image image = new Image(stream);

            //creating the imageview
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(SPACE_WIDTH + addingValueToWidth);
            imageView.setFitHeight(SPACE_HEIGHT + addingValueToHeight);

            //Changing the rotation of the conveyor belt. 90 := --> , -90 := <--, 0 := up and 180 := down
            imageView.setRotate(rotation);

            this.getChildren().addAll(canvas, imageView);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updatePlayer() {
        this.getChildren().clear();

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    20.0, 40.0,
                    40.0, 0.0 );
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90*player.getHeading().ordinal())%360);
            this.getChildren().add(arrow);
        }


    }

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            updatePlayer();
        }

        setBlueConveyorBeltInBoard();
        setwallOnBoard();

    }

}
