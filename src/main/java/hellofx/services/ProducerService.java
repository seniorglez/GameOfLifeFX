package hellofx.services;

import hellofx.GameOfLife;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ProducerService extends Service<Void> {

    private GameOfLife gameOfLife;
    ConcurrentLinkedQueue<boolean [][]> cellsQueue;
    boolean currentGeneration[][];

    @Override
    protected Task<Void> createTask() {
        Task<Void> producerTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while(!isCancelled()) {
                    cellsQueue.add(currentGeneration);
                    currentGeneration = gameOfLife.newGeneration(currentGeneration);
                }
                return null;
            }
        };
        return producerTask;
    }

    public void setGameOfLife(GameOfLife gameOfLife) {
        this.gameOfLife = gameOfLife;
    }

    public void setCellsQueue(ConcurrentLinkedQueue<boolean[][]> cellsQueue) {
        this.cellsQueue = cellsQueue;
    }

    public void setCurrentGeneration(boolean[][] currentGeneration) {
        this.currentGeneration = currentGeneration;
    }
}
