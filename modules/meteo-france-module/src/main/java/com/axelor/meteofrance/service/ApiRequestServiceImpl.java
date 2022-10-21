package com.axelor.meteofrance.service;

import com.axelor.meteo.db.repo.ApiRequestRepository;
import com.google.inject.Inject;
import com.axelor.meteo.db.ApiRequest;

public class ApiRequestServiceImpl implements ApiRequestService {
    private ApiRequestRepository apiRequestRepository;

    @Inject
    public ApiRequestServiceImpl(ApiRequestRepository apiRequestRepository){
        this.apiRequestRepository = apiRequestRepository;
    }

    @Override
    public String buildRequest(ApiRequest apiRequest){
        return "test";
    }
}
