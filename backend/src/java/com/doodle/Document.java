package src.java.com.doodle;

public class Document {
    private int id;
    private String content;
    private String title;

    public Document(int id, String content, String title) {
        this.id = id;
        this.content = content;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
