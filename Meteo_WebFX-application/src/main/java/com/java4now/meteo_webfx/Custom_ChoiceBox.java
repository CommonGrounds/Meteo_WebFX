package com.java4now.meteo_webfx;

import com.java4now.meteo_webfx.shared.Geocode;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.fetch.Fetch;
import dev.webfx.platform.useragent.UserAgent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Objects;

import static com.java4now.meteo_webfx.Meteo_WebFX.geo_is_set;
import static com.java4now.meteo_webfx.Meteo_WebFX.title_icon;

public class Custom_ChoiceBox extends VBox {
    public VBox box = new VBox();
    public TextField field = new TextField();
    ArrayList<Button> btn_list = new ArrayList<>();
    Geocode geocode = new Geocode();
    public static double geocode_lat, geocode_lon;
    public static String choosen_country_code;
    String Geocoding_url = "https://geocoding-api.open-meteo.com/v1/search?name=pancevo";
    String old_geocoding = "?name=pancevo";
    String geocode_result;
    int focused;
    String str = "{\"results\":[{\"id\":2950159,\"name\":\"Berlin\",\"latitude\":52.52437,\"longitude\":13.41053,\"elevation\":74.0,\"feature_code\":\"PPLC\",\"country_code\":\"DE\",\"admin1_id\":2950157,\"admin3_id\":6547383,\"admin4_id\":6547539,\"timezone\":\"Europe/Berlin\",\"population\":3426354,\"postcodes\":[\"10967\",\"13347\"],\"country_id\":2921044,\"country\":\"Germany\",\"admin1\":\"Land Berlin\",\"admin3\":\"Berlin, Stadt\",\"admin4\":\"Berlin\"},{\"id\":5083330,\"name\":\"Berlin\",\"latitude\":44.46867,\"longitude\":-71.18508,\"elevation\":311.0,\"feature_code\":\"PPL\",\"country_code\":\"US\",\"admin1_id\":5090174,\"admin2_id\":5084973,\"admin3_id\":5083340,\"timezone\":\"America/New_York\",\"population\":9367,\"postcodes\":[\"03570\"],\"country_id\":6252001,\"country\":\"United States\",\"admin1\":\"New Hampshire\",\"admin2\":\"Coos\",\"admin3\":\"City of Berlin\"},{\"id\":4500771,\"name\":\"Berlin\",\"latitude\":39.79123,\"longitude\":-74.92905,\"elevation\":50.0,\"feature_code\":\"PPL\",\"country_code\":\"US\",\"admin1_id\":5101760,\"admin2_id\":4501019,\"admin3_id\":4500776,\"timezone\":\"America/New_York\",\"population\":7590,\"postcodes\":[\"08009\"],\"country_id\":6252001,\"country\":\"United States\",\"admin1\":\"New Jersey\",\"admin2\":\"Camden\",\"admin3\":\"Borough of Berlin\"},{\"id\":5245497,\"name\":\"Berlin\",\"latitude\":43.96804,\"longitude\":-88.94345,\"elevation\":246.0,\"feature_code\":\"PPL\",\"country_code\":\"US\",\"admin1_id\":5279468,\"admin2_id\":5255015,\"admin3_id\":5245510,\"timezone\":\"America/Chicago\",\"population\":5420,\"postcodes\":[\"54923\"],\"country_id\":6252001,\"country\":\"United States\",\"admin1\":\"Wisconsin\",\"admin2\":\"Green Lake\",\"admin3\":\"City of Berlin\"},{\"id\":4348460,\"name\":\"Berlin\",\"latitude\":38.32262,\"longitude\":-75.21769,\"elevation\":11.0,\"feature_code\":\"PPL\",\"country_code\":\"US\",\"admin1_id\":4361885,\"admin2_id\":4374180,\"timezone\":\"America/New_York\",\"population\":4529,\"postcodes\":[\"21811\"],\"country_id\":6252001,\"country\":\"United States\",\"admin1\":\"Maryland\",\"admin2\":\"Worcester\"},{\"id\":4930431,\"name\":\"Berlin\",\"latitude\":42.3812,\"longitude\":-71.63701,\"elevation\":100.0,\"feature_code\":\"PPL\",\"country_code\":\"US\",\"admin1_id\":6254926,\"admin2_id\":4956199,\"admin3_id\":4930436,\"timezone\":\"America/New_York\",\"population\":2422,\"postcodes\":[\"01503\"],\"country_id\":6252001,\"country\":\"United States\",\"admin1\":\"Massachusetts\",\"admin2\":\"Worcester\",\"admin3\":\"Town of Berlin\"},{\"id\":4556518,\"name\":\"Berlin\",\"latitude\":39.92064,\"longitude\":-78.9578,\"elevation\":710.0,\"feature_code\":\"PPL\",\"country_code\":\"US\",\"admin1_id\":6254927,\"admin2_id\":5212857,\"admin3_id\":4556520,\"timezone\":\"America/New_York\",\"population\":2019,\"postcodes\":[\"15530\"],\"country_id\":6252001,\"country\":\"United States\",\"admin1\":\"Pennsylvania\",\"admin2\":\"Somerset\",\"admin3\":\"Borough of Berlin\"},{\"id\":4557666,\"name\":\"East Berlin\",\"latitude\":39.9376,\"longitude\":-76.97859,\"elevation\":131.0,\"feature_code\":\"PPL\",\"country_code\":\"US\",\"admin1_id\":6254927,\"admin2_id\":4556228,\"admin3_id\":4557667,\"timezone\":\"America/New_York\",\"population\":1534,\"postcodes\":[\"17316\"],\"country_id\":6252001,\"country\":\"United States\",\"admin1\":\"Pennsylvania\",\"admin2\":\"Adams\",\"admin3\":\"Borough of East Berlin\"},{\"id\":5147132,\"name\":\"Berlin\",\"latitude\":40.56117,\"longitude\":-81.7943,\"elevation\":391.0,\"feature_code\":\"PPL\",\"country_code\":\"US\",\"admin1_id\":5165418,\"admin2_id\":5157783,\"admin3_id\":5147154,\"timezone\":\"America/New_York\",\"population\":898,\"postcodes\":[\"44610\"],\"country_id\":6252001,\"country\":\"United States\",\"admin1\":\"Ohio\",\"admin2\":\"Holmes\",\"admin3\":\"Berlin Township\"},{\"id\":1510159,\"name\":\"Berlin\",\"latitude\":54.00603,\"longitude\":61.19308,\"elevation\":228.0,\"feature_code\":\"PPL\",\"country_code\":\"RU\",\"admin1_id\":1508290,\"admin2_id\":1489213,\"timezone\":\"Asia/Yekaterinburg\",\"population\":613,\"postcodes\":[\"457130\"],\"country_id\":2017370,\"country\":\"Russia\",\"admin1\":\"Chelyabinsk\",\"admin2\":\"Troitskiy Rayon\"}],\"generationtime_ms\":1.8000603}";

    Custom_ChoiceBox() {
        for (int i = 0; i < 10; i++) {
            focused = 0;
            btn_list.add(new Button());
            btn_list.get(i).getStyleClass().add("choice_list");
            btn_list.get(i).setMaxWidth(210);
            btn_list.get(i).setMinWidth(210);
            int finalI = i;
            btn_list.get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    focused = 0;
                    field.setText(btn_list.get(finalI).getText());
                    geocode_lat = geocode.lat_list.get(finalI);
                    geocode_lon = geocode.lon_list.get(finalI);
                    title_icon.set("icon-24px/" + geocode.country_code_list.get(finalI) + ".png");
                    choosen_country_code = geocode.country_code_list.get(finalI);
                    box.getChildren().clear();
                    box.setVisible(false);
                    geo_is_set.setValue(true);
                }
            });
            btn_list.get(i).setOnKeyPressed((btn_key_event) -> {
                if (btn_key_event.getCode() == KeyCode.DOWN) {
                    if (focused < geocode.array_size - 1) {
                        focused++;
                    }
                    btn_list.get(focused).requestFocus();
                    if (UserAgent.isBrowser()) {
                        btn_list.get(focused - 1).getStyleClass().remove("choice_list_focus");
                        btn_list.get(focused).getStyleClass().add("choice_list_focus");
                    }
//                    dev.webfx.platform.console.Console.log("down, focus(" + focused + "): " + btn_list.get(focused).isFocused());
                } else if (btn_key_event.getCode() == KeyCode.UP) {
                    if (focused > 0) {
                        focused--;
                    }
                    Console.log("up");
                    btn_list.get(focused).requestFocus();
                    if (UserAgent.isBrowser()) {
                        btn_list.get(focused + 1).getStyleClass().remove("choice_list_focus");
                        btn_list.get(focused).getStyleClass().add("choice_list_focus");
                    }
                } else if (btn_key_event.getCode() == KeyCode.ENTER) {
                    Console.log("enter");
                    btn_list.get(focused).getStyleClass().remove("choice_list_focus");
                    focused = 0;
                    field.setText(btn_list.get(finalI).getText());
                    title_icon.set("icon-24px/" + geocode.country_code_list.get(finalI) + ".png");
                    choosen_country_code = geocode.country_code_list.get(finalI);
                    geocode_lat = geocode.lat_list.get(finalI);
                    geocode_lon = geocode.lon_list.get(finalI);
                    Console.log(geocode.name_list.get(finalI) + "," + geocode_lat + "," + geocode.country_list.get(finalI));
                    box.getChildren().clear();
                    //--------------- da bi izbegao focus na textfield --------------
                    box.requestFocus();
                    if (UserAgent.isBrowser()) {
                        this.getChildren().remove(0);
                        this.getChildren().add(0, field);
                    }
//----------------------------------------------------------------
                    box.setVisible(false);
                    geo_is_set.setValue(true);
                }
            });
        }
        field.getStyleClass().add("custom_textField");
        field.setMaxWidth(240);
        field.setMinWidth(240);

        field.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if (event.getCode() == KeyCode.BACK_SPACE) {
//                    Console.log("backspace");
//                    field.requestFocus();
                }

                if (field.getText().length() < 2) {
                    return;
                }
                if (event.getCode().isArrowKey()) {
                    btn_list.get(0).requestFocus();
                    if (UserAgent.isBrowser()) {
                        btn_list.get(0).getStyleClass().add("choice_list_focus");
                    }
                    return;
                }

                box.setVisible(false);
                String selected_str = "?name=" + field.getText(); //api_list.getSelectedValue().substring(0, index);
                Geocoding_url = Geocoding_url.replace(old_geocoding, selected_str);
                old_geocoding = selected_str;
                getGeocodeData();
                geocode.parseData(geocode_result);
                geocode.geocode_done.addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        box.getChildren().clear();
                        int limit = geocode.array_size;
                        for (int i = 0; i < limit; i++) {
                            String[] city = geocode.name_list.get(i).split(" ");
                            if (city.length > 1) {
                                btn_list.get(i).setText(city[0] + " " + city[1] + " - " + geocode.country_list.get(i));
                            } else {
                                btn_list.get(i).setText(city[0] + " - " + geocode.country_list.get(i));
                            }
//                            dev.webfx.platform.console.Console.log(btn_list.get(i).getText());
                            box.getChildren().add(btn_list.get(i));
                        }
//                r.setLayoutX(l.getLayoutX());
//                r.setLayoutY(l.getLayoutY() + l.getHeight() + 5);
                        box.setAlignment(Pos.TOP_CENTER);
                        box.setVisible(true);
//                        btn_list.get(0).requestFocus();
                    }
                });
            }
        });
        field.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                field.setText("");
                field.requestFocus(); // key handleri da odmah reaguju
            }
        });

        box.setVisible(false);

        getChildren().addAll(field, box);
        setAlignment(Pos.TOP_CENTER);
    }


    //---------------------------------------
    private void getGeocodeData() {

        Fetch.fetch(Geocoding_url)
                .onFailure(error -> {
                    Console.log("Fetch GEOCODE failure: " + error);
                })
                .onSuccess(response -> {
                    Console.log("Fetch GEOCODE success: ok = " + response.ok());
                    response.text()
                            .onFailure(error -> Console.log("Json GEOCODE failure: " + error))
                            .onSuccess(text -> {

                                geocode_result = text;
                            });
                });
    }
}
