package com.handson.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WeatherService {

    public static final OkHttpClient client = new OkHttpClient();

    @Autowired
    ObjectMapper om;

    public String searchWeather(String keyword) throws IOException {
        Integer locationId = getWeatherLocationId(keyword);
        String res = getWeatherForLocationId(locationId);
        return res;
    }


    private Integer getWeatherLocationId(String keyword) throws IOException {
        Request request = new Request.Builder()
                .url("https://www.yahoo.com/news/_tdnews/api/resource/WeatherSearch;text=" + keyword +"?bkt=%5B%22a2-plutus-rr-backbucket%22%2C%22xray-us-nel-about-03%22%2C%22xray-exp-api-ctrl%22%2C%22JARVISUSNEWSDESK-INSESSION-00%22%5D&device=desktop&ecma=modern&feature=cacheContentCanvas%2CdelayCacheHeaders%2CdisableCommentsMessage%2CenableCCPAFooter%2CenableCMP%2CenableConsentData%2CenableGDPRFooter%2CenableGuceJs%2CenableGuceJsOverlay%2Clivecoverage%2CnewContentAttribution%2CnewLogo%2CrivendellMigration%2Cuserintent%2CvideoDocking%2CoathPlayer%2CenableAdlite&intl=us&lang=en-US&partner=none&prid=fpmog1tgvn0u5&region=US&site=fp&tz=Asia%2FJerusalem&ver=0.0.12106063&returnMeta=true")
                .method("GET", null)
                .addHeader("authority", "www.yahoo.com")
                .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"98\", \"Google Chrome\";v=\"98\"")
                .addHeader("x-requested-with", "XMLHttpRequest")
                .addHeader("x-webp", "1")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .addHeader("accept", "*/*")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("referer", "https://www.yahoo.com/news/weather/israel/givatayim/givatayim-1967398")
                .addHeader("accept-language", "en-US,en;q=0.9,he;q=0.8")
                .addHeader("cookie", "B=8tdhqp1gvm1lg&b=3&s=gh; GUC=AQEBAQFh_FhiBEIhvwTZ; A1=d=AQABBLAG-2ECEAcdkXMXwsgVkT7r98g6to4FEgEBAQFY_GEEYgAAAAAA_eMAAAcIsAb7Ycg6to4&S=AQAAApJNKoBrUWJ5nMosB6Kj660; A1S=d=AQABBLAG-2ECEAcdkXMXwsgVkT7r98g6to4FEgEBAQFY_GEEYgAAAAAA_eMAAAcIsAb7Ycg6to4&S=AQAAApJNKoBrUWJ5nMosB6Kj660&j=WORLD; A3=d=AQABBLAG-2ECEAcdkXMXwsgVkT7r98g6to4FEgEBAQFY_GEEYgAAAAAA_eMAAAcIsAb7Ycg6to4&S=AQAAApJNKoBrUWJ5nMosB6Kj660")
                .build();
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        WeatherLocations locations = om.readValue(res,WeatherLocations.class);
        if (locations.getData() != null && locations.getData().size() > 0) {
            return locations.getData().get(0).getWoeid();
        }else {
            return -1;
        }
    }

    private String getWeatherForLocationId(Integer locationId) throws IOException {
        Request request = new Request.Builder()
                .url("https://www.yahoo.com/news/_tdnews/api/resource/WeatherService;woeids=%5B" + locationId + "%5D?bkt=%5B%22a2-plutus-rr-backbucket%22%2C%22xray-us-nel-about-03%22%2C%22xray-exp-api-ctrl%22%2C%22JARVISUSNEWSDESK-INSESSION-00%22%5D&device=desktop&ecma=modern&feature=cacheContentCanvas%2CdelayCacheHeaders%2CdisableCommentsMessage%2CenableCCPAFooter%2CenableCMP%2CenableConsentData%2CenableGDPRFooter%2CenableGuceJs%2CenableGuceJsOverlay%2Clivecoverage%2CnewContentAttribution%2CnewLogo%2CrivendellMigration%2Cuserintent%2CvideoDocking%2CoathPlayer%2CenableAdlite&intl=us&lang=en-US&partner=none&prid=fpmog1tgvn0u5&region=US&site=fp&tz=Asia%2FJerusalem&ver=0.0.12106063&returnMeta=true")
                .method("GET", null)
                .addHeader("authority", "www.yahoo.com")
                .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"98\", \"Google Chrome\";v=\"98\"")
                .addHeader("x-requested-with", "XMLHttpRequest")
                .addHeader("x-webp", "1")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .addHeader("accept", "*/*")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("referer", "https://www.yahoo.com/news/weather/")
                .addHeader("accept-language", "en-US,en;q=0.9,he;q=0.8")
                .addHeader("cookie", "B=8tdhqp1gvm1lg&b=3&s=gh; GUC=AQEBAQFh_FhiBEIhvwTZ; A1=d=AQABBLAG-2ECEAcdkXMXwsgVkT7r98g6to4FEgEBAQFY_GEEYgAAAAAA_eMAAAcIsAb7Ycg6to4&S=AQAAApJNKoBrUWJ5nMosB6Kj660; A1S=d=AQABBLAG-2ECEAcdkXMXwsgVkT7r98g6to4FEgEBAQFY_GEEYgAAAAAA_eMAAAcIsAb7Ycg6to4&S=AQAAApJNKoBrUWJ5nMosB6Kj660&j=WORLD; A3=d=AQABBLAG-2ECEAcdkXMXwsgVkT7r98g6to4FEgEBAQFY_GEEYgAAAAAA_eMAAAcIsAb7Ycg6to4&S=AQAAApJNKoBrUWJ5nMosB6Kj660")
                .build();
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        WeatherLocationData ld = om.readValue(res,WeatherLocationData.class);
        try {
            return ld.getData().getWeathers().get(0).getObservation().getDayPartTexts().get(0).getText();
        } catch (Exception e){
            e.printStackTrace();
            return "Not Found";
        }
    }

    static class WeatherLocationData {
        Weathers data;

        public Weathers getData() {
            return data;
        }
    }

    static class Weathers {
        List<Weather> weathers;

        public List<Weather> getWeathers() {
            return weathers;
        }
    }

    static class Weather {
        WObservation observation;

        public WObservation getObservation() {
            return observation;
        }
    }
    static class WObservation {
        List<DayPart> dayPartTexts;

        public List<DayPart> getDayPartTexts() {
            return dayPartTexts;
        }
    }

    static class DayPart {
        String text;

        public String getText() {
            return text;
        }
    }

    static class WeatherLocations {
        List<WeatherLocation> data;

        public List<WeatherLocation> getData() {
            return data;
        }
    }

    static class WeatherLocation {
        Integer woeid;

        public Integer getWoeid() {
            return woeid;
        }
    }



}
