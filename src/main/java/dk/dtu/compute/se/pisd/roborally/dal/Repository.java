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

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Repository implements IRepository {

    private static final String GAME_GAMEID = "gameID";
    private static final String GAME_NAME = "name";
    private static final String GAME_CURRENTPLAYER = "currentPlayer";
    private static final String GAME_PHASE = "phase";
    private static final String GAME_STEP = "step";
    private static final String PLAYER_PLAYERID = "playerID";
    private static final String PLAYER_NAME = "name";
    private static final String PLAYER_COLOUR = "colour";
    private static final String PLAYER_GAMEID = "gameID";
    private static final String PLAYER_POSITION_X = "positionX";
    private static final String PLAYER_POSITION_Y = "positionY";
    private static final String PLAYER_HEADING = "heading";
    private Connector connector;
    Repository(Connector connector) {
        this.connector = connector;
    }
    @Override
    public boolean createGameInDB(Board game, String boardName) {
        if (game.getGameId() == null) {
            Connection connection = connector.getConnection();
            try {
                connection.setAutoCommit(false);
                PreparedStatement ps = getInsertGameStatementRGK();
                ps.setString(1, boardName);
                ps.setNull(2, Types.TINYINT); // game.getPlayerNumber(game.getCurrentPlayer())); is inserted after players!
                ps.setInt(3, game.getPhase().ordinal());
                ps.setInt(4, game.getStep());
                int affectedRows = ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (affectedRows == 1 && generatedKeys.next()) {
                    game.setGameId(generatedKeys.getInt(1));
                }
                generatedKeys.close();
                createPlayersInDB(game);
                ps = getSelectGameStatementU();
                ps.setInt(1, game.getGameId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    rs.updateInt(GAME_CURRENTPLAYER, game.getPlayerNumber(game.getCurrentPlayer()));
                    rs.updateRow();
                } else {
                    errorHandling("Error, couldn't update.");
                }
                rs.close();
                connection.commit();
                connection.setAutoCommit(true);
                return true;
            } catch (SQLException e) {
                System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException sqlException) {
                    System.err.format("SQL: %s\n%s", sqlException.getSQLState(), sqlException.getMessage());
                }
            }
        } else {
            System.err.println("Game cannot be created in DB, since it has a game id already!");
        }
        return false;
    }
    @Override
    public boolean updateGameInDB(Board game) {
        assert game.getGameId() != null;
        Connection connection = connector.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = getSelectGameStatementU();
            ps.setInt(1, game.getGameId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rs.updateInt(GAME_CURRENTPLAYER, game.getPlayerNumber(game.getCurrentPlayer()));
                rs.updateInt(GAME_PHASE, game.getPhase().ordinal());
                rs.updateInt(GAME_STEP, game.getStep());
                rs.updateRow();
            } else {
                errorHandling("Couldn't get current player, game phase or step.");
            }
            rs.close();
            updatePlayersInDB(game);
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e1) {
                System.err.format("SQL: %s\n%s", e1.getSQLState(), e1.getMessage());
                e1.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public Board loadGameFromDB(int id) {
        Board game;
        try {
            PreparedStatement ps = getSelectGameStatementU();
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            int playerNo = -1;
            if (rs.next()) {
                String name = rs.getString(GAME_NAME);
                game = LoadBoard.loadBoard(name);
                if (game == null) {
                    return null;
                }
                playerNo = rs.getInt(GAME_CURRENTPLAYER);
                game.setPhase(Phase.values()[rs.getInt(GAME_PHASE)]);
                game.setStep(rs.getInt(GAME_STEP));
            } else {
                return null;
            }
            rs.close();
            game.setGameId(id);
            loadPlayersFromDB(game);
            if (playerNo >= 0 && playerNo < game.getPlayersNumber()) {
                game.setCurrentPlayer(game.getPlayer(playerNo));
            } else {
                return null;
            }
			/* TOODO this method needs to be implemented first
			loadCardFieldsFromDB(game);
			*/
            return game;
        } catch (SQLException e) {
            System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return null;
    }
    @Override
    public List<GameInDB> getGames() {
        List<GameInDB> result = new ArrayList<>();
        try {
            PreparedStatement ps = getSelectGameIdsStatement();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(GAME_GAMEID);
                String name = rs.getString(GAME_NAME);
                result.add(new GameInDB(id, name));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
            errorHandling("Couldn't get the list of games from the DB.");
            e.printStackTrace();
        }
        return result;
    }
    private void createPlayersInDB(Board game) throws SQLException {
        try {
            if (game != null) {
                PreparedStatement ps = getSelectPlayersStatementU();
                ps.setInt(1, game.getGameId());

                ResultSet resultSet = ps.executeQuery();
                for (int i = 0; i < game.getPlayersNumber(); i++) {
                    Player player = game.getPlayer(i);
                    resultSet.moveToInsertRow();
                    resultSet.updateInt(PLAYER_GAMEID, game.getGameId());
                    resultSet.updateInt(PLAYER_PLAYERID, i);
                    resultSet.updateString(PLAYER_NAME, player.getName());
                    resultSet.updateString(PLAYER_COLOUR, player.getColor());
                    resultSet.updateInt(PLAYER_POSITION_X, player.getSpace().x);
                    resultSet.updateInt(PLAYER_POSITION_Y, player.getSpace().y);
                    resultSet.updateInt(PLAYER_HEADING, player.getHeading().ordinal());
                    resultSet.insertRow();
                }
                resultSet.close();
            }
        } catch (SQLException e) {
            System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }
    private void loadPlayersFromDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectPlayersASCStatement();
        ps.setInt(1, game.getGameId());
        ResultSet rs = ps.executeQuery();
        int i = 0;
        while (rs.next()) {
            int playerId = rs.getInt(PLAYER_PLAYERID);
            if (i++ == playerId) {
                String name = rs.getString(PLAYER_NAME);
                String colour = rs.getString(PLAYER_COLOUR);
                Player player = new Player(game, colour, name);
                game.addPlayer(player);
                int x = rs.getInt(PLAYER_POSITION_X);
                int y = rs.getInt(PLAYER_POSITION_Y);
                player.setSpace(game.getSpace(x, y));
                int heading = rs.getInt(PLAYER_HEADING);
                player.setHeading(Heading.values()[heading]);
            } else {
                errorHandling("Games in DB doesn't have the current player!");
                System.err.println("Game in DB does not have a player with id " + i + "!");
            }
        }
        rs.close();
    }
    private void updatePlayersInDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectPlayersStatementU();
        ps.setInt(1, game.getGameId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int playerId = rs.getInt(PLAYER_PLAYERID);
            if (game != null) {
                Player player = game.getPlayer(playerId);
                // rs.updateString(PLAYER_NAME, player.getName()); // not needed: player's names does not change
                rs.updateInt(PLAYER_POSITION_X, player.getSpace().x);
                rs.updateInt(PLAYER_POSITION_Y, player.getSpace().y);
                rs.updateInt(PLAYER_HEADING, player.getHeading().ordinal());
                rs.updateRow();
            }
        }
        rs.close();
    }
    private static final String SQL_INSERT_GAME =
            "INSERT INTO Game(name, currentPlayer, phase, step) VALUES (?, ?, ?, ?)";
    private PreparedStatement insert_game_stmt = null;
    private PreparedStatement getInsertGameStatementRGK() {
        if (insert_game_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                insert_game_stmt = connection.prepareStatement(
                        SQL_INSERT_GAME,
                        Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException sqlException) {
                System.err.format("SQL: %s\n%s", sqlException.getSQLState(), sqlException.getMessage());
            }
        }
        return insert_game_stmt;
    }
    private static final String SQL_SELECT_GAME =
            "SELECT * FROM Game WHERE gameID = ?";
    private PreparedStatement select_game_stmt = null;
    private PreparedStatement getSelectGameStatementU() {
        if (select_game_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_game_stmt = connection.prepareStatement(
                        SQL_SELECT_GAME,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException sqlException) {
                System.err.format("SQL: %s\n%s", sqlException.getSQLState(), sqlException.getMessage());
            }
        }
        return select_game_stmt;
    }
    private static final String SQL_SELECT_PLAYERS =
            "SELECT * FROM Player WHERE gameID = ?";
    private PreparedStatement select_players_stmt = null;
    private PreparedStatement getSelectPlayersStatementU() {
        if (select_players_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_players_stmt = connection.prepareStatement(
                        SQL_SELECT_PLAYERS,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e) {
                System.err.format("SQL: %s\n%s", e.getSQLState(), e.getMessage());
            }
        }
        return select_players_stmt;
    }
    private static final String SQL_SELECT_PLAYERS_ASC =
            "SELECT * FROM Player WHERE gameID = ? ORDER BY playerID ASC";
    private PreparedStatement select_players_asc_stmt = null;
    private PreparedStatement getSelectPlayersASCStatement() {
        if (select_players_asc_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                // This statement does not need to be updatable
                select_players_asc_stmt = connection.prepareStatement(
                        SQL_SELECT_PLAYERS_ASC);
            } catch (SQLException sqlEx) {
                System.err.format("SQL: %s\n%s", sqlEx.getSQLState(), sqlEx.getMessage());
            }
        }
        return select_players_asc_stmt;
    }
    private static final String SQL_SELECT_GAMES =
            "SELECT gameID, name FROM Game";
    private PreparedStatement select_games_stmt = null;
    private PreparedStatement getSelectGameIdsStatement() {
        if (select_games_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_games_stmt = connection.prepareStatement(
                        SQL_SELECT_GAMES);
            } catch (SQLException sqlException) {
                System.err.format("SQL: %s\n%s", sqlException.getSQLState(), sqlException.getMessage());
            }
        }
        return select_games_stmt;
    }
    /**
     * Error handler as a dialogue.
     *
     * @param err is the error message that occurs.
     * @author Sercan Bicen, Najib Hebrawi
     */
    private void errorHandling(String err) {
        Alert errorMessage = new Alert(Alert.AlertType.ERROR, err);
        errorMessage.showAndWait();
    }
}
