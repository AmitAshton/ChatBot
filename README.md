# chatbot


### swagger
pom.xml
<br>
<version>2.6.2</version> -> <version>2.5.2</version>
```
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>2.6.1</version>
		</dependency><!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger2 -->
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>2.6.1</version>
		</dependency>
```

config/SwaggerConfig.java
```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
```
service/AmazonService.java
```java
@Service
public class AmazonService {

    public String searchProducts(String keyword) {
        return "Searched for:" + keyword;
    }
}
```
controller/BotController.java
```java
@Service
@RestController
@RequestMapping("/bot")
public class BotController {

    @Autowired
    AmazonService amazonService;

    @RequestMapping(value = "/amazon", method = RequestMethod.GET)
    public ResponseEntity<?> getProduct(@RequestParam String keyword)
    {
        return new ResponseEntity<>(amazonService.searchProducts(keyword), HttpStatus.OK);
    }
}
```
http://localhost:8080/swagger-ui.html#
<br>
commit - with swagger
###Find the search api
https://www.amazon.com/s?k=ipod
<br>
service/AmazonService.java
```java
    private String getProductHtml(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.amazon.com/s?k=" + keyword +"&crid=1FVOAPA9AELRZ&sprefix=ipod%2Caps%2C181&ref=nb_sb_noss_1")
                .method("GET", null)
                .addHeader("authority", "www.amazon.com")
                .addHeader("cache-control", "max-age=0")
                .addHeader("rtt", "100")
                .addHeader("downlink", "10")
                .addHeader("ect", "4g")
                .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"98\", \"Google Chrome\";v=\"98\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("referer", "https://www.amazon.com/")
                .addHeader("accept-language", "en-US,en;q=0.9,he;q=0.8")
                .addHeader("cookie", "session-id=131-3987483-5342545; session-id-time=2082787201l; i18n-prefs=USD; sp-cdn=\"L5Z9:IL\"; skin=noskin; ubid-main=133-9581942-3780819; session-token=9wuRyGDfGYRNIQddpLSfu/7ZXPkW7pQoYBvMwmGsIhnfY51Iqj+MGS377K23lkwtdjhFx7pU6lcOwFpb0U6WneR7bB7A16Fx7VbQbOEVXnmyIo1i6GXJCcO4dDDR8dN3t5hl/PRi/+vWZPCxL7pYX7A0IOLl74C6MykvrYSxmA29bWSf8tfTif60UEjxD0kk; csm-hit=tb:TTHKG9BMH5E1XG8YFBGP+s-PMZSXATEMDSYE2A3YAS8|1643843190534&t:1643843190534&adb:adblk_yes; session-token=\"2xJZqprgyjzxD7aWv5lvF6L2Jj6wbj75+0bl0405+iQ/teLGzDnuRJLFAIT+TZQlkgfSAzqMtgIwEeHBFVD0aip6GkUe+j61VFbW6p2RqtI4ZpKzPhLXUan7/XN3IT54f1XxTmwQcSjyB2GQ/9kA6/4K7i8kFxf9gYWBIaSqklC4vZX3A8WEtWGUcQxKUx1PP7imQpBv5nT5pijsa/fk2A==\"")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
```
commit - with search product call

### find the regex:
https://regex101.com/
<br>
service/AmazonService.java
```java
    public static final Pattern PRODUCT_PATTERN = Pattern.compile("<span class=\\\"a-size-medium a-color-base a-text-normal\\\">([^<]+)</span> </a> </h2></div><div class=\\\"a-section a-spacing-none a-spacing-top-micro\\\"><div class=\\\"a-row a-size-small\\\"><span aria-label=\\\"([^\\\"]+)\\\"><span.*<span class=\\\"a-offscreen\\\">([^<]+)</span>");

    public String searchProducts(String keyword) throws IOException {
        return parseProductHtml(getProductHtml(keyword));
    }

    private String parseProductHtml(String html) {
        String res = "";
        Matcher matcher = PRODUCT_PATTERN.matcher(html);
        while (matcher.find()) {
            res += matcher.group(1) + " - " + matcher.group(2) + ", price:" + matcher.group(3) + "<br>\n";
        }
        return res;
    }
```
commit - with product regex
### weather search
https://www.yahoo.com/news/weather/
<br>

service/WeatherService.java
```java
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
```

```java

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

```
commit - with weather

<br>
https://dialogflow.cloud.google.com/
<br>
https://webhook.site/
<br>

controller/BotController.java
```java

    @RequestMapping(value = "", method = { RequestMethod.POST})
    public ResponseEntity<?> getBotResponse(@RequestBody BotQuery query) throws IOException {
        HashMap<String, String> params = query.getQueryResult().getParameters();
        String res = "Not found";
        if (params.containsKey("city")) {
            res = weatherService.searchWeather(params.get("city"));
        } else if (params.containsKey("product")) {
            res = amazonService.searchProducts(params.get("product"));
        }
        return new ResponseEntity<>(BotResponse.of(res), HttpStatus.OK);
    }


    static class BotQuery {
        QueryResult queryResult;

        public QueryResult getQueryResult() {
            return queryResult;
        }
    }

    static class QueryResult {
        HashMap<String, String> parameters;

        public HashMap<String, String> getParameters() {
            return parameters;
        }
    }

    static class BotResponse {
        String fulfillmentText;
        String source = "BOT";

        public String getFulfillmentText() {
            return fulfillmentText;
        }

        public String getSource() {
            return source;
        }

        public static BotResponse of(String fulfillmentText) {
            BotResponse res = new BotResponse();
            res.fulfillmentText = fulfillmentText;
            return res;
        }
    }
```
commit - with bot response
<br>
https://www.heroku.com/
fork if needed
