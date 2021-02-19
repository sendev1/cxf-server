package com.cxf.server;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.extension.ExtensionManagerBus;
import org.apache.cxf.common.spi.GeneratedNamespaceClassLoader;
import org.apache.cxf.common.spi.NamespaceClassCreator;
import org.apache.cxf.endpoint.dynamic.ExceptionClassCreator;
import org.apache.cxf.endpoint.dynamic.ExceptionClassLoader;
import org.apache.cxf.jaxb.FactoryClassCreator;
import org.apache.cxf.jaxb.FactoryClassLoader;
import org.apache.cxf.jaxb.WrapperHelperClassLoader;
import org.apache.cxf.jaxb.WrapperHelperCreator;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.spi.WrapperClassCreator;
import org.apache.cxf.jaxws.spi.WrapperClassLoader;
import org.apache.cxf.wsdl.ExtensionClassCreator;
import org.apache.cxf.wsdl.ExtensionClassLoader;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Annotation;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.tutorialspoint.helloworld.Greetings;

@Configuration
public class ApplicationConfig {

    // assumes the current class is called MyLogger
    private final static Logger LOGGER = Logger.getLogger(ApplicationConfig.class.getName());

    @Autowired
    private Bus bus;

    @Bean
    public Endpoint endpoint(Bus bus) {

        // Check package info is present
        Package myPackage = Greetings.class.getPackage();
        Annotation[] myPackageAnnotations = myPackage.getAnnotations();
        System.out.println("Available annotations for package : " + myPackage.getName());
        for(Annotation a : myPackageAnnotations) {
            System.out.println("\t * " + a.annotationType());
        }

        LOGGER.log(Level.INFO, "ENDPOINT ENTRY");
        bus.setExtension(new WrapperHelperClassLoader(bus), WrapperHelperCreator.class);
        bus.setExtension(new ExtensionClassLoader(bus), ExtensionClassCreator.class);
        bus.setExtension(new ExceptionClassLoader(bus), ExceptionClassCreator.class);
        bus.setExtension(new WrapperClassLoader(bus), WrapperClassCreator.class);
        bus.setExtension(new FactoryClassLoader(bus), FactoryClassCreator.class);
        bus.setExtension(new GeneratedNamespaceClassLoader(bus), NamespaceClassCreator.class);

        LOGGER.log(Level.INFO, "ENDPOINT Creating ednpoint object");
        EndpointImpl endpoint =
                new EndpointImpl(bus, new HelloWorldImpl());
        LOGGER.log(Level.INFO, "ENDPOINT Endpoint object created");


        LOGGER.log(Level.INFO, "ENDPOINT About to publish");
        endpoint.publish("/helloworld");

        LOGGER.log(Level.INFO, "ENDPOINT Published");
        LOGGER.log(Level.INFO, "Address: " + endpoint.getAddress());
        LOGGER.log(Level.INFO, "Bean name: " + endpoint.getBeanName());
        LOGGER.log(Level.INFO, "Endpoint URL: " + endpoint.getPublishedEndpointUrl());
        LOGGER.log(Level.INFO, "Binding URI: " + endpoint.getBindingUri());
        LOGGER.log(Level.INFO, "WSDL Location: " + endpoint.getWsdlLocation());
        LOGGER.log(Level.INFO, "Binding Config: " + endpoint.getBindingConfig().toString());
        //LOGGER.log(Level.INFO, "Data Binding: " + endpoint.getDataBinding().toString());

        return endpoint;
    }
}