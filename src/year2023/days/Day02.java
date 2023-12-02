package year2023.days;

import year2023.Day2023;

import java.util.Scanner;

public class Day02 extends Day2023 {

    private static final int CUBES_RED = 12;
    private static final int CUBES_GREEN = 13;
    private static final int CUBES_BLUE = 14;

    public Day02() {
        super(2);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();
        int result = 0;
        while (scanner.hasNextLine()) {
            scanner.skip("Game");
            String sId = scanner.next();
            int gameId = Integer.parseInt(sId.substring(0, sId.length() - 1));
            String game = scanner.nextLine();
            if (isGamePossible(game)) {
                result += gameId;
            }
        }

        return result;
    }

    private static boolean isGamePossible(String game) {
        String[] gameSets = game.split(";");
        for (String gameSet : gameSets) {
            String[] cubes = gameSet.split(",");
            int r = 0;
            int g = 0;
            int b = 0;
            for (String pick : cubes) {
                String[] parts = pick.substring(1).split("\\s+");
                int number = Integer.parseInt(parts[0]);
                switch (parts[1]) {
                    case "red" -> r += number;
                    case "green" -> g += number;
                    case "blue" -> b += number;
                }

                if (r > CUBES_RED || b > CUBES_BLUE || g > CUBES_GREEN) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();
        long result = 0;
        while (scanner.hasNextLine()) {
            scanner.skip("Game");
            scanner.next();
            String game = scanner.nextLine();
            result += getGamePower(game);
        }

        return result;
    }

    private long getGamePower(String game) {
        String[] gameSets = game.split(";");
        long maxR = 0;
        long maxG = 0;
        long maxB = 0;
        for (String gameSet : gameSets) {
            String[] cubes = gameSet.split(",");
            for (String pick : cubes) {
                String[] parts = pick.substring(1).split("\\s+");
                int number = Integer.parseInt(parts[0]);
                switch (parts[1]) {
                    case "red" -> maxR = Math.max(maxR, number);
                    case "green" -> maxG = Math.max(maxG, number);
                    case "blue" -> maxB = Math.max(maxB, number);
                }
            }
        }

        return maxR * maxG * maxB;
    }
}
