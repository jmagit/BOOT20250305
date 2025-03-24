package com.example.application.resources;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.example.application.models.PelisDto;
import com.example.application.models.PhotoDTO;
import com.example.application.proxies.CatalogoProxy;
import com.example.application.proxies.PhotoProxy;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.observation.annotation.Observed;
//import io.micrometer.tracing.Tracer;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Ejemplos de conexiones
 * @author Javier
 *
 */
@Observed
@RefreshScope
@RestController
@RequestMapping(path = "/cotilla")
public class CotillaResource {
	@Autowired
	private EurekaClient discoveryEurekaClient;

	@GetMapping(path = "/1/descubre/eureka/{nombre}")
	public InstanceInfo serviceEurekaUrl(String nombre) {
	    InstanceInfo instance = discoveryEurekaClient.getNextServerFromEureka(nombre, false);
	    return instance; //.getHomePageUrl();
	}
	
	@Autowired
	private DiscoveryClient discoveryClient;

	@GetMapping(path = "/1/descubre/cloud/{nombre}")
	public List<ServiceInstance> serviceUrl(String nombre) {
	    return discoveryClient.getInstances(nombre);
	}
	
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	@LoadBalanced
	RestTemplate restTemplateLB;

	@GetMapping(path = "/21/consulta/pelis/rt")
	public List<PelisDto> getPelisRT(@RequestParam(defaultValue = "http")
	@Parameter(in = ParameterIn.QUERY, name = "mode", required = false, description = "http: Directo a URL, lb: Balanceo de carga", 
	schema = @Schema(type = "string", allowableValues = {"http", "lb" }, defaultValue = "http")) String mode) {
		ResponseEntity<List<PelisDto>> response = ("http".equals(mode)?restTemplate:restTemplateLB).exchange(
				("http".equals(mode)?"http://localhost:8010":"lb://CATALOGO-SERVICE") + "/peliculas/v1?mode=short", 
				HttpMethod.GET,
				HttpEntity.EMPTY, 
				new ParameterizedTypeReference<List<PelisDto>>() {}
		);
		return response.getBody();
	}
	@GetMapping(path = "/22/consulta/pelis/{id}/rt")
	public PelisDto getPelisRT(@PathVariable int id, @RequestParam(defaultValue = "http")
	@Parameter(in = ParameterIn.QUERY, name = "mode", required = false, description = "http: Directo a URL, lb: Balanceo de carga", 
			schema = @Schema(type = "string", allowableValues = {"http", "lb" }, defaultValue = "http")) String mode) {
		if("http".equals(mode))
			return restTemplate.getForObject("http://localhost:8010/peliculas/v1/{key}?mode=short", PelisDto.class, id);
		else
			return restTemplateLB.getForObject("lb://CATALOGO-SERVICE/peliculas/v1/{key}?mode=short", PelisDto.class, id);
	}
	@GetMapping(path = "/31/balancea/rt")
	@SecurityRequirement(name = "bearerAuth")
	public List<String> getBalanceoRT() {
		List<String> rslt = new ArrayList<>();
		LocalDateTime inicio = LocalDateTime.now();
		rslt.add("Inicio: " + inicio);
		for(int i = 0; i < 11; i++)
			try {
				LocalTime ini = LocalTime.now();
				rslt.add(restTemplateLB.getForObject("lb://CATALOGO-SERVICE/actuator/info", String.class)
						+ " (" + ini.until(LocalTime.now(), ChronoUnit.MILLIS) + " ms)" );
			} catch (Exception e) {
				rslt.add(e.getMessage());
			}
		LocalDateTime fin = LocalDateTime.now();
		rslt.add("Final: " + fin + " (" + inicio.until(fin, ChronoUnit.MILLIS) + " ms)");		
		return rslt;
	}
	
	@Autowired
	CatalogoProxy proxy;

	@GetMapping(path = "/23/consulta/pelis/proxy")
	public List<PelisDto> getPelisProxy() {
		return proxy.getPelis();
	}
//	@PreAuthorize("hasRole('ADMINISTRADORES')")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping(path = "/24/consulta/pelis/{id}/proxy")
	public PelisDto getPelisProxy(@PathVariable int id) {
		return proxy.getPeli(id);
	}
	@GetMapping(path = "/32/balancea/proxy")
	public List<String> getBalanceoProxy() {
		List<String> rslt = new ArrayList<>();
		for(int i = 0; i < 11; i++)
			try {
				rslt.add(proxy.getInfo());
			} catch (Exception e) {
				rslt.add(e.getMessage());
			}
		return rslt;
	}

    @Autowired
    private RestClient.Builder restClientBuilder;

    @Autowired
    @LoadBalanced
    private RestClient.Builder restClientBuilderLB;
	
	@GetMapping(path = "/25/consulta/pelis/rc")
	public List<PelisDto> getPelisRC(@RequestParam(defaultValue = "http")
	@Parameter(in = ParameterIn.QUERY, name = "mode", required = false, description = "http: Directo a URL, lb: Balanceo de carga", 
	schema = @Schema(type = "string", allowableValues = {"http", "lb" }, defaultValue = "http")) String mode) {
		if("http".equals(mode))
			return restClientBuilder.build().get().uri("http://localhost:8010/peliculas/v1?mode=short").retrieve().body(new ParameterizedTypeReference<List<PelisDto>>() {});
		else
			return restClientBuilderLB.build().get().uri("lb://CATALOGO-SERVICE/peliculas/v1?mode=short").retrieve().body(new ParameterizedTypeReference<List<PelisDto>>() {});
	}
	@GetMapping(path = "/26/consulta/pelis/{id}/rc")
	public PelisDto getPelisRC(@PathVariable int id, @RequestParam(defaultValue = "http")
	@Parameter(in = ParameterIn.QUERY, name = "mode", required = false, description = "http: Directo a URL, lb: Balanceo de carga", 
			schema = @Schema(type = "string", allowableValues = {"http", "lb" }, defaultValue = "http")) String mode) {
		RestClient restClient = ("http".equals(mode) ? restClientBuilder.baseUrl("http://localhost:8010").build(): restClientBuilderLB.baseUrl("lb://CATALOGO-SERVICE").build());
		return restClient.get().uri("peliculas/v1/{key}?mode=short", id).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, (request, response) -> { 
				      throw new ResponseStatusException(response.getStatusCode()); 
				  })
				.onStatus(HttpStatusCode::is5xxServerError, (request, response) -> { 
				      throw new ResponseStatusException(response.getStatusCode(), new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8)); 
				  })
				.body(PelisDto.class);
	}
	@GetMapping(path = "/33/balancea/rc")
	@SecurityRequirement(name = "bearerAuth")
	public List<String> getBalanceoRC() {
		List<String> rslt = new ArrayList<>();
		LocalDateTime inicio = LocalDateTime.now();
		var restClient = restClientBuilderLB.baseUrl("lb://CATALOGO-SERVICE").build();
		rslt.add("Inicio: " + inicio);
		for(int i = 0; i < 11; i++)
			try {
				LocalTime ini = LocalTime.now();
				rslt.add(restClient.get().uri("lb://CATALOGO-SERVICE/actuator/info").retrieve().body(String.class)
						+ " (" + ini.until(LocalTime.now(), ChronoUnit.MILLIS) + " ms)" );
			} catch (Exception e) {
				rslt.add(e.getClass().getCanonicalName() + ": " + e.getMessage());
			}
		LocalDateTime fin = LocalDateTime.now();
		rslt.add("Final: " + fin + " (" + inicio.until(fin, ChronoUnit.MILLIS) + " ms)");		
		return rslt;
	}

	@Autowired
	private CircuitBreakerFactory cbFactory;
	
	@GetMapping(path = "/41/circuit-breaker/factory")
	public List<String> getCircuitBreakerFactory() {
		List<String> rslt = new ArrayList<>();
		LocalDateTime inicio = LocalDateTime.now();
		rslt.add("Inicio: " + inicio);
		for(int i = 0; i < 100; i++) {
				LocalTime ini = LocalTime.now();
				rslt.add(cbFactory.create("slow").run(
						() -> restTemplateLB.getForObject("lb://CATALOGO-SERVICE/actuator/info", String.class), 
						throwable -> "fallback: circuito abierto")
						+ " (" + ini.until(LocalTime.now(), ChronoUnit.MILLIS) + " .ms)" );
			}
		LocalDateTime fin = LocalDateTime.now();
		rslt.add("Final: " + fin + " (" + inicio.until(fin, ChronoUnit.MILLIS) + " ms)");		
		return rslt;
	}
	
	@GetMapping(path = "/42/circuit-breaker/anota")
	@CircuitBreaker(name = "default", fallbackMethod = "fallback")
	public List<String> getCircuitBreakerAnota() {
		List<String> rslt = new ArrayList<>();
		LocalDateTime inicio = LocalDateTime.now();
		rslt.add("Inicio: " + inicio);
		for(int i = 0; i < 100; i++)
			rslt.add(getInfo(LocalTime.now()));
		LocalDateTime fin = LocalDateTime.now();
		rslt.add("Final: " + fin + " (" + inicio.until(fin, ChronoUnit.MILLIS) + " ms)");		
		return rslt;
	}
	
	@CircuitBreaker(name = "default", fallbackMethod = "fallback")
	private String getInfo(LocalTime ini) {
//		return restTemplateLB.getForObject("lb://CATALOGO-SERVICE/loteria", String.class)
//		+ " (" + ini.until(LocalTime.now(), ChronoUnit.MILLIS) + " ms)";
//		return restTemplate.getForObject("http://localhost:8010/actuator/info", String.class)
//						+ " (" + ini.until(LocalTime.now(), ChronoUnit.MILLIS) + " ms)";
		return restTemplateLB.getForObject("lb://CATALOGO-SERVICE/actuator/info", String.class)
				+ " (" + ini.until(LocalTime.now(), ChronoUnit.MILLIS) + " ms)";
	}
	private List<String> fallback(CallNotPermittedException e) {
		return List.of("CircuitBreaker is open", e.getCausingCircuitBreakerName(), e.getLocalizedMessage());
	}
	
	private String fallback(LocalTime ini, CallNotPermittedException e) {
		return "CircuitBreaker is open";
	}

	private String fallback(LocalTime ini, Exception e) {
		return "Fallback: " + e.getMessage()
						+ " (" + ini.until(LocalTime.now(), ChronoUnit.MILLIS) + " ms)";
	}

	@Value("${server.port}")
	int port;
	
	@GetMapping(path = "/loteria", produces = {"text/plain"})
	public String getIntento() {
		if(port == 8011)
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		return "OK " + port;
	}
	
	@Value("${valor.ejemplo:Valor por defecto}")
	String config;
	
	@GetMapping(path = "/config", produces = {"text/plain"})
	public String getConfig() {
		return config;
	}

	@Autowired
	PhotoProxy proxyExterno;
	@GetMapping(path = "/fotos")
	public List<PhotoDTO> getFotosProxy() {
		return proxyExterno.getAll();
	}
	
//	@Autowired
//	Tracer tracer;
	
//	@PreAuthorize("hasRole('ADMINISTRADORES')")
//	@PreAuthorize("authenticated")
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping(path = "/send/pelis/{id}/like")
//	@Observed(name = "enviar.megusta", contextualName = "enviar-megusta", lowCardinalityKeyValues = {"megustaType", "pelicula"})
	public String getPelisLike(@PathVariable int id, @Parameter(hidden = true) @RequestHeader(required = false) String authorization) {
//		var span = tracer.nextSpan().name("send-like").start();
		if(authorization == null)
			return proxy.meGusta(id);
		var result = proxy.meGusta(id, authorization);
//		span.end();
		return result;
	}
//	@PreAuthorize("hasRole('ADMINISTRADORES')")
//	@PreAuthorize("authenticated")
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping(path = "/send/pelis/like/info")
//	@Observed(name = "enviar.megusta", contextualName = "enviar-megusta", lowCardinalityKeyValues = {"megustaType", "pelicula"})
	public String getPelisLikeDirecto() {
		var result = restTemplateLB.getForObject("lb://MEGUSTA-SERVICE//actuator/health", String.class);
		return result;
	}
	
}
