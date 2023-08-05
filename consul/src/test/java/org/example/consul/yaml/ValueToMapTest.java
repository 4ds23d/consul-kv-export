package org.example.consul.yaml;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.MultipartStream;
import org.example.consul.KValue;
import org.example.consul.Key;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ValueToMapTest {

    @Test
    void onlyKeys() {
        // given
        var keys = List.of(
                aKey("a", "aa"),
                aKey("b", "sample")
                );

        // and
        var mapper = new KeyValueToMap(keys);

        // when
        var map = mapper.toMap();

        // then
        assertObjectTree(map, """
                {
                    "a": "aa",
                    "b": "sample"
                }
                """);
    }

    @Test
    void nestedKey() {
        // given
        var keys = List.of(
                aKey("a.b", "aa"),
                aKey("a.c", "sample")
        );

        // and
        var mapper = new KeyValueToMap(keys);

        // when
        var map = mapper.toMap();

        // then
        assertObjectTree(map, """
                {
                    "a": {
                        "b": "aa",
                        "c": "sample"
                    }
                }
                """);
    }

    @Test
    void list2() {
        // given
        var keys = List.of(
                aKey("a[0]", "aa"),
                aKey("a[1]", "sample")
        );

        // and
        var mapper = new KeyValueToMap(keys);

        // when
        var map = mapper.toMap();

        // then
        assertObjectTree(map, """
                {
                    a: ["aa", "sample"]
                }
                """);
    }


    @Test
    void list() {
        // given
        var keys = List.of(
                aKey("a[0].name", "aa"),
                aKey("a[0].value", "sample")
        );

        // and
        var mapper = new KeyValueToMap(keys);

        // when
        var map = mapper.toMap();

        // then
        assertObjectTree(map, """
                {
                    a: [
                        {
                        "name": aa,
                        "value": "sample"
                        }
                    ]
                }
                """);
    }

    @Test
    void combineTest() {
        // given
        var keys = List.of(
                aKey("a[0].person.name[0]", "bartek"),
                aKey("a[0].person.name[1]", "ala"),
                aKey("a[1].person.name[0]", "franek")
        );

        // and
        var mapper = new KeyValueToMap(keys);

        // when
        var map = mapper.toMap();

        // then
        assertObjectTree(map, """
                {
                    a: [
                        {
                        "person":  {
                               "name": ["bartek", "ala"]
                           }
                        },
                        {
                        "person":  {
                               "name": ["franek"]
                           }
                        }
                    ]
                }
                """);
    }

    @Test
    void twoDimensionList() {
        // given
        var keys = List.of(
                aKey("a[0][0].person", "bartek"),
                aKey("a[0][1].person", "ala"),
                aKey("a[1][0].person", "franek")
        );

        // and
        var mapper = new KeyValueToMap(keys);

        // when
        var map = mapper.toMap();

        // then
        assertObjectTree(map, """
                {
                    "a": [
                        [{ "person":  "bartek" },
                        { "person":  "ala" }],
                        [ { "person":  "franek" }]
                    ]
                }
                """);
    }

    private KValue aKey(String keyName, String value) {
        var kValue = new String(Base64.encodeBase64(value.getBytes()));
        return new KValue(0, 0, 0, 0, new Key(keyName), kValue);
    }

    void assertObjectTree(Map<String, Object> obj, String expectedJson) {
        var gson = new Gson();
        var json = gson.toJson(obj);
        assertThat(json)
                .isEqualTo(gson.toJson(gson.fromJson(expectedJson, Object.class)));
    }
}