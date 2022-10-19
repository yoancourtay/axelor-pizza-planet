package com.axelor.pizza.web;

import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.pizza.service.PizzaDoSomethingService;

public class PizzaController {
    public void doSomething(ActionRequest request, ActionResponse response){
        if(Beans.get(PizzaDoSomethingService.class).doSomething()){
            response.setReload(true);
            response.setFlash("Prefixed all vegetarian pizzas with '[Vegetarian]'");
        }else response.setFlash("No pizzas needing to be prefixed with '[Vegetarian]'");
    }
}
