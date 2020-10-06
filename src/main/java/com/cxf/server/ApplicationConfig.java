package com.cxf.server;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class ApplicationConfig {
    @Autowired
    private Bus bus;

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint =
                new EndpointImpl(bus, new HelloWorldImpl());
        endpoint.publish("/helloworld");

        return endpoint;
    }
}
