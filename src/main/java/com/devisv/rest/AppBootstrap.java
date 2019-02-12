package com.devisv.rest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.flywaydb.core.Flyway;

/**
 * Main Class
 */
public class AppBootstrap {

    public static void main(String[] args) {
        // Initialisation Guice
        Injector injector = Guice.createInjector(new AppModule());

        // Initialisation Migration
        Flyway flyway = injector.getInstance(Flyway.class);
        flyway.migrate();

        // Initialisation Web Server
        WebEntryPoint webEntryPoint = injector.getInstance(WebEntryPoint.class);
        webEntryPoint.boot(args);
    }

}
