import java.util.Comparator;

public class SortByDistance implements Comparator<ConcretePiece> {

    @Override
    public int compare(ConcretePiece o1, ConcretePiece o2) {
        if(o1.getDistanceCount() == o2.getDistanceCount()){
            if(o1.getCountId() == o2.getCountId()){
                if((!o1.getOwner().isPlayerOne() && GameLogic.getSecondPlayerWon()) || (o1.getOwner().isPlayerOne() && !GameLogic.getSecondPlayerWon())){
                    return -1;
                }else{
                    return 1;
                }
            }
            return o1.getCountId() - o2.getCountId();
        }
        return o2.getDistanceCount() - o1.getDistanceCount();
    }
}
