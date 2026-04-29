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

        engine = SearchEngine.load(dataFile);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/add", new AddDocumentHandler());
        server.createContext("/search", new SearchQueryHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server started on port " + port);
    }

    static class AddDocumentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange request) throws IOException {

            if (!request.getRequestMethod().equalsIgnoreCase("POST")) {
                sendJson(request, "{\"error\":\"only POST allowed\"}");
                return;
            }

            String body = new String(request.getRequestBody().readAllBytes());

            String content = extractValue(body, "content");
            String title = extractValue(body, "title");

            engine.addDocument(content, title);
            engine.save(dataFile);

            sendJson(request, "{\"status\":\"document added\"}");
        }
    }

    static class SearchQueryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange request) throws IOException {
            if (!request.getRequestMethod().equalsIgnoreCase("GET")) {
                sendJson(request, "{\"error\":\"only GET allowed\"}");
                return;
            }
            String query = request.getRequestURI().getQuery();
            if (query == null || !query.startsWith("q=")) {
                sendJson(request, "{\"error\":\"missing query\"}");
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

            sendJson(request, json.toString());
        }
    }

    private static void sendJson(HttpExchange request, String response) throws IOException {
        request.getResponseHeaders().add("Content-Type", "application/json");
        request.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        request.sendResponseHeaders(200, response.getBytes().length);

        OutputStream os = request.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static String extractValue(String body, String key) {
        String pattern = "\"" + key + "\":\"";
        int start = body.indexOf(pattern);
        if (start == -1) {
            return "";
        }
        start += pattern.length();
        int end = body.indexOf("\"", start);
        if (end == -1) {
            return "";
        }
        return body.substring(start, end);
    }
}