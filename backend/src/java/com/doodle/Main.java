package src.java.com.doodle;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    private static SearchEngine engine;
    private static ConfigLoader config;
    private static int port;
    private static String dataFile;

    public static void main(String[] args) throws IOException {
        config = new ConfigLoader("backend/src/resources/config.properties");

        port = config.getInt("server.port", 8080);
        dataFile = config.get("data.file");

        File dataDir = new File(dataFile).getParentFile();
        if (dataDir != null && !dataDir.exists()) {
            dataDir.mkdirs();
            System.out.println("Created data directory: " + dataDir.getPath());
        }

        engine = SearchEngine.load(dataFile);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/add", new AddDocumentHandler());
        server.createContext("/search", new SearchQueryHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server started on port " + port);
        System.out.println("Data file: " + new File(dataFile).getAbsolutePath());
    }

    static class AddDocumentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange request) throws IOException {

            if (request.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                request.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                request.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
                request.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                request.sendResponseHeaders(204, -1);
                return;
            }

            if (!request.getRequestMethod().equalsIgnoreCase("POST")) {
                sendJson(request, 405, "{\"error\":\"only POST allowed\"}");
                return;
            }

            String body = new String(request.getRequestBody().readAllBytes());

            String content = extractValue(body, "content");
            String title = extractValue(body, "title");

            if (content.isEmpty() || title.isEmpty()) {
                sendJson(request, 400, "{\"error\":\"title and content are required\"}");
                return;
            }

            engine.addDocument(content, title);
            engine.save(dataFile);

            System.out.println("Document added: \"" + title + "\" -> saved to " + dataFile);

            sendJson(request, 200, "{\"status\":\"document added\"}");
        }
    }

    static class SearchQueryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange request) throws IOException {

            if (request.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                request.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                request.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                request.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                request.sendResponseHeaders(204, -1);
                return;
            }

            if (!request.getRequestMethod().equalsIgnoreCase("GET")) {
                sendJson(request, 405, "{\"error\":\"only GET allowed\"}");
                return;
            }

            String query = request.getRequestURI().getQuery();
            if (query == null || !query.startsWith("q=")) {
                sendJson(request, 400, "{\"error\":\"missing query\"}");
                return;
            }

            String searchText = query.substring(2);
            searchText = URLDecoder.decode(searchText, StandardCharsets.UTF_8);

            List<Document> results = engine.search(searchText);

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < results.size(); i++) {
                Document doc = results.get(i);
                json.append("{")
                        .append("\"id\":").append(doc.getId()).append(",")
                        .append("\"title\":\"").append(doc.getTitle()).append("\",")
                        .append("\"content\":\"").append(doc.getContent()).append("\"")
                        .append("}");
                if (i < results.size() - 1) json.append(",");
            }
            json.append("]");

            sendJson(request, 200, json.toString());
        }
    }

    private static void sendJson(HttpExchange request, int statusCode, String response) throws IOException {
        request.getResponseHeaders().add("Content-Type", "application/json");
        request.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        request.sendResponseHeaders(statusCode, bytes.length);

        OutputStream os = request.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private static String extractValue(String body, String key) {
        String pattern = "\"" + key + "\":\"";
        int start = body.indexOf(pattern);
        if (start == -1) return "";
        start += pattern.length();
        int end = body.indexOf("\"", start);
        if (end == -1) return "";
        return body.substring(start, end);
    }
}
