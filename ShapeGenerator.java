import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapeGenerator extends Application{

    public static void main(String[] args){

        launch(args);
    }

    private Canvas canvas; // A panel where the random art is drawn
    private volatile boolean running;
    private Runner runner;

    private Button startButton;

    public void start(Stage stage){
        canvas = new Canvas(640,480);
        redraw(); // fill the canvas with white
        startButton = new Button("Start!");
        startButton.setOnAction( e -> doStartOrStop() );
        HBox bottom = new HBox(startButton);
        bottom.setStyle("fx-padding: 6px; -fx-border-color: black; -fx-border-width: 3px 0 0 0");
        bottom.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane(canvas);
        root.setBottom(bottom);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Click Start To Make Random Art!");
        stage.setResizable(false);
        stage.show();

    }

    private class Runner extends Thread{
        public void run(){
            while(running){
                Platform.runLater(() -> redraw());
                try{
                    Thread.sleep(2000);
                }
                catch(InterruptedException e){

                }
            }
        }
    }

    private void redraw(){
        GraphicsContext g = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        if(! running){
            g.setFill(Color.WHITE);
            g.fillRect(0, 0, width, height);
            return;
        }
        Color randomGray = Color.hsb(1,0,Math.random());
        g.setFill(randomGray);
        g.fillRect(0, 0, width, height);

        int artType = (int)(3*Math.random());

        switch(artType) {
        
        case 0:
            
            g.setLineWidth(2);
            for(int i=0;i<500;i++){
                int x1 = (int)(width * Math.random());
                int y1 = (int)(height * Math.random());
                int x2 = (int)(width * Math.random());
                int y2 = (int)(height * Math.random());
                Color randomHue = Color.hsb( 360*Math.random(),1, 1);
                g.setStroke(randomHue);
                g.strokeLine(x1, y1, x2, y2);
            }
            break;
        case 1:
            for(int i=0;i<200;i++){
                int centerX = (int)(width * Math.random());
                int centerY = (int)(height * Math.random());
                Color randomHue = Color.hsb( 360*Math.random(), 1, 1);
                g.setStroke(randomHue);
                g.strokeOval(centerX - 50, centerY - 50, 100, 100);
            }
            break;
        default:
            g.setStroke(Color.BLACK);
            g.setLineWidth(4);
            for(int i=0;i<25;i++){
                int centerX = (int)(width * Math.random());
                int centerY = (int)(height * Math.random());
                int size = 30 + (int)(170 * Math.random());
                Color randomColor = Color.color(Math.random(), Math.random(), Math.random());
                g.setFill(randomColor);
                g.fillRect(centerX - size / 2, centerY - size / 2, size, size);
                g.strokeRect(centerX - size / 2, centerY - size / 2, size, size);
            }
            break;
        }
    }

    private void doStartOrStop(){
        if(running == false){
            startButton.setText("Stop");
            runner = new Runner();
            running = true;
            runner.start();
        }else{

            startButton.setDisable(true);

            running = false;
            redraw();

            runner.interrupt();

            try{
                runner.join(1000);
            } catch(InterruptedException e){

            }

            runner = null;

            startButton.setText("Start");
            startButton.setDisable(false);
        }
    }
}

        


    


