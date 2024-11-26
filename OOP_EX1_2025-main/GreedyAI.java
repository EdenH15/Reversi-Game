import java.util.ArrayList;
import java.util.List;

public class GreedyAI extends AIPlayer {
    private final boolean player;

    /**
     * Constructor for the GreedyAI class.
     * Initializes the player (either Player 1 or Player 2).
     * @param isPlayerOne Boolean flag indicating whether this AI controls Player 1 (true) or Player 2 (false).
     */
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
        player = isPlayerOne;
    }

    /**
     * Makes a move based on the greedy strategy, choosing the move with the most number of flips.
     * @param gameStatus The current game status, including the board state and valid moves.
     * @return The best move to make - according to the instructions we were required to follow.
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        Player p;
        // Chooses the current player (player1 or player2)
        if (player) {
            p = gameStatus.getFirstPlayer();
        } else {
            p = gameStatus.getSecondPlayer();
        }

        // Gets the list of valid moves from the game status
        List<Position> posList = new ArrayList<>(gameStatus.ValidMoves());
        // Initialize the best position with the first valid move
        Position pos = posList.get(0);

        // Iterates through the list of valid moves to find the best one - by comparing two moves every time
        for (int i = 1; i < posList.size(); i++) {
            // If the current move flips more discs, update the best position
            if (gameStatus.countFlips(posList.get(i)) > gameStatus.countFlips(pos)) {
                pos = posList.get(i);
            }
            // If the current move flips the same number of discs, picks the position according to the instructions
            // we were required to follow - most right and lowest
            else if (gameStatus.countFlips(posList.get(i)) == gameStatus.countFlips(pos)) {
                pos = rightPos(pos, posList.get(i));
            }
        }

        // Creates a new disc for the player and returns the best move
        Disc newDisc = new SimpleDisc(p);
        Move newMove = new Move(pos, newDisc);
        return newMove;
    }

    /**
     * Compares two positions and returns the one with the "right" coordinates.
     * The "right" position is determined by the highest index of column or,
     * if columns are equal, by the highest index of row.
     * @param p1 The first position to compare.
     * @param p2 The second position to compare.
     * @return The position that is considered "right" according to the comparison rules.
     */
    public Position rightPos(Position p1, Position p2) {
        // Compare columns first
        if (p1.col() > p2.col()) {
            return p1;
        }
        else if (p2.col() > p1.col()) {
            return p2;
        }
        // If columns are equal, compare rows
        else if (p2.row() > p1.row()) {
            return p2;
        }
        // If both row and column are the same, return p1 (can be any)
        return p1;
    }
}



