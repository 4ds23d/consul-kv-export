package org.example.consul.persist;

import org.example.consul.KValue;
import org.example.consul.Key;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class KVValueFileTest {

    @Test
    void persistDirectoryPath(@TempDir Path tempDir) throws IOException {
        // given
        var kvvalueFile = new KValueFile(aKeyValue(), tempDir);

        // when
        kvvalueFile.createDirectoryOrFile();

        // then
        assertThat(Path.of(tempDir.toString(), "a", "b", "file.yml"))
                .isRegularFile()
                .hasContent("password");
    }

    private KValue aKeyValue() {
        return new KValue(1, 1, 1, 0, new Key("a/b/file.yml"), "cGFzc3dvcmQ=");
    }
}