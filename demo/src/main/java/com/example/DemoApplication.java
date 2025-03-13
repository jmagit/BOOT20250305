package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;

import com.example.domains.contracts.repositories.ActoresRepository;
import com.example.domains.entities.Actor;
import com.example.ioc.Configuracion;
import com.example.ioc.Rango;
import com.example.ioc.Repositorio;
import com.example.ioc.Servicio;
import com.example.util.Calculadora;

@SpringBootApplication
//@ComponentScan(basePackages = "com.example.ioc")
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		
	}
	
	@Override
	public void run(String... args) throws Exception {
		System.err.println("Aplicacion arrancada");
		ejemplosDatos();
	}
	
	@Autowired
	private ActoresRepository dao;
	
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
		dao.findAll((root, query, builder) -> builder.lessThanOrEqualTo(root.get("actorId"), 5)).forEach(System.err::println);
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
