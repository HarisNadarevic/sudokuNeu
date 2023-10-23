package at.merkur;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.Executors;

public class SudokuHttpServer {

  public static void main(String[] args) throws IOException {
    int port = 7777;


    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/start", new StartHandler());
    server.createContext("/convert", new ConvertHandler());
    server.createContext("/solve", new SolveHandler());
    server.createContext("/solveOneStep", new SolveOneStepHandler());
    server.start();
    System.out.println("HTTP-Server gestartet auf Port " + port);
  }

  static class StartHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      File[] files = new File(".").listFiles();
      StringBuilder response = new StringBuilder();
      for (File file : files) {
        if (file.isFile()) {
          String fileName = file.getName();
          if (fileName.startsWith("sudoku") && (file.getName().endsWith(".json") || file.getName().endsWith(".csv"))) {
            response.append("Dateiname: ").append(file.getName()).append("\n");

            String fileContent = readFileContent(file);
            response.append("Inhalt:\n").append(fileContent).append("\n");
          }
        }
      }
      Headers headers = exchange.getResponseHeaders();
      headers.set("Content-Type", "text/plain");
      exchange.sendResponseHeaders(200, response.length());
      OutputStream os = exchange.getResponseBody();
      os.write(response.toString().getBytes());
      os.close();
    }
    private String readFileContent(File file) throws IOException {
      StringBuilder content = new StringBuilder();
      List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
      for (String line : lines) {
        content.append(line).append("\n");
      }
      return content.toString();
    }
  }
  static class ConvertHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      String response;
      String contentType;

      // Überprüfe, ob die Datei "output.csv" oder "output.json" existiert
      if (Files.exists(Paths.get("output.csv"))) {
        response = readFileContent("output.csv");
        contentType = "text/csv";
      } else if (Files.exists(Paths.get("output.json"))) {
        response = readFileContent("output.json");
        contentType = "application/json";
      } else {
        response = "Keine Ausgabedatei gefunden.";
        contentType = "text/plain";
      }
      Headers headers = exchange.getResponseHeaders();
      headers.set("Content-Type", contentType);
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
      OutputStream os = exchange.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
    private String readFileContent(String filename) throws IOException {
      StringBuilder content = new StringBuilder();
      List<String> lines = Files.readAllLines(Paths.get(filename));
      for (String line : lines) {
        content.append(line).append("\n");
      }
      return content.toString();
    }
  }
  static class SolveHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      String response = null;
      String contentType = null;

      // Verzeichnis, in dem nach den Dateien gesucht wird
      File directory = new File(".");

      // Alle Dateien im Verzeichnis auflisten
      File[] files = directory.listFiles();

      if (files != null) {
        for (File file : files) {
          if (file.isFile()) {
            String fileName = file.getName();
            if (fileName.startsWith("solved") && (fileName.endsWith(".csv") || fileName.endsWith(".json"))) {
              // Datei gefunden, deren Name mit "solved" beginnt und mit .csv oder .json endet
              response = readFileContent(fileName);
              if (fileName.endsWith(".csv")) {
                contentType = "text/csv";
              } else if (fileName.endsWith(".json")) {
                contentType = "application/json";
              }
              break; // Die erste passende Datei wird verwendet
            }
          }
        }
      }
      if (response == null) {
        response = "Keine passende gelöste Sudoku-Datei gefunden.";
        contentType = "text/plain";
      }
      Headers headers = exchange.getResponseHeaders();
      headers.set("Content-Type", contentType);
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
      OutputStream os = exchange.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
    private String readFileContent(String filename) throws IOException {
      StringBuilder content = new StringBuilder();
      List<String> lines = Files.readAllLines(Paths.get(filename));
      for (String line : lines) {
        content.append(line).append("\n");
      }
      return content.toString();
    }
  }
  static class SolveOneStepHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      // Hier das sudoku meiner ein schritt lösung aufrufen
    }
  }
}