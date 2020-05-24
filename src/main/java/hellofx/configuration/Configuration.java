package hellofx.configuration;

public class Configuration {
    private int canvasWidth = 2500;
    private int canvasHeigth = 2500;
    private int cellSize = 2;
    private int framesPerSecond;
    private boolean useMultiThread = true;

    private  static  Configuration configuration = null;
    private Configuration(){

    }

    public static Configuration getConfiguration(){
        if(configuration == null){
            configuration = new Configuration();
        }
        return configuration;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public int getCanvasHeigth() {
        return canvasHeigth;
    }

    public int getColumns() {
        return canvasHeigth/cellSize;
    }

    public int getRows() {
        return canvasWidth/cellSize;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {//need to check if is possible
        this.cellSize = cellSize;
    }

    public int getFramesPerSecond() {
        return framesPerSecond;
    }

    public void setFramesPerSecond(int framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
    }

    public boolean isUseMultiThread() {
        return useMultiThread;
    }

    public void setUseMultiThread(boolean useMultiThread) {
        this.useMultiThread = useMultiThread;
    }


}
