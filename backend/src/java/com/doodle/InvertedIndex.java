package src.java.com.doodle;

import java.io.Serializable;
import java.util.*;

public class InvertedIndex implements Serializable{
    private static final long serialVersionUID = 1L;
    private Map<String, List<Integer>> index;

    public InvertedIndex(){
        this.index = new HashMap<>();
    }

    private List<String> tokenize(String text){
        List<String> tokens = new ArrayList<>();
        String[] words = text.toLowerCase().split("[^a-zA-Z]+");

        for (String word : words) {
            if (!word.isEmpty()) {
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