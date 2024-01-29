package com.kaf.bootless.repository;

import org.apache.avro.Schema;

public class SchemaRepository {
    private static final String SCHEMA = "{\"type\":\"record\"," +
            "\"name\":\"myrecord\"," +
            "\"fields\":[{\"name\":\"msg\",\"type\":\"string\"}]}";

    private static final Schema SCHEMA_OBJECT = new Schema.Parser().parse(SCHEMA);

    private static SchemaRepository INSTANCE = new SchemaRepository();

    public static SchemaRepository instance() {
        return INSTANCE;
    }

    public Schema getSchemaObject() {
        return SCHEMA_OBJECT;
    }
}
