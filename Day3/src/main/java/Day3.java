import lombok.Data;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {

    public static void main(String[] args) throws FileNotFoundException {

        String[] input;

        if (args.length == 0) {
            InputStream inputStream = Day3.class.getResourceAsStream("input_day3.txt");
            input = new BufferedReader(new InputStreamReader(inputStream)).lines().toArray(String[]::new);
        } else {
            input = new BufferedReader(new FileReader(args[0])).lines().toArray(String[]::new);
        }

        long startTime = System.currentTimeMillis();

        System.out.println("--- Day 3: No Matter How You Slice It ---");

        System.out.println();

        System.out.println("Part One - Number of overlap :");
        System.out.println(getNumberOfOverlaps(input));

        System.out.println();

        System.out.println("Part Two - ID of the no-overlap square :");
        System.out.println(getIdOfTheNoOverlapSquare(input));

        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println("Finished in " + (float) (endTime - startTime) / 1000 + " s.");

        System.out.println();
    }

    private static int getNumberOfOverlaps(String[] input) {

        List<Square> squareList = new ArrayList<>();
        Set<String> positionsSet = new HashSet<>();

        Arrays.stream(input).forEach(line -> {
            Square square = getSquare(line);
            if (!squareList.isEmpty()) {
                squareList.forEach(comparedSquare ->
                        positionsSet.addAll(getOverlapCoordinates(square, comparedSquare)));
            }
            squareList.add(square);
        });

        return positionsSet.size();
    }

    private static int getIdOfTheNoOverlapSquare(String[] input) {

        List<Square> squareList = new ArrayList<>();
        int result = 0;

        Arrays.stream(input).forEach(line -> squareList.add(getSquare(line)));

        for (Square square1 : squareList) {

            boolean noOverlapFlag = true;

            for (Square square2 : squareList) {

                if (square1.getID() != square2.getID()) {
                    // Verify if there is an overlap between square 1 and square 2
                    if (!getOverlapCoordinates(square1, square2).isEmpty()) {
                        noOverlapFlag = false;
                        break;
                    }
                }

            }

            if (noOverlapFlag) {
                result = square1.getID();
                break;
            }
        }

        return result;
    }

    private static Collection<? extends String> getOverlapCoordinates(Square square1, Square square2) {

        Set<String> positionsSet = new HashSet<>();

        // Check if there is an overlap
        if (square1.getX() < square2.getX2() && square1.getX2() > square2.getX() &&
                square1.getY() < square2.getY2() && square1.getY2() > square2.getY()) {

            // The overlap square
            Square overlapSquare = new Square();

            // Calculate positions of the overlap square
            overlapSquare.setX(square1.getX() < square2.getX() ? square2.getX() : square1.getX());
            overlapSquare.setY(square1.getY() < square2.getY() ? square2.getY() : square1.getY());
            overlapSquare.setWidth(square1.getX2() > square2.getX2() ?
                    square2.getX2() - overlapSquare.getX() : square1.getX2() - overlapSquare.getX());
            overlapSquare.setHeight(square1.getY2() > square2.getY2() ?
                    square2.getY2() - overlapSquare.getY() : square1.getY2() - overlapSquare.getY());

            // Add positions into the Set
            for (int x = overlapSquare.getX(); x < overlapSquare.getX2(); x++) {
                for (int y = overlapSquare.getY(); y < overlapSquare.getY2(); y++) {
                    positionsSet.add(x + "," + y);
                }
            }
        }

        return positionsSet;
    }

    private static Square getSquare(String line) {
        Square square = new Square();

        Pattern linePattern = Pattern.compile("^#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)$");
        Matcher lineMatch = linePattern.matcher(line);
        if (lineMatch.matches()) {
            square.setID(Integer.parseInt(lineMatch.group(1)));
            square.setX(Integer.parseInt(lineMatch.group(2)));
            square.setY(Integer.parseInt(lineMatch.group(3)));
            square.setWidth(Integer.parseInt(lineMatch.group(4)));
            square.setHeight(Integer.parseInt(lineMatch.group(5)));
        }

        return square;
    }

    @Data
    private static class Square {
        private int ID;
        private int x;
        private int y;
        private int width;
        private int height;

        int getX2() {
            return x + width;
        }

        int getY2() {
            return y + height;
        }
    }
}
