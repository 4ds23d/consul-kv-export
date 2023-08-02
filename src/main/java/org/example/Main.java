package org.example;

import org.example.consul.ConsulApiConfiguration;
import org.example.consul.ConsulClient;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        var configure = new ConsulApiConfiguration();
        var api = configure.build("http://localhost:8500");

        var keys = api.findKeys("");
        var values = api.findRecursive("");
    }
}