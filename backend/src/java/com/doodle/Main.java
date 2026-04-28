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


        server.setExecutor(null);
        server.start();

        System.out.println("Server started on port 8080");
    }
}