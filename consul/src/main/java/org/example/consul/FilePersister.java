package org.example.consul;

import lombok.Data;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public record FilePersister(Path path, List<KVValue> values) {
    public void persist() throws IOException {
        var kvFiles = values.stream()
                .map(el -> new KVValueFile(el, path))
                .toList();

        for (var kvFile : kvFiles) {
            kvFile.createDirectoryOrFile();
        }
    }

}
