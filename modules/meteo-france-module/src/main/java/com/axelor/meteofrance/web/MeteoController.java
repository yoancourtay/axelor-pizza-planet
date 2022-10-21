package com.axelor.meteofrance.web;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class MeteoController {
    public void sendRequest(ActionRequest request, ActionResponse response){
        response.setFlash("build send");
    }
}
