Tic-tac-toe playing REST service.

Endpoints:
game/new?first={player1}&second={player2} - Creates a new game and returns the game Id. One of the player parameters is the human player's name, while the other is 'computer'(can be ommited).
game/status?gameId={id} Returns JSON representing the game, its status, players and game board.
game/play?gameId={id}&row={row_number}&column={column_number} - Returns JSON representing the game after move is made or HTTP status 412 in case of an illegal move.
stats - Returns the list of players containing their wins and losses.