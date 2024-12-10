import java.util.Comparator;

public class CompareCol implements Comparator<Position> {


    @Override
    public int compare(Position p1, Position p2) {
        if(p1.col()>p2.col()){
            return 1;
        }
        else if (p2.col()>p1.col()){
            return -1;
        }
        else return 0;
    }
}
