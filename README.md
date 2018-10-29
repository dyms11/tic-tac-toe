# Tic Tac Toe 2.0

## Getting started

### Overview

An enhanced version of tic-tac-toe game developed in Java. This is a console game. The features added to this version of the game are:
- Board size could be between 3x3 and 10x10
- Three players instead two. One of them is an AI and two are human.
- The symbols for each player are configurable 
- Random turns

### How to play

When the game starts you'll see the game configuration and empty board:

````
Started new game with #players=3 and playgroundSize=4
--Turn #1 ->  Player{playerType=COMPUTER, mark=O}
--Turn #2 ->  Player{playerType=HUMAN, mark=A}
--Turn #3 ->  Player{playerType=HUMAN, mark=X}

- - - - - - - - -
|   |   |   |   | 
- - - - - - - - -
|   |   |   |   | 
- - - - - - - - -
|   |   |   |   | 
- - - - - - - - -
|   |   |   |   | 
- - - - - - - - -
````


During the game, human players will be prompted to enter the coordinates position where they want to place their mark, the position should be enter in this format `1,1`, first row and then column. If the position is invalid or occupied you'll be prompted again until you enter a valid position. Position row and column should be from 1 - N, where N is the board size. 
````
It's your turn Player{playerType=HUMAN, mark=A}!
Insert coordinates of the position to mark: 3,3
````

Computer AI moves, will be done automatically on its turn and you should see the board updated with the selected position.
````
It's your turn Player{playerType=COMPUTER, mark=O}!

- - - - - - - - -
| C |   |   |   | 
- - - - - - - - -
|   | A |   |   | 
- - - - - - - - -
|   |   |   |   | 
- - - - - - - - -
|   |   |   |   | 
- - - - - - - - -
````


After each player move you will see the updated board and if game isn't finished next player will be asked to insert a position.
````
- - - - - - - - -
| O |   |   |   | 
- - - - - - - - -
|   | A |   |   | 
- - - - - - - - -
|   |   | X |   | 
- - - - - - - - -
|   |   |   | O | 
- - - - - - - - -

It's your turn Player{playerType=HUMAN, mark=A}!
Insert coordinates of the position to mark: _
````

The game could finish for two reasons:
- There is a winner (row, column or diagonal lines) as normal tic-tac-toe
- All the positions have been filled, the board is full

There's another way to finish the game before any of above conditions is accomplished. When being asked to enter a position in your turn, you can write `exit` (case insensitive). This will end current game and ask you if you want to start a new one.
````
Insert coordinates of the position to mark: exit
- - - - - - - - -
| O |   |   |   | 
- - - - - - - - -
|   | A |   |   | 
- - - - - - - - -
|   |   | X |   | 
- - - - - - - - -
|   |   |   | O | 
- - - - - - - - -

EXITING THE GAME!!!
No winner!
````

When the game is over, you'll be asked if want to 'Try again', you should enter `Y` or `N` (case insensitive). If you fail to enter a valid response 5 times, game will exit anyway.
````
Would you like to start a new game? (Y/N): other

Invalid input.
Would you like to start a new game? (Y/N): n
GOOD BYE!!!
````


## Deployment

### Building

````
gradle build
````

### Configuration

Configuration of playground size and players' marks could be done on a .properties file.  This file should be in classpath, the configuration file must have the following format:

````
playground.size=10
player.mark.1=O
player.mark.2=X
player.mark.3=A
````

- `playground.size` could be between 3 - 10
- `player.mark.N` should exist for 1,2,3 and have one single character, not blank, as value 

If you send a configuration file and it doesn't exist or is corrupt, it will use the default configuration. 
````
➜  tic-tac-toe java -jar build/libs/tic-tac-toe-2.0-SNAPSHOT.jar "etc.properties"
An exception occurred while trying to read configuration file 'etc.properties'.
......
````

If you send an existing, not corrupt configuration file but it doesn't have the correct format, the program won't load and will show the log with the reason.
````
➜  tic-tac-toe java -jar build/libs/tic-tac-toe-2.0-SNAPSHOT.jar "other.properties"
Configuration file doesn't contain a valid playground.size between 3-10.
Configuration file doesn't contain 'player.mark.2'
````

### Running

For using default configuration, `playground=4` and `player.marks=O,X,A`

````
java -jar tic-tac-toe-2.0-SNAPSHOT.jar
````


If you want to use a custom configuration:
````
java -jar tic-tac-toe-2.0-SNAPSHOT.jar "file-name.properties"
````

### Dependencies
None

## Authors

- Dirgni Yordania Mayen Sanabria (@dyms11)
