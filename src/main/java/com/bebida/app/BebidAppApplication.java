package com.bebida.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BebidAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BebidAppApplication.class, args);
        System.out.println("==============================================");
        System.out.println("Aplicación BebidApp iniciada!!");
        System.out.println("URL: http://127.0.0.1:8080/bebidapp");
        System.out.println("API REST: http://127.0.0.1:8080/bebidapp");
        System.out.println("==============================================");
    }

}
