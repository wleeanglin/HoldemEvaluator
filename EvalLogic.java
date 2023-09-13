//Straight flush
//Four of a kind
//Full house
//Flush
//Straight
//Three of a kind
//Two pair
//Pair
//High card

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EvalLogic {

	private HashMap<Long, Integer> primeProducts; 

	public EvalLogic() {
		try {
			System.out.println("Loading primes from file please wait...");
			long startTime = System.nanoTime();
			primeProducts = readMapFromFile("primeProducts.ser");
			long endTime = System.nanoTime(); 
			double duration = (endTime - startTime) / 1e6;
			System.out.println("Done! Took : " + duration + " milliseconds");
		} catch (Exception e) {
			System.out.println("Unable to load prime products.");
			System.out.println(e);
		}
	}

	public ArrayList<Result> calcWinner(ArrayList<Hand> hands, ArrayList<Card> board) {
		ArrayList<Result> results = new ArrayList<>(); 
		HashMap<Result, Integer> rankMap = new HashMap(); 
		for(Hand hand : hands) {
			ArrayList<ArrayList<Card>> boards = getBoards(hand, board);
			ArrayList<ArrayList<Card>> bestBoards = new ArrayList<>(); 
			int bestHandRank = Integer.MAX_VALUE;
			//We can have more than one combination of the best hand
			//e.g. TT on 56789r we have two combinations of the straight
			for(ArrayList<Card> boardTmp : boards){
				long prime = boardTmp.stream().mapToLong(Card::getPrime).reduce(1L, (a, b) -> a * b);
				if(primeProducts.get(prime) < bestHandRank) {
					bestHandRank = primeProducts.get(prime);
					bestBoards.clear();
					bestBoards.add(boardTmp); 
				} else if(primeProducts.get(prime) == bestHandRank) { 
					bestBoards.add(boardTmp);
				}
			}

			results.add(new Result(hand, bestBoards, 0));
			rankMap.put(results.get(results.size() - 1), bestHandRank); 
		}

		// Sort results based on their rank from rankMap.
		results.sort((r1, r2) -> rankMap.get(r1).compareTo(rankMap.get(r2)));

		int previousRankValue = -1;
		int currentRank = 0;
	
		for (int i = 0; i < results.size(); i++) {
			Result currentResult = results.get(i);
	
			if (i == 0 || !rankMap.get(currentResult).equals(previousRankValue)) {
				currentRank++;
			}
	
			currentResult.setRank(currentRank);
			previousRankValue = rankMap.get(currentResult);
		}
	
		return results;
	}

	@SuppressWarnings("unchecked")
	public HashMap<Long, Integer> readMapFromFile(String filename) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			return (HashMap<Long, Integer>) ois.readObject();
		}
	}

	public ArrayList<ArrayList<Card>> getBoards(Hand h, ArrayList<Card> board) {
		ArrayList<Card> allCards = new ArrayList<>();
		allCards.addAll(h.getCards());
		allCards.addAll(board);

		ArrayList<ArrayList<Card>> boards = new ArrayList<>();
		generateCombinations(allCards, 5, 0, new ArrayList<Card>(), boards);
		
		return boards;
	}

	private void generateCombinations(List<Card> allCards, int len, int startPosition, ArrayList<Card> currentCombination, ArrayList<ArrayList<Card>> boards) {
		if (len == 0) {
			boards.add(new ArrayList<Card>(currentCombination));
			return;
		}

		for (int i = startPosition; i <= allCards.size() - len; i++) {
			currentCombination.add(allCards.get(i));
			generateCombinations(allCards, len - 1, i + 1, currentCombination, boards);
			currentCombination.remove(allCards.get(i));
		}
	}

}
