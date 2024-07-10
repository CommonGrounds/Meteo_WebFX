package com.java4now.meteo_webfx.shared;

import dev.webfx.platform.ast.json.Json;
import dev.webfx.platform.console.Console;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;

public class IPCodes {
//{"status":"success","country":"Serbia","countryCode":"RS","region":"00","regionName":"Belgrade","city":"Belgrade","zip":"","lat":44.8046,"lon":20.4637,
// "timezone":"Europe/Belgrade","isp":"A1 Srbija d.o.o","org":"A1 Serbia","as":"AS44143 A1 Srbija d.o.o","query":"77.243.24.142"}
    public String error, reason, country, countryCode, city;
    public double lat,lon;

    public boolean parseData(String codes, StringProperty title_icon, BooleanProperty ip_is_set) {
        error = Json.parseObject(codes).getString("fail");
        if(error != null){
            Console.log("error: " + error );
            reason = Json.parseObject(codes).getString("message");
            Console.log("error reason: " + reason );
            return false;
        }

        lat = Json.parseObject(codes).getDouble("latitude");
        lon = Json.parseObject(codes).getDouble("longitude");
        country  = Json.parseObject(codes).getString("country_name");
        countryCode  = Json.parseObject(codes).getString("country_code");
        city = Json.parseObject(codes).getString("city");
        title_icon.set("icon-24px/" + countryCode   + ".png");
        ip_is_set.set(true);
//        Console.log("JsonObject Success: (" + lon + " )\n" + "(" + lat + " )\n" + "(" + country + " )\n" + "(" + countryCode + " )\n" + "(" + city + " )");
        return true;
    }
}
