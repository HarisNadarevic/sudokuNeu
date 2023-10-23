package at.merkur;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SudokuConvert {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Gib den Dateinamen des gespeicherten Sudokus ein:");
    String inputFilename = scanner.nextLine();

    int[][] sudoku = readSudokuFromFile(inputFilename);

    if (sudoku == null) {
      System.out.println("Konnte das Sudoku nicht aus der Datei lesen.");
      return;
    }
    System.out.println("In welches Format möchtest du das Sudoku konvertieren? (CSV oder JSON):");
    String exportFormat = scanner.nextLine().trim().toLowerCase();

    if (exportFormat.equals("csv")) {
      System.out.println("Sudoku im CSV-Format:");
      printSudokuAsCSV(sudoku, "output.csv");
    } else if (exportFormat.equals("json")) {
      System.out.println("Sudoku im JSON-Format:");
      printSudokuAsJSON(sudoku, "output.json");
    } else {
      System.out.println("Ungültiges Format. Bitte gib 'CSV' oder 'JSON' ein.");
    }
    scanner.close();
  }
  public static int[][] readSudokuFromFile(String filename) {
    try {
      if (filename.endsWith(".json")) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filename), int[][].class);
      } else if (filename.endsWith(".csv")) {
        List<String> lines = Files.readAllLines(Paths.get(filename));
        int size = lines.size();
        int[][] puzzle = new int[size][size];

        for (int i = 0; i < size; i++) {
          String[] values = lines.get(i).split(",");
          for (int j = 0; j < size; j++) {
            puzzle[i][j] = Integer.parseInt(values[j]);
          }
        }
        return puzzle;
      } else {
        System.out.println("Unbekannter Dateityp: " + filename);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  public static void printSudokuAsCSV(int[][] sudoku, String outputFilename) {
    try {
      PrintWriter writer = new PrintWriter(outputFilename);
      CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

      for (int[] row : sudoku) {
        List<String> rowValues = new ArrayList<>();
        for (int num : row) {
          rowValues.add(Integer.toString(num));
        }
        csvPrinter.printRecord(rowValues);
      }
      csvPrinter.flush();
      System.out.println("Die CSV-Ausgabe wurde in '" + outputFilename + "' gespeichert.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public static void printSudokuAsJSON(int[][] sudoku, String outputFilename) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFilename), sudoku);
      System.out.println("Das JSON-Sudoku wurde in '" + outputFilename + "' gespeichert.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}