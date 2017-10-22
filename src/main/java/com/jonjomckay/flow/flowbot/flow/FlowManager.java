package com.jonjomckay.flow.flowbot.flow;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.model.Action;
import com.github.seratch.jslack.api.model.Attachment;
import com.github.seratch.jslack.api.webhook.Payload;
import com.google.common.collect.Lists;
import com.manywho.sdk.api.run.elements.ui.PageComponentDataResponse;
import com.manywho.sdk.api.run.elements.ui.PageComponentResponse;
import com.manywho.sdk.api.run.elements.ui.PageResponse;
import com.manywho.sdk.client.flow.FlowState;
import lombok.experimental.var;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FlowManager {

    public void createSlackMessage(UUID tenant, FlowState state, String channel, String responseUrl) {
        var mapElementInvokeResponse = state.getInvokeResponse().getMapElementInvokeResponses().get(0);

        // Find the first presentation element on the page
        var presentation = mapElementInvokeResponse.getPageResponse().getPageComponentResponses().stream()
                .filter(component -> component.getComponentType().equalsIgnoreCase("PRESENTATION"))
                .sorted(Comparator.comparingInt(PageComponentResponse::getOrder))
                .map(component -> getComponentContent(mapElementInvokeResponse.getPageResponse(), component.getId()))
                .findFirst()
                .orElse("");

        // Map all the outcomes without page bindings to button actions
        var actions = mapElementInvokeResponse.getOutcomeResponses().stream()
                .filter(outcomeResponse -> outcomeResponse.getPageObjectBindingId() == null)
                .map(outcomeResponse -> Action.builder()
                        .name("outcome")
                        .text(outcomeResponse.getLabel())
                        .type(Action.Type.BUTTON)
                        .value(outcomeResponse.getId().toString())
                        .build())
                .collect(Collectors.toList());

        // Convert the presentation component's content to Slack's pseudo-Markdown
        String text = convertNodes(Jsoup.parse(presentation).body().childNodes());

        var attachment = Attachment.builder()
                .callbackId(String.format("%s|%s", tenant, state.getInvokeResponse().getStateId()))
                .actions(actions)
                .fallback(text)
                .build();

        var message = Payload.builder()
                .attachments(Lists.newArrayList(
                        attachment
                ))
                .channel(channel)
                .text(text)
                .build();

        try {
            Slack.getInstance().send(responseUrl, message);
        } catch (IOException e) {
            throw new RuntimeException("Unable to post the message to Slack", e);
        }
    }

    private static String getComponentContent(PageResponse page, UUID id) {
        return page.getPageComponentDataResponses().stream()
                .filter(componentData -> componentData.getPageComponentId().equals(id))
                .map(PageComponentDataResponse::getContent)
                .findFirst()
                .orElse("");
    }

    private static String convertNodes(List<Node> nodes) {
        StringBuilder text = new StringBuilder();

        for (var node : nodes) {
            text.append(convertNode(node));
        }

        return text.toString();
    }

    private static String convertNode(Node node) {
        if (node instanceof TextNode) {
            return ((TextNode) node).text();
        }

        switch (node.nodeName()) {
            case "a":
                return "<" + node.attr("href") + "|" + convertNodes(node.childNodes()) + ">";
            case "strong":
            case "b":
                return "*" + convertNodes(node.childNodes()) + "*";
            case "h1":
            case "h2":
            case "h3":
            case "h4":
            case "h5":
            case "h6":
                return "*" + convertNodes(node.childNodes()) + "*\n";
            case "em":
            case "i":
                return "_" + convertNodes(node.childNodes()) + "_";
            case "br":
                return "\n";
            default:
                return convertNodes(node.childNodes());
        }
    }
}
