import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Day1 {

    public static void main(String[] args) throws FileNotFoundException {

        String[] input;

        if (args.length == 0) {
            InputStream inputStream = Day1.class.getResourceAsStream("input_day1.txt");
            input = new BufferedReader(new InputStreamReader(inputStream)).lines().toArray(String[]::new);
        } else {
            input = new BufferedReader(new FileReader(args[0])).lines().toArray(String[]::new);
        }

        long startTime = System.currentTimeMillis();

        System.out.println("--- Day 1: Chronal Calibration ---");

        System.out.println();

        System.out.println("Part One - Resulting frequency :");
        System.out.println(getResultingFrequency(input));

        System.out.println();

        System.out.println("Part Two - First frequency reached twice :");
        System.out.println(getFirstFrequencyReachedTwice(input));

        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println("Finished in " + (float) (endTime - startTime) / 1000 + " s.");

        System.out.println();
    }

    private static int getResultingFrequency(String[] input) {

        int result = 0;

        for (String line : input) {
            int number = Integer.parseInt(line.substring(1));
            result += line.startsWith("+") ? number : -number;
        }

        return result;
    }

    private static int getFirstFrequencyReachedTwice(String[] input) {

        Set<Integer> resultList = new HashSet<>();
        int result = 0;
        boolean foundFlag = false;

        while (!foundFlag) {
            for (String line : input) {

                int number = Integer.parseInt(line.substring(1));
                result += line.startsWith("+") ? number : -number;

                if (resultList.contains(result)) {
                    foundFlag = true;
                    break;
                }

                resultList.add(result);
            }
        }

        return result;
    }
}
