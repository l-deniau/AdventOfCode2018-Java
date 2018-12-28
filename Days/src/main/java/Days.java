import java.io.FileNotFoundException;

public class Days {

    public static void main(String[] args) throws FileNotFoundException {

        long startTime = System.currentTimeMillis();

        Day1.main(args.length > 0 && args[0] != null ? new String[]{args[0]} : new String[]{});
        Day2.main(args.length > 1 && args[1] != null ? new String[]{args[1]} : new String[]{});
        Day3.main(args.length > 2 && args[2] != null ? new String[]{args[2]} : new String[]{});
        Day4.main(args.length > 3 && args[3] != null ? new String[]{args[3]} : new String[]{});
        Day5.main(args.length > 4 && args[4] != null ? new String[]{args[4]} : new String[]{});
        Day6.main(args.length > 5 && args[5] != null ? new String[]{args[5]} : new String[]{});
        Day7.main(args.length > 6 && args[6] != null ? new String[]{args[6]} : new String[]{});
        Day8.main(args.length > 7 && args[7] != null ? new String[]{args[7]} : new String[]{});
        Day9.main(args.length > 8 && args[8] != null ? new String[]{args[8]} : new String[]{});
        Day10.main(args.length > 9 && args[9] != null ? new String[]{args[9]} : new String[]{});

        long endTime = System.currentTimeMillis();

        System.out.println("-----------------------------------");
        System.out.println();
        System.out.println("TOTAL Finished in " + (float) (endTime - startTime) / 1000 + " s.");
    }

}