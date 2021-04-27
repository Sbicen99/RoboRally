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
package dk.dtu.compute.se.pisd.roborally.dal;

import com.mysql.cj.util.StringUtils;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Connector {

	private static final String HOST = "localhost";
	private static final int PORT = 3306;
	private static final String DATABASE = "RoboRally";
	private static final String USERNAME = "najibhebrawi1991";
	private static final String PASSWORD = "";

	private static final String DELIMITER = ";;";

	private Connection connection;


	/**
	 * @author Sercan Bicen
	 * Database connection.
	 */
	Connector() {
		try {
			// String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
			String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?serverTimezone=UTC&verifyServerCertificate=false&useSSL=true";
			connection = DriverManager.getConnection(url, USERNAME, PASSWORD);

			createDatabaseSchema();
		} catch (SQLException e) {
			// TODO we should try to diagnose and fix some problems here and
			//      exit in a more graceful way
			e.printStackTrace();


			// Platform.exit();
		}
	}

	private void createDatabaseSchema() {
		String createTablesStatement;
		try {
			ClassLoader classLoader = Connector.class.getClassLoader();
			URI uri = classLoader.
					getResource("schemas/createschema.sql").toURI();
			byte[] bytes = Files.readAllBytes(Paths.get(uri));
			createTablesStatement = new String(bytes);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			for (String sql : createTablesStatement.split(DELIMITER)) {
				if (!StringUtils.isEmptyOrWhitespaceOnly(sql)) {
					statement.executeUpdate(sql);
				}
			}
			statement.close();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO error handling
			errorHandler("Couldn't create database schema!");
			try {
				connection.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
			}
		}
	}

	Connection getConnection() {
		return connection;
	}


	/**
	 * @author Najib s181663
	 * @author Sercan, s185040
	 */
	private static final String CreateTableGame ="  CREATE TABLE IF NOT EXISTS Game (\n" +
			"gameID int NOT NULL UNIQUE AUTO_INCREMENT,\n" +
			"name varchar(255),\n" +
			"currentPlayer tinyint NULL,\n" +
			"phase tinyint,\n" +
			"step tinyint,\n" +
			"PRIMARY KEY (gameID),\n" +
			"...\n" +
			");; ";


	/**
	 * @author Najib s181663
	 * @author Sercan, s185040
	 */
	private static final String CreateTablePlayer = " CREATE TABLE IF NOT EXISTS Player (\n" +
			"gameID int NOT NULL,\n" +
			"playerID tinyint NOT NULL,\n" +
			"name varchar(255),\n" +
			"colour varchar(31),\n" +
			"positionX int,\n" +
			"positionY int,\n" +
			"heading tinyint,\n" +
			"PRIMARY KEY (gameID, playerID),\n" +
			"FOREIGN KEY (gameID) REFERENCES Game(gameID)\n" +
			"...\n" +
			");;";


	private void errorHandler(String err) {
		Alert errMessage = new Alert(Alert.AlertType.ERROR, err);
		errMessage.showAndWait();
	}
}