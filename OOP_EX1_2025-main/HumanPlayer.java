public class HumanPlayer extends Player {

    public HumanPlayer(Boolean playerOne){
        super(playerOne);
    }
    @Override
    boolean isHuman() {
        return true;
    }
}
