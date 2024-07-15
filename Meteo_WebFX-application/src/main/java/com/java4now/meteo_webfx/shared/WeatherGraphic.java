package com.java4now.meteo_webfx.shared;

import com.java4now.meteo_webfx.Meteo_WebFX;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.resource.Resource;
import dev.webfx.platform.useragent.UserAgent;
import dev.webfx.stack.i18n.I18n;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import service.Service_impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

public class WeatherGraphic {

    private GraphicsContext context1;
//    Text text_in_pane; // mora da se doda u pane da bi se merila duzina teksta
    private boolean SAVE_SCREEN_IMAGE = false;
    double image_scale = 1.0;

    //--------------------------------------------------------------------------
    public void drawchart_canvas(Canvas layer1,Canvas layer2, Forecast_current forecast, Forecast_Daily forecast_daily, Forecast_Hourly forecast_hourly,
                                 AQI_Hourly aqi_hourly) {
        //		int x_pos = (Window.getClientWidth()/2) - (final_vPanel.getOffsetWidth()/2);
        int all_points = forecast_hourly.time.length; // 7 * 24; // 7 dana * 24
//		Debugging.console("all_points " + all_points);
        double one_hour_width = (layer1.getWidth() / all_points); // zaokruzujemo na 1. nizi ceo broj
        double canvasWidth = one_hour_width * all_points /* final_vPanel.getOffsetWidth() */;
        double canvasHeight = layer1.getHeight();
        double scale_temp_height = 1.0;
        double scale_rain_height = 1.0;
        double scale_snowfall_height = 1.0;
        double max_drawing_height = layer1.getHeight() - 50;
        double max_positive_drawing_height = max_drawing_height; // default vrednost
        double temperature_range = (forecast_hourly.temp_max + Math.abs(forecast_hourly.temp_min));
        double positive_temperature_range = forecast_hourly.temp_max;
        double negative_temperature_range = Math.abs(forecast_hourly.temp_min);

        if (forecast_hourly.temp_min < 0.0) {
            scale_temp_height = max_drawing_height / temperature_range;
            max_positive_drawing_height = positive_temperature_range * scale_temp_height;
        } else {
            scale_temp_height = (canvasHeight - 50) / forecast_hourly.temp_max;
        }
        final double scale_temp = scale_temp_height;
        final double max_pos = max_positive_drawing_height;

        double horizontal_axis_line_step = (canvasHeight - 50) / 3;
        scale_snowfall_height = (max_drawing_height - horizontal_axis_line_step) / forecast_hourly.snowfall_max;
        scale_rain_height = max_positive_drawing_height / forecast_hourly.rain_max;
//		Debugging.console("scale_rain_height " + scale_rain_height);
//		Debugging.console("scale_temp_height " + scale_temp_height);

        // Obtain Graphics Contexts
        context1 = layer1.getGraphicsContext2D();
        //       gc2 = layer2.getGraphicsContext2D();

        //################### POSTAVLJA BOJU NA TRANSPARENTNO ( OBAVEZNO PRE REDRAW ) ####################
        context1.clearRect(0, 0, canvasWidth, canvasHeight);
//        context2.clearRect(0, 0, canvasWidth, canvasHeight);
//################################################################################################

//------------------- Koordinatni sistem ------------
        context1.beginPath();
        context1.setStroke(Color.rgb(255, 255, 255));
        //       Console.log(forecast.time);
        context1.moveTo(one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)), 0);
        context1.lineTo(one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)), canvasHeight - 40); // Linija Sada
        context1.setLineWidth(1);
        context1.stroke();
        context1.closePath();
        context1.beginPath();
//----------------- Linije horizontale na osi ----------------------

        if (forecast_hourly.temp_min < 0.0 && forecast_hourly.temp_max > 0.0) {
            context1.setLineWidth(1);
//			context1.moveTo(one_hour_width * Integer.parseInt(forecast.time.substring(12, 14)) - 5, horizontal_axis_line_step*0);
//			context1.lineTo(canvasWidth - 0, horizontal_axis_line_step*0);
            context1.moveTo(one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - 5, horizontal_axis_line_step * 1);
            context1.lineTo(canvasWidth - 0, horizontal_axis_line_step * 1);
            context1.stroke();
            context1.closePath(); // mora ako menjamo boju u okviru istog path-a i debljinu linije
            context1.beginPath();
            context1.moveTo(0, canvasHeight - 50);
            context1.lineTo(canvasWidth - 0, canvasHeight - 50); // Linija debela X osa
            context1.setLineWidth(5);
            context1.stroke();
            context1.closePath(); // mora ako menjamo boju u okviru istog path-a
            context1.beginPath();
            context1.setStroke(Color.rgb(255, 0, 0)); // red za 0 stepeni
            context1.setLineWidth(2);
//			context1.moveTo(one_hour_width * Integer.parseInt(forecast.time.substring(12, 14)) - 5,max_drawing_height -
//					(negative_temperature_range * scale_temp_height)); // pocetak na sada - 5
            context1.moveTo(5, max_drawing_height - (negative_temperature_range * scale_temp_height)); // pocetak canvasa + 5
            context1.lineTo(canvasWidth - 0, max_drawing_height - (negative_temperature_range * scale_temp_height));
            context1.stroke();
            context1.setLineWidth(1);
        } else if (forecast_hourly.temp_max < 0.0 && forecast_hourly.temp_max < 0.0) {
            context1.closePath(); // mora ako menjamo boju u okviru istog path-a
            context1.beginPath();
            context1.setStroke(Color.rgb(255, 0, 0)); // red za 0 stepeni
            context1.moveTo(0, canvasHeight - 50);
            context1.lineTo(canvasWidth - 0, canvasHeight - 50); // Linija debela X osa
            context1.setLineWidth(5);
            context1.stroke();
            context1.closePath(); // mora ako menjamo boju u okviru istog path-a i debljinu linije
            context1.beginPath();
            context1.setLineWidth(2);
//			context1.moveTo(one_hour_width * Integer.parseInt(forecast.time.substring(12, 14)) - 5, horizontal_axis_line_step*0);
//			context1.lineTo(canvasWidth - 0, horizontal_axis_line_step*0);
            context1.moveTo(one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - 5, horizontal_axis_line_step * 1);
            context1.lineTo(canvasWidth - 0, horizontal_axis_line_step * 1);
            context1.stroke();
//			context1.moveTo(one_hour_width * Integer.parseInt(forecast.time.substring(12, 14)) - 5,1); // umesto linije za 0 stepeni crtamo gornju liniju crveno
//			context1.lineTo(canvasWidth - 0, 1);
//			context1.stroke();
//			context1.setLineWidth(1);
        } else {
            context1.moveTo(0, canvasHeight - 50);
            context1.lineTo(canvasWidth - 0, canvasHeight - 50); // Linija debela X osa
            context1.setLineWidth(5);
            context1.stroke();
            context1.closePath(); // mora ako menjamo boju u okviru istog path-a i debljinu linije
            context1.beginPath();
            context1.setLineWidth(1);
            context1.moveTo(one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - 5, horizontal_axis_line_step * 1);
            context1.lineTo(canvasWidth - 0, horizontal_axis_line_step * 1);
            context1.stroke();
            context1.moveTo(one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - 5, horizontal_axis_line_step * 2);
            context1.lineTo(canvasWidth - 0, horizontal_axis_line_step * 2);
            context1.stroke();
        }

//---------------------- GRAPHICON RAIN ---------------------------
        context1.setFill(Color.rgb(0, 255, 255)); // cyan
        for (int i = 0; i < forecast_hourly.time.length; i++) {
            double y = forecast_hourly.rain[i];
            /*
             * x The x-coordinate of the upper-left corner of the rectangle
             * y The y-coordinate of the upper-left corner of the rectangle
             * width The width of the rectangle, in pixels
             * height The height of the rectangle, in pixels
             */
// fillRect pocinje odozgo levo pa na dole
//                                       x          ,                      y                      ,      width        ,         height
            if (y > 0) {
                context1.fillRect(one_hour_width * i, max_positive_drawing_height - (y * scale_rain_height), one_hour_width - 1, (y * scale_rain_height));
            }

        }
//---------------------- GRAPHICON SNOW ---------------------------
        context1.setFill(Color.rgb(220, 220, 220)); // dirty white
        for (int i = 0; i < forecast_hourly.time.length; i++) {
            double y = forecast_hourly.snowfall[i];
            if (y > 0) {
                context1.fillRect(one_hour_width * i, max_drawing_height - (y * scale_snowfall_height), one_hour_width - 1, (y * scale_snowfall_height));
            }
        }
//--------------------- GRAPH TEMPERATURE ----------------------
        context1.closePath();
        context1.beginPath();
        context1.setStroke(Color.rgb(255, 255, 0)); // yellow
        context1.setLineWidth(2);
        for (int i = 0; i < forecast_hourly.time.length; i++) {
            double y = forecast_hourly.temperature[i];
            if (i == 0) {
                context1.moveTo(one_hour_width * i, max_positive_drawing_height - ((int) y * scale_temp_height));
                continue;
            }
//			start = Duration.currentTimeMillis();                          // code za delay
            context1.lineTo(one_hour_width * i, max_positive_drawing_height - ((int) y * scale_temp_height));
//			context1.arc(one_hour_width * i, max_pos - (int)y * scale_temp,2 ,0 ,2 * Math.PI );
            if (i == forecast_hourly.time.length - 1) {
                context1.lineTo(canvasWidth, max_positive_drawing_height - ((int) y * scale_temp_height));
            }
/*
            if(y<0){
                context1.setStrokeStyle(CssColor.make(255, 255, 0)); // yellow
            }else{
                context1.setStrokeStyle(CssColor.make(255, 0, 0));   // red
            }
			context1.stroke();
			context1.closePath();  // mora ako menjamo boju u okviru istog path-a i debljinu linije
			context1.beginPath();
			context1.moveTo(one_hour_width * i, max_positive_drawing_height - ((int) y * scale_temp_height));
 */
        }
        context1.stroke();

        //------------------ FILL GRAPH SHAPE ( PATH ) - TRANSPARENT -----------------
        context1.save();
        context1.lineTo(canvasWidth, max_drawing_height - 3);
        context1.lineTo(0, max_drawing_height - 3); // -3 polovina debljine x-ose
        context1.lineTo(0, ((int) forecast_hourly.temperature[0] * scale_temp_height));
// zatvorena putanja koja pocinje od beginPath , nema stroke za deo putanje koji se ne vidi pa ne treba setGlobalAlpha(0);
        context1.closePath();
        context1.setGlobalAlpha(0.1);
        if (forecast_hourly.temp_max < 0.0) {
            context1.setFill(Color.rgb(255, 0, 0));   // red
        } else {
            context1.setFill(Color.rgb(255, 255, 0)); // yellow
        }
        context1.fill();
        context1.restore(); // vraca na staro ne treba context1.setGlobalAlpha(1);

        //-------------- TEKST NA X OSI -----------------------------
        Font font = Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 12);
        context1.setFont(font);
        String txt;
        context1.closePath();
        context1.beginPath();
        context1.setFill(Color.rgb(255, 255, 255));
        context1.setStroke(Color.rgb(255, 255, 255));
        context1.setLineWidth(2);

        String DANI_U_NEDELJI[] = {I18n.getI18nText("MONDAY"), I18n.getI18nText("TUESDAY"), I18n.getI18nText("WEDNESDAY"),
                I18n.getI18nText("THURSDAY"), I18n.getI18nText("FRIDAY"), I18n.getI18nText("SATURDAY"), I18n.getI18nText("SUNDAY")};

        int offset = 11;
        if (!UserAgent.isBrowser()) {
            offset = 12;
        }
        for (int h = 0; h < forecast_hourly.time.length; h++) {
//            Console.log(forecast_hourly.time[h].substring(12,17) + " , h: " + h);
            if (h == 0) {
                //------------------------ DAN / GODINA ----------------------------------
                txt = I18n.getI18nText("TODAY");
                context1.fillText(txt, (one_hour_width * 12) - Service_impl.measureText(txt, layer1) / 2, canvasHeight - 35);
                continue;
            }

            if (forecast_hourly.time[h].startsWith("00:00", offset)) {
                LocalDateTime date = LocalDateTime.parse(Service_impl.addHoursDatetime(h));
//                Console.log(date.toString());
                txt = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date);
                context1.fillText(txt, (one_hour_width * (h + 12)) - Service_impl.measureText(txt, layer1) / 2, canvasHeight - 35); // sredina dana

//------------------------ NAZIV DANA ISPOD DAN / GODINA -------------------------
                txt = /*fmt.format(today)*/ DANI_U_NEDELJI[date.getDayOfWeek().getValue() - 1]; // -1 jer krece od 1
                context1.fillText(txt, (one_hour_width * (h + 12)) - Service_impl.measureText(txt, layer1) / 2,
                        (canvasHeight - 35) + (context1.getFont().getSize() + 1));

//------------------ VERTIKALNE LINIJE ( DANI ) NA X OSI ------------------------
                context1.moveTo(one_hour_width * h, 0);
                context1.lineTo(one_hour_width * h, canvasHeight - 45);
                context1.stroke();
            }
        }
        //----------------- WEATHER ICONS NA GRAFIKONU -----------------------
        final double[] image_center_v = {0}; // 40
        int hours = 24;
        for (int d = 0; d < forecast_daily.time.length; d++) {
            final int constHours = hours;
            if (UserAgent.isBrowser()) {
                // za upotrebu WebFXUtil backgroundLoading mora biti true da bi imali image.progressProperty() ( radi i za openjfx i za GWT )
                Image image_tmp = new Image(Resource.toUrl(forecast_daily.WMO_image_name[d], Meteo_WebFX.class), true);
                WebFXUtil.onImageLoaded(image_tmp, () -> {
                    image_center_v[0] = (image_tmp.getHeight() / 2) + (image_tmp.getHeight() * 0.25);
                    double finalImage_center_v = image_center_v[0];
                    context1.drawImage(image_tmp, one_hour_width * constHours - image_tmp.getWidth() - 10,
                            finalImage_center_v - (image_tmp.getHeight() / 2));
                });
//             Service_impl.drawCanvasImage(context1,image_tmp.getUrl(),hours,one_hour_width,image_center_v);  // JS funk. ne radi jer FX image nije kompatibilan
            } else {
                // Za direktni drawImage backgroundLoading mora biti false ( radi samo za openjfx )
                Image image_tmp = new Image(Resource.toUrl(forecast_daily.WMO_image_name[d], Meteo_WebFX.class), false);
                if(layer1.getWidth() < 1000){
                    image_tmp = scaleImage(image_tmp);
                }
                image_center_v[0] = (image_tmp.getHeight() / 2) + (image_tmp.getHeight() * 0.25);
                context1.drawImage(image_tmp, one_hour_width * hours - image_tmp.getWidth() - 10,
                        image_center_v[0] - (image_tmp.getHeight() / 2));
            }

            //--------------------- TEKST ISPOD WEATHER ICONS ----------------------
            int index = Weather_Code_Description(forecast_daily.weather_code[d]).indexOf(':');
            if (index > 0) {
                txt = Weather_Code_Description(forecast_daily.weather_code[d]).substring(0, index);
            } else {
                if (forecast_daily.weather_code[d] == 48) {
                    txt = "Frosting Fog"; // krace - umesto Depositing Rime Fog
                } else {
                    txt = Weather_Code_Description(forecast_daily.weather_code[d]);
                }
            }
            context1.save();
            if(layer1.getWidth() < 1000){
                font = Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 8); // TODO
            }else{
                font = Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 14); // TODO
            }
            context1.setFont(font);
            context1.setFill(Color.rgb(0, 255, 0));
            if (Service_impl.measureText(txt, layer1) / 2 > image_center_v[0]/2) { // ako je polovina teksta veca od centra slike do vert. linije
                context1.fillText(txt, (one_hour_width * hours) - Service_impl.measureText(txt, layer1) - 2,  // kraj na vert. liniji - 2 px
                        horizontal_axis_line_step - 5);
            } else {
                context1.fillText(txt, (one_hour_width * hours - 32/*imgWidth/2*/) - (Service_impl.measureText(txt, layer1) / 2) - 10,
                        horizontal_axis_line_step - 5);
            }
            context1.restore();
//				    Debugging.console("txt " + txt);
            //-----------------------------------------------------------------------
            hours += 24;
        }
        context1.closePath();

        //------------------------ WIND GRAPHICS ----------------------------------
        context1.beginPath();
        if(layer1.getWidth() < 1000){
            font = Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 8);
        }else{
            font = Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 10);
        }
        context1.setFont(font);
        context1.setFill(Color.rgb(255, 255, 255));
        hours = 0;
        final boolean[] resized_once = {false};
        AtomicReference<Image> arrow_icon = new AtomicReference<>(new Image(Resource.toUrl("Arrows/wind-dart-white-3.png", Meteo_WebFX.class), true));
        for (int d = 0; d < forecast_daily.time.length; d++) {
            final int constHours = hours;                   // mora da bude const ( final ) u funkcij
            final int const_d = d;

            WebFXUtil.onImageLoaded(arrow_icon.get(), () -> {
                context1.save(); // saves the current state on stack, including the current transform
                double angle_rad = (forecast_daily.wind_direction_10m_dominant[const_d] * Math.PI) / 180; // treba u rad za html canvas
                double angle_deg = forecast_daily.wind_direction_10m_dominant[const_d];                   // u deg za javaFX a ovde i za GWT
                double x = one_hour_width * constHours + 10;
                if(layer1.getWidth() < 1000){
                    if(!resized_once[0]){
                        resized_once[0] = true;
                        arrow_icon.set(scaleImage(arrow_icon.get()));
                    }
                }
                image_center_v[0] = (arrow_icon.get().getHeight() / 2) + (arrow_icon.get().getHeight() * 0.25);
                double finalImage_center_v = image_center_v[0];
                double y = finalImage_center_v - (arrow_icon.get().getHeight() / 2);
                Rotate r = new Rotate(angle_deg, x + arrow_icon.get().getWidth() / 2, y + arrow_icon.get().getHeight() / 2);
                context1.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
                context1.drawImage(arrow_icon.get(), x, y);
                context1.restore(); // back to original state (before rotation);
//                Console.log("angle rad: " + forecast_daily.wind_direction_10m_dominant[const_d] + ", angle deg: " + angle_deg);
            });

            txt = "S";
            context1.fillText(txt, (one_hour_width * constHours + 10 + (32*image_scale)/*image_center_v[0]/2*/) - Service_impl.measureText(txt, layer1) / 2,
                    horizontal_axis_line_step - 12);
            txt = forecast_daily.wind_speed_10m_max[d] + " km/h(" +
                    forecast_daily.wind_direction_description(forecast_daily.wind_direction_10m_dominant[const_d]) + ")";
            context1.save();
            if (forecast_daily.wind_speed_10m_max[d] > 50.0) {
                context1.setFill(Color.rgb(255, 0, 0));
            } else if (forecast_daily.wind_speed_10m_max[d] > 30.0) {
                context1.setFill(Color.rgb(255, 200, 0)); // orange
            } else {
                context1.setFill(Color.rgb(0, 255, 0));
            }
            context1.fillText(txt, (one_hour_width * constHours + 10 + (32*image_scale)/*image_center_v[0]/2*/) - Service_impl.measureText(txt, layer1) / 2,
                    horizontal_axis_line_step - 2);
            context1.restore();

            hours += 24;
        }

        //------------------- TEXT NA Y OSI --------------------------
        // isti font kao na X osi
//		context1.closePath();
        context1.beginPath();
        font = Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 12);
        context1.setFont(font);
        context1.setFill(Color.rgb(255, 255, 255));
        double font_height = font.getSize();
        if (Integer.parseInt(forecast.time.substring(11, 13)) > 11) {  // Levo od linije sada
//-------------- POLUPROVIDNI CRNI BOX ISPOD TEKSTA NA Y OSI ----------------------
            context1.save();      // Save the state
            context1.setFill(Color.rgb(0, 0, 0)); // black
            if (forecast_daily.WMO_image_name[0].contains("snow") || forecast_daily.WMO_image_name[0].contains("rain") || forecast_daily.WMO_image_name[0].contains("drizzle")) {
                context1.setGlobalAlpha(0.8);
            } else {
                context1.setGlobalAlpha(0.6);
            }
            context1.fillRect(one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - 80, 0, 75, canvasHeight - 55);
//			context1.setGlobalAlpha(1.0);
            context1.restore();   //  method restores the state of a saved drawing context:
//---------------------------------------------------------------------------------
            txt = "mm/㎡";
            context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - Service_impl.measureText(txt, layer1) - 5,
                    font_height * 2); // max_drawing_height / 2  // ispod teksta na vrhu
            txt = Double.toString(forecast_hourly.rain_max);
            context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - Service_impl.measureText(txt, layer1) - 10,
                    max_positive_drawing_height - ((forecast_hourly.rain_max * scale_rain_height) - 12/* font height */));
//--------------- FORMATIRANJE NA DVE DECIMALE (ALTERNATIVNO ) ------------------
            double temp;
            if (forecast_hourly.temp_min < 0.0) {
                temp = 100 * ((max_positive_drawing_height - horizontal_axis_line_step) / scale_rain_height);
            } else {
                temp = 100 * (forecast_hourly.rain_max - (forecast_hourly.rain_max / 3));
            }
            temp = (double) (int) temp;
            temp = temp / 100;
            txt = temp > 0.0 ? Double.toString(temp) : " ";

            if (forecast_hourly.temp_min < 0.0) {          // tekst za rain na 1. liniji
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - Service_impl.measureText(txt, layer1) - 10,
                        horizontal_axis_line_step + 12/*font height*/);
            } else {
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - Service_impl.measureText(txt, layer1) - 10,
                        max_positive_drawing_height - (((forecast_hourly.rain_max - (forecast_hourly.rain_max / 3)) * scale_rain_height) - 12/*font height*/));
            }

//--------------- FORMATIRANJE NA DVE DECIMALE (ALTERNATIVNO ) ------------------
            if (forecast_hourly.temp_min < 0.0) {  // 2. linija
                temp = 0;
                txt = "0";
            } else {
                temp = 100 * (forecast_hourly.rain_max / 3);
                temp = (double) (int) temp;
                txt = Double.toString(temp / 100);
            }
//-------------------------------------------------------------------------------
            if (forecast_hourly.temp_min < 0.0) {  // 2. linija
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - Service_impl.measureText(txt, layer1) - 10,
                        max_positive_drawing_height + 12/* font height */);
            } else {
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - Service_impl.measureText(txt, layer1) - 10,
                        max_positive_drawing_height - (((forecast_hourly.rain_max / 3) * scale_rain_height) - 12/* font height */));
            }

            txt = "℃";
            context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - 75,
                    font_height * 2); // max_drawing_height / 2  // ispod teksta na vrhu
            txt = Double.toString(forecast_hourly.temp_max) + "°";
            context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - 75,
                    max_positive_drawing_height - ((forecast_hourly.temp_max * scale_temp_height) - 12/* font height */));
//--------------- FORMATIRANJE NA JEDNU DECIMALE (ALTERNATIVNO ) ------------------
            if (forecast_hourly.temp_min < 0.0) { // 2. temp
                temp = 10 * ((max_positive_drawing_height - horizontal_axis_line_step) / scale_temp_height);
            } else {
                temp = 10 * (forecast_hourly.temp_max - (forecast_hourly.temp_max * 0.33));
            }
            temp = (double) (int) temp;
            txt = Double.toString(temp / 10) + "°"; // Drugi srednji temperaturni broj levo od linije sada
//-------------------------------------------------------------------------------
//		txt = txt.format("%.1f", txt);
            if (forecast_hourly.temp_min < 0.0) { // 2. temp
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - 75,
                        horizontal_axis_line_step + 12/* font height */);
            } else {
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - 75,
                        max_positive_drawing_height - (((forecast_hourly.temp_max - (forecast_hourly.temp_max / 3)) * scale_temp_height) - 12/* font height */));
            }

//--------------- FORMATIRANJE NA JEDNU DECIMALE (ALTERNATIVNO ) ------------------
            if (forecast_hourly.temp_min < 0.0) { // treci temp
                temp = 0;
                txt = "0°"; // Treci donji temperaturni broj desno od linije sada
            } else {
                temp = 10 * (forecast_hourly.temp_max / 3);
                temp = (double) (int) temp;
                txt = Double.toString(temp / 10) + "°"; // Treci donji temperaturni broj desno od linije sada
            }
            if (forecast_hourly.temp_min < 0.0) {
                context1.setFill(Color.rgb(255, 0, 0));
                if (forecast_hourly.temp_min < -2.0) {
                    context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - 75,
                            max_positive_drawing_height + 12/* font height */);   // 3. temp
                }
                txt = Double.toString(forecast_hourly.temp_min) + "°";        // 4. donji temperaturni broj desno od linije sada
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - 75, max_drawing_height - 8/* font height */);
            } else {
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - 75,
                        max_positive_drawing_height - (((forecast_hourly.temp_max / 3) * scale_temp_height) - 12/* font height */));

                txt = "0" + "°";        // 4. donji temperaturni broj desno od linije sada - Nula na pocetku koordinatne ose
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) - 75, max_drawing_height - 8/* Udaljenost od ose */);
            }


        } else {  // DESNO OD LINIJE "SADA"
            //-------------- POLUPROVIDNI CRNI BOX ISPOD TEKSTA NA Y OSI ----------------------
            context1.save();      // Save the state
            context1.setFill(Color.rgb(0, 0, 0)); // white
            if (forecast_daily.WMO_image_name[0].contains("snow") || forecast_daily.WMO_image_name[0].contains("rain") || forecast_daily.WMO_image_name[0].contains("drizzle")) {
                context1.setGlobalAlpha(0.8);
            } else {
                context1.setGlobalAlpha(0.6);
            }
            context1.fillRect(one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + 5, 0, 80, canvasHeight - 55);
//			context1.setGlobalAlpha(1.0);
            context1.restore();   //  method restores the state of a saved drawing context:
//---------------------------------------------------------------------------------
            txt = "mm/㎡";
            context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + 5, font_height * 2); // max_drawing_height / 2  // za sredinu
            txt = Double.toString(forecast_hourly.rain_max); // Prvi gornji kisni broj desno od linije sada
            context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + 5,
                    max_positive_drawing_height - ((forecast_hourly.rain_max * scale_rain_height) - 12/* font height */));
            //--------------- FORMATIRANJE NA DVE DECIMALE (ALTERNATIVNO ) ------------------
            double temp;
            if (forecast_hourly.temp_min < 0.0) {
                temp = 100 * ((max_positive_drawing_height - horizontal_axis_line_step) / scale_rain_height);
            } else {
                temp = 100 * (forecast_hourly.rain_max - (forecast_hourly.rain_max / 3));
            }
            temp = (double) (int) temp;
            temp = temp / 100;
            txt = temp > 0.0 ? Double.toString(temp) : " "; // drugi kisni
//---------------------------------------------------------------
            if (forecast_hourly.temp_min < 0.0) {          // drugi tekst za rain na 1. liniji
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + 5, horizontal_axis_line_step + 12/* font height */);
            } else {
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + 5, max_positive_drawing_height
                        - (((forecast_hourly.rain_max - (forecast_hourly.rain_max / 3)) * scale_rain_height) - 12/* font height */));
            }

//--------------- FORMATIRANJE NA DVE DECIMALE (ALTERNATIVNO ) ------------------
            if (forecast_hourly.temp_min < 0.0) {  // 2. linija treci kisni
                temp = 0;
                txt = "0";
            } else {
                temp = 100 * (forecast_hourly.rain_max / 3);
                temp = (double) (int) temp;
                txt = Double.toString(temp / 100);
            }
//---------------------------------------------------------------
            if (forecast_hourly.temp_min < 0.0) {
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + 5,
                        max_positive_drawing_height + 12/* font height */);
            } else {
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + 5,
                        max_positive_drawing_height - (((forecast_hourly.rain_max / 3) * scale_rain_height) - 12/* font height */));
            }

            txt = "°C";
            context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + Service_impl.measureText(txt, layer1) + 50, font_height * 2);

            txt = Double.toString(forecast_hourly.temp_max) + "°"; // 1. temp
            context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + 60,
                    max_positive_drawing_height - ((forecast_hourly.temp_max * scale_temp_height) - 12/* font height */));

//--------------- FORMATIRANJE NA JEDNU DECIMALU -----------------
            if (forecast_hourly.temp_min < 0.0) { // 2. temp
                temp = 10 * ((max_positive_drawing_height - horizontal_axis_line_step) / scale_temp_height);
            } else {
                temp = 10 * (forecast_hourly.temp_max - (forecast_hourly.temp_max * 0.33));
            }
            temp = (double) (int) temp;
            txt = Double.toString(temp / 10) + "°"; // Drugi donji temperaturni broj desno od linije sada
//		txt = txt.format("%.1f", txt);
//----------------------------------------------------------------
            if (forecast_hourly.temp_min < 0.0) { // 2. temp
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + 60,
                        horizontal_axis_line_step + 12/* font height */);
            } else {
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + 60,
                        max_positive_drawing_height - (((forecast_hourly.temp_max - (forecast_hourly.temp_max / 3)) * scale_temp_height) - 12/* font height */));
            }

//--------------- FORMATIRANJE NA JEDNU DECIMALU -----------------
            if (forecast_hourly.temp_min < 0.0) { // treci temp
                temp = 0;
                txt = "0°"; // Treci donji temperaturni broj desno od linije sada
            } else {
                temp = 10 * (forecast_hourly.temp_max / 3);
                temp = (double) (int) temp;
                txt = Double.toString(temp / 10) + "°"; // Treci donji temperaturni broj desno od linije sada
            }

//----------------------------------------------------------------
            if (forecast_hourly.temp_min < 0.0) {
                context1.setFill(Color.rgb(255, 0, 0));
                if (forecast_hourly.temp_min < -2.0) {
                    context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + 60,
                            max_positive_drawing_height + 12/* font height */);   // 3. temp
                }
                txt = Double.toString(forecast_hourly.temp_min) + "°";        // 4. donji temperaturni broj desno od linije sada
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + 60, max_drawing_height - 8/* Udaljenost od ose */);
            } else {
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + 60,
                        max_positive_drawing_height - (((forecast_hourly.temp_max / 3) * scale_temp_height) - 12/* font height */));

                txt = "0" + "°";        // 4. donji temperaturni broj desno od linije sada - Nula na pocetku koordinatne ose
                context1.fillText(txt, one_hour_width * Integer.parseInt(forecast.time.substring(11, 13)) + 60, max_drawing_height - 8/* Udaljenost od ose */);
            }
        }

        //----------------- TRUGAO ISPOD NOW -----------------
        context1.closePath();
        context1.beginPath();
        font = Font.font("sans-serif", FontWeight.BOLD, FontPosture.REGULAR, 14);
        context1.setFont(font);
        context1.setFill(Color.rgb(255, 0, 0)); // Red
        int x_now = Integer.parseInt(forecast.time.substring(11, 13));
        context1.moveTo(one_hour_width * x_now, canvasHeight - 31);
        context1.lineTo(one_hour_width * x_now + 4, canvasHeight - 17);
        context1.lineTo(one_hour_width * x_now - 4, canvasHeight - 17);
        context1.fill();
//-------------------- TEKST NOW ------------------------
        txt = forecast.time.substring(11, 13) + "h" /*"Sada" */;
        if (Integer.parseInt(forecast.time.substring(11, 13)) < 2) {
            context1.fillText(txt, 0, canvasHeight - 3);
        } else {
            context1.fillText(txt, (one_hour_width * Integer.parseInt(forecast.time.substring(11, 13))) - Service_impl.measureText(txt, layer1) / 2,
                    canvasHeight - 3);
        }

//-------------------- LEGENDA ----------------------------
//----- CRTANJE BELOG PROVIDNOG BOXA ISPOD TEKSTA LEGENDE -------
//		context1.setFill(Color.rgb(255, 255, 255)); // white
//		context1.setGlobalAlpha(0.4);
//		context1.fillRect((canvasWidth / 2) - 100, canvasHeight-16, 175, 16);
//		context1.setGlobalAlpha(1.0);
//---------------------------------------------------------------
        double txt_width = 0;
        if (forecast_hourly.snowfall_max > 0.0) {
            String legenda_temp = "Temperature   ";
            String legenda_snow = null;
            String legenda_rain = null;
            if (forecast_hourly.rain_max > 0.0) {
                legenda_snow = "Snow   ";
                legenda_rain = "Rain";
            } else {
                legenda_snow = "    Snow   ";
                legenda_rain = "";
            }

            txt_width = Service_impl.measureText(legenda_temp + legenda_snow + legenda_rain, layer1);
            context1.setFill(Color.rgb(255, 255, 0)); // yellow
            context1.fillText(legenda_temp, (canvasWidth / 2) - txt_width / 2, canvasHeight - 4);
            context1.setFill(Color.rgb(220, 220, 220)); // dirty white
            context1.fillText(legenda_snow, (canvasWidth / 2) - (txt_width / 2) + Service_impl.measureText(legenda_temp, layer1), canvasHeight - 4);
            context1.setFill(Color.rgb(0, 255, 255));  // cyan
            context1.fillText(legenda_rain, (canvasWidth / 2) - (txt_width / 2) + Service_impl.measureText((legenda_temp + legenda_snow), layer1), canvasHeight - 4);
        } else if (forecast_hourly.rain_max > 0.0) {
            context1.setFill(Color.rgb(255, 255, 0)); // yellow
            txt = "Temperature";
            context1.fillText(txt, (canvasWidth / 2) - Service_impl.measureText(txt, layer1) - 5, canvasHeight-4);
            txt = "Rain";
            context1.setFill(Color.rgb(0, 255, 255));  // cyan
            context1.fillText(txt, (canvasWidth / 2) + Service_impl.measureText(txt, layer1) + 5, canvasHeight-4);
        } else {
            context1.setFill(Color.rgb(255, 255, 0)); // yellow
            txt = "Temperature";
            context1.fillText(txt, (canvasWidth / 2) - Service_impl.measureText(txt, layer1) / 2, canvasHeight-4);
        }
        context1.closePath();

//        Console.log(" pre - timer logging");
        if(SAVE_SCREEN_IMAGE){
            final Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), evt -> {
                Console.log("Save screenshoot");
                Service_impl.saveScreenshoot();
//                timeline.stop();
            }));
            timeline.play();
            SAVE_SCREEN_IMAGE = false;
        }
    }


    //----------------------------------------------------------
    private Image scaleImage(Image orig){
//  Anther approach is to load the whole image, and then use an ImageView to create a cropped, scaled view of it:
// define crop in image coordinates:
        Rectangle2D croppedPortion = new Rectangle2D(0, 0, orig.getWidth(), orig.getHeight());

// target width and height:
        double scaledWidth = orig.getWidth()/2;
        double scaledHeight = orig.getHeight()/2;
        image_scale = 0.6; // malo vise zbog teksta

        ImageView imageView = new ImageView(orig);
//        imageView.setViewport(croppedPortion);             // NEMA GWT
        Service_impl.setViewport(imageView,croppedPortion);  // service
        imageView.setFitWidth(scaledWidth);
        imageView.setFitHeight(scaledHeight);
//        imageView.setSmooth(true);                     // NEMA GWT
        Service_impl.setSmooth(imageView,true);  // service
//        Now you can create a new image with the cropped version of the original image by taking a snapshot of the ImageView. 
//        To do this, you need to place the ImageView into an off-screen scene:

        Pane pane = new Pane(imageView);
        Scene offScreenScene = new Scene(pane);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);   // da bi pozadina bila transparent
        WritableImage croppedImage = imageView.snapshot(parameters, null);
        return croppedImage;
//        and then you can draw the cropped image into the canvas as before.
    }



    // ------------------ WMO CODE DESCRIPTION -----------------
    private String Weather_Code_Description(double weather_code) {
        String description;

        switch ((int) weather_code) {
            case 0:
                description = "Clear sky";
                break;
            case 1:
                description = "Mainly clear";
                break;
            case 2:
                description = "partly cloudy";
                break;
            case 3:
                description = "Overcast";
                break;
            case 45:
                description = "Fog";
                break;
            case 48:
                description = "Depositing Rime Fog"; // magla,talozenje inja
                break;
            case 51:
                description = "Drizzle: Light Intensity";
                break;
            case 53:
                description = "Drizzle: Moderate Intensity";
                break;
            case 55:
                description = "Drizzle: Dense Intensity";
                break;
            case 56:
                description = "Freezing Drizzle: Light intensity";
                break;
            case 57:
                description = "Freezing Drizzle: Dense intensity";
                break;
            case 61:
                description = "Rain: Slight Intensity";
                break;
            case 63:
                description = "Rain: Moderate Intensity";
                break;
            case 65:
                description = "Rain: heavy intensity";
                break;
            case 66:
                description = "Freezing Rain: Light intensity";
                break;
            case 67:
                description = "Freezing Rain: heavy intensity";
                break;
            case 71:
                description = "Snow fall: Slight intensity";
                break;
            case 73:
                description = "Snow fall: moderate intensity";
                break;
            case 75:
                description = "Snow fall: heavy intensity";
                break;
            case 77:
                description = "Snow grains";
                break;
            case 80:
                description = "Rain shower: Slight";
                break;
            case 81:
                description = "Rain shower: moderate";
                break;
            case 82:
                description = "Rain shower: violent";
                break;
            case 85:
                description = "Snow shower: slight";
                break;
            case 86:
                description = "Snow shower: heavy";
                break;
            case 95:
                description = "Thunderstorm: Slight or moderate";
                break;
            case 96:
                description = "Thunderstorm: slight hail";
                break;
            case 99:
                description = "Thunderstorm: heavy hail";
                break;
// Thunderstorm forecast with hail is only available in Central Europe
            default:
                description = "Unknown";
                break;
        }
        return description;

    }
}