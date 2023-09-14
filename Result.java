import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Result {
    private Hand hand;
    private List<ArrayList<Card>> bestBoards;
    private int rank;
    
    public Result(Hand hand, List<ArrayList<Card>> bestBoards, int rank) {
        this.hand = hand;
        this.bestBoards = bestBoards;
        this.rank = rank;
        for(ArrayList<Card> board : bestBoards) {
            Collections.sort(board);
        }
    }
    
    public void setHand(Hand hand) {
        this.hand = hand; 
    }

    public Hand getHand() {
        return hand;
    }

    public void setBestBoards(List<ArrayList<Card>> bestBoards) {
        this.bestBoards = bestBoards; 
        for(ArrayList<Card> board : this.bestBoards) {
            Collections.sort(board);
        }
    }

    public List<ArrayList<Card>> getBestBoards() {
        return bestBoards;
    }

    public void setRank(int rank) {
        this.rank = rank; 
    }

    public int getRank() {
        return rank;
    }

    public void printResult() {
        String toPrint = hand.toString() + " has rank " + rank + " with board";
        toPrint += (bestBoards.size() > 1) ? "(s) " : " "; 
        for(int i = 0; i < bestBoards.size(); i++){
            toPrint += (i == 0) ? "{" : ""; 
            toPrint += boardToString(bestBoards.get(0));
            toPrint += (i == bestBoards.size() - 1) ? "}" : ", "; 
        } 
        System.out.println(toPrint);
    }

	public String boardToString(List<Card> board) {
        String boardString = "";
        for(Card card : board) {
            boardString += card.getValueEncoded();
        }
        return boardString; 
	}
}