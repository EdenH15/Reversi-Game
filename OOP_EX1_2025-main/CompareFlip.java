import java.util.Comparator;

public class CompareFlip implements Comparator<Position> {
    private PlayableLogic playGame;


    public void playGame(PlayableLogic play){
       playGame=play;
    }
    @Override
    public int compare(Position p1, Position p2) {
        int f1=playGame.countFlips(p1);
        int f2=playGame.countFlips(p2);
        if(f1>f2){
            return 1;
        }
        else if (f2>f1){
            return -1;
        }
        else return 0;
    }

}
