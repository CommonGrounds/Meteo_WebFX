package gwt_service;

import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlFonts;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;
import dev.webfx.platform.console.Console;
//import com.google.gwt.user.*;
//import com.google.gwt.core.client.GWT;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import service.ServiceProvider;
import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.HTMLCanvasElement;
//import elemental2.dom.HTMLImageElement;
import com.google.gwt.dom.client.ImageElement;
//import com.google.gwt.user.client.ui.Image;
import com.java4now.meteo_webfx.shared.Loaded;

public class GWT_MyServiceProvider implements ServiceProvider {

    public boolean isMaximized(Stage primaryStage) {
        Console.log("[gwt] isMaximized: true");
        return true;
    }

    public native void getCompilerVersion() /*-{ $wnd.console.log("GWT version: " + $wnd.frames[0].$gwt_version); }-*/;
//    public native void getCompilerVersion() /*-{ $wnd.getGWTVersion() }-*/;                // funkcija u index.html fajlu
//    public  void getCompilerVersion(){ Console.log("GWT version: " + GWT.getVersion());}   // GWT java funk.

// debugger JS keyword - postavlja breakpoint tamo gde se postavi ( samo ako je devtools prozor na browser-u otvoren )
    public native void debugger() /*-{ debugger; }-*/;


    public native String addHoursDatetime(int h) /*-{
      var d = new Date();
//      console.log('Before:\t', d.toLocaleString())
      // Add 1 hour to datetime
      d.setHours(d.getHours() + h);
//      console.log('After:\t', d.toLocaleString())
      return $wnd.toISOLocal(d); // u index.html se nalazi funk. koja formatira na nacin da je LocalDateTime parser prihvata
    }-*/;


    public double measureText(String str, Canvas canvas){
        GraphicsContext context1 = canvas.getGraphicsContext2D();
        Font font = context1.getFont();
        HTMLCanvasElement canvasElement =  HtmlUtil.createElement("canvas");
        CanvasRenderingContext2D gc = dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.Context2DHelper.getCanvasContext2D(canvasElement,true);
        gc.setFont(HtmlFonts.getHtmlFontDefinition(font));
//        gc.setFont(font.getFamily());
//        gc.setFont(font.getStyle());
//        gc.setFont(Double.toString(font.getSize()));
        return gc.measureText(str).width;
    }


    // -------------------------------------------------------------------
    // Java Script funkcija  u .html za Screenshoot celog ekrana uz pomoc html2canvas jscript aplikacije
    public native void saveScreenshoot() /*-{
	// U HTML-u
	        $wnd.screenshot();
		}-*/;


    public void setViewport(ImageView imageView, Rectangle2D croppedPortion){
    }

    public void setSmooth(ImageView imageView, boolean smooth){
    }

// drawCanvasImage ne koristim jer javafx.scene.image.Image nije kompatibilan sa HTMLImageElement ( umesto toga WebFXUtil )
    public void drawCanvasImage(GraphicsContext context1,String image_path,int hours,double width,double image_center_v){
        final int constHours = hours;                                         // mora da bude const ( final ) u funkciji
        LoadCanvasImage(image_path, imageLoaded -> {    // Poziv Java Script funkciji
//  TODO - incompatible types: com.google.gwt.dom.client.ImageElement( kao ni elemental2.dom.HTMLImageElement ) cannot be converted to javafx.scene.image.Image
//            context1.drawImage(imageLoaded, width * constHours -  imageLoaded.getWidth() - 10,
//                    image_center_v - (imageLoaded.getHeight() / 2.0) );
            Console.log("[gwt] drawCanvasImage - height(" + imageLoaded.getHeight() + "), width(" + imageLoaded.getWidth() + ")" + imageLoaded);
        });
    }


// interna funk. samo ovde
    public native void LoadCanvasImage(String imgBase64, Loaded<ImageElement> loaded)/*-{
      var image = new Image();
      image.onload = function() {
          loaded.@com.java4now.meteo_webfx.shared.Loaded::data(*)(image);
      };
      image.src = imgBase64;
    }-*/;



//################### U index.html ########################
/*
    <script>
        function toISOLocal(d) {
        var z  = n =>  ('0' + n).slice(-2);
        var zz = n => ('00' + n).slice(-3);
        var off = d.getTimezoneOffset();
        var sign = off > 0? '-' : '+';
        off = Math.abs(off);

        return d.getFullYear() + '-'
        + z(d.getMonth()+1) + '-' +
        z(d.getDate()) + 'T' +
        z(d.getHours()) + ':'  +
        z(d.getMinutes()) + ':' +
        z(d.getSeconds()) + '.' +
        zz(d.getMilliseconds());
        //                     sign + z(off/60|0) + ':' + z(off%60);
        }
    </script>
 */
}
