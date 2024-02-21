package com.favoland.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserAgentGenerator {

    public List<String> generateUserAgents(int count) {
        List<String> userAgents = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            String userAgent = generateRandomUserAgent(random);
            userAgents.add(userAgent);
        }

        return userAgents;
    }

    public String generateRandomUserAgent(Random random) {
        String[] browsers = {"Chrome", "Firefox", "Safari", "Edge", "Opera"};
        String[] os = {"Windows NT 10.0", "Macintosh", "Linux"};

        String browser = browsers[random.nextInt(browsers.length)];
        String version = "Version/" + (random.nextInt(10) + 1) + "." + random.nextInt(10);
        String platform = "(" + os[random.nextInt(os.length)] + ";";

        return "Mozilla/5.0 " + platform + " x64) AppleWebKit/537.36 (KHTML, like Gecko) " + browser + "/" + version;
    }
}
