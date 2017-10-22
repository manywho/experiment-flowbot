package com.jonjomckay.flow.flowbot.commands;

import javax.ws.rs.FormParam;

public class CommandRequest {
    @FormParam("channel_id")
    private String channel;

    @FormParam("response_url")
    private String responseUrl;

    @FormParam("text")
    private String text;

    @FormParam("token")
    private String token;

    @FormParam("trigger")
    private String trigger;

    public String getChannel() {
        return channel;
    }

    public String getResponseUrl() {
        return responseUrl;
    }

    public String getText() {
        return text;
    }

    public String getToken() {
        return token;
    }

    public String getTrigger() {
        return trigger;
    }
}
