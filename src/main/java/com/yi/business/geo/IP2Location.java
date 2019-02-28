package com.yi.business.geo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * 通过新浪的接口获得ip所在的位置
 *
 * @author YI
 * @date 2018-4-3 15:11:24
 */
public class IP2Location {
    /**
     * 通过ip获得位置信息
     *
     * @param ip
     * @return
     */
    public static String ip2Location(String ip) {
        String result = "";
        Gson gson = new Gson();

        try {
            URLConnection connection = new URL("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=" + ip).openConnection();
            connection.getInputStream();
            connection.setConnectTimeout(5000);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String s;
                while ((s = br.readLine()) != null) {
                    result += s;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String json = result.substring(result.indexOf("=") + 1, result.length() - 1);

        Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());

        StringBuilder location = new StringBuilder();
        if (map.get("country") != null) {
            location.append(map.get("country"));
        }
        if (map.get("province") != null) {
            location.append(map.get("province"));
        }
        if (map.get("city") != null) {
            location.append(map.get("city"));
        }

        return location.toString();
    }

    public static double ip2Location(String ip1, String ip2) {
        String ip1geo = ip2Location(ip1);
        String ip2geo = ip2Location(ip2);

        return TermRelationTreeCoordinate.GetDistance(ip1geo, ip2geo);
    }
}
