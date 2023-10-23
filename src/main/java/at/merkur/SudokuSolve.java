package at.merkur;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SudokuSolve {


public static void main(String[] args) throws IOException {

  Scanner scanner = new Scanner(System.in);

  System.out.println("Welches Sudoku möchtest du lösen?");
  System.out.println("Gib den Dateinamen des Sudoku (z.B. 'sudoku_2x2.json' oder 'sudoku_3x3.csv') ein:");
  String filename = scanner.nextLine();

  int[][] sudoku = readSudokuFromFile(filename);

  if (sudoku == null) {
    System.out.println("Konnte das Sudoku nicht aus der Datei lesen.");
    return;
  }

  boolean resolved = solve(sudoku);

    if (resolved) {
      System.out.println("Das Sudoku wurde gelöst:");
      printSudoku(sudoku); // Gebe gitter aus

      String outputFilename = "solved_" + new File(filename).getName();
      if (filename.endsWith(".json")) {
        saveSudokuToJSON(sudoku, outputFilename);
      } else if (filename.endsWith(".csv")) {
        saveSudokuToCSV(sudoku, outputFilename);
      }

      System.out.println("Das gelöste Sudoku wurde als '" + outputFilename + "' gespeichert.");
    } else {
      System.out.println("Konnte das Sudoku nicht lösen.");
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

  private static boolean solve(int[][] sudoku) {

    int gridSize = sudoku.length;

    for (int row = 0; row < gridSize; row++) {
      for (int col = 0; col < gridSize; col++) {
        if (sudoku[row][col] == 0) {
          for (int num = 1; num <= gridSize; num++) {
            if (isValidMove(sudoku, row, col, num)) {
              sudoku[row][col] = num;

              if (solve(sudoku)) {
                return true; // Sudoku wurde gelöst
              }

              sudoku[row][col] = 0; // Zurücksetzen
            }
          }
          return false; //
        }
      }
    }
    return true; // Sudokugelöst
  }
  private static boolean isValidMove(int[][] sudoku, int row, int col, int num) {
    int gridSize = sudoku.length;
    for (int i = 0; i < gridSize; i++) {
      if (sudoku[row][i] == num || sudoku[i][col] == num) {
        return false;
      }
    }

    int subGridSize = (int) Math.sqrt(gridSize);
    int subGridRow = row - row % subGridSize;
    int subGridCol = col - col % subGridSize;
    for (int i = subGridRow; i < subGridRow + subGridSize; i++) {
      for (int j = subGridCol; j < subGridCol + subGridSize; j++) {
        if (sudoku[i][j] == num) {
          return false;
        }
      }
    }
    return true;
  }
  private static void printSudoku(int[][] sudoku) {
    for (int[] row : sudoku) {
      for (int num : row) {
        System.out.print(num + " ");
      }
      System.out.println();
    }
  }
  private static void saveSudokuToJSON(int[][] sudoku, String filename) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.writeValue(new File(filename), sudoku);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  private static void saveSudokuToCSV(int[][] sudoku, String filename) {
    try (FileWriter writer = new FileWriter(filename);
      CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
      for (int[] row : sudoku) {
        List<String> rowValues = new ArrayList<>();
        for (int num : row) {
          rowValues.add(Integer.toString(num)); // Integer in String umwandeln
        }
        csvPrinter.printRecord(rowValues);
      }
      csvPrinter.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}