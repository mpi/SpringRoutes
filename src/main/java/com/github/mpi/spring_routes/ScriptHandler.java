package com.github.mpi.spring_routes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ScriptHandler implements HandlerMapping, HandlerAdapter, InitializingBean, Ordered, ApplicationContextAware {

    private ApplicationContext applicationContext;
    
    private Routes routes;

    @Override
    public void afterPropertiesSet() throws Exception {
        routes = new Routes(applicationContext);
        routes.registerRoutesFrom("cart.js");
    }
    
    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {

        if (routes.hasHandlerFor(request)) {
            return new HandlerExecutionChain(routes);
        }
        
        return null;
    }

    @Override
    public boolean supports(Object handler) {

        return handler instanceof Routes;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        Routes route = (Routes) handler;
        route.handle(request, response);
        
        return null;
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        return -1;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
