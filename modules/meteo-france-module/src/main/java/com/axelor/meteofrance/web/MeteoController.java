package com.axelor.meteofrance.web;

import com.axelor.inject.Beans;
import com.axelor.meteo.db.Commune;
import com.axelor.meteofrance.service.ApiRequestService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class MeteoController {
    public void sendRequest(ActionRequest request, ActionResponse response){
        response.setFlash("build send");
    }
}
