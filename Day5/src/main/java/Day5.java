import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class Day5 {
    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File(Objects.requireNonNull(Day5.class.getClassLoader()
                .getResource("input.txt")).getFile());

        Scanner scanner = new Scanner(inputFile);

        String input = scanner.hasNext() ? scanner.nextLine() : "";

        System.out.println("--- Day 5: Alchemical Reduction ---");

        System.out.println();

        System.out.println(
                "Part One - number of unit remaining after fully reacting the polymer input :"
        );
        System.out.println(getRemainingUnit(input));

        System.out.println();

        System.out.println(
                "Part Two - length of the shortest polymer produce by removing exactly one type unit :"
        );
        System.out.println(getShortestPolymer(input));
    }

    private static int getRemainingUnit(String input) {

        StringBuilder polymer = new StringBuilder(input);
        boolean stringReacted;

        do {
            stringReacted = false;
            String lastUnit = "";
            for(int i = polymer.length(); i > 0; i--) {
                String unit = String.valueOf(polymer.charAt(i-1));
                if (!lastUnit.isEmpty()
                        && lastUnit.codePointAt(0) != unit.codePointAt(0)
                        && lastUnit.toLowerCase().equals(unit.toLowerCase())) {
                    polymer.deleteCharAt(i);
                    polymer.deleteCharAt(i-1);
                    lastUnit = "";
                    stringReacted = true;
                } else {
                    lastUnit = String.valueOf(polymer.charAt(i-1));
                }
            }
        } while (stringReacted);

        return polymer.length();
    }

    private static int getShortestPolymer(String input) {
        int shortestPolymer = input.length();

        String[] alphabet = "abcdefghijklmnopqrstuvwxyz".split("");

        for (String letter : alphabet) {
            String newInput = input.replaceAll(letter.toLowerCase() + "|" + letter.toUpperCase(),"");
            int remainingUnit = getRemainingUnit(newInput);
            if (remainingUnit < shortestPolymer) {
                shortestPolymer = remainingUnit;
            }
        }

        return shortestPolymer;
    }
}