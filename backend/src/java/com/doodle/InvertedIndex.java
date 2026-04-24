package src.java.com.doodle;

import java.util.*;

public class InvertedIndex {
    private Map<String, List<Integer>> index;

    public InvertedIndex() {
        this.index = new HashMap<>();
    }

    public void addDocument(int docId, String content) {
        List<String> tokens = tokenize(content);

        for (String word : tokens) {
            index.putIfAbsent(word, new ArrayList<>());

            if (!index.get(word).contains(docId)) {
                index.get(word).add(docId);
            }
        }
    }

    public List<Integer> search(String query) {
        String word = query.toLowerCase().trim();
        return index.getOrDefault(word, new ArrayList<>());
    }

    public Map<Integer, Integer> searchMultipleWords(String query) {
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

    public int getWordCount() {
        return index.size();
    }

    public List<Integer> getPostings(String word) {
        return search(word);
    }

    private List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();

        String[] words = text.toLowerCase().split("[^a-zA-Z]+");

        for (String word : words) {
            if (!word.isEmpty()) {
                tokens.add(word);
            }
        }

        return tokens;
    }
}