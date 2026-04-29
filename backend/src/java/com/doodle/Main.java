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

    public static void main(String[] args) throws IOException {
        engine = SearchEngine.load("data/data.ser");

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/add", new AddDocumentHandler());
        server.createContext("/search", new SearchQueryHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server started on port 8080");
    }

    static class AddDocumentHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange request) throws IOException {

            if (!request.getRequestMethod().equalsIgnoreCase("POST")) {
                sendJson(request, "{\"error\":\"only POST allowed\"}");
                return;
            }

            String requestBody = new String(request.getRequestBody().readAllBytes());

            String documentContent = extractValue(requestBody, "content");
            String documentTitle = extractValue(requestBody, "title");

            engine.addDocument(documentContent, documentTitle);
            engine.save("data/data.ser");

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

            String queryString = request.getRequestURI().getQuery();

            if (queryString == null || !queryString.startsWith("q=")) {
                sendJson(request, "{\"error\":\"missing query\"}");
                return;
            }

            String searchText = queryString.substring(2);
            searchText = URLDecoder.decode(searchText, StandardCharsets.UTF_8);

            List<Document> matchedDocuments = engine.search(searchText);

            StringBuilder jsonResponse = new StringBuilder("[");
            for (int i = 0; i < matchedDocuments.size(); i++) {

                Document doc = matchedDocuments.get(i);

                jsonResponse.append("{")
                        .append("\"id\":").append(doc.getId()).append(",")
                        .append("\"title\":\"").append(doc.getTitle()).append("\",")
                        .append("\"content\":\"").append(doc.getContent()).append("\"")
                        .append("}");

                if (i < matchedDocuments.size() - 1) {
                    jsonResponse.append(",");
                }
            }
            jsonResponse.append("]");

            sendJson(request, jsonResponse.toString());
        }
    }

    private static void sendJson(HttpExchange request, String response) throws IOException {
        request.getResponseHeaders().add("Content-Type", "application/json");
        request.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        request.sendResponseHeaders(200, response.getBytes().length);

        OutputStream output = request.getResponseBody();
        output.write(response.getBytes());
        output.close();
    }

    private static String extractValue(String body, String key) {
        String pattern = "\"" + key + "\":\"";
        int startIndex = body.indexOf(pattern);

        if (startIndex == -1) return "";

        startIndex += pattern.length();

        int endIndex = body.indexOf("\"", startIndex);

        if (endIndex == -1) return "";

        return body.substring(startIndex, endIndex);
    }
}