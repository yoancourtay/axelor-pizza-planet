package com.axelor.meteofrance.service;

import java.io.IOException;

import com.axelor.meteo.db.ApiRequest;

public interface ApiRequestService {
    void sendRequest(ApiRequest apiRequest) throws IOException;
}
