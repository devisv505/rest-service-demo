package com.devisv.rest.controller;

import com.devisv.rest.Routing;
import com.devisv.rest.api.TransferApi;
import com.devisv.rest.dto.TransferRequestDto;
import com.google.inject.Injector;
import io.javalin.Context;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Transfer Controller
 */
@Singleton
public class TransferController extends Routing<TransferController> {

    private final TransferApi api;

    private final Javalin javalin;

    @Inject
    public TransferController(Injector injector,
                              TransferApi api,
                              Javalin javalin) {
        super(injector);
        this.api = api;
        this.javalin = javalin;
    }

    /**
     * Transfer with certain UUID
     *
     * @param context
     */
    public void get(Context context) {
        context.json(
                api.getByUuid(context.pathParam(":uuid"))
        );
    }

    /**
     * Get all Transfers
     *
     * @param context
     */
    public void getAll(Context context) {
        context.json(
                api.getAll()
        );
    }

    /**
     * Create new Transfer
     *
     * @param context
     */
    public void post(Context context) {

        TransferRequestDto requestDto = context.bodyAsClass(TransferRequestDto.class);

        context.json(
                api.create(requestDto)
        );
    }

    @Override
    public void bindRoutes() {
        javalin.get("api/transfers/:uuid", ctx -> getController().get(ctx))
               .get("api/transfers", ctx -> getController().getAll(ctx))
               .post("api/transfers", ctx -> getController().post(ctx));
    }

}
