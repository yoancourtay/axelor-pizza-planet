package com.axelor.meteofrance.web;

import com.axelor.inject.Beans;
import com.axelor.meteo.db.ApiRequest;
import com.axelor.meteo.db.repo.ApiRequestRepository;
import com.axelor.meteofrance.service.ApiRequestService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class MeteoController {
    public void sendRequest(ActionRequest request, ActionResponse response){
        ApiRequest apiRequest = Beans.get(ApiRequestRepository.class).find((long) request.getContext().get("id"));
        String URL = apiRequest.getRequest();
        Beans.get(ApiRequestService.class).sendRequest(URL);
        response.setFlash("build send"+URL);
    }
}
