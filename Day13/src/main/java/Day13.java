import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day13 {

    public static void main(String[] args) throws FileNotFoundException {

        String[] input;

        if (args.length == 0) {
            InputStream inputStream = Day13.class.getResourceAsStream("input_day13.txt");
            input = new BufferedReader(new InputStreamReader(inputStream)).lines().toArray(String[]::new);
        } else {
            input = new BufferedReader(new FileReader(args[0])).lines().toArray(String[]::new);
        }

        long startTime = System.currentTimeMillis();

        System.out.println("--- Day 13: Mine Cart Madness ---");

        System.out.println();

        System.out.println(
                "Part One - The location of the first crash :"
        );

        System.out.println(getFirstCrashLocation(input));

        System.out.println();

        System.out.println(
                "Part Two - The location of the last cart :"
        );
        System.out.println(getLastCartLocation(input));

        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println("Finished in " + (float) (endTime - startTime) / 1000 + " s.");

        System.out.println();
    }

    private static String getFirstCrashLocation(String[] input) {

        List<Cart> cartList = new ArrayList<>();
        Map<String, String> trackMap = new HashMap<>();
        getCartAndTrackMap(input, cartList, trackMap);

        String firstCrashLocation = "";

        while (firstCrashLocation.equals("")) {

            // Reorder cartList for move priority
            cartList = cartList
                    .stream()
                    .sorted((o1, o2) -> {
                        if (o1.getY() > o2.getY()) {
                            return 1;
                        } else if (o1.getY() < o2.getY()) {
                            return -1;
                        }
                        if (o1.getX() > o2.getX()) {
                            return 1;
                        } else if (o1.getX() < o2.getX()) {
                            return -1;
                        }
                        return 0;
                    }).collect(Collectors.toList());

            for (Cart cart : cartList) {

                cart.move(trackMap);

                String cartLocation = cart.getX() + "," + cart.getY();
                int x = Integer.parseInt(cartLocation.split(",")[0]);
                int y = Integer.parseInt(cartLocation.split(",")[1]);

                // Detect if there is a crash
                if (cartList
                        .stream()
                        .filter(filteredCart -> filteredCart.getX() == x && filteredCart.getY() == y)
                        .count() > 1) {
                    firstCrashLocation = cartLocation;
                    break;
                }
            }
        }

        return firstCrashLocation;
    }

    private static String getLastCartLocation(String[] input) {

        List<Cart> cartList = new ArrayList<>();
        Map<String, String> trackMap = new HashMap<>();
        getCartAndTrackMap(input, cartList, trackMap);

        String lastCartLocation = "";

        while (lastCartLocation.equals("")) {

            // Reorder cartList for move priority
            cartList = cartList
                    .stream()
                    .sorted((o1, o2) -> {
                        if (o1.getY() > o2.getY()) {
                            return 1;
                        } else if (o1.getY() < o2.getY()) {
                            return -1;
                        }
                        if (o1.getX() > o2.getX()) {
                            return 1;
                        } else if (o1.getX() < o2.getX()) {
                            return -1;
                        }
                        return 0;
                    }).collect(Collectors.toList());

            for (Cart cart : cartList) {

                cart.move(trackMap);

                String cartLocation = cart.getX() + "," + cart.getY();
                int x = Integer.parseInt(cartLocation.split(",")[0]);
                int y = Integer.parseInt(cartLocation.split(",")[1]);

                // Detect if there is a crash
                if (cartList
                        .stream()
                        .filter(filteredCart -> filteredCart.getX() == x && filteredCart.getY() == y)
                        .count() > 1) {
                    cartList
                            .stream()
                            .filter(filteredCart -> filteredCart.getX() == x && filteredCart.getY() == y)
                            .forEach(filteredCart -> filteredCart.setCrashed(true));
                }

            }

            // Remove crashed cart
            cartList.removeIf(Cart::isCrashed);

            // Detect if there is only one Cart remaining
            if (cartList.size() == 1) {
                lastCartLocation = cartList.get(0).getX() + "," + cartList.get(0).getY();
            }
        }

        return lastCartLocation;
    }

    private static void getCartAndTrackMap(String[] input, List<Cart> cartList, Map<String, String> trackMap) {

        for (int y = 0; y < input.length; y++) {

            String[] line = input[y].split("");

            for (int x = 0; x < line.length; x++) {

                String track = line[x];

                if (track.equals(">") || track.equals("<") || track.equals("^") || track.equals("v")) {

                    Cart cart = new Cart();
                    cart.setX(x);
                    cart.setY(y);
                    cart.setDirection(track);
                    cart.setNextDirection(Cart.NextDirection.LEFT);
                    cart.setCrashed(false);

                    cartList.add(cart);

                } else if (track.equals("+") || track.equals("/") || track.equals("\\")) {

                    trackMap.put(x + "," + y, track);

                }
            }
        }
    }

    @Data
    private static class Cart {

        private int x;
        private int y;
        private String direction;
        private NextDirection nextDirection;
        private boolean crashed;

        void move(Map<String, String> trackMap) {
            switch (this.getDirection()) {

                case "<":
                    this.setX(this.getX() - 1);
                    switch (trackMap.getOrDefault(this.getX() + "," + this.getY(), "")) {
                        case "+":
                            if (this.getNextDirection() == Cart.NextDirection.LEFT) {
                                this.setDirection("v");
                                this.setNextDirection(Cart.NextDirection.STRAIGHT);
                            } else if (this.getNextDirection() == Cart.NextDirection.STRAIGHT) {
                                this.setNextDirection(Cart.NextDirection.RIGHT);
                            } else if (this.getNextDirection() == Cart.NextDirection.RIGHT) {
                                this.setDirection("^");
                                this.setNextDirection(Cart.NextDirection.LEFT);
                            }
                            break;
                        case "/":
                            this.setDirection("v");
                            break;
                        case "\\":
                            this.setDirection("^");
                            break;
                    }
                    break;

                case ">":
                    this.setX(this.getX() + 1);
                    switch (trackMap.getOrDefault(this.getX() + "," + this.getY(), "")) {
                        case "+":
                            if (this.getNextDirection() == Cart.NextDirection.LEFT) {
                                this.setDirection("^");
                                this.setNextDirection(Cart.NextDirection.STRAIGHT);
                            } else if (this.getNextDirection() == Cart.NextDirection.STRAIGHT) {
                                this.setNextDirection(Cart.NextDirection.RIGHT);
                            } else if (this.getNextDirection() == Cart.NextDirection.RIGHT) {
                                this.setDirection("v");
                                this.setNextDirection(Cart.NextDirection.LEFT);
                            }
                            break;
                        case "/":
                            this.setDirection("^");
                            break;
                        case "\\":
                            this.setDirection("v");
                            break;
                    }
                    break;

                case "^":
                    this.setY(this.getY() - 1);
                    switch (trackMap.getOrDefault(this.getX() + "," + this.getY(), "")) {
                        case "+":
                            if (this.getNextDirection() == Cart.NextDirection.LEFT) {
                                this.setDirection("<");
                                this.setNextDirection(Cart.NextDirection.STRAIGHT);
                            } else if (this.getNextDirection() == Cart.NextDirection.STRAIGHT) {
                                this.setNextDirection(Cart.NextDirection.RIGHT);
                            } else if (this.getNextDirection() == Cart.NextDirection.RIGHT) {
                                this.setDirection(">");
                                this.setNextDirection(Cart.NextDirection.LEFT);
                            }
                            break;
                        case "/":
                            this.setDirection(">");
                            break;
                        case "\\":
                            this.setDirection("<");
                            break;
                    }
                    break;

                case "v":
                    this.setY(this.getY() + 1);
                    switch (trackMap.getOrDefault(this.getX() + "," + this.getY(), "")) {
                        case "+":
                            if (this.getNextDirection() == Cart.NextDirection.LEFT) {
                                this.setDirection(">");
                                this.setNextDirection(Cart.NextDirection.STRAIGHT);
                            } else if (this.getNextDirection() == Cart.NextDirection.STRAIGHT) {
                                this.setNextDirection(Cart.NextDirection.RIGHT);
                            } else if (this.getNextDirection() == Cart.NextDirection.RIGHT) {
                                this.setDirection("<");
                                this.setNextDirection(Cart.NextDirection.LEFT);
                            }
                            break;
                        case "/":
                            this.setDirection("<");
                            break;
                        case "\\":
                            this.setDirection(">");
                            break;
                    }
                    break;
            }
        }

        public enum NextDirection {
            LEFT,
            STRAIGHT,
            RIGHT
        }
    }
}
