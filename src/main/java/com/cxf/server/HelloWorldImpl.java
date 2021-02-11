package com.cxf.server;

import com.tutorialspoint.helloworld.HelloWorldPortType;

import java.io.File;
import java.io.IOException;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("HelloWorldServiceImpl")
@WebService(serviceName = "HelloWorldService",
	endpointInterface = "com.tutorialspoint.helloworld.HelloWorldPortType",
	targetNamespace = "http://helloworld.tutorialspoint.com/")
public class HelloWorldImpl implements HelloWorldPortType {

	

    @Override
    public String greetings(String name) {
		
		return ("hi " + name);
    }
}
