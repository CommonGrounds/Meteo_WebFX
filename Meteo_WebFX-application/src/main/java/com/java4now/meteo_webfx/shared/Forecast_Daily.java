package com.java4now.meteo_webfx.shared;

import dev.webfx.platform.ast.ReadOnlyAstArray;
import dev.webfx.platform.ast.ReadOnlyAstObject;
import dev.webfx.platform.ast.json.Json;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.useragent.UserAgent;
import dev.webfx.platform.ast.spi.factory.impl.generic.ListAstArray;


public class Forecast_Daily {

    private String str;
    private String[] weather_code_str, wind_direction_10m_dominant_str, wind_speed_10m_max_str;
    public String[] time, WMO_image_name = new String[16];
    public Integer[] weather_code = new Integer[16], wind_direction_10m_dominant = new Integer[16];
    public double[] wind_speed_10m_max = new double[16];

    public void parseData(String DailyData) {

        for (int i = 0; i < 16; i++) {
            weather_code[i] = 0;
            wind_direction_10m_dominant[i] = 0;
            wind_speed_10m_max[i] = 0;
        }


        ReadOnlyAstObject daily = Json.parseObject(DailyData).getObject("daily");
//        Console.log("Daily data: " + daily);
        if(UserAgent.isBrowser()){
            str = daily.getString("time");
//          Console.log("s : " + s );
            time = str.split(",");
//            Console.log("JsonObject: size( " + time.length + ") key( time[0] ) value( " + time[0] + " )");
            ReadOnlyAstArray arr = daily.getArray("weather_code");//getString("weather_code");
//            Console.log("arr: " + arr );
            for (int i = 0; i < arr.size();i++) {
                weather_code[i] = arr.getInteger(i);
//                Console.log( i + ": " + weather_code[i]);
            }
            arr = daily.getArray("wind_speed_10m_max");//getString("weather_code");
//            Console.log("arr: " + arr );
            for (int i = 0; i < arr.size();i++) {
                wind_speed_10m_max[i] = arr.getDouble(i);
//                Console.log( i + ": " + wind_speed_10m_max[i]);
            }
            arr = daily.getArray("wind_direction_10m_dominant");//getString("weather_code");
//            Console.log("arr: " + arr );
            for (int i = 0; i < arr.size();i++) {
                wind_direction_10m_dominant[i] = arr.getInteger(i);
 //               Console.log( i + ": " + wind_direction_10m_dominant[i]);
            }

        }else {
            ListAstArray list = daily.get("time");
//            Console.log("JsonObject: size( " + time_list.getList().size() + ") key( time[0] ) value( " + time_list.getString(0) + " )");
            str = list.getList().toString();
            time = str.split(",", 0); // kada je 0 splituje na neograniceni broj delova stringa
//            Console.log("JsonObject: size( " + time.length + ") key( time[0] ) value( " + time[0] + " )");
            list = daily.get("weather_code");
            for (int i = 0; i < list.size();i++) {
                weather_code[i] = list.getInteger(i);
 //               Console.log( i + ": " + weather_code[i]);
            }
            list = daily.get("wind_speed_10m_max");
            for (int i = 0; i < list.size();i++) {
                wind_speed_10m_max[i] = list.getDouble(i);
//                Console.log( i + ": " + wind_speed_10m_max[i]);
            }
            list = daily.get("wind_direction_10m_dominant");
            for (int i = 0; i < list.size();i++) {
                wind_direction_10m_dominant[i] = list.getInteger(i);
//                Console.log( i + ": " + wind_direction_10m_dominant[i]);
            }
        }
        Weather_Code_Description();
    }


    // ------------------ WMO CODE DESCRIPTION -----------------
    public void Weather_Code_Description() {
        for (int i = 0; i < time.length; i++) {
            switch ((int) weather_code[i]) {
                case 0:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0001_sunny.png";
                    break;
                case 1:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0001_sunny.png";
                    break;
                case 2:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0002_sunny_intervals.png";
                    break;
                case 3:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0003_white_cloud.png";
                    break;
                case 45:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0007_fog.png";
                    break;
                case 48:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0007_fog.png";
                    break;
                case 51:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0009_light_rain_showers.png";
                    break;
                case 53:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0048_drizzle.png";
                    break;
                case 55:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0018_cloudy_with_heavy_rain.png";
                    break;
                case 56:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0013_sleet_showers.png";
                    break;
                case 57:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0050_freezing_rain.png";
                    break;
                case 61:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0009_light_rain_showers.png";
                    break;
                case 63:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0048_drizzle.png";
                    break;
                case 65:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0018_cloudy_with_heavy_rain.png";
                    break;
                case 66:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0013_sleet_showers.png";
                    break;
                case 67:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0050_freezing_rain.png";
                    break;
                case 71:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0011_light_snow_showers.png";
                    break;
                case 73:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0020_cloudy_with_heavy_snow.png";
                    break;
                case 75:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0020_cloudy_with_heavy_snow.png";
                    break;
                case 77:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0020_cloudy_with_heavy_snow.png";
                    break;
                case 80:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0009_light_rain_showers.png";
                    break;
                case 81:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0048_drizzle.png";
                    break;
                case 82:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0018_cloudy_with_heavy_rain.png";
                    break;
                case 85:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0011_light_snow_showers.png";
                    break;
                case 86:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0020_cloudy_with_heavy_snow.png";
                    break;
                case 95:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0024_thunderstorms.png";
                    break;
                case 96:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0024_thunderstorms.png";
                    break;
                case 99:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0024_thunderstorms.png";
                    break;
                // Thunderstorm forecast with hail is only available in Central Europe
                default:
                    WMO_image_name[i] = "mm_api_symbols/wsymbol_0999_unknown.png";
                    break;
            }
        }
    }

    public String wind_direction_description(int wind_direction) {
        String description = "?";
        if (wind_direction < 20) {
            description = "N";
        } else if (wind_direction < 70) {
            description = "NE";
        } else if (wind_direction < 110) {
            description = "E";
        } else if (wind_direction < 160) {
            description = "SE";
        } else if (wind_direction < 200) {
            description = "S";
        } else if (wind_direction < 250) {
            description = "SW";
        } else if (wind_direction < 290) {
            description = "W";
        } else if (wind_direction < 340) {
            description = "NW";
        } else {
            description = "N";
        }
        return description;
    }
}
