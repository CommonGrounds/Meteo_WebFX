package gluon.service;

import dev.webfx.platform.console.Console;
import dev.webfx.platform.util.time.Times;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.ServiceProvider;

import java.time.LocalDateTime;

public class GluonMyServiceProvider implements ServiceProvider {

    public  boolean isMaximized(Stage primaryStage) {
        Console.log("[openjfx] isMaximized: " + primaryStage.isMaximized());
        return primaryStage.isMaximized();
    }

    public void getCompilerVersion() {
        Console.log("Java version: " + System.getProperty("java.version") + "\nJavaFX version - " + System.getProperty("javafx.runtime.version"));
    }

    public void debugger(){
        Console.log("debugger");
//        throw new java.lang.RuntimeException("Explicite Exception");
    }

    public String addHoursDatetime(int h){
        return Times.asLocalDateTime(LocalDateTime.now()).plusHours(h).toString(); //ZonedDateTime.now().plusHours(h);;
    }

    public double measureText(String str, Canvas canvas){
        Text text_in_pane = new Text();
        GraphicsContext context1 = canvas.getGraphicsContext2D();
        text_in_pane.setFont(context1.getFont());
        text_in_pane.setText(str);
        new Scene(new Group(text_in_pane));
        return text_in_pane.getLayoutBounds().getWidth();
    }

    public void saveScreenshoot(){
        Console.log("[openjfx] Saving");
    }

    public void drawCanvasImage(GraphicsContext context1,String image_path,int hours,double width,double image_center_v){
        Console.log("[openjfx] drawCanvasImage");
    }

    public void setViewport(ImageView imageView, Rectangle2D croppedPortion){
        imageView.setViewport(croppedPortion);
    }

    public void setSmooth(ImageView imageView, boolean smooth){
        imageView.setSmooth(smooth);
    }
}
