# RoboRally - Project in Software Development (02362)
In the project, the focus has been on further developing a Java program that represents the strategic board game RoboRally. Roborally has so far been a physical game where players have to program their robots into goals before their teammates, while sabotaging each other's game plans.

(Write below prepared by: Camilla, Thamara and Sercan)

## Configuration
- If errors occur in connection with imports in the classes, the Maven project must be reloaded into the right bar. Click the "Stomach"> "Reload" button
- Update your database information in the Connector class ("RoboRally"> "dal"> "Connector").


## Player rules
- Choose between 2 - 6 players.
- Each player is assigned his color robot.
- Each player is dealt 8 programming cards for their decks.
- All players program their robot by dragging 5 programming cards in the 5 registers.
- The programming maps show different directions e.g. 3 steps forward, turn right, 1 step back, etc. so it's now a matter of planning his robot's path to the next checkpoint.
Place the programming cards in the order in which the robot is to move.
- The game takes place in rounds consisting of a programming phase and then an activation phase, where each round is executed in a register of each of the players.
- After the programming phase, the activation phase will start by pressing “finish programming”.
- Then you can select whether the individual register is to be executed by pressing “Execute current register”.
- Or all registers for all players must be executed by pressing "Execute program".
The activation phase will activate each register in the order of the registers. That is, register 1 is executed for all robots before register 2 will be executed.
- The execution will take place in the order player 1 to player 6.
- During the activation phase, the robot will perform the actions that are on the cards placed in the register.
- When all 5 registers have been executed for all the robots, the programming phase will be repeated.
The Player who first reaches all checkpoints in the correct order wins the game.



## Features in current version

- Players have the opportunity to push each other a field.

Fields:
The game board contains various challenges that the robots will face on their way:
- Wall: Robots cannot move through fields where walls are located.
- Green conveyor belt: If a robot lands on the green conveyor belt, it will automatically be moved one field forward. The robot will follow the belt in the direction the arrows point.
- Blue conveyor belt: If a robot lands on the blue conveyor belt, the robot will advance two fields in the direction of the arrows.
- Green Gear: If the robot lands on this field, it will be turned to the right.
- Red Gear: If the robot lands on this field, it will be turned to the left.
- Checkpoint: The robots must collect checkpoints. First checkpoint 1 then checkpoint 2.



The 8 different programming cards:
- Move one step forward
- Move two steps forward
- Move three steps forward
- Turn left
- Turn right
- Turn back
- Left or right
- Move one step back


Database:
Possibility to save a game in the database with its respective name.
- Retrieve the saved game from the database.
