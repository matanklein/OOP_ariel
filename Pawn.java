public class Pawn extends ConcretePiece{
    private int killConunt;
    public Pawn(Player p){
        super(p);
        killConunt = 0;
    }
    @Override
    public String getType() {
        return "♙";
    }
    public int getKillConunt(){return this.killConunt;}
    public void addKill(){
        this.killConunt++;
    }
    public void addKill(int num){
        this.killConunt = num;
    }
    public void setKill(int num){
        this.killConunt = num;
    }

}
