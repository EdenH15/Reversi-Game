public class Position {
    private int row;
    private int col;

    public Position(int row,int col){
        this.col=col;
        this.row=row;
    }
    public int row() {
      return row;
    }

    public int col() {
       return col;
    }
}
