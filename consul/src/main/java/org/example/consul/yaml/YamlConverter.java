package org.example.consul.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Data;
import org.example.consul.KValue;
import org.example.consul.Key;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

@Data
public class YamlConverter {
    private final Key base;
    private final List<KValue> keyValues;

    public String toYaml() throws IOException {
        var keys = keyValues.stream()
                .map(it -> it.withKey(it.getKey().removePrefix(base)))
                .filter(it -> it.getKey().isProperty())
                .filter(it -> it.getKey().getDirectory().equals(Path.of("")))
                .sorted(Comparator.comparing(el -> el.getKey().name()))
                .toList();

        var objectMapper = new ObjectMapper(new YAMLFactory());
        var stringWriter = new StringWriter();

        var keyValueToMap = new KeyValueToMap(keys);
        objectMapper.writeValue(stringWriter, keyValueToMap.toMap());

        return stringWriter.toString();
    }
}
