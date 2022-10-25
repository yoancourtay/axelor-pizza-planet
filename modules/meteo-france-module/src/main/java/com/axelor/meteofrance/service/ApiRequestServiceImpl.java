package com.axelor.meteofrance.service;

import com.axelor.inject.Beans;
import com.axelor.meteo.db.ApiRequest;
import com.axelor.meteo.db.Prediction;
import com.axelor.meteo.db.repo.ApiRequestRepository;
import com.axelor.meteo.db.repo.PredictionRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;

import org.apache.commons.beanutils.PropertyUtils;
import org.json.JSONObject;


public class ApiRequestServiceImpl implements ApiRequestService {
    private ApiRequestRepository apiRequestRepository;

    @Inject
    public ApiRequestServiceImpl(ApiRequestRepository apiRequestRepository){
        this.apiRequestRepository = apiRequestRepository;
    }

    private String makeHttpRequest(String urlString) throws IOException{
        URL url = new URL(urlString);
        
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        
        Integer responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader inputReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
            );

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = inputReader.readLine()) != null) {
                response.append(inputLine);
            }
            inputReader.close();
            System.out.println("json:\n"+response);
            return response.toString();
        }else {
            System.out.println("Request response code: "+responseCode);
            System.out.println("Response header:\n"+connection.getHeaderFields());
            return null;
        }
    }

    static volatile Boolean isFirstPass = true;
    static volatile int predictionCounter = 0, p = 0;
    
    @Transactional
    private void treatJson(String jsonData, ApiRequest apiRequest){
        if(jsonData == null) return;

        JSONObject obj = new JSONObject(jsonData);
        JSONObject hourly_units = obj.getJSONObject("hourly_units");
        JSONObject hourly = obj.getJSONObject("hourly");
        
        isFirstPass = true;
        predictionCounter = 0;
        List<Prediction> predicList = new LinkedList<Prediction>();

        //Dumping all arrays and their content with units and putting it in the DB.
        for (String curArray : hourly.keySet()) {
            System.out.println("Dumping "+curArray+":");

            p = 0;

            hourly.getJSONArray(curArray).forEach(object -> {
                System.out.println(object+" "+hourly_units.getString(curArray));
                
                Prediction curPrediction = null;
                
                //If we're doing the first pass through json, create a "prediction" in the DB with the first field we get.
                //else go get the relevant existing prediction in DB to add to it.
                if (isFirstPass) {
                    curPrediction = Beans.get(PredictionRepository.class).create(null);
                    apiRequest.addPrediction(curPrediction);
                    predicList.add(curPrediction);
                    predictionCounter++;
                }else {
                    curPrediction = predicList.get(p);
                    p++;
                }

                //Auto-fill fields based on CurArray's name.
                try {
                    if(curPrediction == null) return;
                    //If filling in "time" parse object with LocalDate (format is iso8601).
                    if (curArray.equals("time")) PropertyUtils.setProperty(curPrediction, curArray, LocalDateTime.parse((String) object, DateTimeFormatter.ISO_DATE_TIME));
                    //Else fill in "current array name" with object as string with units appended to it.
                    else PropertyUtils.setProperty(curPrediction, curArray, object+" "+hourly_units.getString(curArray));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {e.printStackTrace();}
                
            });
            isFirstPass = false;
        }
    }
    
    @Override
    @Transactional
    public void sendRequest(ApiRequest apiRequest) throws IOException{
        //http://www.meteofrance.com/mf3-rpc-portlet/rest/pluie/INSEE+0
        //https://meteofrance.com/widget/prevision/INSEE+0
        //https://api.open-meteo.com/v1/meteofrance?latitude=48.8567&longitude=2.3510&hourly=temperature_2m,apparent_temperature,precipitation,cloudcover,windspeed_10m;
        apiRequest.setRequestUrl(
            "https://api.open-meteo.com/v1/meteofrance?latitude="+
            apiRequest.getCommune().getLatitude()+
            "&longitude="+
            apiRequest.getCommune().getLongitude()+
            "&hourly=temperature_2m,apparent_temperature,precipitation,cloudcover,windspeed_10m"
        );
        String response = makeHttpRequest(apiRequest.getRequestUrl());
        apiRequest.setJsonData(response);
        treatJson(apiRequest.getJsonData(), apiRequest);
    }
}
