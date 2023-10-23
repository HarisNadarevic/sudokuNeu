package at.merkur;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import static at.merkur.SudokuGeneratorJSON.isValidMove;

public class SudokuGeneratorCSV {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Du hast das CSV Format gewählt!");
    System.out.println("Wählen Sie die Größe des Sudokus (2x2, 3x3 oder 4x4):");

    String gridSize = scanner.nextLine().trim();
    int size;

    if (gridSize.equals("2x2")) {
      size = 2;
    } else if (gridSize.equals("3x3")) {
      size = 3;
    } else if (gridSize.equals("4x4")) {
      size = 4;
    } else {
      System.out.println("Ungültige Größe. Bitte wählen Sie 2x2, 3x3 oder 4x4.");
      return;
    }

    int[][] sudoku = generateSudoku(size, 0.1);
    String filename = "sudoku_" + gridSize + ".csv";

    printSudoku(sudoku);
    if (saveSudokuToCSV(sudoku, filename)) {
      System.out.println("Das Sudoku wurde erfolgreich generiert und in " + filename + " gespeichert.");
    } else {
      System.out.println("Fehler beim Speichern des Sudokus.");
    }
  }
  public static int[][] generateSudoku(int size, double emptyCellPercentage) {
    int gridSize = size * size;
    int[][] puzzle = new int[gridSize][gridSize];
    Random random = new Random();

    // befüllen nach Regeln
    if (fillSudoku(puzzle, random, gridSize)) {
      // 0 einfügen für den Prozentsatz
      int totalCells = gridSize * gridSize;
      int emptyCellsToKeep = (int) (totalCells * emptyCellPercentage);

      for (int i = 0; i < emptyCellsToKeep; i++) {
        int row, col;
        do {
          row = random.nextInt(gridSize);
          col = random.nextInt(gridSize);
        } while (puzzle[row][col] == 0);
        puzzle[row][col] = 0;
      }
    }
    return puzzle;
  }
  private static boolean fillSudoku(int[][] puzzle, Random random, int gridSize) {
    return fillSudoku(puzzle, random, 0, 0, gridSize);
  }
  private static boolean fillSudoku(int[][] puzzle, Random random, int row, int col, int gridSize) {
    if (row == gridSize) {
      return true; // Alle Zellen wurden gefüllt
    }

    int nextRow = (col == gridSize - 1) ? row + 1 : row;
    int nextCol = (col == gridSize - 1) ? 0 : col + 1;

    List<Integer> numbers = new ArrayList<>();
    for (int num = 1; num <= gridSize; num++) {
      numbers.add(num);
    }

    Collections.shuffle(numbers);

    for (int num : numbers) {
      if (isValidMove(puzzle, row, col, num)) {
        puzzle[row][col] = num;
        if (fillSudoku(puzzle, random, nextRow, nextCol, gridSize)) {
          return true;
        }
        puzzle[row][col] = 0;
      }
    }
    return false;
  }
  public static void printSudoku(int[][] sudoku) {
    for (int[] row : sudoku) {
      for (int num : row) {
        System.out.print(num + " ");
      }
      System.out.println();
    }
  }
  public static boolean saveSudokuToCSV(int[][] sudoku, String filename) {
    try (FileWriter writer = new FileWriter(filename)) {
      for (int[] row : sudoku) {
        for (int num : row) {
          writer.write(num + ",");
        }
        writer.write("\n");
      }
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }
}