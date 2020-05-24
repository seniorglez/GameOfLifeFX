package hellofx;

import hellofx.configuration.Configuration;
import hellofx.customJFXClasses.CanvasApp;
import hellofx.services.ConsumerService;
import hellofx.services.ProducerService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class HelloFX extends CanvasApp {

    boolean currentGeneration[][];
    private GameOfLife gameOfLife;
    ConcurrentLinkedQueue<boolean [][]> cellsQueue;
    private Configuration configuration;
    ProducerService producerService;
    ConsumerService consumerService;

    @Override
    public void setup() {
        setUpNewGame();
        consumerService = new ConsumerService();
        producerService = new ProducerService();
        setupConsumerServicie();
        setupProducerServicie();
        producerService.start();
        consumerService.start();
        Parent root = (Parent) controlPanel();

        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(root, 225, 225));
        stage.show();
        setFrames(0);
        setTitle("Game of life");
    }

    private void setUpNewGame() {
        configuration = Configuration.getConfiguration();
        cellsQueue = new ConcurrentLinkedQueue<>();
        gameOfLife = new GameOfLife(configuration.getColumns(), configuration.getRows(), configuration.getCellSize());
        currentGeneration = gameOfLife.newCells();
    }

    private void setupProducerServicie() {
        producerService.setCellsQueue(cellsQueue);
        producerService.setCurrentGeneration(currentGeneration);
        producerService.setGameOfLife(gameOfLife);
    }

    private void setupConsumerServicie() {
        consumerService.setCellsQueue(cellsQueue);
        consumerService.setGameOfLife(gameOfLife);
        consumerService.setGraphicsContext(graphicsContext);
    }

    private Node controlPanel() {

        Button startButton = new Button("■");
        Button stopButton = new Button("Stop");

        Runnable updateConf = () -> {
            Configuration conf = Configuration.getConfiguration();
        };

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(startButton.textProperty().getValue().equals("■")){
                    producerService.cancel();
                    consumerService.cancel();
                    startButton.setText("►");
                } else {
                    startServices();
                    startButton.setText("■");
                }
            }
        });
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateConf.run();
                producerService.cancel();
                consumerService.cancel();
                setUpNewGame();
                setupConsumerServicie();
                setupProducerServicie();
            }
        });
        HBox hBox = new HBox(startButton,stopButton);
        VBox vBox = new VBox(hBox);
        hBox.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private void startServices(){
        if ((producerService.stateProperty().getValue().equals("READY"))) {
            producerService.start();
            consumerService.start();
        } else {
            producerService.restart();
            consumerService.restart();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}