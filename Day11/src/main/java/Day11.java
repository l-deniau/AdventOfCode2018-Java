import lombok.Data;

import java.io.*;
import java.util.Comparator;
import java.util.stream.IntStream;

public class Day11 {

    public static void main(String[] args) throws FileNotFoundException {

        String[] input;

        if (args.length == 0) {
            InputStream inputStream = Day11.class.getResourceAsStream("input_day11.txt");
            input = new BufferedReader(new InputStreamReader(inputStream)).lines().toArray(String[]::new);
        } else {
            input = new BufferedReader(new FileReader(args[0])).lines().toArray(String[]::new);
        }

        long startTime = System.currentTimeMillis();

        int[][] powerGrid = getPowerGrid(input[0]);

        System.out.println("--- Day 11: Chronal Charge ---");

        System.out.println();

        System.out.println(
                "Part One - Coordinate of the top-left fuel cell of the 3x3 square with the largest total power :"
        );
        Grid largest3x3Grid = getLargestTotalPower(powerGrid, 3);
        System.out.println(largest3x3Grid.getX() + "," + largest3x3Grid.getY());

        System.out.println();

        System.out.println(
                "Part Two - Identifier of the square with the largest total power :"
        );
        Grid largestGrid = IntStream.rangeClosed(1, 300)
                .parallel()
                .mapToObj(gridSize -> getLargestTotalPower(powerGrid, gridSize))
                .max(Comparator.comparing(Grid::getPowerLevel))
                .orElse(new Grid());
        System.out.println(largestGrid.getX() + "," + largestGrid.getY() + "," + largestGrid.getSize());

        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println("Finished in " + (float) (endTime - startTime) / 1000 + " s.");

        System.out.println();
    }

    private static Grid getLargestTotalPower(int[][] powerGrid, int gridSize) {

        Grid grid = new Grid();
        grid.setPowerLevel(Integer.MIN_VALUE);

        for (int x = 0; x < 300 - gridSize; x++) {
            for (int y = 0; y < 300 - gridSize; y++) {

                int totalPower = 0;

                for (int i = 0; i < gridSize; i++) {
                    for (int j=0; j < gridSize; j++) {
                        totalPower += powerGrid[x+i][y+j];
                    }
                }

                if (totalPower > grid.getPowerLevel()) {
                    grid.setX(x);
                    grid.setY(y);
                    grid.setSize(gridSize);
                    grid.setPowerLevel(totalPower);
                }
            }
        }

        return grid;
    }

    private static int[][] getPowerGrid(String input) {

        int[][] powerGrid = new int[300][300];

        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {

                int rackID = x + 10;
                int RackIdPowerLevel = rackID * y;
                int serialNumber = Integer.parseInt(input);
                int powerLevel = ((((RackIdPowerLevel + serialNumber) * rackID) / 100) % 10) - 5;

                powerGrid[x][y] = powerLevel;
            }
        }

        return powerGrid;
    }

    @Data
    private static class Grid {
        private int x;
        private int y;
        private int size;
        private int powerLevel;
    }
}
