public class King extends ConcretePiece{
    public King(Player p){
        super(p);
    }
    @Override
    public String getType() {
        return "♚";
    }
}
