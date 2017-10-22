package com.jonjomckay.flow.flowbot.interactive;

import com.google.gson.Gson;
import com.jonjomckay.flow.flowbot.flow.FlowManager;
import com.manywho.sdk.client.flow.FlowClient;
import lombok.experimental.var;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("interactive")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class InteractiveController {
    private final Gson gson;
    private final FlowClient flowClient;
    private final FlowManager flowManager;

    @Inject
    public InteractiveController(Gson gson, FlowClient flowClient, FlowManager flowManager) {
        this.gson = gson;
        this.flowClient = flowClient;
        this.flowManager = flowManager;
    }

    @POST
    public void run(@FormParam("payload") String payloadStream) {
        var request = gson.fromJson(payloadStream, InteractiveRequest.class);

        var tenant = UUID.fromString(request.getCallback().split("\\|")[0]);
        var id = UUID.fromString(request.getCallback().split("\\|")[1]);

        // We want to join the flow first, so we have the current state
        var state = flowClient.join(tenant, id);

        // If an outcome button was selected in Slack, we follow it and get the updated state
        var outcome = request.getActions().stream()
                .filter(action -> action.getName().equals("outcome"))
                .findFirst();

        if (outcome.isPresent()) {
            state = state.selectOutcome(UUID.fromString(outcome.get().getValue()));
        }

        // Finally, we want to generate a Slack message from the page, and post it back
        flowManager.createSlackMessage(tenant, state, request.getChannel().getId(), request.getResponseUrl());
    }
}
