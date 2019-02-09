package com.devisv.rest;

import com.devisv.rest.config.Config;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;

public class AppModule implements Module {

    @Override
    public void configure(Binder binder) {

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
}
