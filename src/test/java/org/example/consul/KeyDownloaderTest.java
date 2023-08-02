package org.example.consul;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Java6Assertions.assertThat;

@WireMockTest
class KeyDownloaderTest {

    @Test
    void test_something_with_wiremock(WireMockRuntimeInfo wmRuntimeInfo) {
        // given
        aStubConsul();

        // and client
        var client = aClient(wmRuntimeInfo);

        // when
        var keys = client.findRecursive("dev/project-a");

        // then
        assertThat(keys)
                .contains(new KVValue(26, 26, 0, 0, new Key("dev/project-a/"), null))
                .contains(new KVValue(36, 36, 0, 0, new Key("dev/project-a/azure/values.yml"), "dmFsdWVz"));
    }

    @Test
    void verifyValue(WireMockRuntimeInfo wmRuntimeInfo) {
        // given
        aStubConsul();

        // and client
        var client = aClient(wmRuntimeInfo);

        // when
        var keys = client.findRecursive("dev/project-a");

        // then
        var key = findKey(keys, "dev/project-a/azure/values.yml");
        assertThat(key.getValueAsString()).isEqualTo("values");

    }

    @Test
    void verifyValueUtf(WireMockRuntimeInfo wmRuntimeInfo) {
        // given
        aStubConsul();

        // and client
        var client = aClient(wmRuntimeInfo);

        // when
        var keys = client.findRecursive("dev/project-a");

        // then
        var key = findKey(keys, "dev/project-a/spring.name");
        assertThat(key.getValueAsString()).isEqualTo("spring-name ĄŁĘĆŻŃ");

    }

    private KVValue findKey(List<KVValue> keys, String name) {
        return keys.stream()
                .filter(it -> it.getKey().equals(new Key(name)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find key " + name));
    }


    private ConsulClient aClient(WireMockRuntimeInfo wmRuntimeInfo) {
        var config = new ConsulApiConfiguration();
        return config.build(wmRuntimeInfo.getHttpBaseUrl());
    }

    private void aStubConsul() {
        stubFor(get("/v1/kv/dev/project-a?recurse").willReturn(ok("""
                [
                    {
                        "LockIndex": 0,
                        "Key": "dev/project-a/",
                        "Flags": 0,
                        "Value": null,
                        "CreateIndex": 26,
                        "ModifyIndex": 26
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
                        "Key": "dev/project-a/database.key",
                        "Flags": 0,
                        "Value": "amRiYzpvcmFjbGU=",
                        "CreateIndex": 27,
                        "ModifyIndex": 27
                    },
                    {
                        "LockIndex": 0,
                        "Key": "dev/project-a/database.logging.level",
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
                        "Key": "dev/project-a/spring.name",
                        "Flags": 0,
                        "Value": "c3ByaW5nLW5hbWUgxITFgcSYxIbFu8WD",
                        "CreateIndex": 30,
                        "ModifyIndex": 30
                    }
                ]
                """)));
    }
}