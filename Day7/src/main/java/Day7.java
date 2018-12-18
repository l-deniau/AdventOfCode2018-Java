import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Day7 {

    public static void main(String[] args) throws FileNotFoundException {

        String[] input;

        if (args.length == 0) {
            InputStream inputStream = Day7.class.getResourceAsStream("input_day7.txt");
            input = new BufferedReader(new InputStreamReader(inputStream)).lines().toArray(String[]::new);
        } else {
            input = new BufferedReader(new FileReader(args[0])).lines().toArray(String[]::new);
        }

        long startTime = System.currentTimeMillis();
        System.out.println("--- Day 7: The Sum of Its Parts ---");

        System.out.println();

        System.out.println(
                "Part One - Ordered steps :"
        );
        System.out.println(getOrderedSteps(input));

        System.out.println();

        System.out.println(
                "Part Two - Time spend to achieve all step with 5 workers :"
        );
        System.out.println(getTimedSteps(input));

        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println("Finished in " + (float) (endTime - startTime) / 1000 + " s.");

        System.out.println();
    }

    private static String getOrderedSteps(String[] input) {

        Map<String, Set<String>> stepList = new HashMap<>();

        // Creating the stepList from the input
        Arrays.stream(input).forEach(line -> {
            String step = line.split(" ")[7];
            String requirements = line.split(" ")[1];
            if (stepList.containsKey(step)) {
                stepList.get(step).add(requirements);
            } else {
                stepList.put(step, new HashSet<>(Collections.singletonList(requirements)));
            }
        });

        StringBuilder result = new StringBuilder();
        StringBuilder alphabet = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        boolean stopFlag = false;

        while (!stopFlag) {

            if (stepList
                    .values()
                    .stream()
                    .flatMap(Collection::parallelStream)
                    .count() == 0) {
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

    private static int getTimedSteps(String[] input) {

        Map<String, Set<String>> stepList = new HashMap<>();

        // Creating the stepList from the input
        Arrays.stream(input).forEach(line -> {
            String step = line.split(" ")[7];
            String requirements = line.split(" ")[1];
            if (stepList.containsKey(step)) {
                stepList.get(step).add(requirements);
            } else {
                stepList.put(step, new HashSet<>(Collections.singletonList(requirements)));
            }
        });

        // Creating the list of worker
        List<Worker> workerList = new ArrayList<>(Arrays.asList(
                new Worker(1, "", 0),
                new Worker(2, "", 0),
                new Worker(3, "", 0),
                new Worker(4, "", 0),
                new Worker(5, "", 0)
        ));

        AtomicInteger second = new AtomicInteger(0);
        StringBuilder alphabet = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Queue<String> stepsAvailable = new LinkedList<>();

        do {

            // End work
            workerList.stream()
                    .filter(worker -> worker.haveFinishedWork(second.intValue()))
                    .forEach(worker -> {
                        stepList.forEach((key, value) -> value.remove(worker.getActiveStep()));
                        worker.endWork();
                    });

            // Get next available Steps
            if (stepsAvailable.isEmpty() && alphabet.length() > 0) {
                for (String letter : alphabet.toString().split("")) {
                    if (!stepList.containsKey(letter) || stepList.get(letter).size() == 0) {
                        alphabet.deleteCharAt(alphabet.indexOf(letter));
                        stepsAvailable.add(letter);
                    }
                }
            }

            // Begin work
            workerList.stream()
                    .filter(Worker::isAvailable)
                    .forEach(worker ->
                            worker.newWork(second.intValue(), stepsAvailable.isEmpty() ? "" : stepsAvailable.remove())
                    );

            second.incrementAndGet();

        } while (workerList.stream().filter(Worker::isAvailable).count() != 5
                || alphabet.length() > 0);

        return second.intValue() - 1;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    private static class Worker {
        private int ID;
        private String activeStep;
        private int startSecond;

        boolean isAvailable() {
            return activeStep.isEmpty();
        }

        void newWork(int currentSecond, String step) {
            this.activeStep = step;
            this.startSecond = currentSecond;
        }

        boolean haveFinishedWork(int currentSecond) {
            if (this.activeStep.isEmpty()) {
                return false;
            } else {
                return currentSecond - startSecond >= this.activeStep.codePointAt(0) - 4;
            }
        }

        void endWork() {
            this.activeStep = "";
        }
    }
}
