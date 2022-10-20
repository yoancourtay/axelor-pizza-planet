package com.axelor.pizzaplanet.service;

import com.axelor.db.Query;
import com.axelor.pizza.db.Pizza;
import com.axelor.pizzaplanet.repository.PizzaRepositoryExtended;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class PizzaDoSomethingServiceImpl implements PizzaDoSomethingService {
    private PizzaRepositoryExtended pizzaRepository;
    
    @Inject
    public PizzaDoSomethingServiceImpl(PizzaRepositoryExtended pizzaRepository){
        this.pizzaRepository = pizzaRepository;
    }
    
    @Override
    @Transactional
    public boolean doSomething(){
        Query<Pizza> query = pizzaRepository.findByVegetarian(true);
        if(query.count() > 0){
            for (Pizza pizza : query.fetch()) {
                pizza.setName("[Vegetarian] " + pizza.getName());
            }
            
            return true;
        }
        return false;
    }
}
