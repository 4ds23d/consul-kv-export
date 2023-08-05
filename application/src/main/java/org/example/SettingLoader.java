package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Path;

class SettingLoader {
    Setting load() throws IOException {
        var file = Path.of("application.yml").toFile();

        if (!file.isFile()) {
            throw new RuntimeException("Missing configuration file " + file.toPath().toAbsolutePath());
        }

        var objectMapper = new ObjectMapper(new YAMLFactory());
        return objectMapper.readValue(file, Setting.class);
    }
}
