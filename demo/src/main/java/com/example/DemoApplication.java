package com.example;

import java.util.TreeMap;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import com.example.domains.contracts.repositories.ActoresRepository;
import com.example.domains.contracts.services.ActoresService;
import com.example.util.Calculadora;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import jakarta.transaction.Transactional;

@OpenAPIDefinition(
        info = @Info(title = "Microservicio: Demos",  version = "1.0",
                description = "**Demos** de Microservicios.",
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(name = "Javier Martín", url = "https://github.com/jmagit", email = "support@example.com")
        ),
        externalDocs = @ExternalDocumentation(description = "Documentación del proyecto", url = "https://github.com/jmagit/curso")
)
@SpringBootApplication
//@ComponentScan(basePackages = "com.example.ioc")
@EnableFeignClients
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		
	}
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		System.err.println("Aplicacion arrancada");
		ejemplosDatos();
	}
	
	@Bean
	public OpenApiCustomizer sortSchemasAlphabetically() {
	    return openApi -> {
	        var schemas = openApi.getComponents().getSchemas();
	        openApi.getComponents().setSchemas(new TreeMap<>(schemas));
	    };
	}
	
	@Bean
	@Primary
	RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplateLB(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@LoadBalanced
	@Bean
	RestClient.Builder restClientBuilderLB() {
		return RestClient.builder();
	}

	@Primary
	@Bean
	RestClient.Builder restClientBuilder() {
		return RestClient.builder();
	}

	@Autowired
	private ActoresRepository dao;

	@Autowired
	private ActoresService srv;

	private void ejemplosDatos() {
		// var actor = new Actor(0, "Peoito", "Grillo");
		// dao.save(actor);
//		var item = dao.findById(204);
//		if(item.isPresent()) {
//			var actor = item.get();
//			actor.setFirstName("Pepito");
//			actor.setLastName(actor.getLastName().toUpperCase());
//			dao.save(actor);
//		} else {
//			System.err.println("No se ha encontrado el actor");
//		}
//		dao.findAll().forEach(System.err::println);
//		dao.deleteById(204);
//		dao.findAll().forEach(System.err::println);
//		dao.findTop5ByFirstNameStartingWithOrderByLastNameDesc("P").forEach(System.err::println);
//		dao.findTop5ByFirstNameStartingWith("P", Sort.by("FirstName").ascending()).forEach(System.err::println);
//		dao.findByActorIdGreaterThan(200).forEach(System.err::println);
//		dao.findNovedadesJPQL(200).forEach(System.err::println);
//		dao.findNovedadesSQL(200).forEach(System.err::println);
//		dao.findAll((root, query, builder) -> builder.lessThanOrEqualTo(root.get("actorId"), 5)).forEach(System.err::println);
//		srv.getAll().forEach(System.err::println);
//		var item = srv.getOne(1);
//		if(item.isPresent()) {
//			var actor = item.get();
//			System.err.println(item + "\nPeliculas");
////			actor.getFilmActors().forEach(fa -> System.err.println(fa.getFilm().getTitle()));
//			
//		} else {
//			System.err.println("No se ha encontrado el actor");
//		}
//		 var actor = new Actor(0, null, "12345678Z");
//		 if(actor.isValid())
//			 dao.save(actor);
//		 else {
//			System.err.println(actor.getErrorsMessage());
//		}
//		dao.findAll().forEach(o -> System.err.println(ActorDTO.from(o)));
//		dao.queryByActorIdGreaterThan(200).forEach(System.err::println);
//		dao.getByActorIdGreaterThan(200).forEach(o -> System.err.println(o.getId() + " " + o.getNombre()));
//		dao.findByActorIdGreaterThan(200).forEach(System.err::println);
//		dao.findByActorIdGreaterThan(200, ActorDTO.class).forEach(System.err::println);
//		dao.findByActorIdGreaterThan(200, ActorShort.class).forEach(o -> System.err.println(o.getId() + " " + o.getNombre()));
//		dao.findAll(PageRequest.of(1, 10, Sort.by("actorId"))).forEach(System.err::println);
//		dao.findAllBy(ActorShort.class);
//		dao.findAll();
	}
	
////	@Autowired //(required = false)
////	Servicio srv;
//	
//	@Autowired //(required = false)
////	@Qualifier("verdad")
//	Repositorio repo1;
//	@Autowired //(required = false)
////	@Qualifier("mentira")
//	Repositorio repo2;
////	@Autowired //(required = false)
////	Repositorio repo;
//
//	@Value("${mi.valor:valor por defecto}")
//	String valor;
//	
//	@Autowired
//	Rango rango;
//	
//	private void ejemplosIOC() {
//		//Servicio srv = new Servicio(new Repositorio(new Configuracion()));
//		//AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
//
////		srv.guardar();
////		repo.guardar();
//		repo1.guardar();
//		repo2.guardar();
//		System.err.println("Valor: " + valor);
//		System.err.println("Rango: " + rango);
//	}
	
	private void ejemplosPruebas() {
		var calc = new Calculadora();
		System.err.println("Suma: " + calc.suma(2, 3));
	}
	
//	@Bean
//  	CommandLineRunner demo() {
//		return (args) -> {
//			System.err.println("Aplicacion arrancadaaaaaaaaaaaaaa");
//		};
//	}

}
