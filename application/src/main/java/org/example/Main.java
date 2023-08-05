package org.example;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.example.consul.ConsulConfiguration;
import org.example.consul.persist.FilePersist;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws IOException {
        var settings = new SettingLoader().load();

        var textIO = TextIoFactory.getTextIO();

        var consulHttp = getConsulHttp(textIO, settings);

        var consulPrefix = textIO.newStringInputReader()
                .withDefaultValue("dev/project-a")
                .read("consul directory");


        var terminal = textIO.getTextTerminal();
        terminal.print("Download %s from directory: %s".formatted(consulHttp, consulPrefix));

        var configure = new ConsulConfiguration();
        var api = configure.build(consulHttp);
        var values = api.findRecursive(consulPrefix);

        var persist = new FilePersist(Path.of("./application/export"), values);
        persist.persist();


        terminal.dispose("Done");
        textIO.newStringInputReader().withMinLength(0).read("\nPress enter to terminate...");

    }

    private static String getConsulHttp(TextIO textIO, Setting settings) {
        var sb = new StringBuilder();
        var it = 0;
        sb.append("Choose consul instance")
                .append(System.getProperty("line.separator"));

        for (var consul: settings.getConsul()) {
            sb.append(it++)
                    .append(" - ")
                    .append(consul)
                    .append(System.getProperty("line.separator"));
        }
        var id = textIO.newIntInputReader()
                .withDefaultValue(0)
                .withMinVal(0)
                .withMaxVal(settings.getConsul().size() - 1)
                .read(sb.toString());
        return settings.getConsul().get(id);
    }
}