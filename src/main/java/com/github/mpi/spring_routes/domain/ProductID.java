package com.github.mpi.spring_routes.domain;

public class ProductID {

    private final String id;

    public ProductID(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        
        if(!(obj instanceof ProductID)){
            return false;
        }
        
        ProductID other = (ProductID) obj;
        
        return this.id.equals(other.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return id;
    }
}
