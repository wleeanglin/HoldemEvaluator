import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class calcPrimes {

    private static Map<Character, int[]> primes = Map.ofEntries(
        Map.entry('2', new int[]{2, 3, 5, 7}),
        Map.entry('3', new int[]{11, 13, 17, 19}),
        Map.entry('4', new int[]{23, 29, 31, 37}),
        Map.entry('5', new int[]{41, 43, 47, 53}),
        Map.entry('6', new int[]{59, 61, 67, 71}),
        Map.entry('7', new int[]{73, 79, 83, 89}),
        Map.entry('8', new int[]{97, 101, 103, 107}),
        Map.entry('9', new int[]{109, 113, 127, 131}),
        Map.entry('T', new int[]{137, 139, 149, 151}),
        Map.entry('J', new int[]{157, 163, 167, 173}),
        Map.entry('Q', new int[]{179, 181, 191, 193}),
        Map.entry('K', new int[]{197, 199, 211, 223}),
        Map.entry('A', new int[]{227, 229, 233, 239})
    );

    private static HashMap<Long, Integer> resultsMap = new HashMap<>(); 

    //Ordering of Diamonds, Clubs, Spades
    public static void main(String[] args) {
        String filename; 
        if(args.length == 0) {
            filename = "holdem.csv";
        } else {
            filename = args[0];
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            List<String[]> records = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(","); 
                records.add(fields);
            }

            parseLines(records);

            long startTime = System.nanoTime();
            saveMapToFile(resultsMap, "primeProducts.ser");
            long endTime = System.nanoTime(); 
            double duration = (endTime - startTime) / 1e6;
            System.out.println("Saving complete in : " + duration + " milliseconds");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseLines(List<String[]> records) {
        String currentHandType = "SF"; 
        long startTime = System.nanoTime();
        for(int i = 0; i < records.size(); i++) {
            if(!records.get(i)[6].equals(currentHandType) || i == records.size() - 1) {
                long endTime = System.nanoTime(); 
                double duration = (endTime - startTime) / 1e6;
                System.out.println(currentHandType + " hands completed in " + duration + " milliseconds");
                startTime = endTime; 
                currentHandType = records.get(i)[6]; 
            }

            Map<Long, Integer> map = parseLine(records.get(i));
            for(Long key : map.keySet()) {
                resultsMap.put(key, map.get(key));
            }
        }
    }

    public static Map<Long, Integer> parseLine(String[] record) {
        Deck d = new Deck(false); 
        boolean flush = record[6].equals("F") || record[6].equals("SF");
        char[] cards = Arrays.stream(record[5].split(" "))
                     .map(s -> s.charAt(0))
                     .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                     .toString()
                     .toCharArray();

        if(flush) {
            return parseFlush(record, cards);
        } else {
            return parseNonFlush(record, cards);
        }
    }

    public static Map<Long, Integer> parseFlush(String[] record, char[] cards) {
        Map<Long, Integer> resultMap = new HashMap<>();
        Integer recordValue = Integer.parseInt(record[0]);

        // loop through each suit
        for (int i = 0; i < 4; i++) {
            long product = 1L;
            for (char card : cards) {
                product *= primes.get(card)[i];
            }
            resultMap.put(product, recordValue);
        }

        return resultMap;
    }

    public static Map<Long, Integer> parseNonFlush(String[] record, char[] cards) {
        Map<Long, Integer> resultMap = new HashMap<>();
        Integer recordValue = Integer.parseInt(record[0]);
        
        ArrayList<Long> products = new ArrayList<>();
        char currentCard = cards[0];
        int currentCount = 1;
        
        for (int i = 1; i < cards.length; i++) {
            if (cards[i] == currentCard) {
                currentCount++;
            } else {
                products = processCurrentCardAndReset(products, currentCard, currentCount);
                currentCard = cards[i];
                currentCount = 1;
            }
    
            //last card handled differently
            if (i == cards.length - 1) {
                products = processCurrentCardAndReset(products, currentCard, currentCount);
            }
        }
    
        for (Long product : products) {
            resultMap.put(product, recordValue);
        }
    
        return resultMap;
    }
    
    private static ArrayList<Long> processCurrentCardAndReset(ArrayList<Long> products, char card, int count) {
        ArrayList<Long> currentProducts = chooseNProducts(count, card);
        return multiplyLists(products, currentProducts);
    }

    //Given a card and a count, generate all possible combinations. 
    //I.e. if we have a full house we generate all possible 3 prime combinations and all possible 2 prime combinations
    //Then generate all of their products.
    public static ArrayList<Long> chooseNProducts(int count, char card) {
        ArrayList<Long> products = new ArrayList<>();
        int[] choices = primes.get(card);

        generateProducts(choices, count, 0, 1L, products);
        
        return products;
    }

    private static void generateProducts(int[] choices, int count, int start, long currentProduct, ArrayList<Long> products) {
        if (count == 0) {
            products.add(currentProduct);
            return;
        }
        
        for (int i = start; i <= choices.length - count; i++) {
            generateProducts(choices, count - 1, i + 1, currentProduct * choices[i], products);
        }
    }

    private static ArrayList<Long> multiplyLists(ArrayList<Long> l1, ArrayList<Long> l2) {
        // Handle edge cases
        if (l1.isEmpty() && l2.isEmpty()) {
            return new ArrayList<>();
        }
        if (l1.isEmpty()) {
            return new ArrayList<>(l2);
        }
        if (l2.isEmpty()) {
            return new ArrayList<>(l1);
        }
    
        // Multiply lists
        return l1.stream()
                 .flatMap(i -> l2.stream().map(j -> i * j))
                 .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void saveMapToFile(Map<Long, Integer> map, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(map);
        }
    }
}