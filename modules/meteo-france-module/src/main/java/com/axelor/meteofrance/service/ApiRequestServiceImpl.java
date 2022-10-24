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
import java.io.BufferedReader;

import org.apache.commons.beanutils.PropertyUtils;
import org.json.JSONArray;
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
    
    @Transactional
    private void treatJson(String jsonData, ApiRequest apiRequest){
        if(jsonData == null) return;

        JSONObject obj = new JSONObject(jsonData);
        //LocalDate test = LocalDate.now();
        JSONObject hourly_units = obj.getJSONObject("hourly_units");
        JSONObject hourly = obj.getJSONObject("hourly");
        //Prediction tmpPrediction = null;
        
        isFirstPass = true;
        //Dumping all arrays and their content with units into sysout.
        for (String curArray : hourly.keySet()) {
            System.out.println("Dumping "+curArray+":");

            
            
            hourly.getJSONArray(curArray).forEach(object -> {
                System.out.println(object+" "+hourly_units.getString(curArray));
                
                Prediction curPrediction = null;
                if (isFirstPass) {
                    curPrediction = Beans.get(PredictionRepository.class).create(null);
                    apiRequest.addPrediction(curPrediction);
                }else {
                    curPrediction = null;//TODO Get existing prediction
                }

                //Auto-fill fields base on CurArray's name.
                try {
                    if(curPrediction == null) return;
                    if (curArray == "time") PropertyUtils.setProperty(curPrediction, curArray, (LocalDate) object);
                    else PropertyUtils.setProperty(curPrediction, curArray, object+" "+hourly_units.getString(curArray));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {e.printStackTrace();}
                
            });
            isFirstPass = false;
            
            
            
            
            /*
            if (isFirstPass) {
               
                hourly.getJSONArray(curArray).forEach(object -> {
                    System.out.println(object+" "+hourly_units.getString(curArray));
                    Prediction curPrediction = Beans.get(PredictionRepository.class).create(null);
                    apiRequest.addPrediction(curPrediction);
                    //TODO Append current prediction to a list.
                    
                    //Auto-fill fields base on CurArray's name.
                    try {
                        if (curArray == "time") PropertyUtils.setProperty(curPrediction, curArray, (LocalDate) object);
                        else PropertyUtils.setProperty(curPrediction, curArray, object+" "+hourly_units.getString(curArray));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {e.printStackTrace();}
                    
                });
                isFirstPass = false;
            }else{
                //System.out.println(object+" "+hourly_units.getString(curArray));
                //TODO Fill-in the rest.
            }*/
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
