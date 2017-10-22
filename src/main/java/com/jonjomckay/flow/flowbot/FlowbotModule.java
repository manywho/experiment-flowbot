package com.jonjomckay.flow.flowbot;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.manywho.sdk.api.jackson.ObjectMapperFactory;
import com.manywho.sdk.client.run.RunClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class FlowbotModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RunClient.class).toProvider(() -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(JacksonConverterFactory.create(ObjectMapperFactory.create()))
                    .baseUrl("https://flow.manywho.com")
                    .build();

            return retrofit.create(RunClient.class);
        }).in(Singleton.class);
    }
}