public class Position {
    private final int row;
    private final int col;
    private int countPieces;

    public Position(int row,int col){
        this.col = col;
        this.row = row;
        countPieces = 0;
    }
    public int getCol() {
        return this.col;
    }
    public int getRow()
    {
        return this.row;
    }
    public static Position getUp(Position start){
        if(start == null){return null;}
        if(start.col == 0){return null;}
        return new Position(start.row,start.col-1);
    }
    public static Position getDown(Position start){
        if(start == null){return null;}
        if(start.col == 10){return null;}
        return new Position(start.row,start.col+1);
    }
    public static Position getLeft(Position start){
        if(start == null){return null;}
        if(start.row == 0){return null;}
        return new Position(start.row-1,start.col);
    }
    public static Position getRight(Position start){
        if(start == null){return null;}
        if(start.row == 10){return null;}
        return new Position(start.row+1,start.col);
    }
    public boolean isCorner(){
        if(this.col == 0 && this.row == 0){return true;}
        if(this.col == 0 && this.row == 10){return true;}
        if(this.col == 10 && this.row == 0){return true;}
        if(this.col == 10 && this.row == 10){return true;}
        return false;
    }
    public boolean isSide(){
        return this.row == 0 || this.row == 10 || this.col == 0 || this.col == 10;
    }
    public boolean isLeftSide(){
        return this.row == 0 || (this.row == 1 && this.col == 0) || (this.row == 1 && this.col == 10);
    }
    public boolean isRightSide(){
        return this.row == 10 || (this.row == 9 && this.col == 0) || (this.row == 9 && this.col == 10);
    }
    public boolean isUpSide(){
        return this.col == 0 || (this.col == 1 && this.row == 0) || (this.col == 1 && this.row == 10);
    }
    public boolean isDownSide(){
        return this.col == 10 || (this.col == 9 && this.row == 0) || (this.col == 9 && this.row == 10);
    }
    public String toString(){
        return "(" + this.row + ", " + this.col + ")";
    }
    public int distance(Position other){
        if(this.row != other.row && this.col != other.col){return 0;}
        if (this.row == other.row){return Math.abs(other.col - this.col);}
        return Math.abs(other.row - this.row);
    }
    public void setCountPieces(int num){
        this.countPieces = num;
    }
    public int getCountPieces(){
        return this.countPieces;
    }

}
