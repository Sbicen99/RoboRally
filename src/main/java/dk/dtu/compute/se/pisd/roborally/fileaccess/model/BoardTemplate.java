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
package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.oracle.javafx.jmx.json.JSONReader;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.fileaccess.Adapter;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class BoardTemplate {

    public int width;
    public int height;

    public List<SpaceTemplate> spaces = new ArrayList<>();

    GsonBuilder simpleBuilder = new GsonBuilder().
            registerTypeAdapter(FieldAction.class,
                    new Adapter<FieldAction>());
    Gson gson = simpleBuilder.create();
    ClassLoader classLoader =
            LoadBoard.class.getClassLoader();
    InputStream inputStream =
            classLoader.getResourceAsStream(
                    "boards/defaultboard.json");

    InputStreamReader streamReader = new InputStreamReader(inputStream);
    JsonReader reader = gson.newJsonReader(streamReader);
    BoardTemplate boardTemplate =
            gson.fromJson(reader, BoardTemplate.class);




}