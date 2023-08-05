package org.example.consul.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.consul.ConsulClient;
import org.example.consul.ConsulConfiguration;
import org.example.consul.KValue;
import org.example.consul.Key;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
class YamlConverterTest {

    @Test
    void createFilesAndDirectories(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
        // given
        var list = kvValueList(wmRuntimeInfo, YamlModel.consulDefinition);
        var aaa = new YamlConverter(new Key("dev/project-a/"), list);

        // when
        var yaml = aaa.toYaml();

        // then
        var objectMapper = new ObjectMapper(new YAMLFactory());
        var restoredModel = objectMapper.readValue(yaml, YamlModel.class);
        assertThat(yaml).isNotEmpty();
    }

    @Test
    void simpleMap(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
        // given
        var list = kvValueList(wmRuntimeInfo, SimpleModel.consulDefinition);
        var converter = new YamlConverter(new Key("dev/project-a/"), list);

        // when
        var yaml = converter.toYaml();

        // then
        var objectMapper = new ObjectMapper(new YAMLFactory());
        var restoredModel = objectMapper.readValue(yaml, SimpleModel.class);
        assertThat(restoredModel).isEqualTo(new SimpleModel("DEBUG", 10, new SimpleModel.NestedObject("DEBUG")));
    }

    private List<KValue> kvValueList(WireMockRuntimeInfo wmRuntimeInfo, String body) {
        aStubConsul(body);
        var client = aClient(wmRuntimeInfo);
        return client.findRecursive("dev/project-a");
    }

    private ConsulClient aClient(WireMockRuntimeInfo wmRuntimeInfo) {
        var config = new ConsulConfiguration();
        return config.build(wmRuntimeInfo.getHttpBaseUrl());
    }

    private void aStubConsul(String body) {
        stubFor(get("/v1/kv/dev/project-a?recurse").willReturn(ok(body)));
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class SimpleModel {
        static String consulDefinition = """
                [
                    {
                        "LockIndex": 0,
                        "Key": "dev/project-a/name",
                        "Flags": 0,
                        "Value": "REVCVUc=",
                        "CreateIndex": 33,
                        "ModifyIndex": 33
                    },
                    {
                        "LockIndex": 0,
                        "Key": "dev/project-a/nestedObject.name",
                        "Flags": 0,
                        "Value": "REVCVUc=",
                        "CreateIndex": 33,
                        "ModifyIndex": 33
                    },
                    {
                        "LockIndex": 0,
                        "Key": "dev/project-a/ctr",
                        "Flags": 0,
                        "Value": "MTA=",
                        "CreateIndex": 32,
                        "ModifyIndex": 32
                    }
                ]
                """;


        private String name;
        private Integer ctr;
        private NestedObject nestedObject;

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        static class NestedObject {
            private String name;
        }
    }

    @Data
    static class YamlModel {
        static String consulDefinition = """
                [
                    {
                        "LockIndex": 0,
                        "Key": "dev/project-a/database.loggingLevel",
                        "Flags": 0,
                        "Value": "REVCVUc=",
                        "CreateIndex": 33,
                        "ModifyIndex": 33
                    },
                    {
                        "LockIndex": 0,
                        "Key": "dev/project-a/database.password",
                        "Flags": 0,
                        "Value": "cGFzc3dvcmQ=",
                        "CreateIndex": 32,
                        "ModifyIndex": 32
                    },
                    {
                        "LockIndex": 0,
                        "Key": "dev/project-a/springName",
                        "Flags": 0,
                        "Value": "c3ByaW5nLW5hbWUgxITFgcSYxIbFu8WD",
                        "CreateIndex": 30,
                        "ModifyIndex": 30
                    },
                    {
                        "LockIndex": 0,
                        "Key": "dev/project-a/azure/",
                        "Flags": 0,
                        "Value": null,
                        "CreateIndex": 35,
                        "ModifyIndex": 35
                    },
                    {
                        "LockIndex": 0,
                        "Key": "dev/project-a/azure/values.yml",
                        "Flags": 0,
                        "Value": "dmFsdWVz",
                        "CreateIndex": 36,
                        "ModifyIndex": 36
                    },
                    {
                        "LockIndex": 0,
                        "Key": "dev/project-a/table[1].name",
                        "Flags": 0,
                        "Value": "c3ByaW5nLW5hbWUgxITFgcSYxIbFu8WD",
                        "CreateIndex": 36,
                        "ModifyIndex": 36
                    },
                    {
                        "LockIndex": 0,
                        "Key": "dev/project-a/table[0].level",
                        "Flags": 0,
                        "Value": "dmFsdWVz",
                        "CreateIndex": 36,
                        "ModifyIndex": 36
                    },
                    {
                        "LockIndex": 0,
                        "Key": "dev/project-a/table[0].name",
                        "Flags": 0,
                        "Value": "dmFsdWVz",
                        "CreateIndex": 36,
                        "ModifyIndex": 36
                    },
                    {
                        "LockIndex": 0,
                        "Key": "dev/project-a/table[1].level",
                        "Flags": 0,
                        "Value": "dmFsdWVz",
                        "CreateIndex": 36,
                        "ModifyIndex": 36
                    }
                ]
                """;
        private Database database;
        private String springName;
        private List<Table> table;

        @Data
        static class Database {
            private String loggingLevel;
            private String password;
        }

        @Data
        static class Table {
            private String name;
            private String level;
        }
    }
}