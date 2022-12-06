package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class Day {
    protected final int year;
    protected final int day;

    public Day(int year, int day) {
        this.year = year;
        this.day = day;
    }

    public File getInputFile() {
        String filename = String.format("input/year%d/day%02d.txt", year, day);
        return new File(filename);
    }

    public Scanner getInputScanner() {
        try {
            return new Scanner(getInputFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract Object solvePart1();
    public abstract Object solvePart2();
}
