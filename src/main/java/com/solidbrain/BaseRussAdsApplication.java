package com.solidbrain;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableScheduling
public class BaseRussAdsApplication {

	public static void main(String[] args) {
		run(BaseRussAdsApplication.class, args);
	}

}
