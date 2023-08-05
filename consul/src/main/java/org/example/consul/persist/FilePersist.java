package org.example.consul.persist;

import org.example.consul.KValue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public record FilePersist(Path path, List<KValue> values) {
    public void persist() throws IOException {
        var kvFiles = values.stream()
                .map(el -> new KValueFile(el, path))
                .toList();

        for (var kvFile : kvFiles) {
            kvFile.createDirectoryOrFile();
        }
    }

}
