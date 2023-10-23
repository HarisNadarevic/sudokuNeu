package at.merkur;
import java.util.Scanner;

public class Start {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("In welchem Format möchtest du ein Sudoku erstellen?");
    System.out.println("Gib 'csv' für CSV oder 'json' für JSON ein:");

    String format = scanner.nextLine().toLowerCase();

    if (format.equals("csv")) {
      SudokuGeneratorCSV.main(null);
    } else if (format.equals("json")) {
      SudokuGeneratorJSON.main(null);
    } else {
      System.out.println("Ungültiges Format. Bitte gib 'csv' oder 'json' ein.");
    }
    scanner.close();
  }
}
