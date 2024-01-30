package com.kaf.bootless.config;

public final class Const {
    private Const() {
    }

    public static final String KAFKA_URL = "localhost:9092";
    public static final String TOPIC_NAME = "kafka_topic";
    public static final String TOPIC_PROP_CONF = "topic";
    public static final String SCHEMA_PROP_CONF = "SCHEMA";
    public static final String CONTEXT_PATH = "/";
    public static final String APP_BASE = ".";
    public static final int DEFAULT_PORT = 8080;

    public static final String SCHEMA_REGISTRY_URL = "localhost:8081";
}
