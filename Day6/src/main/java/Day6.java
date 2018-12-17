import lombok.Data;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Day6 {

    public static void main(String[] args) throws FileNotFoundException {

        String[] input;

        if (args.length == 0) {
            InputStream inputStream = Day6.class.getResourceAsStream("input_day6.txt");
            input = new BufferedReader(new InputStreamReader(inputStream)).lines().toArray(String[]::new);
        } else {
            input = new BufferedReader(new FileReader(args[0])).lines().toArray(String[]::new);
        }

        long startTime = System.currentTimeMillis();

        // Create a list of Coordinate
        AtomicInteger idGenerator = new AtomicInteger();
        List<Coordinate> coordinateList = Arrays.stream(input).map(line -> {
            Coordinate coordinate = new Coordinate();
            coordinate.setX(Integer.parseInt(line.split(", ")[1]));
            coordinate.setY(Integer.parseInt(line.split(", ")[0]));
            coordinate.setOwnerId(idGenerator.incrementAndGet());
            return coordinate;
        }).collect(toList());

        System.out.println("--- Day 6: Chronal Coordinates ---");

        System.out.println();

        System.out.println(
                "Part One - size of the largest area :"
        );
        System.out.println(getLargestAreaSize(coordinateList));

        System.out.println();

        System.out.println(
                "Part Two - the size of the region containing all locations which have a total " +
                        "distance to all given coordinates of less than 10000 :"
        );
        System.out.println(get10000OrLessRegionSize(coordinateList));

        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println("Finished in " + (float) (endTime - startTime) / 1000 + " s.");

        System.out.println();
    }

    private static int getLargestAreaSize(List<Coordinate> coordinateList) {

        Integer[][] coordinates = initializeCoordonatesArray(coordinateList);

        // Calculate closest Manhattan distance for all coordinates
        for (int x = 0; x < coordinates.length; x++) {
            for (int y = 0; y < coordinates[x].length; y++) {
                if (coordinates[x][y] == null) {
                    coordinates[x][y] = getClosestManhattananDistanceId(x, y, coordinateList);
                }
            }
        }

        Set<Integer> infiniteAreaId = new HashSet<>();

        // Add id found on the top and the bottom of the grid
        for (Integer[] coordinate : coordinates) {
            infiniteAreaId.add(coordinate[0]);
            infiniteAreaId.add(coordinate[coordinate.length - 1]);
        }
        // Add id found on the left and the right of the grid
        infiniteAreaId.addAll(Arrays.asList(coordinates[0]));
        infiniteAreaId.addAll(Arrays.asList(coordinates[coordinates.length - 1]));

        // Removing infinite area from coordinates
        List<Integer> filteredCoordinates = Arrays.stream(coordinates)
                .flatMap(Arrays::stream)
                .filter(id -> !infiniteAreaId.contains(id))
                .sorted()
                .collect(toList());

        // Searching the largest area
        Map.Entry<Integer, Long> result = filteredCoordinates
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get();

        return result.getValue().intValue();
    }

    private static Integer getClosestManhattananDistanceId(int x, int y, List<Coordinate> coordinateList) {

        int closestId = 0;
        int closestDistance = Integer.MAX_VALUE;
        boolean foundEqualClosest = false;

        for (Coordinate coordinate : coordinateList) {

            int distance = Math.abs(coordinate.getX() - x) + Math.abs(coordinate.getY() - y);

            if (distance < closestDistance) {

                closestId = coordinate.getOwnerId();
                closestDistance = distance;
                foundEqualClosest = false;

            } else if (distance == closestDistance) {

                foundEqualClosest = true;

            }
        }

        if (foundEqualClosest) {
            return 0;
        } else {
            return closestId;
        }
    }

    private static int get10000OrLessRegionSize(List<Coordinate> coordinateList) {

        Integer[][] coordinates = initializeCoordonatesArray(coordinateList);

        for (int x = 0; x < coordinates.length; x++) {
            for (int y = 0; y < coordinates[x].length; y++) {

                if (getTotalManhattananDistance(x, y, coordinateList) < 10000) {
                    coordinates[x][y] = 0;
                } else {
                    coordinates[x][y] = -1;
                }

            }
        }

        return (int) Arrays.stream(coordinates)
                .flatMap(Arrays::stream)
                .filter(integer -> integer == 0)
                .count();
    }

    private static int getTotalManhattananDistance(int x, int y, List<Coordinate> coordinateList) {
        int total = 0;

        for (Coordinate coordinate : coordinateList) {
            total += Math.abs(coordinate.getX() - x) + Math.abs(coordinate.getY() - y);
        }

        return total;
    }

    private static Integer[][] initializeCoordonatesArray(List<Coordinate> coordinateList) {

        int minX = coordinateList
                .stream()
                .min(Comparator.comparing(Coordinate::getX))
                .get()
                .getX() - 1;
        int minY = coordinateList
                .stream()
                .min(Comparator.comparing(Coordinate::getY))
                .get()
                .getY() - 1;

        // Reduce coordinate near 0 for better performance
        coordinateList.forEach(coordinate -> {
            coordinate.setX(coordinate.getX() - minX);
            coordinate.setY(coordinate.getY() - minY);
        });

        int maxX = coordinateList
                .stream()
                .max(Comparator.comparing(Coordinate::getX))
                .get()
                .getX() + 1;
        int maxY = coordinateList
                .stream()
                .max(Comparator.comparing(Coordinate::getY))
                .get()
                .getY() + 1;

        Integer[][] coordinates = new Integer[maxX][maxY];

        // Supply the coordinates array with initial coordinates
        for (Coordinate coordinate : coordinateList) {
            coordinates[coordinate.getX()][coordinate.getY()] = coordinate.getOwnerId();
        }

        return coordinates;
    }

    @Data
    private static class Coordinate {
        private int x;
        private int y;
        private int ownerId;
    }

}
