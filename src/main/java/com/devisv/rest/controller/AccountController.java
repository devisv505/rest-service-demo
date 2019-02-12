package com.devisv.rest.controller;

import com.devisv.rest.Routing;
import com.devisv.rest.api.AccountApi;
import com.google.inject.Injector;
import io.javalin.Context;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Account Controller
 */
@Singleton
public class AccountController extends Routing<AccountController> {

    private final AccountApi api;

    private final Javalin javalin;

    @Inject
    public AccountController(Injector injector,
                             AccountApi api,
                             Javalin javalin) {
        super(injector);
        this.api = api;
        this.javalin = javalin;
    }

    /**
     * Get Account with certain UUID
     *
     * @param context
     */
    public void get(Context context) {
        context.json(
                api.getByUuid(context.pathParam(":uuid"))
        );

    }

    /**
     * Get all Accounts
     *
     * @param context
     */
    public void getAll(Context context) {
        context.json(
                api.getAll()
        );
    }

    @Override
    public void bindRoutes() {
        javalin.get("api/accounts/:uuid", ctx -> getController().get(ctx))
               .get("api/accounts", ctx -> getController().getAll(ctx));
    }

}
