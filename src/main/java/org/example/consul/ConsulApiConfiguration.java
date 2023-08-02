package org.example.consul;

import com.google.gson.GsonBuilder;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

public class ConsulApiConfiguration {
    public ConsulClient build(String url) {
        var gson = new GsonBuilder()
                .registerTypeAdapter(Key.class, new KeyDeserializer())
                .create();

        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder(gson))
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(ConsulClient.class))
                .logLevel(Logger.Level.FULL)
                .target(ConsulClient.class, url);
    }
}
