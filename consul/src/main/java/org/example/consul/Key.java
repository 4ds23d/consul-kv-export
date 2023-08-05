package org.example.consul;

import lombok.Data;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

@Data
public class Key {
    private final String name;

    public Path getDirectory() {
        if (isDirectory()) {
            return Path.of(name);
        } else {
            var file = new File(name);
            return Optional.ofNullable(file.getParent())
                    .map(Path::of)
                    .orElse(Path.of(""));
        }
    }

    public boolean isDirectory() {
        var file = new File(name);
        return file.isDirectory() || name.endsWith("/");
    }

    public boolean isFile() {
        return !isDirectory();
    }
}
