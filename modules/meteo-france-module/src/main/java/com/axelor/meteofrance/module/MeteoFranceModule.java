package com.axelor.meteofrance.module;

import com.axelor.app.AxelorModule;
import com.axelor.meteofrance.service.ApiRequestService;
import com.axelor.meteofrance.service.ApiRequestServiceImpl;

public class MeteoFranceModule extends AxelorModule {
    @Override
    protected void configure(){
        super.configure();

        bind(ApiRequestService.class).to(ApiRequestServiceImpl.class);
    }
}