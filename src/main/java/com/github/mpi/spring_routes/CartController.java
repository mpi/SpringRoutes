package com.github.mpi.spring_routes;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.github.mpi.spring_routes.domain.Cart;
import com.github.mpi.spring_routes.domain.ProductID;
import com.github.mpi.spring_routes.domain.Quantity;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private Cart cart;
    
    @RequestMapping(
            method = GET,
            produces = "application/json")
    public CartJson get() {
        
        CartJson json = new CartJson();
        
        cart.forEach(
                (product, qty) -> json.with(product.toString(), new QuantityJson(qty))
        );
        
        return json;
    }

    @RequestMapping(
            method = DELETE)
    public ResponseEntity<Void> delete() {

        cart.discard();
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @RequestMapping(
            method = POST,
            consumes = "application/json")
    public ResponseEntity<Void> addItem(@RequestBody ProductJson json) {
        
        cart.add(new ProductID(json.product), new Quantity(json.qty.amount, json.qty.uom));
        
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/{productID}",
            method = DELETE)
    public ResponseEntity<Void> removeItem(@PathVariable("productID") String productID) {
        
        cart.remove(new ProductID(productID));
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(
            value = "/{productID}",
            consumes = "application/json",
            method = RequestMethod.POST)
    public ResponseEntity<Void> changeQuantityBy(@PathVariable("productID") String productID, @RequestBody ChangeQuantityByJson json) {
        
        ProductID product = new ProductID(productID);

        Quantity delta = new Quantity(json.delta.amount, json.delta.uom);
        
        if("add".equals(json.change)){
            cart.incrementQuantity(product, delta);
        } else if ("subtract".equals(json.change)){
            cart.decrementQuantity(product, delta);
        } else {
            throw new IllegalArgumentException("Invalid change operator: " + json.change);
        }
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @JsonAutoDetect(fieldVisibility=Visibility.ANY)
    class CartJson {

        List<ProductJson> products = new ArrayList<>();
        
        CartJson with(String product, QuantityJson qty){
            products.add(new ProductJson(product, qty));
            return this;
        }
    }
    
    @JsonAutoDetect(fieldVisibility=Visibility.ANY)
    static class ProductJson {
        
        String product;
        QuantityJson qty;

        ProductJson() {
        }
        
        ProductJson(String product, QuantityJson qty) {
            this.product = product;
            this.qty = qty;
        }
    }
    
    @JsonAutoDetect(fieldVisibility=Visibility.ANY)
    static class QuantityJson {
        
        BigDecimal amount;
        String uom;
        
        QuantityJson() {
        }
        
        QuantityJson(Quantity qty) {
            this.amount = qty.amount();
            this.uom = qty.unitOfMeasure();;
        }
    }
    
    @JsonAutoDetect(fieldVisibility=Visibility.ANY)
    static class ChangeQuantityByJson {
        
        String change;
        QuantityJson delta;
    }
}