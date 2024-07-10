package service;

import dev.webfx.platform.service.SingleServiceProvider;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.util.ServiceLoader;

public final class Service_impl {
    private Service_impl() {}

    private static ServiceProvider getProvider() {
        return SingleServiceProvider.getProvider(ServiceProvider.class, () -> ServiceLoader.load(ServiceProvider.class));
    }

    public static boolean isMaximized(Stage primaryStage) {
        return getProvider().isMaximized(primaryStage);
    }

    public static void getCompilerVersion() {
        getProvider().getCompilerVersion();
    }

    public static void debugger() {
        getProvider().debugger();
    }

    public static String addHoursDatetime(int h){
        return getProvider().addHoursDatetime(h);
    }

    public static void saveScreenshoot(){
        getProvider().saveScreenshoot();
    }

    public static double measureText(String str, Canvas canvas){
        return getProvider().measureText(str, canvas);
    }

    public static void drawCanvasImage(GraphicsContext context1, String image_path, int hours, double width, double image_center_vString ){
        getProvider().drawCanvasImage(context1, image_path, hours, width, image_center_vString);
    }

    public static void setViewport(ImageView imageView, Rectangle2D croppedPortion){
        getProvider().setViewport(imageView,croppedPortion);
    }

    public static void setSmooth(ImageView imageView, boolean smooth){
        getProvider().setSmooth(imageView,smooth);
    }
}