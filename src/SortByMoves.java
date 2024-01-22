import java.util.Comparator;

public class SortByMoves implements Comparator<ConcretePiece> {
    @Override
    public int compare(ConcretePiece o1, ConcretePiece o2) {
        return o1.getMoveSize() - o2.getMoveSize();
    }
}
