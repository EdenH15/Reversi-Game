import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer {
    private final boolean player;

    /**
     * Constructor for the RandomAI class.
     * Initializes the player (Player 1 or Player 2).
     * @param isPlayerOne Boolean flag indicating whether this AI controls Player 1 (true) or Player 2 (false).
     */
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
        player = isPlayerOne;
    }

    /**
     * Makes a random move on the board, from the valid moves list and chooses a random type of disc.
     * @param gameStatus The current game status, including the board state and valid moves.
     * @return The randomly selected move to make.
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
        List<Position> validMove = new ArrayList<>(gameStatus.ValidMoves());

        // Generates a random index to choose a valid move from the list
        Random rand = new Random();
        int index = rand.nextInt(validMove.size());
        Position pos = new Position(validMove.get(index).row(), validMove.get(index).col());

        // Array of possible disc types
        String[] discType = { "⬤", "⭕", "💣" };

        // Generates a random index to choose a disc type
        int index2 = rand.nextInt(discType.length);
        Disc newDisc;

        // Creates a disc based on the randomly selected type and available quantity
        if (discType[index2].equals("💣") && p.getNumber_of_bombs() > 0) {
            newDisc = new BombDisc(p);
        }
        else if (discType[index2].equals("⭕") && p.getNumber_of_unflippedable() > 0) {
            newDisc = new UnflippableDisc(p);
        }
        else {
            newDisc = new SimpleDisc(p);
        }

        // Creates a new move with the randomly chosen position and disc
        return new Move(pos, newDisc);
    }
}

