import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 {

    public static void main(String[] args) throws FileNotFoundException {

        String[] input;

        if (args.length == 0) {
            InputStream inputStream = Day10.class.getResourceAsStream("input_day10.txt");
            input = new BufferedReader(new InputStreamReader(inputStream)).lines().toArray(String[]::new);
        } else {
            input = new BufferedReader(new FileReader(args[0])).lines().toArray(String[]::new);
        }

        long startTime = System.currentTimeMillis();

        List<Point> pointList = getPointList(input);
        int secondsWaited = movePointUntilMessage(pointList);

        System.out.println("--- Day 10: The Stars Align ---");

        System.out.println();

        System.out.println(
                "Part One - The message appeared :"
        );
        renderPoint(pointList);

        System.out.println();

        System.out.println(
                "Part Two - Seconds needed to wait for that message to appear :"
        );
        System.out.println(secondsWaited);

        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println("Finished in " + (float) (endTime - startTime) / 1000 + " s.");

        System.out.println();
    }

    private static int movePointUntilMessage(List<Point> pointList) {

        int minX;
        int maxX;
        int second = 0;

        do {

            pointList.forEach(Point::move);

            minX = pointList.stream().min(Comparator.comparing(Point::getX)).map(Point::getX).orElse(0);
            maxX = pointList.stream().max(Comparator.comparing(Point::getX)).map(Point::getX).orElse(0);

            second++;

        } while (maxX - minX > 10);

        return second;
    }

    private static void renderPoint(List<Point> pointList) {

        int minX = pointList.stream().min(Comparator.comparing(Point::getX)).map(Point::getX).orElse(0);
        int minY = pointList.stream().min(Comparator.comparing(Point::getY)).map(Point::getY).orElse(0);
        int maxX = pointList.stream().max(Comparator.comparing(Point::getX)).map(Point::getX).orElse(0);
        int maxY = pointList.stream().max(Comparator.comparing(Point::getY)).map(Point::getY).orElse(0);

        for (int x = minX - 1; x < maxX + 2; x++) {

            for (int y = minY - 1; y < maxY + 2; y++) {

                int finalY = y;
                int finalX = x;

                System.out.print(pointList.stream()
                        .anyMatch(point -> point.getX() == finalX && point.getY() == finalY) ?
                        "#" : "."
                );

            }

            System.out.println();

        }
    }

    private static List<Point> getPointList(String[] input) {

        List<Point> pointList = new ArrayList<>();
        Pattern linePattern = Pattern.compile("^position=<(.*),(.*)> velocity=<(.*),(.*)>$");

        Arrays.stream(input).forEach(line -> {

            Matcher lineMatch = linePattern.matcher(line);

            if (lineMatch.matches()) {

                Point point = new Point();

                point.setY(Integer.parseInt(lineMatch.group(1).trim()));
                point.setX(Integer.parseInt(lineMatch.group(2).trim()));
                point.setYVelocity(Integer.parseInt(lineMatch.group(3).trim()));
                point.setXVelocity(Integer.parseInt(lineMatch.group(4).trim()));

                pointList.add(point);

            }
        });

        return pointList;
    }

    @Data
    private static class Point {
        private int x;
        private int y;
        private int xVelocity;
        private int yVelocity;

        void move() {
            x += xVelocity;
            y += yVelocity;
        }
    }

}
