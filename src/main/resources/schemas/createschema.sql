/* Need to switch of FK check for MySQL since there are crosswise FK references */
SET FOREIGN_KEY_CHECKS = 0;;

CREATE TABLE IF NOT EXISTS Game (
  gameID int NOT NULL UNIQUE AUTO_INCREMENT,
  
  name varchar(255),

  phase tinyint,
  step tinyint,
  currentPlayer tinyint NULL,
  
  PRIMARY KEY (gameID),
  FOREIGN KEY (gameID, currentPlayer) REFERENCES Player(gameID, playerID)
);;
  
CREATE TABLE IF NOT EXISTS Player (
  gameID int NOT NULL,
  playerID tinyint NOT NULL,

  name varchar(255),
  colour varchar(31),
  
  positionX int,
  positionY int,
  heading tinyint,
  
  PRIMARY KEY (gameID, playerID),
  FOREIGN KEY (gameID) REFERENCES Game(gameID)
);;

SET FOREIGN_KEY_CHECKS = 1;;

create table if not exists CardField(
gameID int not null,
playerID tinyint not null,
type tinyint not null,
position tinyint not null,
visible bit not null,
command tinyint,
primary key (gameID,playerID,type,position),
foreign key (gameID)references Game(gameID),
foreign key (gameID,playerID)references Player(gameID,playerID));