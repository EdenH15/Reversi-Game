public class BombDisc implements Disc {
    private Player _owner;

    public BombDisc(Player player){
        _owner=player;
    }
    public Player getOwner() {
        return _owner;
    }

    public void setOwner(Player player) {
        _owner=player;
    }

    public String getType() {
        return "💣";
    }
}
