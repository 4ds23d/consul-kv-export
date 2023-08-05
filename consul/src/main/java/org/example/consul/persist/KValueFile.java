package org.example.consul.persist;

import lombok.extern.slf4j.Slf4j;
import org.example.consul.KValue;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Slf4j
record KValueFile(KValue value, Path base) {

    void createDirectoryOrFile() throws IOException {
        log.info("Create {}", value.getKey());
        var key = value.getKey();
        var directory = Path.of(base.toString(), key.getDirectory().toString()).toFile();
        directory.mkdirs();

        if (key.isProperty()) {
            var file = Path.of(base.toString(), key.name()).toFile();
            try (var fos = new FileOutputStream(file);
                 var osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 var writer = new BufferedWriter(osw)) {

                writer.append(value().getValueAsString());

            }
        }
    }
}
