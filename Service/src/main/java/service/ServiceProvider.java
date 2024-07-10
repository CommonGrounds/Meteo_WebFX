package service;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public interface ServiceProvider {

    default boolean isMaximized(Stage primaryStage) {
        return false;
    }

    default void getCompilerVersion(){
    }

    default void debugger(){
    }

    default String addHoursDatetime(int h){
        return "Default";
    }

    default void saveScreenshoot(){
    }

    default double measureText(String str, Canvas canvas){
        return -1.0;
    }

    default void drawCanvasImage(GraphicsContext context1, String image_path, int hours, double width, double image_center_v){
    }

    default void setViewport(ImageView imageView, Rectangle2D croppedPortion){
    }

    default void setSmooth(ImageView imageView, boolean smooth){
    }
}
