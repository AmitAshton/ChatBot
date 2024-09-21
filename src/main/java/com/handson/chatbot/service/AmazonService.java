package com.handson.chatbot.service;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AmazonService {

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
}
