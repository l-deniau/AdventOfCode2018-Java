import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Day2 {

    public static void main(String[] args) throws FileNotFoundException {

        String[] input;

        if (args.length == 0) {
            InputStream inputStream = Day2.class.getResourceAsStream("input_day2.txt");
            input = new BufferedReader(new InputStreamReader(inputStream)).lines().toArray(String[]::new);
        } else {
            input = new BufferedReader(new FileReader(args[0])).lines().toArray(String[]::new);
        }

        long startTime = System.currentTimeMillis();

        System.out.println("--- Day 2: Inventory Management System ---");

        System.out.println();

        System.out.println("Part One - The checksum :");
        System.out.println(getChecksum(input));

        System.out.println();

        System.out.println("Part Two - Common letters between the two correct box IDs :");
        System.out.println(getCommonLetters(input));

        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println("Finished in " + (float) (endTime - startTime) / 1000 + " s.");

        System.out.println();
    }

    private static int getChecksum(String[] input) {

        int twoTimes = 0;
        int treeTimes = 0;

        for (String line : input) {

            char[] lineLetters = line.toCharArray();
            Multiset<Character> commonLetters = HashMultiset.create();
            boolean twoTimesFound = false;
            boolean treeTimesFound = false;

            for (char letter : lineLetters) {
                commonLetters.add(letter);
            }

            for (Multiset.Entry<Character> commonLetter : commonLetters.entrySet()) {
                if (commonLetter.getCount() == 2) {
                    twoTimesFound = true;
                } else if (commonLetter.getCount() == 3) {
                    treeTimesFound = true;
                }
            }

            if (twoTimesFound) {
                twoTimes++;
            }
            if (treeTimesFound) {
                treeTimes++;
            }
        }

        return twoTimes * treeTimes;
    }

    private static String getCommonLetters(String[] input) {

        String result = null;

        List<String> lineList = new ArrayList<>();
        boolean foundFlag = false;

        for (String line : input) {
            for (String lineToCompare : lineList) {

                int differentLettersCount = 0;
                int lastIndexOfDifferentChar = 0;

                for (int index = 0;
                    // In case the two lines have different length
                     index < (line.length() <= lineToCompare.length() ? line.length() : lineToCompare.length());
                     index++
                ) {
                    // Comparing letters
                    if (line.charAt(index) != lineToCompare.charAt(index)) {
                        differentLettersCount++;
                        lastIndexOfDifferentChar = index;
                    }
                }

                if (differentLettersCount == 1) {
                    result = new StringBuilder(line).deleteCharAt(lastIndexOfDifferentChar).toString();
                    foundFlag = true;
                    break;
                }
            }

            if (foundFlag) {
                break;
            } else {
                lineList.add(line);
            }
        }

        return result;
    }
}
