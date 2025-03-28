package com.example;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.example.exceptions.BadRequestException;
import com.example.exceptions.DuplicateKeyException;
import com.example.exceptions.InvalidDataException;
import com.example.exceptions.NotFoundException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RestControllerAdvice
public class ApiExceptionHandler {
	private final Log log = LogFactory.getLog(getClass().getName());

	// https://datatracker.ietf.org/doc/html/rfc7807
	// Content-Type: application/problem+json
	@JsonInclude(value = Include.NON_EMPTY)
	public static class ErrorMessage extends ProblemDetail {
		private Map<String, String> errors;

		public ErrorMessage(int status, String title) {
			this(status, title, null, null);
		}

		public ErrorMessage(int status, String title, String detail) {
			this(status, title, detail, null);
		}

		public ErrorMessage(int status, String title, Map<String, String> errors) {
			this(status, title, null, errors);
		}

		public ErrorMessage(int status, String title, String detail, Map<String, String> errors) {
			this(getTypeURL(status), status, title, detail, errors);
		}

		public ErrorMessage(String type, int status, String title, String detail, Map<String, String> errors) {
			super(status);
			try {
				this.setType(new URI(type));
			} catch (URISyntaxException e) {
				try {
					this.setType(new URI("about:blank"));
				} catch (URISyntaxException e1) {
				}
			}
			this.setTitle(title); 
			this.setDetail(detail);
			this.errors = errors;
		}
		public Map<String, String> getErrors() {
			return errors;
		}

		public static String getTypeURL(int status) {
			switch (status) {
			case 400:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.1";
			case 401:
				return "https://datatracker.ietf.org/doc/html/rfc7235#section-3.1";
			case 402:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.2";
			case 403:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.3";
			case 404:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.4";
			case 405:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.5";
			case 406:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.6";
			case 407:
				return "https://datatracker.ietf.org/doc/html/rfc7235#section-3.2";
			case 408:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.7";
			case 409:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.8";
			case 410:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.9";
			case 411:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.10";
			case 412:
				return "https://datatracker.ietf.org/doc/html/rfc7232#section-4.2";
			case 413:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.11";
			case 414:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.12";
			case 415:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.13";
			case 416:
				return "https://datatracker.ietf.org/doc/html/rfc7233#section-4.4";
			case 417:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.14";
			case 426:
				return "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.15";
			default:
				return "about:blank";
			}
		}
	}

	@ExceptionHandler({ ErrorResponseException.class, ResponseStatusException.class, HttpRequestMethodNotSupportedException.class })
	public ProblemDetail defaultResponse(ErrorResponse exception) {
		return exception.getBody();
	}

//	@ExceptionHandler({ NotFoundException.class })
//	@ResponseStatus(HttpStatus.NOT_FOUND)
//	public ErrorMessage notFoundRequest(Exception exception) {
//		return new ErrorMessage(404, exception.getMessage(), ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString(), null);
//	}

	@ExceptionHandler({ NotFoundException.class })
	public ProblemDetail notFoundRequest(Exception exception) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler({ BadRequestException.class, DuplicateKeyException.class, HttpMessageNotReadableException.class })
	public ProblemDetail badRequest(Exception exception) {
		log.error("Bad Request exception", exception);
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
	}
	@ExceptionHandler({ InvalidDataException.class, MethodArgumentNotValidException.class })
	public ProblemDetail invalidData(Exception exception) {
		log.error("Invalid Data exception", exception);
		var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Datos invalidos");
		if (exception instanceof InvalidDataException ex) {
			problem.setProperty("errors", ex.hasErrors() ? ex.getErrors() : exception.getMessage());
		} else if (exception instanceof BindException ex && ex.hasFieldErrors()) {
			var errors = new HashMap<String, String>();
			ex.getFieldErrors().forEach(item -> errors.put(item.getField(), item.getDefaultMessage()));
			problem.setProperty("errors", errors);
		}
		return problem;
	}

	@ExceptionHandler({ Exception.class })
	public ProblemDetail unknow(Exception exception) {
		log.error("Unhandled exception", exception);
		return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
	}

}
