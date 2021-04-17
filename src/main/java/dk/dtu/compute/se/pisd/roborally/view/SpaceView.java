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
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

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
    final public static int SPACE_HEIGHT = 40; // 60; // 75;
    final public static int SPACE_WIDTH = 40;  // 60; // 75;

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
            this.setStyle("-fx-background-color: #000000;");
        }

        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    /**
     * setBlueConveyorBeltInBoard
     * @author Najib s181663
     * @author Sercan, s185040
     */
    private void createBlueConveyorBelt(int rotation) {
        Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);

        //creating the image object
        try {
            InputStream stream = new FileInputStream("src/main/resources/images/blue_arrow.png");
            Image image = new Image(stream);

            //creating the imageview
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(SPACE_WIDTH);
            imageView.setFitHeight(SPACE_HEIGHT);

            //Changing the rotation of the conveyor belt. 90 := --> , -90 := <--, 0 := up and 180 := down
            imageView.setRotate(rotation);

            this.getChildren().addAll(canvas, imageView);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * setBlueConveyorBeltInBoard with different directions.
     * @author Najib s181663
     * @author Sercan, s185040
     */
    private void setBlueConveyorBeltOnBoard() {
        if (space.x == 3 && space.y == 4) {
            createBlueConveyorBelt(180);

        } else if (space.x == 6 && space.y == 6) {
            createBlueConveyorBelt(90);
        }
    }



    /**
     * Adding the first checkpoint as an image.
     * @author Sercan Bicen
     * @author Najib Hebrawi
     */

    private void checkpointOne() {
        Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);

        //creating the image object
        try {
            InputStream stream = new FileInputStream("src/main/resources/images/checkpoint1.png");
            Image image = new Image(stream);

            //creating the imageview
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(SPACE_WIDTH);
            imageView.setFitHeight(SPACE_HEIGHT);

            this.getChildren().addAll(canvas, imageView);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Adding the second checkpoint as an image.
     * @author Sercan Bicen
     * @author Najib Hebrawi
     */

    private void checkpointTwo() {
        Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);

        //creating the image object
        try {
            InputStream stream = new FileInputStream("src/main/resources/images/checkpoint2.png");
            Image image = new Image(stream);

            //creating the imageview
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(SPACE_WIDTH);
            imageView.setFitHeight(SPACE_HEIGHT);

            this.getChildren().addAll(canvas, imageView);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Adding the checkpoints to a specific position on the board.
     *  @author Sercan Bicen
     *  @author Najib Hebrawi
     * @return
     */

    /*private void addCheckPoints() {
        if (space.x == 5 && space.y == 2) {
            checkpointOne();
        } else if (space.x == 2 && space.y == 6) {
            checkpointTwo();
        }
    }*/

    private ImageView imagesOnBoard(String imageName) {
        Image image = null;

        try {
            image = new Image(SpaceView.class.getClassLoader().getResource(imageName).toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ImageView imageView = new ImageView(image);
        imageView.setImage(image);
        imageView.setFitHeight(SPACE_HEIGHT);
        imageView.setFitWidth(SPACE_WIDTH);
        imageView.setVisible(true);

        this.getChildren().add(imageView);

        return imageView;
    }

    private ImageView addingImages(String name, int rotation) {
        ImageView imgv = imagesOnBoard(name);
        imgv.setRotate(rotation);

        return imgv;
    }


    /**
     * Placing walls on board
     * @author Thamara Chellakooty & Camilla Boejden
     */
    private void createStaticObject() {
        List<Heading> spaceWalls = space.getWalls();

        Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(5);
        graphicsContext.setLineCap(StrokeLineCap.ROUND);

        for (Heading heading: spaceWalls) {
            switch (heading){
                case SOUTH: graphicsContext.strokeLine(2, SPACE_HEIGHT-2, SPACE_WIDTH-2, SPACE_HEIGHT-2);
                    break;
                case NORTH: graphicsContext.strokeLine(2, 2, SPACE_WIDTH-2, 2);
                    break;
                case EAST: graphicsContext.strokeLine(SPACE_HEIGHT-2, SPACE_HEIGHT-2, SPACE_WIDTH-2, 2);
                    break;
                default:
            }
        }
        this.getChildren().add(canvas);
    }



    /**
     * Adding vertical walls to the boards with Canvas.
     * @author Thamara Chellakooty & Camilla Boejden
     */
    private void addingVerticalWallWithCanvas() {
        Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(5);
        graphicsContext.setLineCap(StrokeLineCap.ROUND);

        graphicsContext.strokeLine(SPACE_HEIGHT-2, 2, SPACE_WIDTH-2, SPACE_WIDTH-2);
        this.getChildren().add(canvas);
    }



    /**
     * Adding the gear turning right as an image
     * @author Lauritz s191179
     * @author Pernille Lyngholm
     */

    private void gearTurnRight() {
        Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);

        //creating the image object
        try {
            InputStream stream = new FileInputStream("src/main/resources/images/gearright.png");
            Image image = new Image(stream);

            //creating the imageview
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(SPACE_WIDTH);
            imageView.setFitHeight(SPACE_HEIGHT);

            this.getChildren().addAll(canvas, imageView);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adding the gear turning left as an image
     * @author Lauritz s191179
     * @author Pernille Lyngholm
     */

    private void gearTurnLeft() {
        Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);

        //creating the image object
        try {
            InputStream stream = new FileInputStream("src/main/resources/images/gearleft.png");
            Image image = new Image(stream);

            //creating the imageview
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(SPACE_WIDTH);
            imageView.setFitHeight(SPACE_HEIGHT);

            this.getChildren().addAll(canvas, imageView);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adding the gear to a specific position on the board
     * @author Lauritz s191179
     * @author Pernille Lyngholm
     */

    private void addGearToBoard() {
        if (space.x == 2 && space.y == 3) {
            gearTurnRight();
        } else if (space.x == 5 && space.y == 7)
            gearTurnLeft();
    }



    private void updatePlayer() {
        this.getChildren().clear();

        //------------------------
        //Adding elements to the board where each triangle/player is in front of it.
        //setBlueConveyorBeltOnBoard();
        createStaticObject();
        //addCheckPoints();
        //addGearToBoard();
        //------------------------

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0 );
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90*player.getHeading().ordinal())%360);
            this.getChildren().add(arrow);
        }

        for (FieldAction action : space.getActions()) {
            if (action instanceof Checkpoint) {
                addingImages("images/checkpoint" + ((Checkpoint) action).checkpointnumber + ".png", -90);
            }
        }

    }

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            updatePlayer();
        }
    }
}
