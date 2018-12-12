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
}
