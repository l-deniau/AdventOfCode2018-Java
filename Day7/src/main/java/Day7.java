import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day7 {

    public static void main(String[] args) throws Exception {

        Path inputPath = Paths.get(Objects.requireNonNull(Day7.class.getClassLoader()
                .getResource("input.txt")).toURI());

        Map<String, Set<String>> stepList = new HashMap<>();
        Files.lines(inputPath).forEach(line -> {
            String step = line.split(" ")[7];
            String requirements = line.split(" ")[1];
            if (stepList.containsKey(step)) {
                stepList.get(step).add(requirements);
            } else {
                stepList.put(step, new HashSet<>(Collections.singletonList(requirements)));
            }
        });

        System.out.println("--- Day 7: The Sum of Its Parts ---");

        System.out.println();

        System.out.println(
                "Part One - Ordered steps :"
        );
        System.out.println(getOrderedSteps(stepList));

        System.out.println();

        System.out.println(
                "Part Two - Time spend to achieve all step with 5 workers :"
        );
        System.out.println(getTimedSteps(stepList));
    }

    private static String getOrderedSteps(Map<String, Set<String>> stepList) {
        StringBuilder result = new StringBuilder();
        StringBuilder alphabet = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        boolean stopFlag = false;
        while (!stopFlag) {
            if (stepList.values().stream().flatMap(Collection::parallelStream).count() == 0) {
                stopFlag = true;
            }
            for (String letter : alphabet.toString().split("")) {
                if (!stepList.containsKey(letter) || stepList.get(letter).size() == 0) {
                    result.append(letter);
                    stepList.forEach((key, value) -> value.remove(letter));
                    alphabet.deleteCharAt(alphabet.indexOf(letter));
                    break;
                }
            }
        }
        return result.toString();
    }

    private static int getTimedSteps(Map<String, Set<String>> stepList) {
        int second = 0;

        List<Worker> workerlist = new ArrayList<>(Arrays.asList(
                new Worker(1, "", true, 0),
                new Worker(2, "", true, 0),
                new Worker(3, "", true, 0),
                new Worker(4, "", true, 0),
                new Worker(5, "", true, 0)
        ));

        return second;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    private static class Worker {
        private int ID;
        private String activeStep;
        private boolean isIdle;
        private int startSecond;

        public void work(int currentSecond) {
            // Determine duration of a step helped by unicode number
            // example : A unicode equal 65 and A take 61 seconds.
            // So we just need to subtract 4 to the unicode number
            int activeStepDuration = this.activeStep.codePointAt(0) - 4;
            if (currentSecond - startSecond >= activeStepDuration) {
                this.isIdle = true;
            };
        }
        public void newWork(int currentSecond, String step) {
            this.activeStep = step;
            this.isIdle = false;
        }
    }
}
