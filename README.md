# How to Run

This implementation of battleship uses a Socket-Server system. To run, start a first instance of the program and choose to host a game. Connections details will be provided. Begin a second instance of the program and choose not to host a game. Enter the requested connection details provided by the first program instance.  Setup will begin for both players (program instances). The empty board and total number of each ship (different lengths) will be provided. Inputs will be requested as all the positions of the requested ship, separating by commas. For example, if a ship of length 3 is requested, entering the input A1,A2,A3 is allowed as each position component is separated by a comma. When placing ships, each position in the ship must be horizontally or vertically adjacent.  The players will begin taking turns as soon as both players finish the setup. When it is their turn, a player enters a coordinate to fire to.  The player will be notified if the shot was a hit or miss. When one player loses all ships, the game ends!

# Battleship Game Details

- The game board size will be randomly chosen by the program.
- Board sizes range from a 5X5 grid to a 9X9 grid.
- Both players have boards of the same size.
- Board rows are labeled with numbers and board columns are labeled with letters.
- The amount of ships is derived from the program using the formula board edge size divided by 10 (rounded down) plus one.
- Each ship is randomly chosen by the program to be between sizes 2 through 4 (holds that many positions).
- Positions held by ships must all be either horizontally or vertically adjacent.
- Each player has the same amount of each Ship size.
- Each player will select where to place their ships on the board.
- Ship selections will be made by entering all ship positions in a comma separated string.
- Each player makes one shot per turn.
- A player can shoot the same location twice (doing so will not help).
- The program will show both the player's and opponent's board to the player.
- Both presented boards will use the "*" symbol to indicate a miss and the "X" symbol to indicate a hit.
- The presented opponent board will use the "-" symbol to indicate a shot has not been fired to the location (regardless of ship presence).
- The presented player's board will use the "-" symbol to indicate empty water not yet shot to and the "B" symbol to indicate a ship position not yet shot.
- The program will notify the player of the hit or miss result of both their and opponent shots.
- The program will notify the player if their or an opponent's ship sinks.
- The program will notify the player when the game is over and if they won or lost.
- The program will need one instance of player hosting the game and one instance of a player finding the hosts game.