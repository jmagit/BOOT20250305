package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.example.application.proxies.CalculatorProxyImpl;
import com.example.domains.contracts.proxies.CalculatorProxy;

@Configuration
public class WSClientConfiguration {
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.example.webservice.schema");
		return marshaller;
	}
	@Bean
	public CalculatorProxy calculatorProxy(Jaxb2Marshaller marshaller) {
		var client = new CalculatorProxyImpl();
		client.setDefaultUri("http://localhost:8090/ws/calculator");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}
