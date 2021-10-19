package org.mysoap;

import org.mysoap.domain.Fat;
import org.mysoap.repository.FatRepository;
import org.mysoap.repository.microstream.FatRepositoryMSImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
public class MysoapAppApplication {

	private static final Logger LOG = LoggerFactory.getLogger(MysoapAppApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MysoapAppApplication.class, args);
//		context.close();
//		System.exit(0);
	}

//	@Bean
//	public CommandLineRunner crudDemo(final FatRepository repository)
//	{
//		return (args) ->
//		{
//			repository.addFat(new Fat("fat1", "1"));
//			repository.addFat(new Fat("fat2", "2"));
//
//			final Consumer<Fat> logAll = fat -> LOG.info(fat.toString());
//
//			LOG.info("Our fats:");
//			repository.findAll().forEach(logAll);
//			LOG.info(" ");
//
//			LOG.info("Find some specific customer:");
//			repository.findByName("fat1").forEach(logAll);
//			LOG.info(" ");
//
//			LOG.info("Update name of all fats:");
//			repository.findAll().forEach(c -> c.setName("xyz"));
//			repository.storeAll();
//			repository.findAll().forEach(logAll);
//			LOG.info(" ");
//
////			LOG.info("Delete customers:");
////			repository.deleteAll();
////			repository.findAll().forEach(logAll);
////			LOG.info(" ");
//		};
//	}

}
