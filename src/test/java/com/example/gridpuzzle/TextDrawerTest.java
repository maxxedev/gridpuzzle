package com.example.gridpuzzle;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class TextDrawerTest {

    @Test
    public void testGetters() throws Exception {
        int[] row2Column = new int[] { 9, 6, 10, 1, 4, 0, 5, 8, 2, 11, 3, 7 };
        String drawing = drawSolution(row2Column);
        String expected = ""
                + "11   |_|_|_|_|_|_|_|X|_|_|_|_|\n"
                + "10   |_|_|_|X|_|_|_|_|_|_|_|_|\n"
                + " 9   |_|_|_|_|_|_|_|_|_|_|_|X|\n"
                + " 8   |_|_|X|_|_|_|_|_|_|_|_|_|\n"
                + " 7   |_|_|_|_|_|_|_|_|X|_|_|_|\n"
                + " 6   |_|_|_|_|_|X|_|_|_|_|_|_|\n"
                + " 5   |X|_|_|_|_|_|_|_|_|_|_|_|\n"
                + " 4   |_|_|_|_|X|_|_|_|_|_|_|_|\n"
                + " 3   |_|X|_|_|_|_|_|_|_|_|_|_|\n"
                + " 2   |_|_|_|_|_|_|_|_|_|_|X|_|\n"
                + " 1   |_|_|_|_|_|_|X|_|_|_|_|_|\n"
                + " 0   |_|_|_|_|_|_|_|_|_|X|_|_|\n"
                + "\n"
                + "      0 1 2 3 4 5 6 7 8 9 0 1 \n"
                + "                          1 1 ";
        assertThat(drawing.trim()).isEqualTo(expected.trim());
    }

    private String drawSolution(int[] row2Column) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(bytes);

        TextDrawer drawer = new TextDrawer(printStream);
        GridSolution gridSolution = new GridSolution(row2Column, null);
        drawer.draw(gridSolution);

        return bytes.toString("UTF-8");
    }
}
