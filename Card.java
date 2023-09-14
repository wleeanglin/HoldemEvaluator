import java.util.List;

public class Card implements Comparable<Card>{
    private int value;

    private static final List<Character> ranks = List.of('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A');
    private static final List<Suit> suits = List.of(Suit.DIAMOND, Suit.CLUB, Suit.SPADE, Suit.HEART);
    private static final List<Integer> primes = List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239);

    public static Card createCard(char rank, char suit) {
        Integer r = ranks.indexOf(rank);
        Suit s = Suit.fromEncoding(suit);
        
        if (r == -1 || s == null) {
            return null;
        }

        return new Card(r, s);
    }
    
    public Card(int rank, Suit suit) {
        value = (rank << 2) | suit.getBitwiseRepresentation(); 
    }

    public char getRank() {
        return ranks.get(value >> 2);
    }

    public Suit getSuit() {
        return suits.get(value & 0b11);
    }

    public int getRankInt() {
        return value >> 2;
    }

    public int getSuitInt() {
        return value & 0b11;
    }

    public int getValue() {
        return value;
    }

    public int getPrime() {
        return primes.get(value); 
    }

    public String getValueEncoded(){
        return this.getRank() + "" + this.getSuit().getEncoding();
    }

    public enum Suit {
        DIAMOND('d'), CLUB('c'), SPADE('s'), HEART('h');

        private final char encoding; 

        Suit(char encoding) {
            this.encoding = encoding;
        }

        public char getEncoding() {
            return encoding; 
        }

        public static Suit fromEncoding(char encoding) {
            encoding = Character.toLowerCase(encoding);
            for (Suit suit : Suit.values()) {
                if (suit.getEncoding() == encoding) {
                    return suit;
                }
            }
            return null;
        }

        public int getBitwiseRepresentation() {
            return this.ordinal();
        }
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;

        if(o == null || (this.getClass() != o.getClass())) {
            return false;
        }

        Card oc = (Card) o;
        return (oc.getValue() == this.getValue());
    }

    @Override
    public int compareTo(Card o) {
        return (-1) * Integer.compare(this.value, o.value); 
    }
}
