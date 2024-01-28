import java.util.*;

public class GameLogic implements PlayableLogic{
    private ConcretePlayer player1;
    private ConcretePlayer player2;
    private static final int[][] startBoard = {{0,0,0,1,1,1,1,1,0,0,0},{0,0,0,0,0,1,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0},
                     {1,0,0,0,0,2,0,0,0,0,1},{1,0,0,0,2,2,2,0,0,0,1},{1,1,0,2,2,3,2,2,0,1,1},
                     {1,0,0,0,2,2,2,0,0,0,1},{1,0,0,0,0,2,0,0,0,0,1},{0,0,0,0,0,0,0,0,0,0,0},
                     {0,0,0,0,0,1,0,0,0,0,0},{0,0,0,1,1,1,1,1,0,0,0}}; /*
                     0 is nothing, 1 is the attacker, 2 is the defender's pawn, 3 is the defender's king.
                     */
    private static ConcretePiece[][] board = new ConcretePiece[11][11];
    private static Stack<ConcretePiece[][]> historicalBoard = new Stack<ConcretePiece[][]>();
    private static Stack<ConcretePiece> lastPieceMoved = new Stack<ConcretePiece>();
    private static ArrayList<ConcretePiece> playerOnePieces = new ArrayList<ConcretePiece>();
    private static ArrayList<ConcretePiece> playerTwoPieces = new ArrayList<ConcretePiece>();
    private static ArrayList<Pawn> bothPlayerPawns = new ArrayList<Pawn>();
    private static ArrayList<ConcretePiece> bothPlayerPieces = new ArrayList<ConcretePiece>();
    private static ArrayList<ConcretePiece>[][] allPiecesAtPosition = new ArrayList[11][11];
    private static boolean secondPlayerTurn;
    private static boolean gameFinished;
    private static boolean secondPlayerWon;
    public GameLogic(){
        player1 = new ConcretePlayer(true);
        player2 = new ConcretePlayer(false);
        reset();
    }

    @Override
    public boolean move(Position a, Position b) {
        if(isLegalMove(a, b)) {
            ConcretePiece p = board[a.getRow()][a.getCol()];
            board[b.getRow()][b.getCol()] = p;
            board[a.getRow()][a.getCol()] = null;
            capturedPiece(b);
            p.addMove(b);
            p.addDistanceCount(a.distance(b));
            allPiecesAtPosition[b.getRow()][b.getCol()].add(p);
            GameLogic.secondPlayerTurn = !(GameLogic.secondPlayerTurn);
            ConcretePiece[][] currBoard = copyBoard();
            GameLogic.historicalBoard.push(currBoard);
            lastPieceMoved.push(p);
            if(p.getType().equals("♚") && b.isCorner()){
                gameFinished = true;
                player1.win();
            }
            if(GameLogic.gameFinished){
                statisticMoves();
                endOfSubInfo();

                statisticKills();
                endOfSubInfo();

                statisticDistance();
                endOfSubInfo();

                statisticAllPiecesAtPosition();
                endOfSubInfo();

                return true;
            }
            return true;
        }
        return false;
    }

    private ConcretePiece[][] copyBoard() {
        ConcretePiece[][] ans = new ConcretePiece[11][11];
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                ans[i][j] = board[i][j];
            }
        }
        return ans;
    }

    private void capturedPiece(Position current) {
        if(current == null){return;}
        ConcretePiece currentP = (ConcretePiece) getPieceAtPosition(current);
        if(currentP == null){return;}
        if(currentP.getType().equals("♚")){return;}
        Position left = Position.getLeft(current);
        Position right = Position.getRight(current);
        Position up = Position.getUp(current);
        Position down = Position.getDown(current);

        if(left != null){
            ConcretePiece leftP = (ConcretePiece) getPieceAtPosition(left);
            if(leftP != null){
                if(leftP.getType().equals("♚")){
                    if(isKingCaptured(left)){GameLogic.gameFinished = true;}
                }
                if(left.isLeftSide() && !leftP.getType().equals("♚")){
                    if(leftP.getOwner() != currentP.getOwner()){
                        int row = left.getRow();
                        int col = left.getCol();
                        board[row][col] = null;
                        ((Pawn)currentP).addKill();
                    }
                }else{
                    Position leftleft = Position.getLeft(left);
                    if(leftleft != null) {
                            ConcretePiece leftleftP = (ConcretePiece) getPieceAtPosition(leftleft);
                            if (leftleftP != null && !leftP.getType().equals("♚")) {
                                if (leftleftP.getOwner() == currentP.getOwner() && currentP.getOwner() != leftP.getOwner()) {
                                    int row = left.getRow();
                                    int col = left.getCol();
                                    board[row][col] = null;
                                    ((Pawn) currentP).addKill();
                                }
                            }
                    }
                }
            }
        }

        if(right != null){
            ConcretePiece rightP = (ConcretePiece) getPieceAtPosition(right);
            if(rightP != null){
                if(rightP.getType().equals("♚")){
                    if(isKingCaptured(right)){GameLogic.gameFinished = true;}
                }
                if(right.isRightSide() && !rightP.getType().equals("♚")){
                    if(rightP.getOwner() != currentP.getOwner()){
                        int row = right.getRow();
                        int col = right.getCol();
                        board[row][col] = null;
                        ((Pawn)currentP).addKill();
                    }
                }else{
                    Position rightright = Position.getRight(right);
                    if(rightright != null) {
                            ConcretePiece rightrightP = (ConcretePiece) getPieceAtPosition(rightright);
                            if (rightrightP != null && !rightP.getType().equals("♚")) {
                                if (rightrightP.getOwner() == currentP.getOwner() && currentP.getOwner() != rightP.getOwner()) {
                                    int row = right.getRow();
                                    int col = right.getCol();
                                    board[row][col] = null;
                                    ((Pawn) currentP).addKill();
                                }
                            }
                    }
                }
            }
        }

        if(down != null){
            ConcretePiece downP = (ConcretePiece) getPieceAtPosition(down);
            if(downP != null){
                if(downP.getType().equals("♚")){
                    if(isKingCaptured(down)){GameLogic.gameFinished = true;}
                }
                if(down.isDownSide() && !downP.getType().equals("♚")){
                    if(downP.getOwner() != currentP.getOwner()){
                        int row = down.getRow();
                        int col = down.getCol();
                        board[row][col] = null;
                        ((Pawn)currentP).addKill();
                    }
                }else{
                    Position downdown = Position.getDown(down);
                    if(downdown != null) {
                            ConcretePiece downdownP = (ConcretePiece) getPieceAtPosition(downdown);
                            if (downdownP != null && !downP.getType().equals("♚")) {
                                if (downdownP.getOwner() == currentP.getOwner() && currentP.getOwner() != downP.getOwner()) {
                                    int row = down.getRow();
                                    int col = down.getCol();
                                    board[row][col] = null;
                                    ((Pawn) currentP).addKill();
                                }
                            }
                    }
                }
            }
        }

        if(up != null){
            ConcretePiece upP = (ConcretePiece) getPieceAtPosition(up);
            if(upP != null){
                if(upP.getType().equals("♚")){
                    if(isKingCaptured(up)){GameLogic.gameFinished = true;}
                }
                if(up.isUpSide() && !upP.getType().equals("♚")){
                    if(upP.getOwner() != currentP.getOwner() && !upP.getType().equals("♚")){
                        int row = up.getRow();
                        int col = up.getCol();
                        board[row][col] = null;
                        ((Pawn)currentP).addKill();
                    }
                }else{
                    Position upup = Position.getUp(up);
                    if(upup != null) {
                            ConcretePiece upupP = (ConcretePiece) getPieceAtPosition(upup);
                            if (upupP != null && !upP.getType().equals("♚")) {
                                if (upupP.getOwner() == currentP.getOwner() && currentP.getOwner() != upP.getOwner()) {
                                    int row = up.getRow();
                                    int col = up.getCol();
                                    board[row][col] = null;
                                    ((Pawn) currentP).addKill();
                                }
                            }
                    }
                }
            }
        }

    }

    private boolean isKingCaptured(Position position) {
        Player p = getPieceAtPosition(position).getOwner();
        Position left = Position.getLeft(position);
        Position right = Position.getRight(position);
        Position up = Position.getUp(position);
        Position down = Position.getDown(position);
        int countPiece = 0;
        if(left != null && getPieceAtPosition(left) != null && getPieceAtPosition(left).getOwner() != p){
            countPiece++;
        }
        if(right != null && getPieceAtPosition(right) != null && getPieceAtPosition(right).getOwner() != p){
            countPiece++;
        }
        if(up != null && getPieceAtPosition(up) != null && getPieceAtPosition(up).getOwner() != p){
            countPiece++;
        }
        if(down != null && getPieceAtPosition(down) != null && getPieceAtPosition(down).getOwner() != p){
            countPiece++;
        }
        if((position.isSide() && countPiece == 3) || countPiece == 4){
            player2.win();
            secondPlayerWon = true;
            return true;
        }
        return false;
    }

    private boolean isLegalMove(Position a, Position b) {
        if(a == null || b == null){return false;}
        if(getPieceAtPosition(a) == null){return false;}
        int srcCol = a.getCol();
        int srcRow = a.getRow();
        int dstCol = b.getCol();
        int dstRow = b.getRow();
        if(srcCol != dstCol && dstRow != srcRow){return false;}
        if(srcCol == dstCol){
            if(srcRow > dstRow) {
                for (int i = dstRow; i < srcRow; i++) {
                    if (board[i][srcCol] != null) {return false;}
                }
            }else{
                for (int i = srcRow; i < dstRow; i++) {
                    if(board[i+1][srcCol] != null){return false;}
                }
            }
        }else{
            if(srcCol > dstCol) {
                for (int i = dstCol; i < srcCol; i++) {
                    if (board[srcRow][i] != null) {return false;}
                }
            }else{
                for (int i = srcCol; i < dstCol; i++) {
                    if(board[srcRow][i+1] != null){return false;}
                }
            }
        }
        ConcretePiece p = board[srcRow][srcCol];
        if(p.getOwner().isPlayerOne() && GameLogic.secondPlayerTurn){return false;}
        if(!(p.getOwner().isPlayerOne()) && !(GameLogic.secondPlayerTurn)){return false;}
        if(p.getType().equals("♙") && b.isCorner()){return false;}
        return true;
    }

    @Override
    public Piece getPieceAtPosition(Position position) {
        if(position == null){return null;}
        int col = position.getCol();
        int row = position.getRow();
        return board[row][col];
    }

    @Override
    public Player getFirstPlayer() {
        return player1;
    }

    @Override
    public Player getSecondPlayer() {
        return player2;
    }

    @Override
    public boolean isGameFinished() {
        return GameLogic.gameFinished;
    }

    private void statisticAllPiecesAtPosition() {
        ArrayList<Position> allPositions = new ArrayList<Position>();
        Set<ConcretePiece> pos;
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                allPositions.add(new Position(i,j));
                pos = new HashSet<ConcretePiece>(allPiecesAtPosition[i][j]);
                allPositions.get(i * 11 + j).setCountPieces(pos.size());
            }
        }
        Collections.sort(allPositions, new SortByDifferentPieces());
        for (int i = 0; i < allPositions.size(); i++) {
            if(allPositions.get(i).getCountPieces() > 1){
                System.out.println(allPositions.get(i).toString() + allPositions.get(i).getCountPieces() + " pieces");
            }
        }
    }

    public void statisticDistance(){
        Collections.sort(bothPlayerPieces, new SortByDistance());
        for (int i = 0; i < bothPlayerPieces.size(); i++) {
            if(bothPlayerPieces.get(i).getDistanceCount() > 0){
                System.out.println(bothPlayerPieces.get(i).getId() + ": " + bothPlayerPieces.get(i).getDistanceCount() + " squares");
            }
        }
    }

    public void statisticKills(){
        Collections.sort(bothPlayerPawns, new SortByKills());
        for (int i = 0; i < bothPlayerPawns.size(); i++) {
            if(bothPlayerPawns.get(i).getKillConunt() > 0){
            System.out.println(bothPlayerPawns.get(i).getId() + ": " + bothPlayerPawns.get(i).getKillConunt() + " kills");}
        }


    }
    public void statisticMoves(){
        Collections.sort(playerOnePieces, new SortByMoves());
        Collections.sort(playerTwoPieces, new SortByMoves());
        if(GameLogic.secondPlayerWon){
            for (int i = 0; i < playerTwoPieces.size(); i++) {
                if(playerTwoPieces.get(i).getMoveSize() > 1){
                    System.out.println(playerTwoPieces.get(i).getId() + ": [" + playerTwoPieces.get(i).printMove() + "]");}
            }
            for (int j = 0; j < playerOnePieces.size(); j++) {
                if(playerOnePieces.get(j).getMoveSize() > 1){
                    System.out.println(playerOnePieces.get(j).getId() + ": [" + playerOnePieces.get(j).printMove() + "]");}
            }

        }else{
            for (int j = 0; j < playerOnePieces.size(); j++) {
                if(playerOnePieces.get(j).getMoveSize() > 1){
                    System.out.println(playerOnePieces.get(j).getId() + ": [" + playerOnePieces.get(j).printMove() + "]");}
            }
            for (int i = 0; i < playerTwoPieces.size(); i++) {
                if(playerTwoPieces.get(i).getMoveSize() > 1){
                    System.out.println(playerTwoPieces.get(i).getId() + ": [" + playerTwoPieces.get(i).printMove() + "]");}
            }

        }
    }
    public void endOfSubInfo(){
        for (int i = 1; i <= 75; i++) {
            System.out.print("*");
        }
        System.out.print("\n");
    }

    @Override
    public boolean isSecondPlayerTurn() {
        return GameLogic.secondPlayerTurn;
    }

    @Override
    public void reset() {
        GameLogic.secondPlayerTurn = true;
        GameLogic.gameFinished = false;
        GameLogic.secondPlayerWon = false;
        for (int j = 0; j < 11; j++) {
            for (int i = 0; i < 11; i++) {
                ConcretePiece p = null;
                if(startBoard[i][j] == 1){
                    p = new Pawn(player2);
                    Position pos = new Position(i,j);
                    p.addMove(pos);
                    playerTwoPieces.add(p);
                    bothPlayerPawns.add((Pawn) p);
                    bothPlayerPieces.add(p);
                }
                if(startBoard[i][j] == 2){
                    p = new Pawn(player1);
                    Position pos = new Position(i,j);
                    p.addMove(pos);
                    playerOnePieces.add(p);
                    bothPlayerPawns.add((Pawn) p);
                    bothPlayerPieces.add(p);
                }
                if(startBoard[i][j] == 3){
                    p = new King(player1);
                    Position pos = new Position(i,j);
                    p.addMove(pos);
                    playerOnePieces.add(p);
                    bothPlayerPieces.add(p);
                }
                board[i][j] = p;
                allPiecesAtPosition[i][j] = new ArrayList<ConcretePiece>();
                if(p != null) {
                    allPiecesAtPosition[i][j].add(p);
                }
            }
        }
        ConcretePiece[][] currBoard = copyBoard();
        GameLogic.historicalBoard.push(currBoard);
    }

    @Override
    public void undoLastMove() {
        if(GameLogic.historicalBoard != null && GameLogic.historicalBoard.size() > 1){
            ConcretePiece[][] last = GameLogic.historicalBoard.pop();
            ConcretePiece[][] current = GameLogic.historicalBoard.peek();
            changeStatistics(current, last);
            changeBoard(current);
            GameLogic.secondPlayerTurn = !GameLogic.secondPlayerTurn;
        }
    }

    private void changeStatistics(ConcretePiece[][] current, ConcretePiece[][] last) {
        ConcretePiece pieceStatistics = lastPieceMoved.pop();
        Position dstStatistics = pieceStatistics.getLastMove();
        Position srcStatistics = pieceStatistics.getLastMove();
        pieceStatistics.removeMove();

        if(pieceStatistics instanceof Pawn){
            int count = countKills(current, last);
            ((Pawn) pieceStatistics).addKill(-count);
        }


        int distance = srcStatistics.distance(dstStatistics);
        pieceStatistics.addDistanceCount(-distance);

        if(!allPiecesAtPosition[dstStatistics.getRow()][dstStatistics.getCol()].isEmpty()){
            allPiecesAtPosition[dstStatistics.getRow()][dstStatistics.getCol()].remove(allPiecesAtPosition[dstStatistics.getRow()][dstStatistics.getCol()].size()-1);
        }
    }

    private int countKills(ConcretePiece[][] current, ConcretePiece[][] last) {
        int countCurr = 0;
        int countLast = 0;
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if(current[i][j] != null){countCurr++;}
                if(last[i][j] != null){countLast++;}
            }
        }
        return Math.abs(countCurr - countLast);
    }

    private void changeBoard(ConcretePiece[][] current) {
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                GameLogic.board[row][col] = current[row][col];
            }
        }
    }

    @Override
    public int getBoardSize() {
        return 11;
    }
    public static boolean getSecondPlayerWon(){
        return secondPlayerWon;
    }
}
