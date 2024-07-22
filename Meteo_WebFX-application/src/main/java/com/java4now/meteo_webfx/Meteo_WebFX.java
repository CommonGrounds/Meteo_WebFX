package com.java4now.meteo_webfx;

import com.java4now.meteo_webfx.shared.*;
import dev.webfx.extras.webtext.HtmlText;
import dev.webfx.platform.audio.Audio;
import dev.webfx.platform.audio.AudioService;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.fetch.Fetch;
import dev.webfx.platform.os.OperatingSystem;
import dev.webfx.platform.resource.Resource;
import dev.webfx.platform.storage.LocalStorage;
import dev.webfx.platform.useragent.UserAgent;
import dev.webfx.platform.util.Booleans;
import dev.webfx.platform.util.Numbers;
import dev.webfx.platform.util.Strings;
import dev.webfx.stack.i18n.I18n;
//import dev.webfx.stack.i18n.controls.I18nControls;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.Service_impl;

import java.util.Date;
import java.util.Objects;

import static com.java4now.meteo_webfx.Custom_ChoiceBox.*;
import static com.java4now.meteo_webfx.shared.Forecast_current.Background_audio_name;


public class Meteo_WebFX extends Application {

//    Stage dialog;
    Stage dialogParent;
    Scene scene;
    Stage_One_Pane root_pane;
    BorderPane border_pane;
    Forecast_current forecast;
    Forecast_Daily forecast_daily;
    Forecast_Hourly forecast_hourly;
    AQI_Index aqi_index;
    AQI_Hourly aqi_hourly;
    IPCodes ip_codes;
    Custom_ChoiceBox choice_box = new Custom_ChoiceBox();
    ImageView choice_image = new ImageView();
    Button Update;
    Button save;
    private Canvas layer1;
    private Canvas layer2;
    private GraphicsContext context2;
    double old_x = 0;
    boolean meteo_ok = false, aqi_ok = false;
    boolean INITIAL_LANG = false;
    boolean FAVORITES_SAVED = false;
    String Saved_Language = "default";
    String fav_city,fav_country_code;
    double fav_city_lat, fav_city_lon;
    boolean fav_audio_set = true;
    Paint HOVER_COLOR;

    Audio music;
    Button audio_btn;
    Text txt = null;
    HtmlText htmlText = null;
    Image image_wmo_icon;
    ImageView image_view;
    Button en, fr, de, sr;
    Label Temperature, Wind_Speed, Wind_Direction, Vlaznost, Pritisak,choose_city_lbl;
    Label EAQI, PM_10, PM_2_5, CO, NO2, SO2, Prasina, UV;
    Label european_aqi_lbl2, pm10_lbl2, pm2_5_lbl2, carbon_monoxide_lbl2, nitrogen_dioxide_lbl2, sulphur_dioxide_lbl2, empty_lbl, empty_lbl2;
    String Meteo_url = "https://api.open-meteo.com/v1/forecast?latitude=44.8782999&longitude=20.6652891&current=temperature_2m,is_day,wind_speed_10m,wind_direction_10m,"
            + "relative_humidity_2m,weather_code,surface_pressure&hourly=temperature_2m,rain,snowfall&daily=weather_code,wind_speed_10m_max,"
            + "wind_direction_10m_dominant&timezone=auto";
    String AQI_url = "https://air-quality-api.open-meteo.com/v1/air-quality?latitude=44.8782999&longitude=20.6652891&current=european_aqi,pm10,pm2_5,carbon_monoxide,"
            + "nitrogen_dioxide,sulphur_dioxide,dust,uv_index&hourly=uv_index&timezone=auto&forecast_days=7";

    private final BooleanProperty dataLoaded = new SimpleBooleanProperty(false);
    private final BooleanProperty storage_loading = new SimpleBooleanProperty(false);
    private final BooleanProperty ip_is_set = new SimpleBooleanProperty(false);
    public static final BooleanProperty geo_is_set = new SimpleBooleanProperty(false);
    public static final StringProperty title_icon = new SimpleStringProperty("GB.png");
    //---------------------- I18N PROPERTIES --------------------------
    public static final StringProperty wind_spd_lbl = new SimpleStringProperty("speed");
    public static final StringProperty wind_dir_lbl = new SimpleStringProperty("dir");
    public static final StringProperty hum_lbl = new SimpleStringProperty("hum");
    public static final StringProperty press_lbl = new SimpleStringProperty("press");
    public static final StringProperty dust_lbl = new SimpleStringProperty("dust");
    public static final StringProperty city_lbl = new SimpleStringProperty("Choose City");

    @Override
    public void start(Stage primaryStage) {
        Service_impl.getCompilerVersion();

        // TODO - i18n more translations
//        I18n.bindI18nTextProperty(wind_spd_lbl, "Wind_Speed","","");
        I18n.bindI18nTextProperty(wind_dir_lbl, "Wind_Direction"); // samo binding ne koristim translete ovde nego u setNewData()
        I18n.bindI18nTextProperty(hum_lbl, "humidity");
        I18n.bindI18nTextProperty(press_lbl, "Pressure");
        I18n.bindI18nTextProperty(dust_lbl, "Dust");
        I18n.bindI18nTextProperty(city_lbl, "ChooseCity");    // city_lbl sam koristio kao bind a ostale samo direktno - bez bind

        forecast = new Forecast_current("Background/rain"); // default
        forecast_daily = new Forecast_Daily();
        forecast_hourly = new Forecast_Hourly();
        aqi_index = new AQI_Index();
        aqi_hourly = new AQI_Hourly();
        ip_codes = new IPCodes();

        I18n.languageProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setNewData();
                }
            });
        });

        storage_loading.addListener((observable, oldValue, newValue) -> {
            // Only if completed
            if (newValue) {
                //  in javaFX only the FX thread can modify the UI elements
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(fav_city_lat != 0){
                            Console.log("After loadState()");
                            ip_codes.lon = fav_city_lon;
                            ip_codes.lat = fav_city_lat;
                            String[] str = fav_city.split(" - ");
                            ip_codes.city = str[0];
                            ip_codes.country = str[1];
                            ip_codes.countryCode = Saved_Language;
                            title_icon.set("icon-24px/" + fav_country_code.toUpperCase()   + ".png");
                            FAVORITES_SAVED = true;
                            ip_is_set.setValue(true);
                        }

                        if (!FAVORITES_SAVED) {
                            Console.log("getIPCodes()");
                            getIPCodes(); // Kada se pribavi country i city onda ip_is_set property aktivira forecast fetch
                        }
                        storage_loading.setValue(false);
                    }
                });
            }
        });

        dataLoaded.addListener((observable, oldValue, newValue) -> {
            // Only if completed
            if (newValue) {
                //  in javaFX only the FX thread can modify the UI elements
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setNewData();
//                        Console.log("newValue : " + newValue); // izvrsava samo kada se postavi dataLoaded.setValue(true);
                    }
                });
            }
        });

        ip_is_set.addListener((observable, oldValue, newValue) -> {
            // Only if completed
            if (newValue) {
                //  in javaFX only the FX thread can modify the UI elements
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Meteo_url = "https://api.open-meteo.com/v1/forecast?latitude=" + ip_codes.lat + "&longitude=" + ip_codes.lon + "&current=temperature_2m,is_day,wind_speed_10m,wind_direction_10m,"
                                + "relative_humidity_2m,weather_code,surface_pressure&hourly=temperature_2m,rain,snowfall&daily=weather_code,wind_speed_10m_max,"
                                + "wind_direction_10m_dominant&timezone=auto";
                        AQI_url = "https://air-quality-api.open-meteo.com/v1/air-quality?latitude=" + ip_codes.lat + "&longitude=" + ip_codes.lon + "&current=european_aqi,pm10,pm2_5,carbon_monoxide,"
                                + "nitrogen_dioxide,sulphur_dioxide,dust,uv_index&hourly=uv_index&timezone=auto&forecast_days=7";
//                        Console.log("Meteo_url: " + Meteo_url );
                        choice_box.field.setText(ip_codes.city + " - " + ip_codes.country);
                        String lang;
                        if(Saved_Language.length() == 2){
                            lang = Saved_Language;
                        }else{
                            lang = ip_codes.countryCode.toLowerCase();
                        }
                        if (lang.equals("rs") || lang.equals("fr") || lang.equals("de")) {
                            if(!INITIAL_LANG){
                                I18n.setLanguage(lang);
                                if(lang.equals("rs")){sr.setTextFill(Color.LIGHTGREEN);en.setTextFill(Color.WHITE);fr.setTextFill(Color.WHITE);de.setTextFill(Color.WHITE);}
                                if(lang.equals("fr")){fr.setTextFill(Color.LIGHTGREEN);sr.setTextFill(Color.WHITE);en.setTextFill(Color.WHITE);de.setTextFill(Color.WHITE);}
                                if(lang.equals("de")){de.setTextFill(Color.LIGHTGREEN);sr.setTextFill(Color.WHITE);fr.setTextFill(Color.WHITE);en.setTextFill(Color.WHITE);}
                                INITIAL_LANG = true;
                            }
                        } else {
                            if(!INITIAL_LANG){
                                I18n.setLanguage("en");
                                en.setTextFill(Color.LIGHTGREEN);sr.setTextFill(Color.WHITE);fr.setTextFill(Color.WHITE);de.setTextFill(Color.WHITE);
                                INITIAL_LANG = true;
                            }
                        }
                        doParsing();
                        doAQIParsing();
//                        dataLoaded.setValue(true);
                        ip_is_set.setValue(false);
                    }
                });
            }
        });

        geo_is_set.addListener((observable, oldValue, newValue) -> {
            // Only if completed
            if (newValue) {
                //  in javaFX only the FX thread can modify the UI elements
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Meteo_url = "https://api.open-meteo.com/v1/forecast?latitude=" + geocode_lat + "&longitude=" + geocode_lon + "&current=temperature_2m,is_day,wind_speed_10m,wind_direction_10m,"
                                + "relative_humidity_2m,weather_code,surface_pressure&hourly=temperature_2m,rain,snowfall&daily=weather_code,wind_speed_10m_max,"
                                + "wind_direction_10m_dominant&timezone=auto";
                        AQI_url = "https://air-quality-api.open-meteo.com/v1/air-quality?latitude=" + geocode_lat + "&longitude=" + geocode_lon + "&current=european_aqi,pm10,pm2_5,carbon_monoxide,"
                                + "nitrogen_dioxide,sulphur_dioxide,dust,uv_index&hourly=uv_index&timezone=auto&forecast_days=7";
//                        Console.log("Meteo_url: " + Meteo_url );
                        doParsing();
                        doAQIParsing();
//                        dataLoaded.setValue(true);
                        geo_is_set.setValue(false);
                    }
                });
            }
        });

        title_icon.addListener((observable, oldValue, newValue) -> {
            //  in javaFX only the FX thread can modify the UI elements
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
//                    Console.log("title_icon : " + title_icon.get());
                    // Za set stage icon backgroundLoading mora da bude false
                    Image image = new Image(Resource.toUrl(title_icon.get(), Meteo_WebFX.class), false);
                    primaryStage.getIcons().add(0, image);
                    choice_image.setImage(image);
                }
            });
        });

        dialogParent = primaryStage;

//        forecast.Old_Background_image_name = "url(Background/cloudy.jpg)";
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(false);

        image_wmo_icon = new Image(Resource.toUrl("mm_api_symbols/wsymbol_0999_unknown.png", Meteo_WebFX.class), true);   // from resources
        image_view = new ImageView(image_wmo_icon);

//        I18n.setLanguage("sr");
        Temperature = new Label("Temp: --");
        Wind_Speed = new Label("Wind Spd: --");
//        I18nControls.bindI18nProperties(Wind_Speed, "Wind_Speed",": --","");
        Wind_Direction = new Label("Wind dir: --");
        Vlaznost = new Label("Humidity: --");
        Pritisak = new Label("Pressure: --");
        //lbl.getStyleClass().add("my_label");

        VBox vbox_left = new VBox(image_view, Temperature, Wind_Speed, Wind_Direction, Vlaznost, Pritisak);
        vbox_left.setSpacing(2);
        vbox_left.setAlignment(Pos.BOTTOM_LEFT);

        EAQI = new Label("EAQI: --");
        PM_10 = new Label("PM10: --");
        PM_2_5 = new Label("PM_2_5: --");
        CO = new Label("CO: --");
        NO2 = new Label("NO2: --");
        SO2 = new Label("SO2: --");
        Prasina = new Label("Dust: --");
        UV = new Label("UV Index: --");
        //lbl.getStyleClass().add("my_label");

        VBox vbox_center = new VBox(EAQI, PM_10, PM_2_5, CO, NO2, SO2, Prasina, UV);
        vbox_center.setSpacing(2);
        vbox_center.setAlignment(Pos.BOTTOM_LEFT);

        european_aqi_lbl2 = new Label("|_____|");
        pm10_lbl2 = new Label("|_____|");
        pm2_5_lbl2 = new Label("|_____|");
        carbon_monoxide_lbl2 = new Label("|_____|");
        nitrogen_dioxide_lbl2 = new Label("|_____|");
        sulphur_dioxide_lbl2 = new Label("|_____|");
        empty_lbl = new Label("|_____|");
        empty_lbl2 = new Label("|_____|");

        VBox vbox_right = new VBox(european_aqi_lbl2, pm10_lbl2, pm2_5_lbl2, carbon_monoxide_lbl2, nitrogen_dioxide_lbl2, sulphur_dioxide_lbl2, empty_lbl, empty_lbl2);
        vbox_right.setSpacing(2);
        vbox_right.setAlignment(Pos.BOTTOM_LEFT);

        HBox hbox_center = new HBox(vbox_left, vbox_center, vbox_right);
        hbox_center.setSpacing(20);
        if (UserAgent.isBrowser()) {
            hbox_center.setPadding(new Insets(30, 0, 0, 0));
        } else {
            hbox_center.setPadding(new Insets(10, 0, 0, 0));
        }
        hbox_center.setAlignment(Pos.CENTER);

        Update = new Button("Update");
        Update.getStyleClass().add("my_button");
        Button forecast_graph_btn = new Button("Forecast");
        forecast_graph_btn.getStyleClass().add("my_button");

        Update.setMinWidth(50);
        Update.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                storage_loading.setValue(true);
//                getIPCodes(); // Kada se pribavi country i city onda ip_is_set property aktivira forecast fetch
            }
        });

        forecast_graph_btn.setMinWidth(50);
        forecast_graph_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // TODO GRAPH
                if (OperatingSystem.isDesktop() && !Service_impl.isMaximized(primaryStage) || OperatingSystem.isMobile()) {
                    if (Objects.equals(forecast_graph_btn.getText(), "Forecast")) {
                        primaryStage.setWidth(800);
                        primaryStage.setHeight(400);
                        border_pane.setVisible(false);
                        forecast_graph_btn.setText("Current");
                        layer1.setVisible(true);
                        layer2.setVisible(true);
                    } else {
                        primaryStage.setWidth(400);
                        primaryStage.setHeight(800);
                        border_pane.setVisible(true);
                        forecast_graph_btn.setText("Forecast");
                        layer1.setVisible(false);
                        layer2.setVisible(false);
                    }
                } else {
//                    for(int i = 0;i<10;i++){Service_impl.debugger();Console.log("i: " + i);} // webfx service primer za JS debugger keyword
//                  GWT
                }
            }
        });

        HBox hbox_bottom = new HBox(Update);
        hbox_bottom.setSpacing(35);
        hbox_bottom.setPadding(new Insets(20, 0, 0, 0));
        hbox_bottom.setAlignment(Pos.BOTTOM_CENTER);

        //        txt.setFont(new Font(24));
        if (UserAgent.isBrowser()) {
            htmlText = new HtmlText();
            htmlText.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 28));
            htmlText.setFill(Color.WHITE);
        } else {
            txt = new Text("");
            txt.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
            txt.setFill(Color.WHITE);
        }

        HBox hpane = new HBox(htmlText == null ? txt : htmlText);
        hpane.setAlignment(Pos.CENTER);

        border_pane = new BorderPane(hbox_center);
        BorderPane.setAlignment(hbox_center, javafx.geometry.Pos.CENTER);
        border_pane.setTop(hpane);
        BorderPane.setAlignment(hpane, javafx.geometry.Pos.TOP_CENTER);
        border_pane.setBottom(hbox_bottom);
        BorderPane.setAlignment(hbox_bottom, Pos.BOTTOM_CENTER);
        // setBackground radi za pane koji je nalepljen na glavni pane
//        border_pane.setBackground(new Background(new BackgroundFill(Color.rgb(120, 120, 200, 0.3), null, null))); // mora color
//        border_pane.setOpacity(0.7); // opacity za sve ( front i background )
        border_pane.setPadding(new Insets(10, 10, 15, 10));
        border_pane.getStyleClass().add("panel");

//---------- Izvan sredisnjeg border_pane-a -----------
        layer1 = new Canvas(300, 200);
        layer2 = new Canvas(300, 200);
        context2 = layer2.getGraphicsContext2D();
        layer2.toFront();
        handleLayers();

        VBox toolTip_Vbox = new VBox(5);
        choose_city_lbl = new Label("ChooseCity");
//        choose_city_lbl.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        choose_city_lbl.getStyleClass().add("city_label");
        HBox choice_group = new HBox(choice_image, choice_box, choose_city_lbl);
        choice_group.setSpacing(5);
        choice_group.setAlignment(Pos.TOP_CENTER);

        en = new Button("EN");
        en.getStyleClass().add("settings_button"); // ima efekta samo dok se ne pozove neka node style funk. koja ponistava css kao u handle()
     //   en.getStyleClass().add("tooltip");
        en.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                I18n.setLanguage("en");
                en.setTextFill(Color.LIGHTGREEN);sr.setTextFill(Color.WHITE);fr.setTextFill(Color.WHITE);de.setTextFill(Color.WHITE);
                HOVER_COLOR = en.getTextFill();
            }
        });
        en.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                HOVER_COLOR = en.getTextFill();
                en.setTextFill(Color.GREEN);;
            }
        });
        en.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                en.setTextFill(HOVER_COLOR);
            }
        });

        fr = new Button("FR");
        fr.getStyleClass().add("settings_button");
        fr.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                I18n.setLanguage("fr");
                fr.setTextFill(Color.LIGHTGREEN);en.setTextFill(Color.WHITE);sr.setTextFill(Color.WHITE);de.setTextFill(Color.WHITE);
                HOVER_COLOR = fr.getTextFill();
            }
        });
        fr.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                HOVER_COLOR = fr.getTextFill();
                fr.setTextFill(Color.GREEN);;
            }
        });
        fr.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                fr.setTextFill(HOVER_COLOR);
            }
        });

        de = new Button("DE");
        de.getStyleClass().add("settings_button");
        de.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                I18n.setLanguage("de");
                de.setTextFill(Color.LIGHTGREEN);en.setTextFill(Color.WHITE);fr.setTextFill(Color.WHITE);sr.setTextFill(Color.WHITE);
                HOVER_COLOR = de.getTextFill();
            }
        });
        de.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                HOVER_COLOR = de.getTextFill();
                de.setTextFill(Color.GREEN);;
            }
        });
        de.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                de.setTextFill(HOVER_COLOR);
            }
        });

        sr = new Button("SR");
        sr.getStyleClass().add("settings_button");
        sr.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                I18n.setLanguage("rs");
                sr.setTextFill(Color.LIGHTGREEN);en.setTextFill(Color.WHITE);fr.setTextFill(Color.WHITE);de.setTextFill(Color.WHITE);
                HOVER_COLOR = sr.getTextFill();
            }
        });
        sr.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                HOVER_COLOR = sr.getTextFill();
                sr.setTextFill(Color.GREEN);;
            }
        });
        sr.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                sr.setTextFill(HOVER_COLOR);
            }
        });

        audio_btn = new Button("Audio on");
        audio_btn.getStyleClass().add("my_button");
        audio_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (music != null && music.isPlaying()) {
                    music.pause();
                    audio_btn.setText("Audio off");
                } else if (music != null && !music.isPlaying()) {
                    music.play();
                    audio_btn.setText("Audio on");
                }
                saveState();
            }
        });

        HBox  lang_audio_pane = new HBox(en, fr, de, sr, audio_btn);
        lang_audio_pane.setSpacing(8);
        lang_audio_pane.setAlignment(Pos.BOTTOM_CENTER);

        save = new Button("Save Settings");
        save.getStyleClass().add("my_button");
        save.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                saveState();
            }
        });

        VBox settings_pane = new VBox(lang_audio_pane/*,save*/);
        settings_pane.setSpacing(20);
        settings_pane.setAlignment(Pos.BASELINE_RIGHT);

        root_pane = new Stage_One_Pane(imageView, border_pane, layer1, layer2, forecast_graph_btn, toolTip_Vbox, forecast, forecast_daily,
                forecast_hourly, aqi_hourly, choice_group, settings_pane);
//        ScalePane root = new ScalePane(pane);

        scene = new Scene(root_pane, 400, 800);
        // set title for the stage
        primaryStage.setTitle("WebFX Meteo");
        // set the scene
        primaryStage.setScene(scene);
        primaryStage.show();

        loadState();
    }


    //--------------------------------------------------
    private void setNewData() {
        Update.setText(I18n.getI18nText("Update"));
        save.setText(I18n.getI18nText("Save_Settings"));
        choose_city_lbl.setText(city_lbl.get()/*I18n.getI18nText("ChooseCity")*/);
        if (UserAgent.isBrowser()) {
            htmlText.setText("<center>" + forecast.latitude + " ° Lat, " + forecast.longitude
                    + " ° Lon<br>" + forecast.elevation + "m " + I18n.getI18nText("Altitude") + ", " + I18n.getI18nText("Date")
                    + forecast.time.substring(0, 10) + "<br>" + I18n.getI18nText("Time") + forecast.time.substring(11, 16) + " " + I18n.getI18nText("In")
                    + forecast.timezone + " (" + forecast.timezone_abbreviation.substring(0, 4)
                    + "+" + (forecast.utc_offset_seconds / 3600) + " h)<br>"
                    + forecast.weather_code_description + "</center>");
//            Console.log(htmlText.getFont().getFamily());
        } else {
            txt.setText(forecast.latitude + " ° Širine, " + forecast.longitude
                    + " ° Dužine\n" + forecast.elevation + "m Nadmorske visine" + "\nDatum "
                    + (forecast.time == null ? "Update":forecast.time.substring(0, 10)) + ", Vreme " + (forecast.time == null ? "Update":forecast.time.substring(11, 16)) + " " + "\nu "
                    + forecast.timezone + " (" + (forecast.timezone_abbreviation == null ? "Update":forecast.timezone_abbreviation.substring(0, 3))
                    + "+" + (forecast.utc_offset_seconds / 3600) + " h)\n"
                    + forecast.weather_code_description);
        }


        Temperature.setText("Temp: " + forecast.temperature + "  °C");
//        Wind_Speed.setText("Wind Spd: " + forecast.wind_speed + " km/h");
        Wind_Speed.setText(I18n.getI18nText("Wind_Speed") + forecast.wind_speed + " km/h"); // direktno
        // TODO I18nControls
//        wind_spd_lbl.set(": " + forecast.wind_speed);
//        I18nControls.bindI18nProperties(Wind_Speed, "Wind_Speed", wind_spd_lbl, new SimpleStringProperty(" km/h")); // i18controls with param
        Wind_Direction.setText(I18n.getI18nText("Wind_Direction") + forecast.wind_direction + " °"); // direct
        Vlaznost.setText(I18n.getI18nText("humidity") + forecast.relative_humidity + " %");
        Pritisak.setText(I18n.getI18nText("Pressure") + forecast.surface_pressure + " hPa");

        EAQI.setText("EAQI: " + aqi_index.european_aqi + " - " + aqi_index.european_aqi_def);
        european_aqi_lbl2.setBackground(new Background(new BackgroundFill(Color.web(aqi_index.color_definition[aqi_index.color_index_aqi]), null, null)));
        european_aqi_lbl2.setTextFill(Color.web(aqi_index.color_definition[aqi_index.color_index_aqi]));
        PM_10.setText("PM-10: " + aqi_index.pm10 + " μg/m³" + " - " + aqi_index.pm10_def);
        pm10_lbl2.setBackground(new Background(new BackgroundFill(Color.web(aqi_index.color_definition[aqi_index.color_index_pm10]), null, null)));
        pm10_lbl2.setTextFill(Color.web(aqi_index.color_definition[aqi_index.color_index_pm10])); // font
        PM_2_5.setText("PM-2.5: " + aqi_index.pm2_5 + "  μg/m³" + " - " + aqi_index.pm2_5_def);
        pm2_5_lbl2.setBackground(new Background(new BackgroundFill(Color.web(aqi_index.color_definition[aqi_index.color_index_pm2_5]), null, null)));
        pm2_5_lbl2.setTextFill(Color.web(aqi_index.color_definition[aqi_index.color_index_pm2_5]));
        CO.setText("CO: " + aqi_index.carbon_monoxide + "  μg/m³" + " - " + aqi_index.carbon_monoxide_def);
        carbon_monoxide_lbl2.setBackground(new Background(new BackgroundFill(Color.web(aqi_index.color_definition[aqi_index.color_index_carbon_monoxide]), null, null)));
        carbon_monoxide_lbl2.setTextFill(Color.web(aqi_index.color_definition[aqi_index.color_index_carbon_monoxide])); // font
        NO2.setText("NO2: " + aqi_index.nitrogen_dioxide + "  μg/m³" + " - " + aqi_index.nitrogen_dioxide_def);
        nitrogen_dioxide_lbl2.setBackground(new Background(new BackgroundFill(Color.web(aqi_index.color_definition[aqi_index.color_index_nitrogen_dioxide]), null, null)));
        nitrogen_dioxide_lbl2.setTextFill(Color.web(aqi_index.color_definition[aqi_index.color_index_nitrogen_dioxide])); // font
        SO2.setText("SO2: " + aqi_index.sulphur_dioxide + "  μg/m³" + " - " + aqi_index.sulphur_dioxide_def);
        sulphur_dioxide_lbl2.setBackground(new Background(new BackgroundFill(Color.web(aqi_index.color_definition[aqi_index.color_index_sulphur_dioxide]), null, null)));
        sulphur_dioxide_lbl2.setTextFill(Color.web(aqi_index.color_definition[aqi_index.color_index_sulphur_dioxide])); // font
        if (aqi_index.dust > 49.0) {
            Prasina.setText(I18n.getI18nText("Dust") + aqi_index.dust + " μg/m³" + " - Danger");  // Dangerous
        } else {
            Prasina.setText(I18n.getI18nText("Dust") + aqi_index.dust + " μg/m³");
        }
        if (aqi_index.uv_index > 10) {
            UV.setText("UV Index: " + aqi_index.uv_index + " - Danger");
        } else {
            UV.setText("UV Index: " + aqi_index.uv_index);
        }

        Color color;
        if (aqi_index.dust > 49.0) {
            color = Color.web("#7D2181"); // dark Violet
        } else {
            double dust_max = 50.0;
            double red = 255 * ((aqi_index.dust > 49.0 ? 50.0 : aqi_index.dust) / dust_max);
            double green = 255 - (255 * ((aqi_index.dust > 49.0 ? 50.0 : aqi_index.dust) / dust_max));
            color = Color.rgb((int) red, (int) green, 50);
        }
        empty_lbl.setBackground(new Background(new BackgroundFill(color, null, null)));
        empty_lbl.setTextFill(color); // font
        if (aqi_index.uv_index > 10) {
            color = Color.web("#7D2181"); // dark Violet
        } else {
            double red = (aqi_index.uv_index / 10) * 255.0;
            double green = 255 - ((aqi_index.uv_index / 10) * 255.0); // uv index je max 10 pa je mapiranje / 10
            color = Color.rgb((int) red, (int) green, 50);        // blue na 50 - tamnije
        }
        empty_lbl2.setBackground(new Background(new BackgroundFill(color, null, null)));
        empty_lbl2.setTextFill(color); // font


        if (music != null && music.isPlaying()) {
            music.stop();
            music.dispose();
            music = null;
        }
        music = AudioService.loadMusic(Resource.toUrl("sounds/" + Background_audio_name, Meteo_WebFX.class));
        music.setLooping(true);
        music.setVolume(1);
        if(fav_audio_set && audio_btn.getText().contains("on")){
            music.play();
        }else{
            audio_btn.setText("audio off");
        }

        image_wmo_icon = new Image(Resource.toUrl("mm_api_symbols/" + forecast.WMO_image_name, Meteo_WebFX.class), true);
        image_view.setImage(image_wmo_icon);

        dataLoaded.setValue(false);
        saveState();
    }


    //-----------------------------------------------------------------
    private void doParsing() {

        Fetch.fetch(Meteo_url) // objekat ovde sa array-ma unutar )
                .onFailure(error -> {
                    Console.log("Fetch Meteo failure: " + error);
                })
                .onSuccess(response -> {
                    Console.log("Fetch Meteo success: ok = " + response.ok());
                    response.text()
                            .onFailure(error -> Console.log("Json Meteo failure: " + error))
                            .onSuccess(text -> {

                                meteo_ok = forecast.parseData(text);
                                forecast_daily.parseData(text);
                                forecast_hourly.parseData(text);
// u listeneru preko Platform.runLater() jer je Fetch non FX process thread ( vazi samo za javafx, Java Script radi i bez toga a moze i ovako )
                                if (aqi_ok && meteo_ok) {
                                    dataLoaded.setValue(true); // posle AQI
                                    meteo_ok = false;
                                    aqi_ok = false;
                                    root_pane.DATA_IS_FETCHED = true;
                                }
                            });
                });
    }


    //-------------------------------------------
    private void doAQIParsing() {

        Fetch.fetch(AQI_url)
                .onFailure(error -> {
                    Console.log("Fetch AQI failure: " + error);
                })
                .onSuccess(response -> {
                    Console.log("Fetch AQI success: ok = " + response.ok());
                    response.text()
                            .onFailure(error -> Console.log("Json AQI failure: " + error))
                            .onSuccess(text -> {

                                aqi_ok = aqi_index.parseData(text);
                                aqi_hourly.parseData(text);
// u listeneru preko Platform.runLater() jer je Fetch non FX process thread ( vazi samo za javafx, Java Script radi i bez toga a moze i ovako )
                                if (meteo_ok && aqi_ok) {
                                    dataLoaded.setValue(true); // posle AQI
                                    meteo_ok = false;
                                    aqi_ok = false;
                                    root_pane.DATA_IS_FETCHED = true;
                                }
                            });
                });
    }


    //---------------------------------------
    private void getIPCodes() {
/*
This endpoint is limited to 45 requests per minute from an IP address.
If you go over the limit your requests will be throttled (HTTP 429) until your rate limit window is reset. If you constantly go over the limit your IP address
will be banned for 1 hour.
The returned HTTP header X-Rl contains the number of requests remaining in the current rate limit window. X-Ttl contains the seconds until the limit is reset.
Your implementation should always check the value of the X-Rl header, and if its is 0 you must not send any more requests for the duration of X-Ttl in seconds.
We do not allow commercial use of this endpoint. Please see our pro service for SSL access, unlimited queries and commercial support.
http://ip-api.com/json/?fields=61439
61439 -generated, numeric value (to save bandwidth) - fields=status,message,country,countryCode,region,regionName,city,zip,lat,lon,timezone,isp,org,as,query
 */

        // https://ipapi.co - 30000 monthly free - have ssl
        String IP_API_url = "https://ipapi.co/json/";

        Fetch.fetch(IP_API_url)
                .onFailure(error -> {
                    Console.log("Fetch IP_API failure: " + error);
                })
                .onSuccess(response -> {
                    Console.log("Fetch IP_API success: ok = " + response.ok());
                    response.text()
                            .onFailure(error -> Console.log("Json IP_API failure: " + error))
                            .onSuccess(text -> {
//                                Console.log(text);
                                boolean ok = ip_codes.parseData(text, title_icon, ip_is_set);
                                if (ok) {
                                    ip_is_set.setValue(true);
                                }
// u listeneru preko Platform.runLater() jer je Fetch non FX process thread ( vazi samo za javafx, Java Script radi i bez toga a moze i ovako )
//                                dataLoaded.setValue(true);
                            });
                });
    }


    //---------------------------------------
    private void loadState() {
//        start_counter = Numbers.intValue(LocalStorage.getItem("start_counter"));
        Saved_Language = Strings.asString(LocalStorage.getItem("Saved_Language"));
        if(Saved_Language == null){
            Saved_Language = "default";
        }
        fav_country_code = Strings.asString(LocalStorage.getItem("fav_country_code"));
        fav_city = Strings.asString(LocalStorage.getItem("fav_city"));
        fav_city_lat = Numbers.doubleValue(LocalStorage.getItem("fav_city_lat"));
        fav_city_lon = Numbers.doubleValue(LocalStorage.getItem("fav_city_lon"));
        try{
            if(Strings.asString(LocalStorage.getItem("fav_audio_set")).equals("false")){
                fav_audio_set = false;
            }else{
                fav_audio_set = true;
            }
        }catch(Exception e){
            fav_audio_set = true;
        }
//        OSName = Strings.asString(LocalStorage.getItem("OSName"));
//        isLinux = Booleans.booleanValue(LocalStorage.getItem("isLinux"));
//        dev.webfx.platform.console.Console.log("Saved_Language: " + Saved_Language + "\nlat:" + fav_city_lat + "\nlon:" +
//                fav_city_lon + "\ncity:" + fav_city + "\ncountry_code:" + fav_country_code + "\nfav_audio_set:" + fav_audio_set);
        storage_loading.setValue(Booleans.booleanValue(LocalStorage.getItem("isSaved")));
    }

    private void saveState() {
        if(geocode_lat == 0){
            LocalStorage.setItem("Saved_Language", String.valueOf(I18n.getLanguage()));
            if(choosen_country_code != null && choosen_country_code.length() == 2){
                LocalStorage.setItem("fav_country_code", choosen_country_code);
            }
            LocalStorage.setItem("fav_audio_set", String.valueOf(music.isPlaying()));
            LocalStorage.setItem("isSaved", String.valueOf(true));
            return;
        }
//        LocalStorage.removeItem("OSFamily"); // brise samo taj key
//        LocalStorage.clear(); // brise sve
        LocalStorage.setItem("Saved_Language", String.valueOf(I18n.getLanguage()));
        fav_city_lat = geocode_lat;
        fav_city_lon = geocode_lon;
        fav_country_code = choosen_country_code;//ip_codes.countryCode;  // TODO
//        dev.webfx.platform.console.Console.log("fav_country_code: " + fav_country_code );
        fav_city = choice_box.field.getText();
        LocalStorage.setItem("fav_city", fav_city);
        LocalStorage.setItem("fav_country_code", fav_country_code);
        LocalStorage.setItem("fav_city_lat", String.valueOf(fav_city_lat));
        LocalStorage.setItem("fav_city_lon", String.valueOf(fav_city_lon));
        LocalStorage.setItem("fav_audio_set", String.valueOf(music.isPlaying()));
        LocalStorage.setItem("isSaved", String.valueOf(true));
    }


    //---------------------------------------
    private void handleLayers() {
        // Handler for Layer 1
        layer2.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
//                gc1.fillOval(e.getX(),e.getY(),20,20);
//                if(dialog.isShowing()){dialog.close();}
                root_pane.hide_ToolTip();
            }
        });


        // Handler for Layer 2
        layer2.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
//                gc2.fillOval(e.getX(),e.getY(),20,20);
            }
        });

        layer2.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (root_pane.DATA_IS_FETCHED) {
                    double hour = (double) forecast_hourly.time.length / (layer2.getWidth() / event.getX());
                    double temp = forecast_hourly.temperature[(int) hour];
                    double snow_fall = forecast_hourly.snowfall[(int) hour];
                    double rain_fall = forecast_hourly.rain[(int) hour];
                    double uv_index_h = aqi_hourly.uv_index[(int) hour];
                    Date date_tooltip = new Date();
                    int all_points = forecast_hourly.time.length;
                    double one_hour_width = (layer2.getWidth() / all_points);
                    double x = 0;
                    double y = 0;

                    if (UserAgent.isBrowser()) {
//                    dialog.setY(layer1.getLayoutY() + event.getY());
                        x = dialogParent.getX() + layer2.getLayoutX() + event.getX() + 20;
                        y = layer2.getLayoutY() + event.getY();
                    } else {
//                    dialog.setY(dialogParent.getY() + layer1.getLayoutY() + event.getY());
                        x = layer2.getLayoutX() + event.getX() + 20;
                        y = layer2.getLayoutY() + event.getY();
                    }

                    context2.clearRect(old_x - 2, 0, 4, layer2.getHeight() - 38); // brisanje prethodne linije
                    context2.beginPath();
                    context2.setStroke(Color.web("#FFFFFF")); // white
                    context2.setLineWidth(2);
                    context2.moveTo(one_hour_width * (int) hour, 0);
                    context2.lineTo(one_hour_width * (int) hour, layer2.getHeight() - 40);
                    old_x = one_hour_width * (int) hour; // skladistim prethodnu vrednost za x
                    context2.stroke();
                    context2.closePath();

                    if ((y - layer2.getLayoutY()) < 20 || y > layer2.getLayoutY() + layer2.getHeight() - 20) {
                        context2.clearRect(old_x - 2, 0, 4, layer2.getHeight() - 38);
                        root_pane.hide_ToolTip();
                    } else {
                        root_pane.show_ToolTip(x, y, hour, date_tooltip.toString(), temp, rain_fall, snow_fall, uv_index_h);
                    }
                }
            }
        });
    }
}


