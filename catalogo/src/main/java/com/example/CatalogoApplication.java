package com.example;

import java.util.TreeMap;
import java.util.logging.Logger;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

import com.example.domains.event.DomainEvent;
import com.example.domains.event.EntityChangedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.transaction.Transactional;

@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
@EnableAspectJAutoProxy
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Microservicio: Catalogo de peliculas",
                version = "1.0",
                description = "Ejemplo de Microservicio utilizando la base de datos **Sakila**.",
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(name = "Javier Martín", url = "https://github.com/jmagit", email = "support@example.com")
        ),
        externalDocs = @ExternalDocumentation(description = "Documentación del proyecto", url = "https://github.com/jmagit/BOOT20250305/tree/main/catalogo")
)
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@EnableFeignClients
public class CatalogoApplication implements CommandLineRunner {
	private final Logger log = Logger.getLogger(getClass().getName());

	public static void main(String[] args) {
		SpringApplication.run(CatalogoApplication.class, args);
	}

    @Bean
    OpenApiCustomizer sortSchemasAlphabetically() {
        return openApi -> {
            var schemas = openApi.getComponents().getSchemas();
            openApi.getComponents().setSchemas(new TreeMap<>(schemas));
        };
    }
    
    // Application metrics: https://microservices.io/patterns/observability/application-metrics.html
    
//    @Bean
//    HttpExchangeRepository httpTraceRepository() {
//        return new InMemoryHttpExchangeRepository();
//    }
//
//    @Bean
//    AuditEventRepository auditEventRepository() {
//        return new InMemoryAuditEventRepository();
//    }
//    @Bean
//    AuthenticationEventPublisher authenticationEventPublisher
//            (ApplicationEventPublisher applicationEventPublisher) {
//        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
//    }
    
    // Domain events: https://microservices.io/patterns/data/domain-event.html

//	@Autowired
//	KafkaTemplate<String, String> kafkaTemplate;
//	
//	@Bean
//	NewTopic entityChangedTopic() {
//		return TopicBuilder.name("catalogo-events").partitions(1).replicas(1).build();
//	}
//	ObjectMapper converter = new ObjectMapper();
//
//	@EventListener
//	void evento(EntityChangedEvent event) throws JsonProcessingException {
//		kafkaTemplate.send("catalogo-events", event.entity(), converter.writeValueAsString(event))
//			.thenAccept(result -> log.warning(String.format("EVENT: %s OFFSET: %s", event, result.getRecordMetadata().offset())))
//			.exceptionally(ex -> {
//				log.severe(String.format("EVENT: %s ERROR: %s", event, ex.getMessage()));
//				return null;
//			});
//	}
//	
//	@EventListener
//	void evento(DomainEvent event) {
//		System.err.println(event);
//	}

//	@Autowired
//	FilmRepository dao;
//	@Autowired
//	FilmService srv;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		System.err.println("Aplicación arrancada...");
//		publisher.publishEvent("Aplicación arrancada...");
		log.info("Aplicación arrancada...");
//		var item = new Film(0, "Test", new Language(1), (byte)1, new BigDecimal(1.0), new BigDecimal(10.5));
//		item.addActor(new Actor(1));
//		item.addActor(new Actor(2));
//		item.addActor(new Actor(3));
//		item.addCategory(1);
//		item.addCategory(2);
//		dao.save(item);
//		System.err.println(item);
//		item = dao.findAll().getLast();
//		item.setTitle("KKKKKKK");
//		item.removeActor(new Actor(1));
//		item.addActor(new Actor(4));
//		item.removeCategory(new Category(2));
//		item.addCategory(3);
//		dao.save(item);
//		System.err.println(item);
//		item = dao.findAll().getLast();
//		dao.delete(item);
//		srv.getAll((root, query, builder) -> builder.lessThan(root.get("length"), 50)).forEach(System.out::println);
//		srv.getAll((root, query, builder) -> builder.and(
//				builder.equal(root.get("rating"), Rating.GENERAL_AUDIENCES),
//				builder.greaterThan(root.get("rentalDuration"), 5))
//			).forEach(System.out::println);
	}

}
