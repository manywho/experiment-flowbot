package com.jonjomckay.flow.flowbot.interactive;

import com.github.seratch.jslack.api.model.Action;
import com.github.seratch.jslack.api.model.Channel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InteractiveRequest {
    private List<Action> actions;

    @SerializedName("callback_id")
    private String callback;

    private Channel channel;

    @SerializedName("response_url")
    private String responseUrl;

    public List<Action> getActions() {
        return actions;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getCallback() {
        return callback;
    }

    public String getResponseUrl() {
        return responseUrl;
    }
}
