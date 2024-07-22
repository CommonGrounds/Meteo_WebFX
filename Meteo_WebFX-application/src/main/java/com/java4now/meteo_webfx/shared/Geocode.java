package com.java4now.meteo_webfx.shared;

import dev.webfx.platform.console.Console;
import dev.webfx.platform.ast.ReadOnlyAstArray;
import dev.webfx.platform.ast.ReadOnlyAstObject;
import dev.webfx.platform.ast.json.Json;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;


public class Geocode {

    public final BooleanProperty geocode_done = new SimpleBooleanProperty(false);
    public ArrayList<String> name_list = new ArrayList<>();
    public ArrayList<Double> lat_list = new ArrayList<>();
    public ArrayList<Double> lon_list = new ArrayList<>();
    public ArrayList<String> admin1_list = new ArrayList<>();
    public ArrayList<String> admin3_list = new ArrayList<>();
    public ArrayList<String> country_list = new ArrayList<>();
    public ArrayList<String> country_code_list = new ArrayList<>();
    public ArrayList<String> timezone = new ArrayList<>();
    public ArrayList<Double> postcodes_list = new ArrayList<>();
    public int array_size = 0;

    public void parseData(String GeoData) {

        geocode_done.setValue(false);
        array_size = 0;
        name_list.clear();
        lat_list.clear();
        lon_list.clear();
        admin1_list.clear();
        admin3_list.clear();
        country_list.clear();
        country_code_list.clear();
        ReadOnlyAstArray results = null;
        try {
            results = Json.parseObject(GeoData).getArray("results");
        } catch (Exception e) {
            return;
        }
        for (int obj = 0; obj < results.size(); obj++) {
            ReadOnlyAstObject list;
            list = results.getObject(obj);
//                Console.log( list);
            name_list.add(list.getString("name"));
//            timezone.add(list.getString("timezone"));
//                    Console.log( name_list.get(i));
            lat_list.add(list.getDouble("latitude"));
            lon_list.add(list.getDouble("longitude"));
//            admin1_list.add(list.getString("admin1").equals(null) ? "???" : list.getString("admin1"));
//            admin3_list.add(list.getString("admin3").equals(null) ? "???" : list.getString("admin3"));
            try{
                String s = list.getString("country");
                if(s == null){
                    String[] str = list.getString("timezone").split("/");
                    country_list.add(str[0]);
                }else{
                    country_list.add(s);
                }
            }catch(Exception e){
                country_list.add("Unknown");
                Console.log("geocode parsing error");
            }
            country_code_list.add(list.getString("country_code"));
//                postcodes_list.add(results.getDouble("postcodes"));
//                Console.log( obj + ": " + name_list.get(obj) + " , " + lat_list.get(obj));
//            Console.log(list.getString("admin1") + "," + list.getString("admin3") + "," + list.getString("country") + "," +
//                    list.getString("country_code") + "," + list.getString("timezone"));

                /*
                try {
                	// primer 5 - "postcodes":["40741","40742","40743","40744","40745"] - String array u JSONArray - ovde je samo substring nacin izvlacenja
                	// podataka - treba razraditi JSON nacin
                	String data = object.get("postcodes").toString();
                	str = data.substring(1,(data.length()-1)).split(",", 0); // str je niz za ovu petlju koji moze da sadrzi vise vrednosti
                	postcodes[i] = Double.valueOf(str[0].substring(1,(str[0].length()-1))); // Interesuje nas samo 1. post code [0] za i unos
                }
                catch (NullPointerException e) {
                	postcodes[i] = Double.NaN; // ako ga nema
//                	Debugging.console("str nan" + str );
                }
                catch (Exception e) {
                	postcodes[i] = Double.NaN; // ako ga nema
//                	Debugging.console(i + ": str e " + str );
                }
                 */
        }
        array_size = name_list.size();
        geocode_done.setValue(true);
    }
}

