package ru.mail.polis.Korskov;

import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;
import ru.mail.polis.KVService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.NoSuchElementException;

public class MyService implements KVService {
    private static final String PREFIX = "id=";
    private static final String ERROR_ID = "Error in ID";
    private static final String ERROR_GET = "Error while sending";
    private static final String ERROR_DELETE = "Error during deletion";
    private static final String ERROR_PUT = "Error in the process of obtaining";
    @NotNull
    private final HttpServer server;
    @NotNull
    private final MyDAO dao;

    public MyService(int port, @NotNull final MyDAO dao) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.dao = dao;
        this.server.createContext("/v0/status", http -> {
            final String response = "ONLINE";
            http.sendResponseHeaders(200, response.length());
            http.getResponseBody().write(response.getBytes());
            http.close();
        });
        this.server.createContext("/v0/entity", http -> {
            final String id = extractId(http.getRequestURI().getQuery());
            if (id.isEmpty()){
                http.sendResponseHeaders(400, ERROR_ID.length());
                http.getResponseBody().write(ERROR_ID.getBytes());
                http.close();
                return;
            }
            switch (http.getRequestMethod()){
                case "GET":
                    final byte[] getValue;
                    try {
                        getValue = dao.get(id);
                    }catch (NoSuchElementException | IOException e) {
                        http.sendResponseHeaders(404, ERROR_GET.length());
                        http.getResponseBody().write(ERROR_GET.getBytes());
                        http.close();
                        return;
                    }
                    http.sendResponseHeaders(200, getValue.length);
                    http.getResponseBody().write(getValue);
                    break;
                case "DELETE":
                    try {
                        dao.delete(id);
                    }catch (IllegalArgumentException e) {
                        http.sendResponseHeaders(400, ERROR_DELETE.length());
                        http.getResponseBody().write(ERROR_DELETE.getBytes());
                        http.close();
                        return;
                    }
                    http.sendResponseHeaders(202, 0);
                    break;
                case "PUT":
                    final int contentLength = Integer.valueOf(http.getRequestHeaders().getFirst("Content-Length"));
                    final byte[] putValue = new byte[contentLength];
                    http.getRequestBody().read(putValue);
                    try {
                        dao.upsert(id, putValue);
                    }catch (IllegalArgumentException e) {
                        http.sendResponseHeaders(400, ERROR_PUT.length());
                        http.getResponseBody().write(ERROR_PUT.getBytes());
                        http.close();
                        return;
                    }
                    http.sendResponseHeaders(201, 0);
                    break;
                default:
                    http.sendResponseHeaders(405,0);
            }
            http.close();
        });
    }

    @NotNull
    private static String extractId(@NotNull final String query) {
        if(!query.startsWith(PREFIX)) {
            throw new IllegalArgumentException("Error");
        }
        return query.substring(PREFIX.length());
    }

    @Override
    public void start() {
        this.server.start();
    }

    @Override
    public void stop() {
        this.server.stop(0);
    }
}
