package com.axelor.pizzaplanet.repository;

import com.axelor.db.Query;
import com.axelor.pizza.db.Pizza;
import com.axelor.pizza.db.repo.PizzaRepository;
import com.axelor.pizzaplanet.repository.PizzaRepositoryExtended;

public class PizzaRepositoryExtended extends PizzaRepository {
    
    public Query<Pizza> findByName2(String name){
        return Query.of(Pizza.class).filter("self.name = :name").bind("name", name);
    }
}
