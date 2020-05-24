package hellofx.services;

import hellofx.GameOfLife;
import hellofx.configuration.Configuration;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConsumerService extends Service<Void> {

    private ConcurrentLinkedQueue<boolean [][]> cellsQueue;

    private GameOfLife gameOfLife;
    private int framesPerSecond;
    private GraphicsContext graphicsContext;
    private int canvasWidth;
    private int canvasHeigth;
    private Configuration configuration = Configuration.getConfiguration();

    @Override
    protected Task<Void> createTask() {
        applyConfiguration();
        Task<Void> consumerTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while(!isCancelled()){
                    while(!cellsQueue.isEmpty()){
                        boolean[][] data = cellsQueue.poll();
                        Platform.runLater(()-> {
                            graphicsContext.setFill(Color.LIGHTGRAY);
                            graphicsContext.fillRect(0,0,canvasWidth,canvasHeigth);
                            gameOfLife.drawCells(data,graphicsContext);
                        });
                        if(framesPerSecond > 0) {
                            Thread.sleep(1000/framesPerSecond);
                        }
                    }
                }
                return null;
            }
        };
        return consumerTask;
    }

    private void applyConfiguration() {
        framesPerSecond = configuration.getFramesPerSecond();
        canvasWidth = configuration.getCanvasWidth();
        canvasHeigth = configuration.getCanvasHeigth();
    }

    public void setCellsQueue(ConcurrentLinkedQueue<boolean[][]> cellsQueue) {
        this.cellsQueue = cellsQueue;
    }

    public void setGameOfLife(GameOfLife gameOfLife) {
        this.gameOfLife = gameOfLife;
    }

    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }
}
