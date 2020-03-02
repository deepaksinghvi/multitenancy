package com.tenancy.model.multitenancy;
/**
 * Multitenancy example with the single DB multiple schemas
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.tenancy.model*")
public class MultitenancyApplicationOp2 {

	public static void main(String[] args) {
		SpringApplication.run(MultitenancyApplicationOp2.class, args);
	}

}
