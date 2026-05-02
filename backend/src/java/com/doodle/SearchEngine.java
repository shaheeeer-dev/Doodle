package src.java.com.doodle;

import java.io.*;
import java.util.*;

public class SearchEngine implements Serializable {
    private static final long serialVersionUID = 1L;

    private InvertedIndex invertedIndex;
    private Map<Integer, Document> documentStore;
    private int nextId;

    public SearchEngine() {
        this.invertedIndex = new InvertedIndex();
        this.documentStore = new HashMap<>();
        this.nextId = 1;
    }

    public void addDocument(String content, String title){
        if (content == null || content.trim().isEmpty()) {
            return;
        }
        int id = nextId++;

        title = (title == null) ? "" : title;
        Document doc =  new Document(id,content, title);

        documentStore.put(id, doc);
        invertedIndex.addDocument(id, title + " " + content);
    }

    public List<Document> search(String query){
        Map<Integer, Integer> scores = invertedIndex.searchMultipleWords(query);

        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(scores.entrySet());

        list.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
        List<Document> results = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : list) {
            Document doc = documentStore.get(entry.getKey());
            if (doc != null) {
                results.add(doc);
            }
        }

        return results;
    }

    public void save(String filename){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
            out.writeObject(this);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SearchEngine load(String filename) {
        try {
            File file = new File(filename);

            if (!file.exists() || file.length() == 0) {
                System.out.println("No data file found. Starting fresh engine.");
                return new SearchEngine();
            }

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            SearchEngine engine = (SearchEngine) in.readObject();
            in.close();
            return engine;

        } catch (Exception e) {
            System.out.println("Failed to load data file. Starting fresh engine.");
            return new SearchEngine();
        }
    }

    public int getTotalDocumentCount(){
        return documentStore.size();
    }
}