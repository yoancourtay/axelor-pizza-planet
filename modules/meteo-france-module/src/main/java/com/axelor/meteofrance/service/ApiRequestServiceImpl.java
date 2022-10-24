package com.axelor.meteofrance.service;

import com.axelor.meteo.db.ApiRequest;
import com.axelor.meteo.db.repo.ApiRequestRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.io.BufferedReader;

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
    
    @Override
    @Transactional
    public void sendRequest(ApiRequest apiRequest) throws IOException{
        //http://www.meteofrance.com/mf3-rpc-portlet/rest/pluie/
        //https://meteofrance.com/widget/prevision/
        //https://api.open-meteo.com/v1/meteofrance?latitude=48.8567&longitude=2.3510&hourly=temperature_2m,apparent_temperature,precipitation,cloudcover,windspeed_10m
        //String URL = "https://meteofrance.com/widget/prevision/"+apiRequest.getCommune().getInsee()+"0";
        String URL = 
            "https://api.open-meteo.com/v1/meteofrance?latitude="+
            apiRequest.getCommune().getLatitude()+
            "&longitude="+
            apiRequest.getCommune().getLongitude()+
            "&hourly=temperature_2m,apparent_temperature,precipitation,cloudcover,windspeed_10m";
        String response = makeHttpRequest(URL);
        apiRequest.setResponse(response);
    }
}
