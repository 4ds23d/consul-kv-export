package org.example.consul;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;


class KeyTest {

    @ParameterizedTest
    @CsvSource(value = {
            "a/, a",
            "a/abc.yml,a",
            "a/d/aaa.yml,a/d"
    })
    void properlyExtractDirectory(String input, String expectedDirectory) {
        // given
        var key = new Key(input);
        // when
        assertThat(key.getDirectory()).isEqualTo(Path.of(expectedDirectory));
    }

    @Test
    void extractDirectoryForFile() {
        // given
        var key = new Key("abc.yml");

        // when & then
        assertThat(key.getDirectory()).isEqualTo(Path.of(""));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "a/, true",
            "a/abc.yml, false",
            "a/d/aaa.yml, false",
            "a/d/aaa.yml/, true"
    })
    void isDirectory(String input, Boolean expectedDirectory) {
        // given
        var key = new Key(input);
        // when
        assertThat(key.isDirectory()).isEqualTo(expectedDirectory);
    }
}