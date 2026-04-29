package test;

import src.java.com.doodle.InvertedIndex;

public class TestInvertedIndex {
    public static void main(String[] args) {
        InvertedIndex index = new InvertedIndex();

        index.addDocument(1, "Java is fast");
        index.addDocument(2, "Python is easy");
        index.addDocument(3, "Java and Python");

        System.out.println("Search 'java': " + index.search("java"));
        System.out.println("Search 'python': " + index.search("python"));

        System.out.println("\nMultiple word search:");
        System.out.println(index.searchMultipleWords("java python"));

        System.out.println("\nTotal unique words:");
    }
}
