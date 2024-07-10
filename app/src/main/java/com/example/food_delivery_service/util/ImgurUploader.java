package com.example.food_delivery_service.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ImgurUploader {
    private static final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/image";
    private static final String IMGUR_CLIENT_ID = "bf71705ec5dbca2";

    public static String uploadToImgur(InputStream imageStream) throws IOException, JSONException {
        HttpURLConnection conn = (HttpURLConnection) new URL(IMGUR_UPLOAD_URL).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID " + IMGUR_CLIENT_ID);

        try (OutputStream out = conn.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = imageStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        int responseCode = conn.getResponseCode();
        String responseBody = new Scanner(conn.getInputStream()).useDelimiter("\\A").next();
        conn.disconnect();

        if (responseCode == 200) {
            JSONObject jsonObject = new JSONObject(responseBody);
            String link = jsonObject.getJSONObject("data").getString("link");
            return link;
        } else {
            throw new IOException("Failed to upload image to Imgur. Response: " + responseBody);
        }
    }
}

