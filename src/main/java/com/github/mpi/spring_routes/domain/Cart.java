package com.github.mpi.spring_routes.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class Cart {

    private Map<ProductID, Quantity> products = new HashMap<>();
    
    public void add(ProductID product, Quantity quantity){
        products.merge(product, quantity, (a, b) -> a.add(b));
    }
    
    public void incrementQuantity(ProductID product, Quantity additional){

        if(!products.containsKey(product)){
            throw error("Item for product '%s' not found!", product);
        }

        products.merge(product, additional, (a, b) -> a.add(b));
    }

    public void decrementQuantity(ProductID product, Quantity excessive){
        
        if(!products.containsKey(product)){
            throw error("Item for product '%s' not found!", product);
        }
        
        products.merge(product, excessive, (oldValue, toRemove) -> {
        
            Quantity newValue = oldValue.subtract(toRemove); 

            if(newValue.isZero()){
                return null;
            }
            if(newValue.isNegative()){
                throw error("Cannot remove %d items of product %s. There are only %s in cart!", toRemove, product, oldValue);
            }

            return newValue;
            
        });
    }
    
    private IllegalArgumentException error(String message, Object... args) {
        return new IllegalArgumentException(String.format(message, args));
    }
    
    public void remove(ProductID product){
        products.remove(product);
    }
    
    public void discard(){
        products.clear();
    }

    public void forEach(BiConsumer<ProductID, Quantity> consumer) {
        products.forEach(consumer);
    }

    @PostConstruct
    public void sampleData(){
        add(new ProductID("ham"), Quantity.kg(new BigDecimal("0.5")));
        add(new ProductID("egg"), Quantity.units(6));
        add(new ProductID("roll"), Quantity.units(3));
        add(new ProductID("jam"), Quantity.units(1));
        add(new ProductID("butter"), Quantity.units(1));
    }
    
}
