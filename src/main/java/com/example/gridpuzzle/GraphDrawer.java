package com.example.gridpuzzle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.util.Comparator.comparingDouble;

public class GraphDrawer {

    private static final Random random = new Random();

    private final int gridSize;
    private final Set<Line> lines;

    public GraphDrawer(GridSolution solution) {
        this.gridSize = solution.getArray().length;
        this.lines = solution.getLineMap().getLines();
    }

    public GraphDrawer(int gridSize, Set<Line> lines) {
        this.gridSize = gridSize;
        this.lines = lines;
    }

    public void draw() {
        GraphPanel panel = new GraphPanelImpl(gridSize);
        for (Line line : lines) {
            List<Point2D.Double> gridPoints = calculateGridPoints(line);
            if (gridPoints.size() >= 2) {
                Point2D.Double leftPoint = gridPoints.get(0);
                Point2D.Double rightPoint = gridPoints.get(gridPoints.size() - 1);
                Color color = chooseColor(line);
                panel.acceptLine(leftPoint, rightPoint, color);
            }
        }

        Stream<ImmutablePoint> points = lines
                .stream()
                .map(line -> line.getPoints())
                .flatMap(Collection::stream);

        points.distinct()
                .map(point -> new Point2D.Double(point.x, point.y))
                .forEach(panel::acceptPoint);

        JFrame frame = new JFrame("Grid Puzzle - Press any key to close");
        frame.add((JPanel) panel);
        frame.pack();
        frame.setLocation(100, 100);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                frame.dispose();
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private List<Point2D.Double> calculateGridPoints(Line line) {
        List<Point2D.Double> points = new ArrayList<>();
        for (int i = 0; i <= gridSize; i++) {
            Point2D.Double point = line.isVerticalLine()
                    ? new Point2D.Double(line.getIntercept(), i)
                    : new Point2D.Double(i, line.getSlope() * i + line.getIntercept());
            points.add(point);
        }

        // points when row is 0 or gridSize
        if (!line.isVerticalLine()) {
            points.add(new Point2D.Double(-line.getIntercept() / line.getSlope(), 0));
            points.add(new Point2D.Double((gridSize - line.getIntercept()) / line.getSlope(), gridSize));
        }

        return points.stream()
                .filter(this::isWithinGrid)
                .sorted(comparingDouble((Point2D.Double point) -> point.x))
                .collect(Collectors.toList());
    }

    private boolean isWithinGrid(Point2D.Double point) {
        return isWithinGridInterval(point.x) && isWithinGridInterval(point.y);
    }

    private boolean isWithinGridInterval(double value) {
        return 0 <= value && value <= gridSize;
    }

    private static interface GraphPanel {
        void acceptLine(Point2D p1, Point2D p2, Color color);

        void acceptPoint(Point2D point);
    }

    private static int chooseUnitSize(int gridSize) {
        int uiSize = Integer.getInteger("puzzle.uiSize", 800);
        return uiSize / gridSize;
    }

    @SuppressWarnings("serial")
    private static class GraphPanelImpl extends JPanel implements GraphPanel {

        private final int unitSize;
        private final int startX;
        private final int startY;
        private final int endX;
        private final int endY;

        private List<Consumer<Graphics2D>> callbacks = new ArrayList<>();

        public GraphPanelImpl(int gridSize) {
            this(gridSize, chooseUnitSize(gridSize));
        }

        public GraphPanelImpl(int gridSize, int unitSize) {
            this.unitSize = unitSize;
            startX = 100;
            startY = 100;
            endX = startX + (gridSize * unitSize);
            endY = startY + (gridSize * unitSize);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

            drawBackgroundGrid(graphics);

            callbacks.forEach(callback -> callback.accept(graphics));
        }

        private void drawBackgroundGrid(Graphics2D graphics) {
            graphics.setColor(Color.LIGHT_GRAY);
            for (int i = startX; i <= endX; i += unitSize) {
                graphics.drawLine(i, startY, i, endY);
            }

            for (int i = startY; i <= endY; i += unitSize) {
                graphics.drawLine(startX, i, endX, i);
            }

            graphics.drawLine(startX, startY, startX, endY);
            graphics.drawLine(startX, endY, endX, endY);
        }

        @Override
        public void acceptLine(Point2D p1, Point2D p2, Color color) {
            callbacks.add(graphics -> {
                drawLine(graphics, p1, p2, color);
            });
        }

        @Override
        public void acceptPoint(Point2D point) {
            callbacks.add(graphics -> {
                drawOval(graphics, scalePoint(point));
            });
        }

        private void drawLine(Graphics2D graphics, Point2D p1, Point2D p2, Color color) {
            Point2D p1Scaled = scalePoint(p1);
            Point2D p2Scaled = scalePoint(p2);

            graphics.setColor(color);
            graphics.drawLine((int) p1Scaled.getX(), (int) p1Scaled.getY(), (int) p2Scaled.getX(), (int) p2Scaled.getY());
        }

        private Point2D.Double scalePoint(Point2D p1) {
            double x = startX + (unitSize * p1.getX());
            double y = endY - (p1.getY() * unitSize);
            return new Point2D.Double(x, y);
        }

        private void drawOval(Graphics2D graphics, Point2D p1Scaled) {
            int ovalSize = unitSize / 10;
            int pointOffset = ovalSize / 2; // offset to center oval at the point
            double x = p1Scaled.getX() - pointOffset;
            double y = p1Scaled.getY() - pointOffset;
            graphics.setColor(Color.RED);
            graphics.fillOval((int) x, (int) y, ovalSize, ovalSize);

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(startX + endX, startX + endX);
        }

    }

    private static Color chooseColor(Line line) {
        double slope = line.getSlope();
        if (line.isVerticalLine() || slope == 0.0D || slope == 1.0D || slope == -1.0D) {
            return Color.GRAY;
        }
        return new Color(nextRandomRgb(), nextRandomRgb(), nextRandomRgb());
    }

    private static int nextRandomRgb() {
        return random.nextInt(200); // avoid light colors
    }

}