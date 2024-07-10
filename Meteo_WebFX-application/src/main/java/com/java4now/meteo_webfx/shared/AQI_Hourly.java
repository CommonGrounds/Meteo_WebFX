package com.java4now.meteo_webfx.shared;

import dev.webfx.platform.ast.ReadOnlyAstArray;
import dev.webfx.platform.ast.ReadOnlyAstObject;
import dev.webfx.platform.ast.json.Json;
import dev.webfx.platform.ast.spi.factory.impl.generic.ListAstArray;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.useragent.UserAgent;

public class AQI_Hourly {

    public String[] time;
    public double[] uv_index = new double[7*24];

    public void parseData(String openAQIData) {

        for(int i =0;i<(7*24);i++) {
            uv_index[i] = 0;
        }

        ReadOnlyAstObject hourly = Json.parseObject(openAQIData).getObject("hourly");
//        Console.log("data " + hourly);
        String str;
        if(UserAgent.isBrowser()) {
            str = hourly.getString("time");
//          Console.log("s : " + s );
            time = str.split(",");
//            Console.log("JsonObject: size( " + time.length + ") key( time[0] ) value( " + time[0] + " )");
            ReadOnlyAstArray arr = hourly.getArray("uv_index");//getString("weather_code");
//            Console.log("arr: " + arr );
            for (int i = 0; i < arr.size(); i++) {
                uv_index[i] = arr.getDouble(i);
//                Console.log( i + ": " + uv_index[i]);
            }
        }else{
            ListAstArray list = hourly.get("time");
//            Console.log("JsonObject: size( " + time_list.getList().size() + ") key( time[0] ) value( " + time_list.getString(0) + " )");
            str = list.getList().toString();
            time = str.split(",", 0); // kada je 0 splituje na neograniceni broj delova stringa
//            Console.log("JsonObject: size( " + time.length + ") key( time[0] ) value( " + time[0] + " )");
            list = hourly.get("uv_index");
            for (int i = 0; i < list.size();i++) {
                uv_index[i] = list.getDouble(i);
//                Console.log( i + ": " + uv_index[i]);
            }
        }
        //uv_index[i] = Double.NaN;
    }
}
