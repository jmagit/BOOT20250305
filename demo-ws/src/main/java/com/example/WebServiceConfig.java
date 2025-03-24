package com.example;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
	@Bean
	ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean<>(servlet, "/ws/*");
	}

	@Bean(name = "calculator") // nombre del WSDL -> http://localhost:8080/ws/calculator.wsdl
	DefaultWsdl11Definition calculatorWsdl11Definition(XsdSchema calculatorSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("CalculatorPort");
		wsdl11Definition.setLocationUri("/ws/calculator"); // POST: http://localhost:8080/ws/calculator
		wsdl11Definition.setTargetNamespace("http://example.com/webservices/schemas/calculator");
		wsdl11Definition.setSchema(calculatorSchema);
		return wsdl11Definition;
	}

	@Bean
	XsdSchema calculatorSchema() {
		return new SimpleXsdSchema(new ClassPathResource("calculator.xsd"));
	}

}
