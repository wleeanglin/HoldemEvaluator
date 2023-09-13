import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }
   
    public Hand(Deck d){
        cards = new ArrayList<>();
        cards.add(d.deal());
        cards.add(d.deal());
    }

    public void addCard(Card c){
        cards.add(c);
    }

    public ArrayList<Card> getCards(){
        return cards;
    }

    public String toString() {
        String handString = "";
        for(Card card : cards) {
            handString += card.getValueEncoded();
        }
        return handString; 
    }
}
