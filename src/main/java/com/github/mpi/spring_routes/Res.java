package com.github.mpi.spring_routes;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class Res {

    private HttpServletResponse resp;
    
    public Res(HttpServletResponse resp) {
        this.resp = resp;
    }

    public void send(String text) throws IOException{
        resp.getOutputStream().println(text);
    }
}