import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Day6 {

    public static void main(String[] args) throws Exception {
        Path inputPath = Paths.get(Objects.requireNonNull(Day6.class.getClassLoader()
                .getResource("input.txt")).toURI());

        // Create a list of Coordinate
        AtomicInteger idGenerator = new AtomicInteger();
        List<Coordinate> coordinateList = Files.lines(inputPath).map(line -> {
            Coordinate coordinate = new Coordinate();
            coordinate.setX(Integer.parseInt(line.split(", ")[1]));
            coordinate.setY(Integer.parseInt(line.split(", ")[0]));
            // id begin from 1
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
    }

    private static int getLargestAreaSize(List<Coordinate> coordinateList) {

        int minX = coordinateList.stream().min(Comparator.comparing(Coordinate::getX))
                .orElse(new Coordinate(0,0,0)).getX()-1;
        int minY = coordinateList.stream().min(Comparator.comparing(Coordinate::getY))
                .orElse(new Coordinate(0,0,0)).getY()-1;

        // Reduce coordinate near 0 for better performance
        coordinateList.forEach(coordinate -> {
            coordinate.setX(coordinate.getX() - minX);
            coordinate.setY(coordinate.getY() - minY);
        });

        int maxX = coordinateList.stream().max(Comparator.comparing(Coordinate::getX))
                .orElse(new Coordinate(0,0,0)).getX()+1;
        int maxY = coordinateList.stream().max(Comparator.comparing(Coordinate::getY))
                .orElse(new Coordinate(0,0,0)).getY()+1;

        Integer[][] coordinates = new Integer[maxX][maxY];

        for (Coordinate coordinate : coordinateList) {
            coordinates[coordinate.getX()][coordinate.getY()] = coordinate.getOwnerId();
        }

        for (int x = 0; x < coordinates.length; x++) {
            for (int y = 0; y < coordinates[x].length; y++) {
                if (coordinates[x][y] == null) {
                    coordinates[x][y] = getClosestManhattananDistanceId(x, y, coordinateList);
                }
            }
        }

        Set<Integer> wrongId = new HashSet<>();

        // Add id found on the top and bottom of the grid
        for (Integer[] coordinate : coordinates) {
            wrongId.add(coordinate[0]);
            wrongId.add(coordinate[coordinate.length - 1]);
        }
        // Add id found on the left and right of the grid
        wrongId.addAll(Arrays.asList(coordinates[0]));
        wrongId.addAll(Arrays.asList(coordinates[coordinates.length-1]));

        // Removing infinite area
        List<Integer> filteredCoordinates = Arrays.stream(coordinates).flatMap(Arrays::stream)
                .filter(id -> !wrongId.contains(id))
                .sorted()
                .collect(toList());

        // Searching the largest area
        Map.Entry<Integer, Long> result = filteredCoordinates.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get();

        return result.getValue().intValue();
    }

    private static Integer getClosestManhattananDistanceId(int x, int y, List<Coordinate> coordinateList) {

        Map<Coordinate, Integer> manhattanDistanceCoordinate = new HashMap<>();

        for (Coordinate coordinate : coordinateList) {
            manhattanDistanceCoordinate.put(coordinate,
                    Math.abs(coordinate.getX() - x) + Math.abs(coordinate.getY() - y));
        }

        List<Coordinate> closestManhattananDistanceCoord = manhattanDistanceCoordinate.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(manhattanDistanceCoordinate.entrySet()
                        .stream().min(Map.Entry.comparingByValue())
                        .get().getValue()))
                .map(Map.Entry::getKey).collect(toList());

        if (closestManhattananDistanceCoord.size() == 1) {
            return closestManhattananDistanceCoord.get(0).getOwnerId();
        } else return 0;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Coordinate {
        private int x;
        private int y;
        private int ownerId;
    }


}
