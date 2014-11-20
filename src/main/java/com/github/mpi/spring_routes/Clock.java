package com.github.mpi.spring_routes;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class Clock {

    public Date time(){
        return new Date();
    }
    
}
