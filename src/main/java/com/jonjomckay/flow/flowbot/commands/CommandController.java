package com.jonjomckay.flow.flowbot.commands;

import com.jonjomckay.flow.flowbot.flow.FlowManager;
import com.manywho.sdk.client.flow.FlowClient;
import lombok.experimental.var;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/commands")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class CommandController {
    private final FlowClient flowClient;
    private final FlowManager flowManager;

    @Inject
    public CommandController(FlowClient flowClient, FlowManager flowManager) {
        this.flowClient = flowClient;
        this.flowManager = flowManager;
    }

    @POST
    @Path("/flow")
    public void run(@BeanParam CommandRequest request) {
        var tenant = UUID.fromString(request.getText().split(" ")[1]);
        var id = UUID.fromString(request.getText().split(" ")[2]);

        // Initialize the flow and execute the first invoke
        var state = flowClient.start(tenant, id);

        // Finally, we want to generate a Slack message from the page, and post it back
        flowManager.createSlackMessage(tenant, state, request.getChannel(), request.getResponseUrl());
    }
}
