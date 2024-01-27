import java.util.ArrayList;

public abstract class ConcretePiece implements Piece{
    protected Player Owner;
    private static int countPlayerOne = 1;
    private static int countPlayerTwo = 1;
    private String id;
    private int countId;
    private int distanceCount;

    private ArrayList<Position> moves = new ArrayList<Position>();
    public ConcretePiece(Player p){
        this.Owner = p;
        if(p.isPlayerOne()){
            if(countPlayerOne == 7){
                this.id = "K" + countPlayerOne;
                this.countId = countPlayerOne;
            }else{
                this.id = "D" + countPlayerOne;
                this.countId = countPlayerOne;
            }
            countPlayerOne++;
        }else{
            this.id = "A" + countPlayerTwo;
            this.countId = countPlayerTwo;
            countPlayerTwo++;
        }
    }
    abstract public String getType();
    @Override
    public Player getOwner() {
        return Owner;
    }
    public String getId(){
        return this.id;
    }
    public void addMove(Position pos){
        moves.add(pos);
    }
    public void removeMove(){
        if(!moves.isEmpty() && moves.size() > 1){
            moves.removeLast();
        }
    }
    public Position getLastMove(){
        if(!moves.isEmpty()){
            return moves.getLast();
        }
        return null;
    }
    public int getMoveSize(){
        return moves.size();
    }
    public int getCountId(){
        return this.countId;
    }

    public String printMove() {
        if(moves == null){return "";}
        String s = "";
        for (int i = 0; i < moves.size(); i++) {
            if(i == 0){
                s = s  + moves.get(i).toString();
            }else{
                s = s + ", " + moves.get(i).toString();
            }
        }
        return s;
    }
    public int getDistanceCount(){
        return this.distanceCount;
    }
    public void addDistanceCount(int num){
        this.distanceCount = this.distanceCount + num;
    }
}
