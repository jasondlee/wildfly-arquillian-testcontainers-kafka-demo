package com.steeplesoft.watkdemo;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
@RequestScoped
public class MyResource {
    @Inject
    private MyService service;

    @POST
    public Response create(MyModel model) {
        return Response.ok(service.sendModel(model))
                .build();
    }
}
