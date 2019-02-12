package com.devisv.rest;

import com.devisv.rest.controller.AccountController;
import com.devisv.rest.controller.TransferController;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import io.javalin.Javalin;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import java.sql.SQLException;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<Routing> multibinder = Multibinder.newSetBinder(binder(), Routing.class);
        multibinder.addBinding().to(AccountController.class);
        multibinder.addBinding().to(TransferController.class);
    }

    /**
     * Provide DataSource implementation as H2 JdbcConnectionPool
     *
     * @param config
     * @return
     */
    @Provides
    @Singleton
    public DataSource dataSourceProvider(Config config) {
        return JdbcConnectionPool.create(
                config.getDBUrl(),
                config.getDBUser(),
                config.getDBPassword()
        );
    }

    @Provides
    @Singleton
    public JdbcTemplate jdbcTemplateProvider(DataSource dataSource) throws SQLException {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Provide Flyway
     *
     * @param dataSource
     * @return
     */
    @Provides
    @Singleton
    public Flyway flywayProvider(DataSource dataSource) {
        return Flyway.configure()
                     .dataSource(dataSource)
                     .load();
    }

    @Provides
    @Singleton
    public Javalin javalinProvider() {
        return Javalin.create();
    }
}
