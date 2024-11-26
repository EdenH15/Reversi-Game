public class UnflippableDisc implements Disc {

    private Player _owner;

    public UnflippableDisc(Player player){
        _owner=player;
    }
    public Player getOwner() {
        return _owner;
    }

    public void setOwner(Player player) {
        _owner=player;

    }

    public String getType() {
        return "⭕";
    }
}
