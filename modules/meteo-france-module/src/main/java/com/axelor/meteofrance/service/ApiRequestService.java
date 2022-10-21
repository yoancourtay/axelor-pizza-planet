package com.axelor.meteofrance.service;

import com.axelor.meteo.db.ApiRequest;

public interface ApiRequestService {
    String buildRequest(ApiRequest apiRequest);
}
