package com.axelor.pizza;

import com.axelor.app.AxelorModule;
import com.axelor.pizza.db.repo.PizzaRepository;
import com.axelor.pizza.repository.PizzaRepositoryExtended;
import com.axelor.pizza.service.PizzaDoSomethingService;
import com.axelor.pizza.service.PizzaDoSomethingServiceImpl;

public class PizzaModule extends AxelorModule {
    @Override
    protected void configure(){
        super.configure();
        bind(PizzaRepository.class).to(PizzaRepositoryExtended.class);
        
        bind(PizzaDoSomethingService.class).to(PizzaDoSomethingServiceImpl.class);
    }
}
