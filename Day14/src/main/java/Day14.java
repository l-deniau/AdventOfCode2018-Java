import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Day14 {

    public static void main(String[] args) throws FileNotFoundException {

        String[] input;

        if (args.length == 0) {
            InputStream inputStream = Day14.class.getResourceAsStream("input_day14.txt");
            input = new BufferedReader(new InputStreamReader(inputStream)).lines().toArray(String[]::new);
        } else {
            input = new BufferedReader(new FileReader(args[0])).lines().toArray(String[]::new);
        }

        long startTime = System.currentTimeMillis();

        System.out.println("--- Day 14: Chocolate Charts ---");

        System.out.println();

        System.out.println(
                "Part One - The scores of the ten recipes immediately after " +
                        Integer.parseInt(input[0]) + " recipes :"
        );

        System.out.println(getTenNumberAfterRecipesNumber(Integer.parseInt(input[0])));

        System.out.println();

        System.out.println(
                "Part Two - Number of recipes on the scoreboard to the left of the score sequence :"
        );
        System.out.println(getRecipesNumberBeforeFirstScore(input[0]));

        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println("Finished in " + (float) (endTime - startTime) / 1000 + " s.");

        System.out.println();
    }

    private static String getTenNumberAfterRecipesNumber(int recipesNumber) {

        List<Integer> scoreList = new ArrayList<>();
        scoreList.add(3);
        scoreList.add(7);
        int index1 = 0;
        int index2 = 1;

        while (scoreList.size() < recipesNumber + 10) {

            int score1 = scoreList.get(index1);
            int score2 = scoreList.get(index2);
            int scoreToAdd = score1 + score2;

            if (scoreToAdd < 10) {
                scoreList.add(scoreToAdd);
            } else {
                scoreList.add(scoreToAdd / 10);
                scoreList.add(scoreToAdd % 10);
            }

            index1 = (index1 + 1 + score1) % scoreList.size();
            index2 = (index2 + 1 + score2) % scoreList.size();

        }

        StringBuilder result = new StringBuilder();

        for (int i = scoreList.size() - 1; i >= scoreList.size() - 10; i--) {
            result.insert(0, scoreList.get(i));
        }

        return result.toString();
    }

    private static int getRecipesNumberBeforeFirstScore(String score) {

        List<Integer> scoreList = new ArrayList<>();
        scoreList.add(3);
        scoreList.add(7);
        int index1 = 0;
        int index2 = 1;
        StringBuilder resultTemp = new StringBuilder();

        while (true) {

            int score1 = scoreList.get(index1);
            int score2 = scoreList.get(index2);
            int scoreToAdd = score1 + score2;

            if (scoreToAdd < 10) {

                scoreList.add(scoreToAdd);
                resultTemp.append(scoreToAdd);

                if (resultTemp.toString().equals(score)) {
                    break;
                }

                if (resultTemp.length() == score.length()) {
                    resultTemp.deleteCharAt(0);
                }

            } else {

                int tenthNumber = scoreToAdd / 10;
                int remainderTenDivision = scoreToAdd % 10;

                scoreList.add(tenthNumber);
                resultTemp.append(tenthNumber);

                if (resultTemp.toString().equals(score)) {
                    break;
                }

                if (resultTemp.length() == score.length()) {
                    resultTemp.deleteCharAt(0);
                }

                scoreList.add(remainderTenDivision);
                resultTemp.append(remainderTenDivision);

                if (resultTemp.toString().equals(score)) {
                    break;
                }

                if (resultTemp.length() == score.length()) {
                    resultTemp.deleteCharAt(0);
                }
            }

            index1 = (index1 + 1 + score1) % scoreList.size();
            index2 = (index2 + 1 + score2) % scoreList.size();
        }

        return scoreList.size() - score.length();
    }
}