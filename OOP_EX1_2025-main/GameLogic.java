import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {
    private final Disc[][] reversiBoard;  // game board
    private Player player1;
    private Player player2;
    private Player playerNow;  // the current player whose turn it is
    private boolean whoPlayer;  // a flag to keep track of which of the two players is the current player
    private final Stack<Position> posStack;  // keeps track of the current discs that are placed - used in the Undo method
    private final Stack<List<Move>> movesStack;  // keeps track of all the discs that were flipped - used in the Undo method

    /**
     * Constructor for the GameLogic class.
     * initializes the game board, player turn flag, and the stacks for moves and positions.
     */
    public GameLogic() {
        reversiBoard = new Disc[8][8]; // Initializes the 8x8 game board
        whoPlayer = true; // Player 1 starts
        posStack = new Stack<>(); // Stack to store positions for undo functionality
        movesStack = new Stack<>(); // Stack to store moves for undo functionality
    }

    /**
     * Starts a new game by setting up the initial positions on the board - giving each player two discs at the center of the board.
     */
    public void startGame() {
        reversiBoard[3][3] = new SimpleDisc(player1);
        reversiBoard[4][4] = new SimpleDisc(player1);
        reversiBoard[3][4] = new SimpleDisc(player2);
        reversiBoard[4][3] = new SimpleDisc(player2);
    }

    /**
     * Places a disc on the board at a specified position if the location is valid.
     * @param a The position for locating a new disc on the board.
     * @param disc The type of the disc to be placed.
     * @return true if the location was valid and successful, false otherwise.
     */
    public boolean locate_disc(Position a, Disc disc) {
        int c = a.col();
        int r = a.row();
        // makes sure you can't add an Unflippable or a bomb disc if you ran out of them
        if (reversiBoard[r][c] != null || (disc.getType().equals("⭕")  && playerNow.getNumber_of_unflippedable() == 0) || (disc.getType().equals("💣") && playerNow.getNumber_of_bombs() == 0)) {
            return false;
        }
        // if the location is valid - adds it to the stack of the positions and updates the amount of
        // Unflippable or a bomb discs of the owner if needed
        if (countFlips(a) > 0) {
            posStack.push(a);
            if (disc.getType().equals("⭕")) {
                playerNow.reduce_unflippedable();
            }
            if (disc.getType().equals("💣")) {
                playerNow.reduce_bomb();
            }
            reversiBoard[r][c] = disc;
            System.out.println("player "+playerType()+" placed a "+reversiBoard[r][c].getType()+" in "+"("+r+", "+c+")");
            flipDisc(a);  // Updates the discs that were flipped as a result
            whoPlayer = !whoPlayer;  // Changes the turn from the current player to the next player
            if (whoPlayer) {
                playerNow = player1;
            } else {
                playerNow = player2;
            }
            return true;
        }
        return false;
    }

    /**
     * Determines the current player's type (1 for player1, 2 for player2).
     * @return 1 if it's player1's turn, 2 if it's player2's turn.
     */
    public int playerType() {
        if (playerNow == player1) {
            return 1;
        }
        return 2;
    }

    /**
     * Flips the discs on the board after a valid move.
     * Saves all flipped discs in the stack holding lists of moves  - to use in the Undo method
     * @param p The position where the move was made.
     */
    public void flipDisc(Position p) {
        List<Move> flipList = new ArrayList<>(whatDiscFlip(p));
        movesStack.push(flipList);
        for (Move move : flipList) {
            move.disc().setOwner(playerNow);
            System.out.println("player "+playerType()+" flipped the "+move.disc().getType()+" in ("+move.position().row()+", "+move.position().col()+")");
        }
        System.out.println();
    }

    /**
     * Counts the flips caused by a bomb disc that is placed on the board.
     * @param list The list of moves to add the flipped discs to.
     * @param move The move representing the bomb's position.
     */
    private void countBombFlip(List<Move> list, Move move) {
        int c = move.position().col();
        int r = move.position().row();
        int[] indexR = {-1, +1, -1, 1, 1, 0, 0, -1};
        int[] indexC = {-1, +1, 1, -1, 0, 1, -1, 0};
        // Circling the bomb position
        for (int i = 0; i < 8; i++) {
            int R = r + indexR[i];
            int C = c + indexC[i];
            if (R >= 0 && R < reversiBoard.length && C >= 0 && C < reversiBoard.length && reversiBoard[R][C] != null && reversiBoard[R][C].getOwner() != playerNow && !reversiBoard[R][C].getType().equals("⭕")) {
                Position p = new Position(R, C);
                Move m = new Move(p, reversiBoard[R][C]);
                if (!ifContains(list, m)) {
                    list.add(m);
                }
            }
        }
    }

    /**
     * Checks if a list of moves contains a specific move - used to avoid double counting of moves.
     * @param list The list of moves to check.
     * @param m The move to check for in the list.
     * @return true if the move is already in the list, false otherwise.
     */
    public boolean ifContains(List<Move> list, Move m) {
        int c = m.position().col();
        int r = m.position().row();
        for (Move move : list) {
            int C = move.position().col();
            int R = move.position().row();
            if (reversiBoard[r][c] == reversiBoard[R][C] && c == C && r == R) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines which discs will be flipped if a disc is placed at the given position.
     * @param a The position to check for possible flips.
     * @return A list of moves holding the discs to be flipped.
     */
    public List<Move> whatDiscFlip(Position a) {
        List<Move> moveList = new ArrayList<>();
        int c = a.col();
        int r = a.row();
        List<Move> tempMove = new ArrayList<>();
        int[] indexR = {-1, +1, -1, 1, 1, 0, 0, -1};
        int[] indexC = {-1, +1, 1, -1, 0, 1, -1, 0};
        for (int i = 0; i < 8; i++) {
            int R = r + indexR[i];
            int C = c + indexC[i];
            while (R >= 0 && R < reversiBoard.length && C >= 0 && C < reversiBoard.length && reversiBoard[R][C] != null && reversiBoard[R][C].getOwner().isPlayerOne() != playerNow.isPlayerOne()) {
                if (!reversiBoard[R][C].getType().equals("⭕")) {
                    Position p = new Position(R, C);
                    tempMove.add(new Move(p, reversiBoard[R][C]));
                }
                R = R + indexR[i];
                C = C + indexC[i];
            }
            if (R >= 0 && R < reversiBoard.length && C >= 0 && C < reversiBoard.length && reversiBoard[R][C] != null && reversiBoard[R][C].getOwner().isPlayerOne() == playerNow.isPlayerOne()) {
                moveList.addAll(tempMove);
            }
            tempMove.clear();
        }
        for (int j = 0; j < moveList.size(); j++) {
            if (moveList.get(j).disc().getType().equals("💣")) {
                countBombFlip(moveList, moveList.get(j));
            }
        }
        return moveList;
    }

    /**
     * Returns the disc that is placed at the given position.
     * @param position The position of the disc to return.
     * @return The disc at the specified position.
     */
    public Disc getDiscAtPosition(Position position) {
        return reversiBoard[position.row()][position.col()];
    }

    /**
     * Returns the size of the game board.
     * @return The size of the board (8 for a 8x8 board).
     */
    public int getBoardSize() {
        return reversiBoard.length;
    }

    /**
     * Returns a list of all the valid moves for the current player to choose from.
     * @return A list of valid positions where the current player can place a disc.
     */
    public List<Position> ValidMoves() {
        List<Position> validList = new ArrayList<>();
        for (int i = 0; i < reversiBoard.length; i++) {
            for (int j = 0; j < reversiBoard[0].length; j++) {
                Position pos = new Position(i, j);
                if (countFlips(pos) > 0 && reversiBoard[i][j] == null) {
                    validList.add(pos);
                }
            }
        }
        return validList;
    }

    /**
     * Counts the number of discs that would be flipped if a disc is placed at a specified position.
     * @param a The position to check for flips from.
     * @return The number of flips that would occur.
     */
    public int countFlips(Position a) {
        return whatDiscFlip(a).size();
    }

    /**
     * Returns the first player.
     * @return Player 1.
     */
    public Player getFirstPlayer() {
        return player1;
    }

    /**
     * Returns the second player.
     * @return Player 2.
     */
    public Player getSecondPlayer() {
        return player2;
    }

    /**
     * Sets the two players for the game.
     * @param player1 The first player.
     * @param player2 The second player.
     */
    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        playerNow = player1;
    }

    /**
     * Checks if it is the first player's turn.
     * @return true if it's the first player's turn, false otherwise.
     */
    public boolean isFirstPlayerTurn() {
        return (whoPlayer);
    }

    /**
     * Determines if the game has finished.
     * @return true if the game is finished, false otherwise.
     */
    public boolean isGameFinished() {
        int discPlayer1 = 0;
        int discPlayer2 = 0;
        if (ValidMoves().isEmpty()) {
            for (Disc[] discs : reversiBoard) {
                for (int j = 0; j < reversiBoard[0].length; j++) {
                    if (discs[j] != null && discs[j].getOwner().isPlayerOne()) {
                        discPlayer1++;
                    } else if (discs[j] != null) {
                        discPlayer2++;
                    }
                }
            }
            if (discPlayer1 > discPlayer2) {
                player1.addWin();
                System.out.println("player 1 win with "+discPlayer1+" disc! player 2 had "+discPlayer2+" disc");
            } else if (discPlayer1 < discPlayer2) {
                player2.addWin();
                System.out.println("player 2 win with "+discPlayer2+" disc! player 1 had "+discPlayer1+" disc");
            }
            return true;
        }
        return false;
    }

    /**
     * Resets the game board and the players settings to start a new game.
     */
    public void reset() {
        for (int i = 0; i < reversiBoard.length; i++) {
            for (int j = 0; j < reversiBoard[0].length; j++) {
                reversiBoard[i][j] = null;
            }
        }
        startGame();
        whoPlayer = true;
        playerNow = player1;
        posStack.clear();
        movesStack.clear();
        player1.reset_bombs_and_unflippedable();
        player2.reset_bombs_and_unflippedable();
    }

    /**
     * Undoes the last move, restoring the game board and player states.
     * Only works if both players are human.
     */
    public void undoLastMove() {
        if (player1.isHuman() && player2.isHuman() && !posStack.empty()) {
            System.out.println("Undoing last move :" );
            Position undoPos = posStack.pop();
            if (reversiBoard[undoPos.row()][undoPos.col()].getType().equals("⭕")) {
                reversiBoard[undoPos.row()][undoPos.col()].getOwner().number_of_unflippedable++;
            }
            if (reversiBoard[undoPos.row()][undoPos.col()].getType().equals("💣")) {
                reversiBoard[undoPos.row()][undoPos.col()].getOwner().number_of_bombs++;
            }
            System.out.println("\tUndo: removing "+ reversiBoard[undoPos.row()][undoPos.col()].getType()+" from "+"("+undoPos.row()+", "+undoPos.col()+")");
            reversiBoard[undoPos.row()][undoPos.col()] = null;

            List<Move> undoMove = movesStack.pop();
            for (Move move : undoMove) {
                if (reversiBoard[move.position().row()][move.position().col()].getOwner() == player1) {
                    reversiBoard[move.position().row()][move.position().col()].setOwner(player2);
                    System.out.println("\tUndo: flipping back " + reversiBoard[move.position().row()][move.position().col()].getType() + " in (" + move.position().row() + ", " + move.position().col() + ")");
                } else {
                    reversiBoard[move.position().row()][move.position().col()].setOwner(player1);
                    System.out.println("\tUndo: flipping back " + reversiBoard[move.position().row()][move.position().col()].getType() + " in (" + move.position().row() + ", " + move.position().col() + ")");
                }
            }
            if (playerNow == player1) {
                playerNow = player2;
            } else {
                playerNow = player1;
            }
            whoPlayer = !whoPlayer;
            System.out.println();
        }
        else {
            System.out.println("\tNo previous move available to undo");
        }
    }
}


