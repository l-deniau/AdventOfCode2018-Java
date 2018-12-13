import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day4 {

    private static final Pattern linePattern = Pattern.compile("^\\[(.*)\\] (.*)$");

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path inputPath = Paths.get(Objects.requireNonNull(Day4.class.getClassLoader()
                .getResource("input.txt")).toURI());

        List<String> sortedInput = Files.lines(inputPath)
                .sorted(Comparator.comparing(Day4::getDateInLine))
                .collect(Collectors.toList());

        Map<Integer, Map<LocalDate, Set<Integer>>> recordsList = createRecordsList(sortedInput);

        System.out.println("--- Day 4: Repose Record ---");

        System.out.println();

        System.out.println(
                "Part One - Id of the guard the most asleep multiplied by the minute spent asleep the most :"
        );
        System.out.println(getStrategy1Result(recordsList));

        System.out.println();

        System.out.println(
                "Part Two - Id of the guard the most frequently asleep on the same minute multiplied by this minute :"
        );
        System.out.println(getStrategy2Result(recordsList));
    }

    private static int getStrategy1Result(Map<Integer, Map<LocalDate, Set<Integer>>> recordsList) {

        int maxAsleepGuardId = 0;
        int maxMinuteAsleep = 0;

        // The old school searching way
        // Searching the most asleep Guard
        for (Map.Entry<Integer, Map<LocalDate, Set<Integer>>> record : recordsList.entrySet()) {
            int minuteAsleep = 0;
            for (Map.Entry<LocalDate, Set<Integer>> nightShift : record.getValue().entrySet()) {
                minuteAsleep += nightShift.getValue().size();
            }
            if (minuteAsleep > maxMinuteAsleep) {
                maxAsleepGuardId = record.getKey();
                maxMinuteAsleep = minuteAsleep;
            }
        }

        // Now having fun with stream....
        // Searching the most asleep minute
        int mostAsleepMinute = recordsList.get(maxAsleepGuardId).values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get().getKey();

        return maxAsleepGuardId*mostAsleepMinute;
    }

    private static int getStrategy2Result(Map<Integer, Map<LocalDate, Set<Integer>>> recordsList) {

        int guardId = 0;
        int minute = 0;
        int minuteCount = 0;

        HashMap<Integer, Integer> minuteAsleepCountMap;

        for (Map.Entry<Integer, Map<LocalDate, Set<Integer>>> record : recordsList.entrySet()) {
            minuteAsleepCountMap = new HashMap<>();
            // Creating the minuteAsleepCountMap
            for (Map.Entry<LocalDate, Set<Integer>> nightShift : record.getValue().entrySet()) {
                for (Integer minuteAsleep : nightShift.getValue()) {
                    if (minuteAsleepCountMap.containsKey(minuteAsleep)) {
                        minuteAsleepCountMap.put(minuteAsleep, minuteAsleepCountMap.get(minuteAsleep) + 1);
                    } else {
                        minuteAsleepCountMap.put(minuteAsleep, 1);
                    }
                }
            }
            // Condition to avoid the case where the guard never slept during his shift !!! Amazing :o
            if (!minuteAsleepCountMap.isEmpty()) {

                Integer maxSameMinuteCountAsleep =
                        minuteAsleepCountMap
                            .values()
                            .stream()
                            .max(Integer::compareTo)
                            .orElse(0);

                Map<Integer, Integer> maxSameMinuteCountAsleepMap =
                        minuteAsleepCountMap
                            .entrySet()
                            .stream()
                            .filter(entry -> entry.getValue().equals(maxSameMinuteCountAsleep))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                if (maxSameMinuteCountAsleep > minuteCount) {
                    guardId = record.getKey();
                    // I assume that with the provided input there is only one possible solution.
                    minute = maxSameMinuteCountAsleepMap.entrySet().stream().findFirst().get().getKey();
                    minuteCount = maxSameMinuteCountAsleep;
                }
            }
        }

        return guardId*minute;
    }

    private static LocalDateTime getDateInLine(String line) {
        LocalDateTime date = null;
        Matcher lineMatch = linePattern.matcher(line);
        if (lineMatch.find()) {
            date = LocalDateTime.parse(lineMatch.group(1), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        return date;
    }

    private static String getActionInLine(String line) {
        String action = "";
        Matcher lineMatch = linePattern.matcher(line);
        if (lineMatch.find()) {
            action = lineMatch.group(2);
        }
        return action;
    }

    private static Map<Integer, Map<LocalDate, Set<Integer>>> createRecordsList(List<String> sortedInput) {

        Map<Integer, Map<LocalDate, Set<Integer>>> recordsList = new HashMap<>();

        int lastId = 0;
        LocalDateTime lastDate = null;
        String lastAction = "";

        for (String line : sortedInput) {

            String action = getActionInLine(line);
            LocalDateTime date = getDateInLine(line);
            int id = action.startsWith("Guard") ? Integer.parseInt(action.split(" ")[1].substring(1)) : lastId;

            // If the shift of a new Guard begin before midnight
            if (date.getHour() != 0 && action.startsWith("Guard")) {
                date = date.with(LocalDate.from(date.plusDays(1)).atTime(0,0));
            }

            // Add minutes to last guard shift record if he was asleep
            if (lastId != 0 && lastAction.startsWith("falls")) {
                for (int minute = lastDate.getMinute();
                     minute < (action.startsWith("Guard") ? 60 : date.getMinute());
                     minute++
                ) {
                    recordsList.get(lastId).get(lastDate.toLocalDate()).add(minute);
                }
            }

            // Update the records of a Guard or add a new one
            if (action.startsWith("Guard")) {

                if (recordsList.containsKey(id)) {
                    recordsList.get(id).put(date.toLocalDate(), new HashSet<>());
                } else {
                    Map<LocalDate, Set<Integer>> record = new HashMap<>();
                    record.put(date.toLocalDate(), new HashSet<>());
                    recordsList.put(id, record);
                }

            }

            lastId = id;
            lastDate = date;
            lastAction = action;
        }

        return recordsList;
    }
}
