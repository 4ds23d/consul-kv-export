package org.example;

import org.example.consul.ConsulApiConfiguration;
import org.example.consul.FilePersister;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {

        var configure = new ConsulApiConfiguration();
        var api = configure.build("http://localhost:8500");
        var values = api.findRecursive("dev/project-a");

        var persist = new FilePersister(Path.of("."), values);
        persist.persist();
    }
}