package test;

import src.java.com.doodle.*;

public class TestSearchEngine {
    public static void main(String[] args) {

        SearchEngine engine = new SearchEngine();

        engine.addDocument("Java is fast", "Java Intro");
        engine.addDocument("Python is easy", "Python Guide");
        engine.addDocument("Java and Python both popular", "Comparison");

        System.out.println("Search: java");
        for (Document d : engine.search("java")) {
            System.out.println(d);
        }

        System.out.println("\nSearch: python");
        for (Document d : engine.search("python")) {
            System.out.println(d);
        }

        System.out.println("\nSearch: java python");
        for (Document d : engine.search("java python")) {
            System.out.println(d);
        }

        engine.save("data/data.ser");

        SearchEngine loaded = SearchEngine.load("data/data.ser");

        System.out.println("\nAfter loading from file:");
        for (Document d : loaded.search("java")) {
            System.out.println(d);
        }
    }
}