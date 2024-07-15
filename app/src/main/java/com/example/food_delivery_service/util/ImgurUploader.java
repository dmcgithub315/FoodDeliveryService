package com.example.food_delivery_service.util;

import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;

public class ImgurUploader {
    private static final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/image";
    private static final String IMGUR_CLIENT_ID = "bf71705ec5dbca2";
    private static final String LOCAL_SAVE_PATH = "/path/to/save/image"; // Update this path

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
        String responseBody;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            responseBody = new Scanner(conn.getInputStream()).useDelimiter("\\A").next();
        } else {
            responseBody = new Scanner(conn.getErrorStream()).useDelimiter("\\A").next();
        }
        conn.disconnect();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            JSONObject jsonObject = new JSONObject(responseBody);
            String link = jsonObject.getJSONObject("data").getString("link");
            return link;
        } else if (responseCode == 500) {
            String localImagePath = saveImageLocally(imageStream);
            return localImagePath;
        } else {
            throw new IOException("Failed to upload image to Imgur. Response Code: " + responseCode + " Response: " + responseBody);
        }
    }

    public static String saveImageLocally(InputStream imageStream) throws IOException {
        String directoryPath = LOCAL_SAVE_PATH;
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean wasDirectoryMade = directory.mkdirs();
            if (!wasDirectoryMade) {
                throw new IOException("Failed to create directory for saving images");
            }
        }

        String filePath = directoryPath + "/localImage_" + System.currentTimeMillis() + ".png";
        File file = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = imageStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        return filePath;
    }

    public Uri uploadImage(InputStream imageStream) {
        try {
            String link = uploadToImgur(imageStream);
            return Uri.parse(link);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
}