package com.axelor.meteofrance.web;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class MeteoController {
    public void buildRequest(ActionRequest request, ActionResponse response){
        response.setFlash("build request");
    }

    public void sendRequest(ActionRequest request, ActionResponse response){
        response.setFlash("build send");
    }
}
