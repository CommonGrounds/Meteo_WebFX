package com.java4now.meteo_webfx.shared;

import dev.webfx.platform.console.Console;
import dev.webfx.platform.ast.ReadOnlyAstObject;
import dev.webfx.platform.ast.json.Json;
import dev.webfx.platform.useragent.UserAgent;


public class Forecast_current {

    public double temperature, wind_speed, wind_direction, relative_humidity, elevation, surface_pressure, longitude, latitude, utc_offset_seconds,
            is_day, weather_code;
    public String temperature_unit, wind_speed_unit, wind_direction_unit, relative_humidity_unit, surface_pressure_unit, timezone, time,
            weather_code_description, timezone_abbreviation, WMO_image_name;
    public static String Background_image_name, Old_Background_image_name,Background_audio_name;
    public boolean image_switch = false;
    /*JSONValue*/ String error, reason;

    public Forecast_current(String image_name){
        super();
        Background_image_name = Old_Background_image_name = image_name;
        Background_audio_name = "nature-birds-singing-217212.mp3";
        WMO_image_name = "wsymbol_0999_unknown.png";
    }

    public boolean parseData(String text) {

        error = Json.parseObject(text).getString("error");
        if(error != null){
            Console.log("error: " + error );
            reason = Json.parseObject(text).getString("reason");
            Console.log("error reason: " + reason );
            return false;
        }

        longitude = Json.parseObject(text).getDouble("longitude");
        latitude = Json.parseObject(text).getDouble("latitude");  // sirina
        elevation = Json.parseObject(text).getDouble("elevation");
        timezone = Json.parseObject(text).getString("timezone");
        utc_offset_seconds = Json.parseObject(text).getDouble("utc_offset_seconds");
        timezone_abbreviation = Json.parseObject(text).getString("timezone_abbreviation");
//		Console.log("JsonObject Success: (" + longitude + " )\n" + "(" + latitude + " )\n" + "(" + elevation + " )\n" +
//		"(" + timezone + " )\n" + "(" + utc_offset_seconds + " )\n" + "(" + timezone_abbreviation + " )" );

        ReadOnlyAstObject current = Json.parseObject(text).getObject("current");
        try {
            if(UserAgent.isBrowser()){
                time = current.getString("time");
            }else {
                time = current.get("time");
            }
//            time = Json.parseObject(current.getString("time")).toString();
            temperature = Double.parseDouble(current.getString("temperature_2m"));
            wind_speed = Double.parseDouble(current.getString("wind_speed_10m"));
            wind_direction = Double.parseDouble(current.getString("wind_direction_10m"));
            relative_humidity = Double.parseDouble(current.getString("relative_humidity_2m"));
            weather_code = Double.parseDouble(current.getString("weather_code"));
            surface_pressure = Double.parseDouble(current.getString("surface_pressure"));
            is_day = Double.parseDouble(current.getString("is_day"));

            weather_code_description = Weather_Code_Description(weather_code); // za trenutni weather_code

        } catch (Exception e) {
            Console.log("Exception : " + e.getMessage());
            Console.log("current.toString() :" + current.toString());
            Console.log("current object size :" + current.size());
        }
//		Console.log("JsonObject Success: \n(" + temperature + ")\n" + "(" + wind_speed + ")\n" + "(" + wind_direction + ")\n" + "(" + weather_code + ")\n" +
//		"(" + surface_pressure + ")\n" + "(" + time + ")\n" + "(" + is_day + ")\n" + "(" + weather_code_description + ")\n" + "(" + WMO_image_name + ")");
        return true;
    }

    // ------------------ WMO CODE DESCRIPTION -----------------
    private String Weather_Code_Description(double weather_code) {
        String description;
        if ((int) weather_code < 2) {
            Background_image_name = is_day > 0 ? "Background/sunny" : "Background/night";
            Background_audio_name = "nature-birds-singing-217212.mp3";
        } else if ((int) weather_code < 50) { // bilo 60
            Background_image_name = is_day > 0 ? "Background/sunny" : "Background/night";
            Background_audio_name = "nature-birds-singing-217212.mp3";
        } else if ((int) weather_code < 95) {
            if ((int) weather_code > 70 && (int) weather_code < 80) {
                Background_image_name = "Background/snow";
                Background_audio_name = "mixkit-bad-weather-heavy-rain-and-thunder-1261.wav";
            } else if ((int) weather_code > 82 && (int) weather_code < 90) {
                Background_image_name = "Background/snow";
                Background_audio_name = "mixkit-bad-weather-heavy-rain-and-thunder-1261.wav";
            } else {
                Background_image_name = "Background/rain";
                Background_audio_name = "mixkit-bad-weather-heavy-rain-and-thunder-1261.wav";
            }
        } else {
            Background_image_name = "Background/thunder";
            Background_audio_name = "mixkit-bad-weather-heavy-rain-and-thunder-1261.wav";
        }

        StringBuilder buf = new StringBuilder(Old_Background_image_name);
        if (!Background_image_name.contentEquals(buf)) {
            image_switch = true;
            Old_Background_image_name = Background_image_name;
        }

        switch ((int) weather_code) {
            case 0:
                description = "Clear sky";
                WMO_image_name = is_day > 0 ? "wsymbol_0001_sunny.png" : "wsymbol_0008_clear_sky_night.png";
                break;
            case 1:
                description = "Mainly clear";
                WMO_image_name = is_day > 0 ? "wsymbol_0001_sunny.png" : "wsymbol_0041_partly_cloudy_night.png";
                break;
            case 2:
                description = "partly cloudy";
                WMO_image_name = is_day > 0 ? "wsymbol_0002_sunny_intervals.png" : "wsymbol_0044_mostly_cloudy_night.png";
                break;
            case 3:
                description = "Overcast";
                WMO_image_name = is_day > 0 ? "wsymbol_0003_white_cloud.png" : "wsymbol_0042_cloudy_night.png";
                break;
            case 45:
                description = "Fog";
                WMO_image_name = is_day > 0 ? "wsymbol_0007_fog.png" : "wsymbol_0064_fog_night.png";
                break;
            case 48:
                description = "Depositing Rime Fog"; // magla,talozenje inja
                WMO_image_name = is_day > 0 ? "wsymbol_0007_fog.png" : "wsymbol_0064_fog_night.png";
                break;
            case 51:
                description = "Drizzle: Light Intensity";
                WMO_image_name = is_day > 0 ? "wsymbol_0009_light_rain_showers.png" : "wsymbol_0025_light_rain_showers_night.png";
                break;
            case 53:
                description = "Drizzle: Moderate Intensity";
                WMO_image_name = is_day > 0 ? "wsymbol_0048_drizzle.png" : "wsymbol_0066_drizzle_night.png";
                break;
            case 55:
                description = "Drizzle: Dense Intensity";
                WMO_image_name = is_day > 0 ? "wsymbol_0018_cloudy_with_heavy_rain.png" : "wsymbol_0034_cloudy_with_heavy_rain_night.png";
                break;
            case 56:
                description = "Freezing Drizzle: Light intensity";
                WMO_image_name = is_day > 0 ? "wsymbol_0013_sleet_showers.png" : "wsymbol_0029_sleet_showers_night.png";
                break;
            case 57:
                description = "Freezing Drizzle: Dense intensity";
                WMO_image_name = is_day > 0 ? "wsymbol_0050_freezing_rain.png" : "wsymbol_0068_freezing_rain_night.png";
                break;
            case 61:
                description = "Rain: Slight Intensity";
                WMO_image_name = is_day > 0 ? "wsymbol_0009_light_rain_showers.png" : "wsymbol_0025_light_rain_showers_night.png";
                break;
            case 63:
                description = "Rain: Moderate Intensity";
                WMO_image_name = is_day > 0 ? "wsymbol_0048_drizzle.png" : "wsymbol_0066_drizzle_night.png";
                break;
            case 65:
                description = "Rain: heavy intensity";
                WMO_image_name = is_day > 0 ? "wsymbol_0018_cloudy_with_heavy_rain.png" : "wsymbol_0034_cloudy_with_heavy_rain_night.png";
                break;
            case 66:
                description = "Freezing Rain: Light intensity";
                WMO_image_name = is_day > 0 ? "wsymbol_0013_sleet_showers.png" : "wsymbol_0029_sleet_showers_night.png";
                break;
            case 67:
                description = "Freezing Rain: heavy intensity";
                WMO_image_name = is_day > 0 ? "wsymbol_0050_freezing_rain.png" : "wsymbol_0068_freezing_rain_night.png";
                break;
            case 71:
                description = "Snow fall: Slight intensity";
                WMO_image_name = is_day > 0 ? "wsymbol_0011_light_snow_showers.png" : "wsymbol_0027_light_snow_showers_night.png";
                break;
            case 73:
                description = "Snow fall: moderate intensity";
                WMO_image_name = is_day > 0 ? "wsymbol_0020_cloudy_with_heavy_snow.png" : "wsymbol_0036_cloudy_with_heavy_snow_night.png";
                break;
            case 75:
                description = "Snow fall: heavy intensity";
                WMO_image_name = is_day > 0 ? "wsymbol_0020_cloudy_with_heavy_snow.png" : "wsymbol_0036_cloudy_with_heavy_snow_night.png";
                break;
            case 77:
                description = "Snow grains";
                WMO_image_name = is_day > 0 ? "wsymbol_0020_cloudy_with_heavy_snow.png" : "wsymbol_0036_cloudy_with_heavy_snow_night.png";
                break;
            case 80:
                description = "Rain shower: Slight";
                WMO_image_name = is_day > 0 ? "wsymbol_0009_light_rain_showers.png" : "wsymbol_0025_light_rain_showers_night.png";
                break;
            case 81:
                description = "Rain shower: moderate";
                WMO_image_name = is_day > 0 ? "wsymbol_0048_drizzle.png" : "wsymbol_0066_drizzle_night.png";
                break;
            case 82:
                description = "Rain shower: violent";
                WMO_image_name = is_day > 0 ? "wsymbol_0018_cloudy_with_heavy_rain.png" : "wsymbol_0034_cloudy_with_heavy_rain_night.png";
                break;
            case 85:
                description = "Snow shower: slight";
                WMO_image_name = is_day > 0 ? "wsymbol_0011_light_snow_showers.png" : "wsymbol_0027_light_snow_showers_night.png";
                break;
            case 86:
                description = "Snow shower: heavy";
                WMO_image_name = is_day > 0 ? "wsymbol_0020_cloudy_with_heavy_snow.png" : "wsymbol_0036_cloudy_with_heavy_snow_night.png";
                break;
            case 95:
                description = "Thunderstorm: Slight or moderate";
                WMO_image_name = "wsymbol_0024_thunderstorms.png";
                break;
            case 96:
                description = "Thunderstorm: slight hail";
                WMO_image_name = "wsymbol_0024_thunderstorms.png";
                break;
            case 99:
                description = "Thunderstorm: heavy hail";
                WMO_image_name = "wsymbol_0024_thunderstorms.png";
                break;
// Thunderstorm forecast with hail is only available in Central Europe
            default:
                description = "Unknown";
                WMO_image_name = "wsymbol_0999_unknown.png";
                break;
        }
        return description;

    }
}
