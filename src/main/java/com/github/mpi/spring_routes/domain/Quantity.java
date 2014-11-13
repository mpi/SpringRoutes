package com.github.mpi.spring_routes.domain;

import java.math.BigDecimal;

public class Quantity implements Comparable<Quantity> {

    private BigDecimal amount;
    private String unitOfMeasure;

    @Override
    public String toString() {
        return String.format("%s %s", amount, unitOfMeasure);
    }

    public Quantity(BigDecimal amount, String unitOfMeasure) {
        this.amount = amount;
        this.unitOfMeasure = unitOfMeasure;
    }

    public static Quantity kg(BigDecimal amount){
        return new Quantity(amount, "kg");
    }
    
    public static Quantity units(int amount){
        return new Quantity(BigDecimal.valueOf(amount), "szt.");
    }

    public static Quantity dag(BigDecimal amount){
        return new Quantity(amount, "dag");
    }
    
    public Quantity add(Quantity other){
        
        if(!this.unitOfMeasure.equals(other.unitOfMeasure)){
            throw new IllegalArgumentException("Cannot add Quantities of different Unit of Measure!");
        }
        
        return new Quantity(this.amount.add(other.amount), this.unitOfMeasure);
    }

    public Quantity subtract(Quantity other){
        
        if(!this.unitOfMeasure.equals(other.unitOfMeasure)){
            throw new IllegalArgumentException("Cannot subtract Quantities of different Unit of Measure!");
        }
        
        BigDecimal newAmount = this.amount.subtract(other.amount);
        
        return new Quantity(newAmount, this.unitOfMeasure);
    }

    public boolean isZero() {
        return amount.equals(BigDecimal.ZERO);
    }

    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }
    
    @Override
    public int compareTo(Quantity other) {

        if(!other.unitOfMeasure.equals(this.unitOfMeasure)){
            throw new IllegalArgumentException("Cannot compate Quantities of different Unit of Measure!");
        }
        
        return this.amount.compareTo(other.amount);
    }

    public BigDecimal amount() {
        return amount;
    }

    public String unitOfMeasure() {
        return unitOfMeasure;
    }

}
