package hellofx.customJFXClasses;

import hellofx.configuration.Configuration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class CanvasApp extends Application {
    protected int canvasWidth;
    protected int canvasHeigth;
    protected GraphicsContext graphicsContext;

    private Paint backgroundColor = Color.BLACK;
    private Timeline timeline = new Timeline();
    private int frames = 30;
    private BorderPane root;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Starting canvas APP...");
        applyConfiguration();
        this.stage = stage;
        Canvas canvas = new Canvas(canvasWidth,canvasHeigth);
        graphicsContext = canvas.getGraphicsContext2D();
        canvas.requestFocus();
        root = new BorderPane(canvas);
        stage.setScene(new Scene(root));
        setup();
        canvas.setWidth(canvasWidth);
        canvas.setHeight(canvasHeigth);
        startDrawing();
        stage.show();
        internalDraw();
    }

    private void applyConfiguration(){
        Configuration configuration = Configuration.getConfiguration();
        canvasHeigth = configuration.getCanvasHeigth();
        canvasWidth = configuration.getCanvasWidth();
    }

    public abstract void setup();


    protected void internalDraw() {
        graphicsContext.setFill(backgroundColor);
        graphicsContext.fillRect(0,0,canvasWidth,canvasHeigth);
    }

    private void startDrawing() {
        timeline.stop();
        if(frames > 0) {
            timeline.getKeyFrames().clear();
            KeyFrame frame = new KeyFrame(Duration.millis(1000/ frames), e -> internalDraw());
            timeline.getKeyFrames().add(frame);
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
    }

    //setter
    protected void setTitle(String title) {
        stage.setTitle(title);
    }
    public void setBackgroundColor(Paint color) {
        backgroundColor = color;
    }
    public void setFrames(int frames) {
        this.frames = frames;
        startDrawing();
    }
    public void setBottom(Node node) {
        root.setBottom(node);
    }

    public double map(double value, double start1, double stop1, double start2, double stop2) {
        return start2 + (stop2 - start2) * ((value -start1)/ (stop1 - start1));
    }
}
