package src.java.com.doodle;

import java.io.*;
import java.util.*;

public class SearchEngine implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private InvertedIndex invertedIndex;
    private Map<Integer, Document> documentStore;
    private int nextId;

    public SearchEngine() {
        invertedIndex = new InvertedIndex();
        documentStore = new HashMap<>();
        nextId = 1;
    }


    public void addDocument(String content, String title) {
        if (content == null || content.trim().isEmpty()) {
            return;
        }

        int id = nextId++;
        Document doc = new Document(id, content, title);

        documentStore.put(id, doc);

        String combinedText = content + " " + (title != null ? title : "");
        invertedIndex.addDocument(id, combinedText);
    }

    //Search (ranked)
    public List<Document> search(String query) {
        Map<Integer, Integer> scores = invertedIndex.searchMultipleWords(query);

        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(scores.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        List<Document> results = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : list) {
            Document doc = documentStore.get(entry.getKey());
            if (doc != null) {
                results.add(doc);
            }
        }

        return results;
    }

    public void save(String path) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SearchEngine load(String path) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            return (SearchEngine) in.readObject();
        } catch (Exception e) {
            return new SearchEngine();
        }
    }

    public Document getDocument(int id) {
        return documentStore.get(id);
    }

    public int getTotalDocuments() {
        return documentStore.size();
    }
}