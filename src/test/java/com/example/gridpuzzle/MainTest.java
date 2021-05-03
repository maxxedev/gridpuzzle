package com.example.gridpuzzle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedConstruction.Context;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;

public class MainTest {

    @BeforeEach
    public void setUp() {
        System.setProperty("puzzle.showUi", "true");
    }
    
    @AfterEach
    public void tearDown() {
        System.clearProperty("puzzle.showUi");
    }
    
    @Test
    public void testMain() throws Exception {
        try (MockedConstruction<Main> mocked = mockConstruction(Main.class, this::prepareMain)) {
            Main.main();

            Main mockedMain = mocked.constructed().get(0);
            verify(mockedMain).run();
        }
    }

    @Test
    public void testRun() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(bytes);
        try (MockedConstruction<GraphDrawer> mocked = mockConstruction(GraphDrawer.class, this::prepareGraphDrawer)) {
            Main main = new Main();
            main.setPrintStream(printStream);
            main.run();

            GraphDrawer mockedGraphDrawer = mocked.constructed().get(0);
            verify(mockedGraphDrawer).draw();
        }

        String stdout = bytes.toString("UTF-8");
        assertThat(stdout)
                .contains("Solving for size=8 ...")
                .contains("Points:");
    }

    private void prepareMain(Main mockedMain, Context context) {
        doNothing().when(mockedMain).run();
    }

    private void prepareGraphDrawer(GraphDrawer mockedGraphDrawer, Context context) {
        doNothing().when(mockedGraphDrawer).draw();
    }
}
