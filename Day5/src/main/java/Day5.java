import java.io.*;
import java.util.Arrays;
import java.util.OptionalInt;

public class Day5 {
    public static void main(String[] args) throws FileNotFoundException {

        String[] input;

        if (args.length == 0) {
            InputStream inputStream = Day5.class.getResourceAsStream("input_day5.txt");
            input = new BufferedReader(new InputStreamReader(inputStream)).lines().toArray(String[]::new);
        } else {
            input = new BufferedReader(new FileReader(args[0])).lines().toArray(String[]::new);
        }

        long startTime = System.currentTimeMillis();

        System.out.println("--- Day 5: Alchemical Reduction ---");

        System.out.println();

        System.out.println(
                "Part One - number of unit remaining after fully reacting the polymer input :"
        );
        System.out.println(getRemainingUnit(input[0]));

        System.out.println();

        System.out.println(
                "Part Two - length of the shortest polymer produce by removing exactly one type unit :"
        );
        System.out.println(getShortestPolymer(input[0]));

        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println("Finished in " + (float) (endTime - startTime) / 1000 + " s.");

        System.out.println();
    }

    private static int getRemainingUnit(String input) {

        StringBuilder polymer = new StringBuilder(input);
        boolean stringReacted;

        do {

            stringReacted = false;
            String lastUnit = "";

            for (int i = polymer.length(); i > 0; i--) {

                String unit = String.valueOf(polymer.charAt(i - 1));

                if (!lastUnit.isEmpty()
                        && lastUnit.codePointAt(0) != unit.codePointAt(0)
                        && lastUnit.toLowerCase().equals(unit.toLowerCase())) {
                    polymer.deleteCharAt(i);
                    polymer.deleteCharAt(i - 1);
                    lastUnit = "";
                    stringReacted = true;
                } else {
                    lastUnit = String.valueOf(polymer.charAt(i - 1));
                }

            }

        } while (stringReacted);

        return polymer.length();
    }

    private static int getShortestPolymer(String input) {

        OptionalInt result = Arrays.stream("abcdefghijklmnopqrstuvwxyz".split(""))
                .parallel()
                .mapToInt(letter ->
                        getRemainingUnit(input.replaceAll(letter.toLowerCase() + "|" + letter.toUpperCase(), ""))
                ).min();

        return result.isPresent() ? result.getAsInt() : input.length();
    }
}