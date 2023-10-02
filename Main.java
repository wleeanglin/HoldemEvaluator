import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class Main {
    ArrayList<Hand> hands = new ArrayList<>();
    ArrayList<Card> board = new ArrayList<>();

    public static void main(String[] args) {
        Main m = new Main(); 
        m.run();  
    }

    public void run(){
        Deck d = null;
        EvalLogic e  = new EvalLogic(); 

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                String line = null;
                line = reader.readLine();
                switch(line){
                    case "hand":
                        Hand h = getHand(reader);
                        Optional.ofNullable(h).ifPresent(hands::add);
                        break;
                    case "board":
                        while(board.size() < 5) {
                            line = reader.readLine();
                            if(line == null || line.equals("exit")) { 
                                board = new ArrayList<>();
                                continue;
                            }

                            Card c = parse(line);
                            if(c != null){
                                board.add(c); 
                            }
                        }
                        Collections.sort(board);
                        break;
                    case "calc":
                        ArrayList<Result> calcResults = e.calcWinner(hands, board);
                        printResults(calcResults, board);
                        clear();
                        break;
                    case "deal":
                        d = new Deck(true);
                        for (int i = 0; i < 10; i++) {
                            Card c = d.deal();
                            System.out.println(c.getValueEncoded() + " : " + c.getPrime());
                        }
                        break;
                    case "debug":
                        d = new Deck(true);
                        d.debug(5);
                    case "clear":
                        clear();
                        break;
                    case "sim":
                        int n = 3; 
                        d = new Deck(true); 
                        for(int i = 0; i < n; i++) {
                            hands.add(new Hand(d));
                        }
                        board = d.board();
                        Collections.sort(board);
                        ArrayList<Result> simResults = e.calcWinner(hands, board);
                        printResults(simResults, board);
                        clear();
                        break;
                    case "exit":
                        System.exit(1); 
                    case "help":
                        printHelp();
                    default:
                        System.err.println(line + ": not found");
                        break;
                }
            }
        } catch (IOException ex){

        }
    }

    public Card parse(String input){
        return Card.createCard(input.charAt(0), input.charAt(1)); 
    }

    public Hand getHand(BufferedReader reader) throws IOException{
        ArrayList<Card> cards = new ArrayList<>();
        String line = null;

        while(cards.size() < 2) {
            line = reader.readLine();


            if(line == null || line.equals("exit")) { 
                return null;
            }

            Card c = parse(line);
            if(c != null){
                cards.add(c); 
            }
        }

        Hand h = new Hand();
        for(Card c : cards) {
            h.addCard(c);
        }
        return h;
    }

    public void printBoard(ArrayList<Card> board){
        for(Card card: board) {
            System.out.printf("%s", card.getValueEncoded());
        }
        System.out.println();
    }

    public void printResults(ArrayList<Result> results, ArrayList<Card> board) {
        System.out.printf("On board: ");
        printBoard(board);
        for(Result result : results) {
            result.printResult(); 
        }
    }

    public void clear() {
        hands = new ArrayList<>();
        board = new ArrayList<>();
    }

    public void printHelp() {
        System.out.println("Enter one of the following commands;");
        System.out.println("    -hand: Enter a new hand");
        System.out.println("    -board: Enter a new board");
        System.out.println("    -calc: Determine winner from set up");
        System.out.println("    -deal: deal 10 cards from a new deck");
        System.out.println("    -clear: reset");
        System.out.println("    -sim: Sim 3 random hands");
        System.out.println("    -exit: End program");
        System.out.println("    -help: Show this message");
    }
}