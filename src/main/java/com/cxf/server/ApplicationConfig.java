package com.cxf.server;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.common.spi.GeneratedClassClassLoaderCapture;
import org.apache.cxf.common.spi.GeneratedNamespaceClassLoader;
import org.apache.cxf.common.spi.NamespaceClassCreator;
import org.apache.cxf.endpoint.dynamic.ExceptionClassCreator;
import org.apache.cxf.endpoint.dynamic.ExceptionClassLoader;
import org.apache.cxf.jaxb.FactoryClassCreator;
import org.apache.cxf.jaxb.FactoryClassLoader;
import org.apache.cxf.jaxb.WrapperHelperClassLoader;
import org.apache.cxf.jaxb.WrapperHelperCreator;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.spi.WrapperClassCreator;
import org.apache.cxf.jaxws.spi.WrapperClassLoader;
import org.apache.cxf.wsdl.ExtensionClassCreator;
import org.apache.cxf.wsdl.ExtensionClassLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
	@Autowired
	private DumpingClassLoaderCapturer capturer;

	@Bean(name=Bus.DEFAULT_BUS_ID)
	public SpringBus springBus() {
		final Bus bus = new SpringBus();
		/*bus.setExtension(new WrapperHelperClassLoader(bus), WrapperHelperCreator.class);
		bus.setExtension(new ExtensionClassLoader(bus), ExtensionClassCreator.class);
		bus.setExtension(new ExceptionClassLoader(bus), ExceptionClassCreator.class);
		bus.setExtension(new WrapperClassLoader(bus), WrapperClassCreator.class);
		bus.setExtension(new FactoryClassLoader(bus), FactoryClassCreator.class);
		bus.setExtension(new GeneratedNamespaceClassLoader(bus), NamespaceClassCreator.class);*/
		bus.setExtension(capturer, GeneratedClassClassLoaderCapture.class);
		return new SpringBus();
	}

    @Bean
    public Endpoint endpoint(@Qualifier(Bus.DEFAULT_BUS_ID) SpringBus bus, @Qualifier("HelloWorldServiceImpl") HelloWorldImpl helloWorld) {
        EndpointImpl endpoint =
                new EndpointImpl(bus, helloWorld);
        endpoint.publish("/helloworld");

        return endpoint;
    }
}