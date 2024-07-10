package com.java4now.meteo_webfx;

import dev.webfx.platform.os.OperatingSystem;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Web_Text extends Region {

    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();

//    @GwtIncompatible
    public void set_non_gwt_stuff(){
        if (OperatingSystem.isDesktop()){
            webView.setPageFill(Color.rgb(0, 0, 0, 0)); // zadrzavamo boju i opacity parent-a ( nema za GLUON android )
        }
        webEngine.setUserStyleSheetLocation("data:,body { font-family: Verdana; font-size: 80%; text-align: left; color: white; line-height: 0.8;}" +
                ".numbers {font-weight: bold;} .temp { color: yellow;}  .rain { color: cyan; }  .snow { color: white; } ");
    }

    public Web_Text() {
//        getStyleClass().add("web_region");
//        setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.5), null, null)));
//        webView.setFontScale(0.7);
        set_non_gwt_stuff();
        getChildren().add(webView);
    }

    public void setText(String received){
        webEngine.loadContent(received, "text/html");
    }

    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(webView, 0, 0, w, h, 0, HPos.LEFT, VPos.CENTER);
    }
}
