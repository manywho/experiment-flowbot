package com.jonjomckay.flow.flowbot;

import com.codahale.metrics.health.HealthCheck;

public class FlowbotHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
