package com.liaoyun.mes.controller;



import cn.hutool.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DingTalkAccessToken {
    private static final String APP_KEY = "your_app_key";
    private static final String APP_SECRET = "your_app_secret";

    // 获取Access Token的方法
    public  String getAccessToken() {
        String accessToken = null;
        String url = "https://oapi.dingtalk.com/gettoken?appkey=dingxozlsqnzm0b6afyy&appsecret=VlljCwRk0Cf4-5TT76LhnEoVY9HTJfwa3LEPYSeyu7K0b6pq44Bx_sJBSkyzsAtr";
        try {
            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject jsonObj = new JSONObject(response.toString());
            accessToken = jsonObj.get("access_token").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }
}
