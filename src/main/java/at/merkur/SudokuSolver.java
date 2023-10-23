//package at.merkur;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.csv.*;
//import java.io.*;
//import java.nio.file.*;
//import java.util.*;
//import java.util.stream.*;
//
//public class SudokuSolver {
//
//  static int[][] board =  readSudokuFromFile(String filename)
//
//
//  private static final int BOARD_SIZE = 9;
//  private static final int SUBSECTION_SIZE = 3;
//
//  private static final int BOARD_START_INDEX = 0;
//  private static final int NO_VALUE = 0;
//  private static final int MIN_VALUE = 1;
//  private static final int MAX_VALUE = 9;
//
//  public static void main(String[] args) throws IOException {
//    Scanner scanner = new Scanner(System.in);
//
//    System.out.println("Welches Sudoku möchtest du lösen?");
//    System.out.println("Gib den Dateinamen des Sudoku (z.B. 'sudoku_2x2.json' oder 'sudoku_3x3.csv') ein:");
//    String filename = scanner.nextLine();
//
//    int[][] sudoku = readSudokuFromFile(filename);
//
//    if (sudoku == null) {
//      System.out.println("Konnte das Sudoku nicht aus der Datei lesen.");
//      return;
//    }
//
//    boolean resolved = solve(sudoku);
//
//    if (resolved) {
//      System.out.println("Das Sudoku wurde gelöst:");
//      printBoard(sudoku);
//
//      String outputFilename = "solved_" + new File(filename).getName();
//      if (filename.endsWith(".json")) {
//        saveSudokuToJSON(sudoku, outputFilename);
//      } else if (filename.endsWith(".csv")) {
//        saveSudokuToCSV(sudoku, outputFilename);
//      }
//
//      System.out.println("Das gelöste Sudoku wurde als '" + outputFilename + "' gespeichert.");
//    } else {
//      System.out.println("Konnte das Sudoku nicht lösen.");
//    }
//
//    scanner.close();
//  }
//
//  private static int[][] readSudokuFromFile(String filename) {
//    try {
//      if (filename.endsWith(".json")) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readValue(new File(filename), int[][].class);
//      } else if (filename.endsWith(".csv")) {
//        List<String> lines = Files.readAllLines(Paths.get(filename));
//        int size = lines.size();
//        int[][] puzzle = new int[size][size];
//
//        for (int i = 0; i < size; i++) {
//          String[] values = lines.get(i).split(",");
//          for (int j = 0; j < size; j++) {
//            puzzle[i][j] = Integer.parseInt(values[j]);
//          }
//        }
//
//        return puzzle;
//      } else {
//        System.out.println("Unbekannter Dateityp: " + filename);
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    return null;
//  }
//
//  private static boolean solve(int[][] board) {
//    for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
//      for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
//        if (board[row][column] == NO_VALUE) {
//          for (int k = MIN_VALUE; k <= MAX_VALUE; k++) {
//            board[row][column] = k;
//            if (isValid(board, row, column) && solve(board)) {
//              return true;
//            }
//            board[row][column] = NO_VALUE;
//          }
//          return false;
//        }
//      }
//    }
//    return true;
//  }
//
//  private static boolean isValid(int[][] board, int row, int column) {
//    return (rowConstraint(board, row)
//            && columnConstraint(board, column)
//            && subsectionConstraint(board, row, column));
//  }
//
//  private static boolean rowConstraint(int[][] board, int row) {
//    boolean[] constraint = new boolean[BOARD_SIZE];
//    return IntStream.range(BOARD_START_INDEX, BOARD_SIZE)
//                    .allMatch(column -> checkConstraint(board, row, constraint, column));
//  }
//
//  private static boolean columnConstraint(int[][] board, int column) {
//    boolean[] constraint = new boolean[BOARD_SIZE];
//    return IntStream.range(BOARD_START_INDEX, BOARD_SIZE)
//                    .allMatch(row -> checkConstraint(board, row, constraint, column));
//  }
//
//  private static boolean subsectionConstraint(int[][] board, int row, int column) {
//    boolean[] constraint = new boolean[BOARD_SIZE];
//    int subsectionRowStart = (row/SUBSECTION_SIZE)*SUBSECTION_SIZE;
//    int subsectionRowEnd = subsectionRowStart + SUBSECTION_SIZE;
//
//    int subsectionColumnStart = (column/SUBSECTION_SIZE)*SUBSECTION_SIZE;
//    int subsectionColumnEnd = subsectionColumnStart + SUBSECTION_SIZE;
//
//    for (int r = subsectionRowStart; r < subsectionRowEnd; r++) {
//      for (int c = subsectionColumnStart; c < subsectionColumnEnd; c++) {
//        if (!checkConstraint(board, r, constraint, c)) return false;
//      }
//    }
//    return true;
//  }
//
//  private static boolean checkConstraint(
//    int[][] board,
//    int row,
//    boolean[] constraint,
//    int column) {
//    if (board[row][column] != NO_VALUE) {
//      if (!constraint[board[row][column] - 1]) {
//        constraint[board[row][column] - 1] = true;
//      } else {
//        return false;
//      }
//    }
//    return true;
//  }
//
////  int subGridSize = (int) Math.sqrt(gridSize);
////  int subGridRow = row - row%subGridSize;
////  int subGridCol = col - col%subGridSize;
////      for(
////  int i = subGridRow;
////  i<subGridRow +subGridSize;i++)
////
////  {
////    for (int j = subGridCol; j < subGridCol + subGridSize; j++) {
////      if (sudoku[i][j] == num) {
////        return false;
////      }
////    }
////  }
////
////      return true;
////}
//
//  private static void printBoard(final int[][] sudoku) {
//    for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
//      for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
//        System.out.print(board[row][column] + " ");
//      }
//      System.out.println();
//    }
//  }
//  public static void saveSudokuToJSON(int[][] sudoku, String filename) {
//    try {
//      ObjectMapper objectMapper = new ObjectMapper();
//      objectMapper.writeValue(new File(filename), sudoku);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//  public static void saveSudokuToCSV(int[][] sudoku, String filename) {
//    try (FileWriter writer = new FileWriter(filename);
//      CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
//      for (int[] row : sudoku) {
//        List<String> rowValues = Arrays.stream(row)
//                                       .mapToObj(Integer::toString)
//                                       .collect(Collectors.toList());
//        csvPrinter.printRecord(rowValues);
//      }
//      csvPrinter.flush();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//}