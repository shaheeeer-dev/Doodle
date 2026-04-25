package test;

import src.java.com.doodle.Document;

class TestDocument {
    public static void main(String[] args) {
        Document doc = new Document(1, "Java is fast", "Java Intro");

        System.out.println("ID: " + doc.getId());
        System.out.println("Content: " + doc.getContent());
        System.out.println("Title: " + doc.getTitle());

        System.out.println("\nFull Object:");
        System.out.println(doc);
    }
}