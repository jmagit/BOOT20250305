package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.example.domains.contracts.proxies.CalculatorProxy;
import com.example.webservice.schema.AddRequest;
import com.example.webservice.schema.AddResponse;

@SpringBootApplication
public class DemoWsClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoWsClientApplication.class, args);
	}
	
	
	@Bean
	CommandLineRunner sinProxy(Jaxb2Marshaller marshaller) {
		return args -> {		
			WebServiceTemplate ws = new WebServiceTemplate(marshaller);
			var request = new AddRequest();
			request.setOp1(2);
			request.setOp2(3);
			var response = (AddResponse) ws.marshalSendAndReceive("http://localhost:8090/ws/calculator", 
					 request, new SoapActionCallback("http://example.com/webservices/schemas/calculator"));
			System.err.println("WebServiceTemplate --> " + response.getAddResult());
		};
	}

	@Bean
	CommandLineRunner conProxy(CalculatorProxy client) {
		return args -> { System.err.println("CalculatorProxy --> " + client.add(0.1, 0.2)); };
	}
}
