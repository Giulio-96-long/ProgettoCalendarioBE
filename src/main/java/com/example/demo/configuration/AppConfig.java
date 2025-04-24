package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync //Abilita le chiamate asincrone
public class AppConfig {

    @Bean
    Executor taskExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(5);  // Numero di thread base
	        executor.setMaxPoolSize(10);  // Numero massimo di thread
	        executor.setQueueCapacity(25); // Capacità della coda
	        executor.initialize();
	        return executor;
	    }
	
}
