public class Move {
    private Position pos;
    private Disc disc;

    public Move(Position pos,Disc disc){
        this.pos=pos;
        this.disc=disc;

    }
    public Position position() {
        return pos;

    }

    public Disc disc() {
        return disc;
    }
}
