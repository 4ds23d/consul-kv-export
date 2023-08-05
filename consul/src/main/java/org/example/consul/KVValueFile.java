package org.example.consul;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Slf4j
record KVValueFile(KVValue value, Path base) {

    public void createDirectoryOrFile() throws IOException {
        log.info("Create {}", value.getKey());
        var key = value.getKey();
        var directory = Path.of(base.toString(), key.getDirectory().toString()).toFile();
        directory.mkdirs();

        if (key.isFile()) {
            var file = Path.of(base.toString(), key.getName()).toFile();
            try (var fos = new FileOutputStream(file);
                 var osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 var writer = new BufferedWriter(osw)) {

                writer.append(value().getValueAsString());

            }
        }
    }
}
