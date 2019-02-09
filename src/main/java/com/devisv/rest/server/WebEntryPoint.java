package com.devisv.rest.server;

import com.devisv.rest.config.Config;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The entry point of starting a web server
 */
@Singleton
public class WebEntryPoint {

    private final Javalin app;

    private final Config config;

    @Inject
    public WebEntryPoint(Javalin app, Config config) {
        this.app = app;
        this.config = config;
    }

    public void boot(String[] args) {
        app.port(config.getServerPort());
        app.start();
    }
}
