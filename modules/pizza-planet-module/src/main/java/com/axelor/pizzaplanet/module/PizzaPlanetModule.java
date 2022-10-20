package com.axelor.pizzaplanet.module;

import com.axelor.app.AxelorModule;
import com.axelor.pizza.db.repo.PizzaRepository;
import com.axelor.pizzaplanet.repository.PizzaRepositoryExtended;
import com.axelor.pizzaplanet.service.PizzaDoSomethingService;
import com.axelor.pizzaplanet.service.PizzaDoSomethingServiceImpl;

public class PizzaPlanetModule extends AxelorModule {
    @Override
    protected void configure(){
        super.configure();
        bind(PizzaRepository.class).to(PizzaRepositoryExtended.class);
        
        bind(PizzaDoSomethingService.class).to(PizzaDoSomethingServiceImpl.class);
    }
}
