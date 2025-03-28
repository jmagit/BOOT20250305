package com.example.application.proxies;

import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.example.domains.contracts.proxies.CalculatorProxy;
import com.example.webservice.schema.AddRequest;
import com.example.webservice.schema.AddResponse;
import com.example.webservice.schema.DivideRequest;
import com.example.webservice.schema.DivideResponse;
import com.example.webservice.schema.MultiplyRequest;
import com.example.webservice.schema.MultiplyResponse;
import com.example.webservice.schema.SubtractRequest;
import com.example.webservice.schema.SubtractResponse;

public class CalculatorProxyImpl extends WebServiceGatewaySupport implements CalculatorProxy {
	private static final WebServiceMessageCallback requestCallback = new SoapActionCallback("http://example.com/webservices/schemas/calculator");

	@Override
	public double add(double a, double b) {
		var request = new AddRequest();
		request.setOp1(a);
		request.setOp2(b);
		var response = (AddResponse) getWebServiceTemplate().marshalSendAndReceive(request, requestCallback);
		return response.getAddResult();
	}

	@Override
	public double subtract(double a, double b) {
		var request = new SubtractRequest();
		request.setOp1(a);
		request.setOp2(b);
		var response = (SubtractResponse) getWebServiceTemplate().marshalSendAndReceive(request, requestCallback);
		return response.getSubtractResult();
	}

	@Override
	public double multiply(double a, double b) {
		var request = new MultiplyRequest();
		request.setOp1(a);
		request.setOp2(b);
		var response = (MultiplyResponse) getWebServiceTemplate().marshalSendAndReceive(request, requestCallback);
		return response.getMultiplyResult();
	}

	@Override
	public double divide(double a, double b) {
		var request = new DivideRequest();
		request.setOp1(a);
		request.setOp2(b);
		var response = (DivideResponse) getWebServiceTemplate().marshalSendAndReceive(request, requestCallback);
		return response.getDivideResult();
	}

}
