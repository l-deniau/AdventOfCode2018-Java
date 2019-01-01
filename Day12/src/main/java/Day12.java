import lombok.Data;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12 {

    public static void main(String[] args) throws FileNotFoundException {

        String[] input;

        if (args.length == 0) {
            InputStream inputStream = Day12.class.getResourceAsStream("input_day12.txt");
            input = new BufferedReader(new InputStreamReader(inputStream)).lines().toArray(String[]::new);
        } else {
            input = new BufferedReader(new FileReader(args[0])).lines().toArray(String[]::new);
        }

        long startTime = System.currentTimeMillis();

        String initialState = input[0].split(": ")[1];

        Map<String, String> spreadTemplate = new HashMap<>();
        Pattern spreadPattern = Pattern.compile("^(.*) => (.*)$");
        for (String line : input) {
            Matcher spreadMatcher = spreadPattern.matcher(line);
            if (spreadMatcher.matches()) {
                spreadTemplate.put(spreadMatcher.group(1), spreadMatcher.group(2));
            }
        }

        System.out.println("--- Day 12: Subterranean Sustainability ---");

        System.out.println();

        System.out.println(
                "Part One - The sum of the numbers of all pots which contain a plant after 20 generations :"
        );
        Pots potsAfter20Generations = getStateAfterGenerations(initialState, spreadTemplate,
                20, false);
        System.out.println(getSumPots(potsAfter20Generations.getState(), potsAfter20Generations.getZeroPosition()));

        System.out.println();

        System.out.println(
                "Part Two - The sum of the numbers of all pots which contain a plant after 50B generations :"
        );
        Pots potsAfterXGenerations = getStateAfterGenerations(initialState, spreadTemplate,
                Integer.MAX_VALUE, true);
        System.out.println(
                getSumPots(potsAfterXGenerations.getState(),
                        potsAfterXGenerations.getZeroPosition() +
                                (potsAfterXGenerations.getShift() *
                                        (50000000000L - potsAfterXGenerations.getGenerationRegularPattern())
                                )
                )
        );

        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println("Finished in " + (float) (endTime - startTime) / 1000 + " s.");

        System.out.println();
    }

    private static Pots getStateAfterGenerations(String state, Map<String, String> spreadTemplate,
                                                 int generations, boolean breakPattern) {

        Pots pots = new Pots();
        int zeroPosition = 0;

        for (int i = 0; i < generations; i++) {

            StringBuilder actualStateBuilder = new StringBuilder(state);
            String[] actualStateArray = actualStateBuilder
                    .append("...")
                    .insert(0, "...")
                    .toString()
                    .split("");

            StringBuilder nextStateBuilder = new StringBuilder();

            for (int j = 2; j < actualStateArray.length - 2; j++) {

                StringBuilder chunkBuilder = new StringBuilder();
                for (int k = j - 2; k <= j + 2; k++) {
                    chunkBuilder.append(actualStateArray[k]);
                }
                String chunk = chunkBuilder.toString();

                nextStateBuilder.append(spreadTemplate.getOrDefault(chunk, "."));
            }

            int lastZeroPosition = zeroPosition;

            // Keeping track of the 0 position
            if (nextStateBuilder.substring(0, 1).equals("#")) {
                zeroPosition++;
            } else {
                zeroPosition -= nextStateBuilder.indexOf("#") - 1;
            }

            String nextState = nextStateBuilder.substring(
                    nextStateBuilder.indexOf("#"), nextStateBuilder.lastIndexOf("#") + 1
            );

            // If a regular pattern is detected, no need to continue
            if (breakPattern && state.equals(nextState)) {
                pots.setGenerationRegularPattern(i + 1);
                pots.setShift(zeroPosition - lastZeroPosition);
                break;
            }

            state = nextState;
        }

        pots.setState(state);
        pots.setZeroPosition(zeroPosition);

        return pots;
    }

    private static long getSumPots(String state, long zeroPosition) {

        long result = 0;
        String[] stateArray = state.split("");

        for (int i = 0; i < stateArray.length; i++) {
            if (stateArray[i].equals("#")) {
                result += (i - zeroPosition);
            }
        }

        return result;
    }

    @Data
    private static class Pots {
        private String state;
        private int zeroPosition;
        private int shift;
        private int generationRegularPattern;
    }
}
