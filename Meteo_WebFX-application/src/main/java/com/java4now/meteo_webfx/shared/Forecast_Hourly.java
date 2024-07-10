package com.java4now.meteo_webfx.shared;

import dev.webfx.platform.ast.ReadOnlyAstArray;
import dev.webfx.platform.ast.ReadOnlyAstObject;
import dev.webfx.platform.ast.json.Json;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.useragent.UserAgent;
import dev.webfx.platform.ast.spi.factory.impl.generic.ListAstArray;

public class Forecast_Hourly {

    private String[] temperature_str, rain_str, snowfall_str;
    private String str;
    public String[] time;
    public double[] temperature = new double[16 * 24], rain = new double[16 * 24], snowfall = new double[16 * 24];
    public double temp_max = -30.0, temp_min = 50.0, rain_max = 0.0, rain_min = 0.0, snowfall_max = 0.0;


    public void parseData(String HourlyData) {

        for (int i = 0; i < (16 * 24); i++) {
            temperature[i] = 0;
            rain[i] = 0;
            snowfall[i] = 0;
        }

// mora reset min,max jer kada ucitam forecast sa nekog drugog izvora ostaju stare vrednosti
        temp_max = -30.0;
        temp_min = 50.0;
        rain_max = 0.0;
        rain_min = 0.0;
        snowfall_max = 0.0;

        ReadOnlyAstObject hourly = Json.parseObject(HourlyData).getObject("hourly");
//        Console.log("data " + hourly);
        if(UserAgent.isBrowser()){
            str = hourly.getString("time");
//          Console.log("s : " + s );
            time = str.split(",");
//            Console.log("JsonObject: size( " + time.length + ") key( time[0] ) value( " + time[0] + " )");
            ReadOnlyAstArray arr = hourly.getArray("temperature_2m");//getString("weather_code");
//            Console.log("arr: " + arr );
            for (int i = 0; i < arr.size();i++) {
                temperature[i] = arr.getDouble(i);
//                Console.log( i + ": " + temperature[i]);
            }
            arr = hourly.getArray("rain");//getString("weather_code");
            for (int i = 0; i < arr.size();i++) {
                rain[i] = arr.getDouble(i);
//                Console.log( i + ": " + rain[i]);
            }
            arr = hourly.getArray("snowfall");//getString("weather_code");
            for (int i = 0; i < arr.size();i++) {
                snowfall[i] = arr.getDouble(i);
//                Console.log( i + ": " + snowfall[i]);
            }
        }else{
            ListAstArray list = hourly.get("time");
//            Console.log("JsonObject: size( " + time_list.getList().size() + ") key( time[0] ) value( " + time_list.getString(0) + " )");
            str = list.getList().toString();
            time = str.split(",", 0); // kada je 0 splituje na neograniceni broj delova stringa
//            Console.log("JsonObject: size( " + time.length + ") key( time[0] ) value( " + time[0] + " )");
            list = hourly.get("temperature_2m");
            for (int i = 0; i < list.size();i++) {
                temperature[i] = list.getDouble(i);
//                Console.log( i + ": " + temperature[i]);
            }
            list = hourly.get("rain");
            for (int i = 0; i < list.size();i++) {
                rain[i] = list.getDouble(i);
//                Console.log( i + ": " + rain[i]);
            }
            list = hourly.get("snowfall");
            for (int i = 0; i < list.size();i++) {
                snowfall[i] = list.getDouble(i);
//                Console.log( i + ": " + snowfall[i]);
            }
        }
        loadInToArray();
    }

    //---------------------------------------------------------------
    private void loadInToArray() {
        for (int i = 0; i < time.length; i++) {
            temp_max = Double.max(temp_max, temperature[i]);
            temp_min = Double.min(temp_min, temperature[i]);
            rain_max = Double.max(rain_max, rain[i]);
            rain_min = Double.min(rain_min, rain[i]);
            snowfall_max = Double.max(snowfall_max, snowfall[i]);
        }
 //       Console.log("temperature " + temperature + "\nmax - ( " + temp_max + " )\nmin - ( " + temp_min + " )");
 //       Console.log("rain " + rain  + "\nmax - " + rain_max);
 //       Console.log("snow " + snowfall  + "\nmax - " + snowfall_max);
    }
}
