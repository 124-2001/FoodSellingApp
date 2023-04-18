package com.example.foodsellingapp.service.distance;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
@Component
public class DistanceService {
    private static final String DISTANCE_MATRIX_API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private static final String API_KEY = "AIzaSyA0nbhG4Pp90jmkZapt5NntyQucVKE-pkY";
    private static final String address ="814 láng";

    public double getDistance(String destination) throws IOException {
        String url = DISTANCE_MATRIX_API_URL + "?origins=" + URLEncoder.encode(address, "UTF-8")
                + "&destinations=" + URLEncoder.encode(destination, "UTF-8")
                + "&key=" + API_KEY;

        URL urlObject = new URL(url);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(urlObject.openStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            JSONObject jsonObject = new JSONObject(response.toString());

            String status = jsonObject.getString("status");

            if (!"OK".equals(status)) {
                throw new IOException("API returned status code " + status);
            }

            JSONObject element = jsonObject.getJSONArray("rows").getJSONObject(0)
                    .getJSONArray("elements").getJSONObject(0);
//            String distanceText = element.getJSONObject("distance").getString("text");
            double distanceValue = element.getJSONObject("distance").getDouble("value") / 1000.0;

            return distanceValue;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }


}
