public class  SimpleDisc implements Disc {
    private Player _owner;

    public SimpleDisc(Player player){
        _owner=player;
    }

    public String getType(){
        return "⬤";
    }
    public Player getOwner(){
        return _owner;
    }

    public void setOwner(Player player) {
        _owner=player;

    }

}
