package org.example.consul;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public record Key(String name) {
    public Key removePrefix(Key key) {
        if (key.isDirectory() && name.startsWith(key.name)) {
            return new Key(name.substring(key.name.length()));
        }
        return this;
    }

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

    public boolean isProperty() {
        return !isDirectory();
    }

}
