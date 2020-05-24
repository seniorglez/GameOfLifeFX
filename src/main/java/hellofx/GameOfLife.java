package hellofx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Arrays;
import java.util.stream.IntStream;

public class GameOfLife {
    private int columns;
    private int rows;
    private int cellSize;
    private boolean useMultiThread = true;

    public GameOfLife(int columns, int rows, int cellSize) {
        this.columns = columns;
        this.rows = rows;
        this.cellSize = cellSize;
    }

    public boolean[][] newCells() {
        boolean[][] newCells = new boolean[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0;j <rows; j++) {
                newCells[i][j] = Math.random() > 0.5;
            }
        }
        return newCells;
    }

    public void drawCells(boolean[][] cells, GraphicsContext graphicsContext) {
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (cells[i][j]) {
                    graphicsContext.setFill(Color.BLACK);
                    graphicsContext.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    public boolean [][] newGeneration(boolean previusGeneration[][]) {
        return (useMultiThread)?newGenerationParallel(previusGeneration):newGenerationOneThread(previusGeneration);
    }

    public boolean[][] newGenerationOneThread(boolean previusGeneration[][]){
        boolean[][] newGeneration = new boolean[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                updateCell(previusGeneration, newGeneration, i, j);
            }
        }
        return newGeneration;
    }

    public boolean [][] newGenerationParallel (boolean previusGeneration[][]){
        boolean[][] newGeneration = new boolean[columns][rows];
        IntStream.range(0, columns).parallel().forEach(i -> {
            for(int j = 0; j < rows; j++) {
                updateCell(previusGeneration, newGeneration, i, j);
            }
        });
        return newGeneration;
    }

    private void updateCell(boolean[][] previusGeneration, boolean[][] newGeneration, int i, int j) {
        int countNeighbours = countNeighbours(previusGeneration, i , j);
        if(previusGeneration[i][j] && (countNeighbours < 2 || countNeighbours > 3)) {
            newGeneration[i][j] = false;
        } else if (!previusGeneration[i][j] && countNeighbours == 3) {
            newGeneration[i][j] = true;
        } else if (previusGeneration[i][j]) {
            newGeneration[i][j] = true;
        }
    }

    private int countNeighbours(boolean[][] copy, int i, int j) {
        int[][] borders = {
                {i - 1, j -1}, {1 - 1, j}, {i - 1, j + 1}, {i , j -1}, {i, j +1},
                {i + 1, j -1}, {i + 1, j}, {i + 1, j +1}
        };

        return (int) Arrays.stream(borders).filter(b -> b[0] > -1 &&
                b[0] < columns &&
                b[1] > -1 &&
                b[1] < rows &&
                copy[b[0]][b[1]]).count();
    }
}
