package org.example.consul.yaml;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

class KeyValueToMapTest {

    @ParameterizedTest
    @CsvSource(value = {
            "a.aaa,a,.aaa",
            "a.second.third,a,.second.third",
            "a333a.second.third,a333a,.second.third",
            "a333a.second[1],a333a,.second[1]",
    })
    void containerPattern(String input, String expectedContainer, String leftPart) {
        // when
        var matcher = KeyValueToMap.containerPattern.matcher(input);

        // then
        assertThat(matcher.matches()).isTrue();
        assertThat(matcher.group(1)).isEqualTo(expectedContainer);
        assertThat(matcher.group(2)).isEqualTo(leftPart);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "[1],1,",
            "[2].aaa,2,.aaa",
            "[2][1].aaa,2,[1].aaa",
    })
    void tablePattern(String input, String expectedContainer, String leftPart) {
        // when
        var matcher = KeyValueToMap.tablePattern.matcher(input);

        // then
        assertThat(matcher.matches()).isTrue();
        assertThat(matcher.group(1)).isEqualTo(expectedContainer);
        assertThat(matcher.group(2)).isEqualTo(Optional.ofNullable(leftPart).orElse(""));
    }
}