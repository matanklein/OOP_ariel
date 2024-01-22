import java.util.Comparator;

public class SortByKills implements Comparator<Pawn> {
    @Override
    public int compare(Pawn o1, Pawn o2) {
        if(o1.getKillConunt() == o2.getKillConunt()){
            if(o1.getCountId() == o2.getCountId()){
                if((!o1.getOwner().isPlayerOne() && GameLogic.getSecondPlayerWon()) || (o1.getOwner().isPlayerOne() && !GameLogic.getSecondPlayerWon())){
                    return -1;
                }else{
                    return 1;
                }
            }
            return o1.getCountId() - o2.getCountId();
        }
        return o2.getKillConunt() - o1.getKillConunt();
    }

}
