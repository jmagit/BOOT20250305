package com.example;

import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

import jakarta.transaction.Transactional;

@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
@EnableAspectJAutoProxy
@SpringBootApplication
public class CatalogoApplication implements CommandLineRunner {
	private final Logger log = Logger.getLogger(getClass().getName());

	public static void main(String[] args) {
		SpringApplication.run(CatalogoApplication.class, args);
	}

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
