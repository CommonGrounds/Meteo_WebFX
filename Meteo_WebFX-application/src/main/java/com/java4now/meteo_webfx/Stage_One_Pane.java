package com.java4now.meteo_webfx;

import com.java4now.meteo_webfx.shared.*;
import dev.webfx.extras.webtext.HtmlText;
import dev.webfx.platform.resource.Resource;
import dev.webfx.platform.useragent.UserAgent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import service.Service_impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.java4now.meteo_webfx.shared.Forecast_current.Background_image_name;
//import dev.webfx.extras.webtext.peers.openjfx.FxHtmlTextTextFlowPeer;

public class Stage_One_Pane extends Pane {

    public boolean DATA_IS_FETCHED = false;
    ArrayList <Image> image_list = new ArrayList<>();
    String rotate_image = "_rotate";
    String not_rotate_image = "_not";
    Image image_rain = new Image(Resource.toUrl("Background/rain" + not_rotate_image + ".jpg", Meteo_WebFX.class), true);
    Image image_rain_rot = new Image(Resource.toUrl("Background/rain" + rotate_image + ".jpg", Meteo_WebFX.class), true);
    Image image_sunny = new Image(Resource.toUrl("Background/sunny" + not_rotate_image + ".jpg", Meteo_WebFX.class), true);
    Image image_sunny_rot = new Image(Resource.toUrl("Background/sunny" + rotate_image + ".jpg", Meteo_WebFX.class), true);
    Image image_night = new Image(Resource.toUrl("Background/night" + not_rotate_image + ".jpg", Meteo_WebFX.class), true);
    Image image_night_rot = new Image(Resource.toUrl("Background/night" + rotate_image + ".jpg", Meteo_WebFX.class), true);
    Image image_snow = new Image(Resource.toUrl("Background/snow" + not_rotate_image + ".jpg", Meteo_WebFX.class), true);
    Image image_snow_rot = new Image(Resource.toUrl("Background/snow" + rotate_image + ".jpg", Meteo_WebFX.class), true);
    Image image_thunder = new Image(Resource.toUrl("Background/thunder" + not_rotate_image + ".jpg", Meteo_WebFX.class), true);
    Image image_thunder_rot = new Image(Resource.toUrl("Background/thunder" + rotate_image + ".jpg", Meteo_WebFX.class), true);
    Image image_tmp;
    ImageView image_view;
    BorderPane main_pane;
    Canvas layer_1;
    Canvas layer_2;
    VBox toolTip_Vbox;
    HtmlText html_text;
    Web_Text web_text;
    Button forecast_graph_btn;
    VBox settings_pane;
    Forecast_current forecast;
    Forecast_Daily forecast_daily;
    Forecast_Hourly forecast_hourly;
    AQI_Hourly aqi_hourly;
    boolean IS_MOBILE_VIEW_PORTRAIT = false;
    WeatherGraphic graph = new WeatherGraphic();
    HBox choice_group;
//    Text text = new Text();

    String razmak_caption = "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";
    String razmak_temp = "&nbsp";
    String razmak_kisa = "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";
    String razmak_sneg = "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";
    String razmak_uv = "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";

    public Stage_One_Pane(ImageView imageView, BorderPane main_pane, Canvas layer1, Canvas layer2, Button forecast_graph_btn, VBox toolTip_Vbox,
                          Forecast_current forecast, Forecast_Daily forecast_daily, Forecast_Hourly forecast_hourly, AQI_Hourly aqi_hourly, HBox choice_group,VBox settings_pane){
        super(imageView,main_pane,layer1,layer2,forecast_graph_btn,toolTip_Vbox,choice_group,settings_pane);
        this.main_pane = main_pane;
        this.image_view = imageView;
        this.layer_1 = layer1;
        this.layer_2 = layer2;
        this.forecast_graph_btn = forecast_graph_btn;
        this.settings_pane = settings_pane;
        this.toolTip_Vbox = toolTip_Vbox;
        this.forecast = forecast;
        this.forecast_hourly = forecast_hourly;
        this.forecast_daily = forecast_daily;
        this.aqi_hourly = aqi_hourly;
        this.choice_group = choice_group;
        image_list.add(image_rain);
        image_list.add(image_rain_rot);
        image_list.add(image_sunny);
        image_list.add(image_sunny_rot);
        image_list.add(image_night);
        image_list.add(image_night_rot);
        image_list.add(image_snow);
        image_list.add(image_snow_rot);
        image_list.add(image_thunder);
        image_list.add(image_thunder_rot);
        setToolTip();
        addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                hide_ToolTip();
            }
        });
    }

    private void setToolTip(){
        toolTip_Vbox.getStyleClass().add("ToolTip_html");    // pane style
        toolTip_Vbox.setPrefWidth(180);
        toolTip_Vbox.setPrefHeight(140);
        if(UserAgent.isBrowser()){
            html_text = new HtmlText();
            toolTip_Vbox.getChildren().add(html_text);
        }else{
            web_text = new Web_Text();
            toolTip_Vbox.getChildren().add(web_text);
        }
        toolTip_Vbox.setVisible(false);
    }

    public void show_ToolTip(double x, double y,double hours ,String datum,
                             final double temperature_text,final double rain_text , final double snow_text ,final double uv_index_text){

        double left = x;
        if((left + toolTip_Vbox.getWidth()) > this.getWidth() -30 ) {
            left = x - toolTip_Vbox.getWidth() - 40; // 20px - 20px
        }
        double top = y;
        if((top + toolTip_Vbox.getHeight()) > this.getHeight() -30 ) {
            top = (y - toolTip_Vbox.getHeight()) - 20;
        }
        toolTip_Vbox.setLayoutX(left);
        toolTip_Vbox.setLayoutY(top);

        //   trenutno vreme + sati - sati sada ( da bi smo dobili plus sati od 00:00 danas )
        LocalDateTime date = LocalDateTime.parse(Service_impl.addHoursDatetime((int)hours - Integer.parseInt(forecast.time.substring(11, 13))));
//                Console.log(date.toString());
        String datum_caption = DateTimeFormatter.ofPattern("dd/MM").format(date);

        if((int)hours % 24 < 10) {
            datum_caption = datum_caption +  " - 0" + (int)hours % 24 + ":00 h";
        }else {
            datum_caption = datum_caption +  " - " + (int)hours % 24 + ":00 h";
        }
        String html;
        if(snow_text > 0 ) {
            html = "<p> <span class=\"numbers\">" + razmak_caption + datum_caption + "</span> </p><br>" +
                    "<p> <span class=\"temp\">Temperature</span> : <span class=\"numbers\">" + razmak_temp + temperature_text  + "</span> &nbsp℃</p>"    +
                    "<p> <span class=\"rain\">Rain</span> : <span class=\"numbers\">" + razmak_kisa + rain_text + "</span> &nbsp&nbspLit</p>"   +
                    "<p> <span class=\"snow\">Snow</span> : <span class=\"numbers\">" + razmak_sneg + snow_text + "</span> &nbspcm</p>";
        }else {
            html = "<p> <span class=\"numbers\">" + razmak_caption + datum_caption + "</span> </p><br>" +
                    "<p> <span class=\"temp\">Temperature</span> : <span class=\"numbers\">" + razmak_temp + temperature_text + "</span> &nbsp℃</p>" +
                    "<p> <span class=\"rain\">Rain</span> : <span class=\"numbers\">" + razmak_kisa + rain_text + "</span> &nbsp&nbspLit</p>" +
                    "<p> <span class=\"snow\">UV Index</span> : <span class=\"numbers\">" + razmak_uv + uv_index_text + "</span></p>";
        }
        if(UserAgent.isBrowser()){
            html_text.setText(html);
        }else{
            web_text.setText(html);
        }
        toolTip_Vbox.setVisible(true);
    }

    public void hide_ToolTip(){
        toolTip_Vbox.setVisible(false);
    }

    @Override
    protected void layoutChildren() {
//        Console.log(forecast.Background_image_name + rotate_image + ".jpg");
        if(getHeight() > getWidth()){
            for (Image image : image_list) {
                if(image.getUrl().contains(Background_image_name + rotate_image )){
                    image_tmp = image;
                }
            }
            image_view.setImage(image_tmp);
        }else{
            for (Image image : image_list) {
                if(image.getUrl().contains(Background_image_name + not_rotate_image)){
                    image_tmp = image;
                }
            }
            image_view.setImage(image_tmp);
        }

        image_view.setFitHeight(getHeight());
        image_view.setFitWidth(getWidth());
        image_view.setX(0);
        image_view.setY(0); // getImage().getHeight() // originalna velicina

        main_pane.setMaxWidth(0.9 * getWidth());
        main_pane.setMaxHeight(0.9 * getHeight());
        main_pane.setMinWidth(0.5 * getWidth());
        main_pane.setMinHeight(0.4 * getHeight());
        main_pane.setLayoutX(getWidth()/2 - main_pane.getWidth()/2);
        if (UserAgent.isBrowser()) {
            main_pane.setLayoutY(getHeight() * 0.05);    // pri vrhu
        }else{
            main_pane.setLayoutY(getHeight()/2 - main_pane.getHeight()/2);       // 1/2
        }

        forecast_graph_btn.setLayoutX(getWidth()/2 - forecast_graph_btn.getWidth()/2);
        if(forecast_graph_btn.getText() == "Forecast"){
            forecast_graph_btn.setLayoutY(main_pane.getLayoutY() + main_pane.getHeight() + 15 );
        } else {
            forecast_graph_btn.setLayoutX(choice_group.getLayoutX() + choice_group.getWidth() + 50);
            forecast_graph_btn.setLayoutY(15);
        }

        if (UserAgent.isBrowser() || getWidth() >= 700){
            if(!UserAgent.isBrowser()){
                layer_1.setWidth(getWidth());
                layer_1.setHeight(getHeight() * 0.625);
                layer_1.setLayoutX(0);
                layer_1.setLayoutY(getHeight()/2 - layer_1.getHeight()/2);
                layer_2.setWidth(getWidth());
                layer_2.setHeight(getHeight() * 0.625);
                layer_2.setLayoutX(0);
                layer_2.setLayoutY(getHeight()/2 - layer_1.getHeight()/2);
                forecast_graph_btn.setVisible(true);
                /*
                layer_1.scaleXProperty().set(0.5);
                layer_1.scaleYProperty().set(0.5);
                layer_2.scaleXProperty().set(0.5);
                layer_2.scaleYProperty().set(0.5);
                 */
            }else{
                layer_1.setWidth(getWidth() * 0.95);
                layer_1.setHeight(getHeight() * 0.38);
                layer_1.setLayoutX(getWidth()/2 - layer_1.getWidth()/2);
                layer_1.setLayoutY(getHeight() - layer_1.getHeight()-20);
                layer_2.setWidth(getWidth() * 0.95);
                layer_2.setHeight(getHeight() * 0.38);
                layer_2.setLayoutX(getWidth()/2 - layer_1.getWidth()/2);
                layer_2.setLayoutY(getHeight() - layer_1.getHeight()-20);
                forecast_graph_btn.setVisible(false);
            }
            IS_MOBILE_VIEW_PORTRAIT = false;
        }else {
            IS_MOBILE_VIEW_PORTRAIT = true;
        }

        settings_pane.setLayoutX(getWidth() - settings_pane.getWidth() - 10);
        if (UserAgent.isBrowser()){
            settings_pane.setLayoutY(10);
        }else{
            settings_pane.setLayoutY(getHeight() - settings_pane.getHeight() - 10 );
        }

        if(DATA_IS_FETCHED && !IS_MOBILE_VIEW_PORTRAIT && !toolTip_Vbox.isVisible()){
            choice_group.setLayoutX(10);
            choice_group.setLayoutY(10);
            graph.drawchart_canvas(layer_1,layer_2,forecast, forecast_daily, forecast_hourly, aqi_hourly);
        }

        super.layoutChildren();
    }
}
