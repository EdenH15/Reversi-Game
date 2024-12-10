import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GreedyAI extends AIPlayer {

    /**
     * Constructor for the GreedyAI class.
     * Initializes the player (either Player 1 or Player 2).
     * @param isPlayerOne Boolean flag indicating whether this AI controls Player 1 (true) or Player 2 (false).
     */
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
        this.isPlayerOne = isPlayerOne;
    }

    /**
     * Makes a move based on the greedy strategy, choosing the move with the most number of flips.
     * @param gameStatus The current game status, including the board state and valid moves.
     * @return The best move to make - using comparators.
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        CompareCol compCol=new CompareCol();
        CompareRow compRow=new CompareRow();
        CompareFlip compFlip=new CompareFlip();
        Comparator<Position> comparator=compFlip.thenComparing(compCol).thenComparing(compRow);
        compFlip.playGame(gameStatus);
        // Gets the list of valid moves from the game status
        List<Position> posList = new ArrayList<>(gameStatus.ValidMoves());
        posList.sort(comparator);
        Position p=new Position(posList.getLast().row(),posList.getLast().col());
        // Creates a new disc for the player and returns the best move
        Disc newDisc = new SimpleDisc(this);
        return new Move(p, newDisc);
    }
}



