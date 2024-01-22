public class ConcretePlayer implements Player{
    private boolean player;
    private int countWins;
    public ConcretePlayer(boolean isPlayerOne){
        this.player = isPlayerOne;
        countWins = 0;
    }

    @Override
    public boolean isPlayerOne() {
        return player;
    }

    @Override
    public int getWins() {
        return countWins;
    }
    public void win(){
        this.countWins++;
    }
}
