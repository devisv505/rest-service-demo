package com.devisv.rest;

import com.devisv.rest.exception.HttpException;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * The entry point of starting a web server
 */
@Singleton
public class WebEntryPoint {

    private final Javalin app;

    private final Config config;

    private final Set<Routing> routes;

    @Inject
    public WebEntryPoint(Javalin app,
                         Config config,
                         Set<Routing> routes) {
        this.app = app;
        this.config = config;
        this.routes = routes;

        // handle NotFoundExceptions
        app.exception(HttpException.class, (e, ctx) -> {
            ctx.status(e.getHttpStatus());
            ctx.json(e);
        });
    }

    public void boot(String[] args) {
        app.start(config.getServerPort());

        routes.forEach(r -> r.bindRoutes());
    }
}
