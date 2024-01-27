import java.util.Comparator;

public class SortByDifferentPieces implements Comparator<Position> {
    @Override
    public int compare(Position o1, Position o2) {
        if(o1.getCountPieces() == o2.getCountPieces()){
            if(o1.getRow() == o2.getRow()){
                if(o1.getCol() == o2.getCol()){
                    return o1.getCol() - o2.getCol();
                }
            }
            return o1.getRow() - o2.getRow();
        }
        return o2.getCountPieces() - o1.getCountPieces();
    }
}
