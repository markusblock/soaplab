package org.soaplab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soaplab.domain.Fat;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.microstream.FatRepositoryMSImpl;
import org.soaplab.repository.microstream.MicrostreamRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

//@Configuration
//@ComponentScan
public class ApplicationConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfiguration.class);

//    @Bean
//    FatRepository getFatRepository(@Value("${microstream.store.location}") final String location){
//        return new FatRepositoryMSImpl(location);
//    }
//    
//    @Bean
//    MicrostreamRepository getRepository(@Value("${microstream.store.location}") final String location){
//        return new MicrostreamRepository(location);
//    }

}
