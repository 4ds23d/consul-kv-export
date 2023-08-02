package org.example.consul;

import lombok.Data;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Data
public
class FilePersister {
    private final Path path;
    private final List<KVValue> values;

    public void persist() throws IOException {
        var kvFiles = values.stream()
                .map(el -> new KVValueFile(el, path))
                .toList();

        for (var kvFile: kvFiles) {
            kvFile.createDirectoryOrFile();
        }
    }

}
