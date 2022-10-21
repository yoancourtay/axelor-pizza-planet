package com.axelor.meteofrance.service;

import com.axelor.meteo.db.repo.ApiRequestRepository;
import com.google.inject.Inject;

public class ApiRequestServiceImpl implements ApiRequestService {
    private ApiRequestRepository apiRequestRepository;

    @Inject
    public ApiRequestServiceImpl(ApiRequestRepository apiRequestRepository){
        this.apiRequestRepository = apiRequestRepository;
    }

    @Override
    public void sendRequest(String URL){

    }
}
