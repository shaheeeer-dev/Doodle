package src.java.com.doodle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.*;

public class InvertedIndex implements Serializable{
    private static final long serialVersionUID = 1L;
    private Map<String, List<Integer>> index;
    private Set<String> stopWords;

    public InvertedIndex(){
        this.index = new HashMap<>();
        this.stopWords = loadStopWords("backend/src/resources/stopwords.txt");
    }

    private Set<String> loadStopWords(String filePath) {
        Set<String> words = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        } catch (Exception e) {
            System.out.println("Stopwords file not found, continuing without filtering.");
        }

        return words;
    }

    private List<String> tokenize(String text){
        List<String> tokens = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\W+");

        for (String word : words) {
            if (!word.isEmpty() && !stopWords.contains(word)) {
                tokens.add(word);
            }
        }
        return tokens;
    }

    public void addDocument(int docId, String content){
        List<String> tokens = tokenize(content);

        for (String word : tokens) {
            index.putIfAbsent(word, new ArrayList<>());

            if (!index.get(word).contains(docId)) {
                index.get(word).add(docId);
            }
        }
    }

    public List<Integer> search(String query){
        List<String> tokens = tokenize(query);
        if (tokens.isEmpty()){
            return new ArrayList<>();
        }

        return index.getOrDefault(tokens.get(0), new ArrayList<>());
    }

    public Map<Integer, Integer> searchMultipleWords(String query){
        Map<Integer, Integer> result = new HashMap<>();
        List<String> words = tokenize(query);

        for (String word : words) {
            List<Integer> docs = index.get(word);

            if (docs != null) {
                for (int docId : docs) {
                    result.put(docId, result.getOrDefault(docId, 0) + 1);
                }
            }
        }

        return result;
    }
}