import java.util.ArrayList;
import java.util.Collections;

public class Hand {
    private ArrayList<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }
   
    public Hand(Deck d){
        cards = new ArrayList<>();
        cards.add(d.deal());
        cards.add(d.deal());
        sort(); 
    }

    public void addCard(Card c){
        cards.add(c);
        sort();
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

    private void sort() {
        Collections.sort(cards);
    }
}
