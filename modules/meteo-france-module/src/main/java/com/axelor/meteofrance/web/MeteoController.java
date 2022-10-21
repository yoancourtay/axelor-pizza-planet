package com.axelor.meteofrance.web;

import com.axelor.inject.Beans;
import com.axelor.meteo.db.ApiRequest;
import com.axelor.meteo.db.repo.ApiRequestRepository;
import com.axelor.meteofrance.service.ApiRequestService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class MeteoController {
    public void buildRequest(ActionRequest request, ActionResponse response){
        ApiRequest apiRequest = Beans.get(ApiRequestRepository.class).find((Long) request.getContext().get("id"));
        String testing = Beans.get(ApiRequestService.class).buildRequest(apiRequest);
        response.setFlash("build request "+testing);
    }

    public void sendRequest(ActionRequest request, ActionResponse response){
        response.setFlash("build send");
    }
}
