package org.example.consul;

import org.example.consul.KVValue;
import org.example.consul.KVValueFile;
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
        var kvvalueFile = new KVValueFile(aKeyValue(), tempDir);

        // when
        kvvalueFile.createDirectoryOrFile();

        // then
        assertThat(Path.of(tempDir.toString(), "a", "b", "file.yml"))
                .isRegularFile()
                .hasContent("password");
    }

    private KVValue aKeyValue() {
        return new KVValue(1, 1, 1, 0, new Key("a/b/file.yml"), "cGFzc3dvcmQ=");
    }
}