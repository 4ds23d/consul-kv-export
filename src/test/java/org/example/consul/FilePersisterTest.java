package org.example.consul;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.commons.fileupload.MultipartStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
class FilePersisterTest {

    @Test
    void createFilesAndDirectories(@TempDir Path tempDir, WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
        // given
        var list = kvValueList(wmRuntimeInfo);
        var persister = new FilePersister(tempDir, list);

        // when
        persister.persist();

        // then
        assertThat(Path.of(tempDir.toString(), "dev", "project-a")).exists().isDirectory();
        assertThat(Path.of(tempDir.toString(), "dev", "project-a", "database.key")).exists().hasContent("jdbc:oracle");
    }

    private List<KVValue> kvValueList(WireMockRuntimeInfo wmRuntimeInfo) {
        aStubConsul();
        var client = aClient(wmRuntimeInfo);
        return client.findRecursive("dev/project-a");
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