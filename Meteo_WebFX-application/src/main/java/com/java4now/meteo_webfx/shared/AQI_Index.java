package com.java4now.meteo_webfx.shared;

import dev.webfx.platform.ast.ReadOnlyAstObject;
import dev.webfx.platform.ast.json.Json;


public class AQI_Index {

    public double european_aqi,pm10,pm2_5,carbon_monoxide,nitrogen_dioxide,sulphur_dioxide,dust,uv_index;
    public String european_aqi_unit,pm10_unit,pm2_5_unit,carbon_monoxide_unit,nitrogen_dioxide_unit,sulphur_dioxide_unit,dust_unit,
            european_aqi_def,pm10_def,pm2_5_def,carbon_monoxide_def,nitrogen_dioxide_def,sulphur_dioxide_def;
    public int color_index_aqi,color_index_pm10,color_index_pm2_5,color_index_carbon_monoxide,color_index_nitrogen_dioxide,color_index_sulphur_dioxide;

    public String[] color_definition = {"#50F0E6","#50CCAA","#F0E641","#FF5050","#960032","#7D2181"};

    public boolean parseData(String openAQIData) {

        ReadOnlyAstObject current = Json.parseObject(openAQIData).getObject("current");
        european_aqi = Double.parseDouble(current.getString("european_aqi"));
        pm10 = Double.parseDouble(current.getString("pm10"));
        pm2_5 = Double.parseDouble(current.getString("pm2_5"));
        carbon_monoxide = Double.parseDouble(current.getString("carbon_monoxide"));
        nitrogen_dioxide = Double.parseDouble(current.getString("nitrogen_dioxide"));
        sulphur_dioxide = Double.parseDouble(current.getString("sulphur_dioxide"));
        dust = Double.parseDouble(current.getString("dust"));
        uv_index = Double.parseDouble(current.getString("uv_index"));
//        Console.log("european_aqi : " + european_aqi + "\npm10 : " + pm10 + "\npm2_5 : " + pm2_5 + "\ndust : " + dust);

        european_aqi_def = european_aqi_definition();
        pm10_def = pm10_definition();
        pm2_5_def = pm2_5_definition();
        carbon_monoxide_def = carbon_monoxide_definition();
        nitrogen_dioxide_def = nitrogen_dioxide_definition();
        sulphur_dioxide_def = sulphur_dioxide_definition();
        return true;
    }

    //---------------------------------------------------------------
    private String european_aqi_definition() {
        String def = "unknown";
        if(european_aqi < 20) {
            def = "Good";
            color_index_aqi = 0;
        }else if(european_aqi < 40) {
            def = "Fair";
            color_index_aqi = 1;
        }else if(european_aqi < 60) {
            def = "Moderate";
            color_index_aqi = 2;
        }else if(european_aqi < 80) {
            def = "Poor";
            color_index_aqi = 3;
        }else if(european_aqi < 100) {
            def = "Very poor";
            color_index_aqi = 4;
        }else {
            def = "Extremely poor";
            color_index_aqi = 5;
        }
        return def;
    }

    private String pm10_definition() {
        String def = "unknown";
        if(pm10 < 20) {
            def = "Good";
            color_index_pm10 = 0;
        }else if(pm10 < 40) {
            def = "Fair";
            color_index_pm10 = 1;
        }else if(pm10 < 50) {
            def = "Moderate";
            color_index_pm10 = 2;
        }else if(pm10 < 100) {
            def = "Poor";
            color_index_pm10 = 3;
        }else if(pm10 < 150) {
            def = "Very poor";
            color_index_pm10 = 4;
        }else {
            def = "Extremely poor";
            color_index_pm10 = 5;
        }
        return def;
    }

    private String pm2_5_definition() {
        String def = "unknown";
        if(pm2_5 < 10) {
            def = "Good";
            color_index_pm2_5 = 0;
        }else if(pm2_5 < 20) {
            def = "Fair";
            color_index_pm2_5 = 1;
        }else if(pm2_5 < 25) {
            def = "Moderate";
            color_index_pm2_5 = 2;
        }else if(pm2_5 < 50) {
            def = "Poor";
            color_index_pm2_5 = 3;
        }else if(pm2_5 < 75) {
            def = "Very poor";
            color_index_pm2_5 = 4;
        }else {
            def = "Extremely poor";
            color_index_pm2_5 = 5;
        }
        return def;
    }

    private String carbon_monoxide_definition() {
        String def = "unknown";
// concentration (ppm) = 24.45 x concentration (mg/m3) ÷ molecular weight ( 28.01 g/mol za CO )
        double ppm = 24.45 * ((carbon_monoxide / 1000) / 28);
        if(ppm < 4) {
            def = "Good";
            color_index_carbon_monoxide = 0;
        }else if(ppm < 9) {
            def = "Fair";
            color_index_carbon_monoxide = 1;
        }else if(ppm < 12) {
            def = "Moderate";
            color_index_carbon_monoxide = 2;
        }else if(ppm < 15) {
            def = "Poor";
            color_index_carbon_monoxide = 3;
        }else if(ppm < 30) {
            def = "Very poor";
            color_index_carbon_monoxide = 4;
        }else {
            def = "Extremely poor";
            color_index_carbon_monoxide = 5;
        }
        return def;
    }

    private String nitrogen_dioxide_definition() {
        String def = "unknown";
        if(nitrogen_dioxide < 40) {
            def = "Good";
            color_index_nitrogen_dioxide = 0;
        }else if(nitrogen_dioxide < 90) {
            def = "Fair";
            color_index_nitrogen_dioxide = 1;
        }else if(nitrogen_dioxide < 120) {
            def = "Moderate";
            color_index_nitrogen_dioxide = 2;
        }else if(nitrogen_dioxide < 230) {
            def = "Poor";
            color_index_nitrogen_dioxide = 3;
        }else if(nitrogen_dioxide < 340) {
            def = "Very poor";
            color_index_nitrogen_dioxide = 4;
        }else {
            def = "Extremely poor";
            color_index_nitrogen_dioxide = 5;
        }
        return def;
    }

    private String sulphur_dioxide_definition() {
        String def = "unknown";
        if(sulphur_dioxide < 100) {
            def = "Good";
            color_index_sulphur_dioxide = 0;
        }else if(sulphur_dioxide < 200) {
            def = "Fair";
            color_index_sulphur_dioxide = 1;
        }else if(sulphur_dioxide < 350) {
            def = "Moderate";
            color_index_sulphur_dioxide = 2;
        }else if(sulphur_dioxide < 500) {
            def = "Poor";
            color_index_sulphur_dioxide = 3;
        }else if(sulphur_dioxide < 750) {
            def = "Very poor";
            color_index_sulphur_dioxide = 4;
        }else {
            def = "Extremely poor";
            color_index_sulphur_dioxide = 5;
        }
        return def;
    }
}
