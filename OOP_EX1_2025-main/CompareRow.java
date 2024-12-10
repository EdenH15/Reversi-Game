import java.util.Comparator;

public class CompareRow implements Comparator<Position> {
    @Override
    public int compare(Position p1, Position p2) {
        if(p1.row()>p2.row()){
            return 1;
        }
        else if (p2.row()>p1.row()){
            return -1;
        }
        else return 0;
    }
}
