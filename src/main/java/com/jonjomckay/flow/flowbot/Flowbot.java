package com.jonjomckay.flow.flowbot;

import com.google.inject.Stage;
import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class Flowbot extends Application<FlowbotConfiguration> {
    public static void main(String[] args) throws Exception {
        new Flowbot().run(args);
    }

    @Override
    public void initialize(Bootstrap<FlowbotConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());

        bootstrap.addBundle(GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage().getName())
                .modules(new FlowbotModule())
                .build(Stage.DEVELOPMENT));
    }

    public void run(FlowbotConfiguration configuration, Environment environment) throws Exception {
        environment.healthChecks().register("default", new FlowbotHealthCheck());
    }
}
