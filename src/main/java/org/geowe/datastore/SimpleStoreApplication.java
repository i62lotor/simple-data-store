package org.geowe.datastore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackageClasses = { SimpleStoreApplication.class})
@SpringBootApplication
public class SimpleStoreApplication /*implements CommandLineRunner*/ {

	public static void main(String[] args) {
		SpringApplication.run(SimpleStoreApplication.class, args);
	}

}
