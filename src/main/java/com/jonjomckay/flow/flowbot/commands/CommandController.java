package com.jonjomckay.flow.flowbot.commands;

import com.jonjomckay.flow.flowbot.flow.FlowManager;
import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.client.flow.FlowClient;
import com.manywho.sdk.client.flow.FlowInitializationOptions;
import com.manywho.sdk.client.flow.entities.Input;
import lombok.experimental.var;

import javax.inject.Inject;
import javax.ws.rs.*;
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
    public void flow1(@BeanParam CommandRequest request) {
        var tenant = UUID.fromString(request.getText().split(" ")[1]);
        var id = UUID.fromString(request.getText().split(" ")[2]);

        startFlow(request, tenant, id, request.getText());
    }

    @POST
    @Path("/flow/{tenant}/{id}")
    public void flow2(@BeanParam CommandRequest request, @PathParam("tenant") UUID tenant, @PathParam("id") UUID id) {
        startFlow(request, tenant, id, request.getText());
    }

    private void startFlow(CommandRequest request, UUID tenant, UUID id, String text) {
        Input inputMessage = new Input();
        inputMessage.setContentType(ContentType.String);
        inputMessage.setContentValue(text);
        inputMessage.setName("Slack Input: Message");

        Input inputUser = new Input();
        inputUser.setContentType(ContentType.String);
        inputUser.setContentValue(request.getUser());
        inputUser.setName("Slack Input: User ID");

        FlowInitializationOptions flowInitializationOptions = new FlowInitializationOptions();
        flowInitializationOptions.addInput(inputMessage);
        flowInitializationOptions.addInput(inputUser);

        // Initialize the flow and execute the first invoke
        var state = flowClient.start(tenant, id, null, flowInitializationOptions);

        // Finally, we want to generate a Slack message from the page, and post it back
        flowManager.createSlackMessage(tenant, state, request.getChannel(), request.getResponseUrl());
    }
}
