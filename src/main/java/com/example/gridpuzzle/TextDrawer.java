package com.example.gridpuzzle;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;

public class TextDrawer {

    private PrintStream printStream;

    public TextDrawer(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void draw(GridSolution gridSolution) {
        int[][] matrix2 = asMatrix(gridSolution.getArray());

        printMatrix(matrix2);
        printStream.println();
    }

    private int[][] asMatrix(int[] result) {
        int[][] matrix2 = new int[result.length][result.length];
        for (int row = 0; row < result.length; row++) {
            int queenY = result[row];
            matrix2[row][queenY] = 1;
        }
        return matrix2;
    }

    private void printMatrix(int[][] matrix) {
        int numDigitsOfGridSize = Integer.toString(matrix.length).length();
        for (int row = matrix.length - 1; row >= 0; row--) {
            printStream.print(StringUtils.leftPad(row + "", numDigitsOfGridSize) + "   |");
            for (int column = 0; column < matrix[row].length; column++) {
                int value = matrix[row][column];
                String symbol = value == 1 ? "X" : "_";
                printStream.print(symbol + "|");
            }
            printStream.println();
        }

        printStream.println();
        for (int i = 0; i < numDigitsOfGridSize; i++) {
            printStream.print(StringUtils.leftPad("", numDigitsOfGridSize) + "    ");
            for (int column = 0; column < matrix[0].length; column++) {
                String columnString = Integer.toString(column);
                char digit = columnString.length() > i
                        ? columnString.charAt(columnString.length() - i - 1)
                        : ' ';
                printStream.print(digit + " ");
            }
            printStream.println();
        }
        printStream.println();
    }

}
