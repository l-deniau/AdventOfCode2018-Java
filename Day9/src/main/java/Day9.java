import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day9 {

    public static void main(String[] args) throws FileNotFoundException {

        String[] input;

        if (args.length == 0) {
            InputStream inputStream = Day9.class.getResourceAsStream("input_day9.txt");
            input = new BufferedReader(new InputStreamReader(inputStream)).lines().toArray(String[]::new);
        } else {
            input = new BufferedReader(new FileReader(args[0])).lines().toArray(String[]::new);
        }

        long startTime = System.currentTimeMillis();

        Pattern linePattern = Pattern.compile("^(\\d+) players.*?(\\d+) points$");
        Matcher lineMatcher = linePattern.matcher(input[0]);

        int playerNumber = 0;
        int marbleNumber = 0;

        if (lineMatcher.matches()) {
            playerNumber = Integer.parseInt(lineMatcher.group(1));
            marbleNumber = Integer.parseInt(lineMatcher.group(2));
        }

        System.out.println("--- Day 9: Marble Mania ---");

        System.out.println(
                "Part One - The highest score :"
        );
        System.out.println(getHighestScore(playerNumber, marbleNumber));

        System.out.println();

        System.out.println(
                "Part Two -  :"
        );
        System.out.println();

        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println("Finished in " + (float) (endTime - startTime) / 1000 + " s.");

        System.out.println();
    }

    private static int getHighestScore(int playerNumber, int marbleNumber) {

        int[] playerScore = new int[playerNumber + 1];
        List<Integer> marbleCircle = new ArrayList<>();
        marbleCircle.add(0);
        int currentMarblePosition = 0;
        int currentPlayer = 1;

        for (int turn = 1; turn <= marbleNumber; turn++) {

            int newMarblePosition;

            if (turn % 23 == 0) {

                int marbleToRemovePosition = currentMarblePosition - 7 < 0 ?
                        marbleCircle.size() - Math.abs(currentMarblePosition - 7) : currentMarblePosition - 7;
                int marbleRemoved = marbleCircle.remove(marbleToRemovePosition);
                newMarblePosition = marbleToRemovePosition;

                playerScore[currentPlayer] = playerScore[currentPlayer] + turn + marbleRemoved;

            } else {

                newMarblePosition = (currentMarblePosition + 2) % marbleCircle.size();
                marbleCircle.add(newMarblePosition, turn);

            }

            currentMarblePosition = newMarblePosition;
            currentPlayer = currentPlayer + 1 > playerNumber ? 1 : currentPlayer + 1;
        }

        return Arrays.stream(playerScore).max().orElse(0);
    }
}
