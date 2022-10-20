package com.axelor.pizzaplanet.web;

import com.axelor.inject.Beans;
import com.axelor.pizzaplanet.service.PizzaDoSomethingService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class PizzaController {
    public void doSomething(ActionRequest request, ActionResponse response){
        if(Beans.get(PizzaDoSomethingService.class).doSomething()){
            response.setReload(true);
            response.setFlash("Prefixed all vegetarian pizzas with '[Vegetarian]'");
        }else response.setFlash("No pizzas needing to be prefixed with '[Vegetarian]'");
    }
}
