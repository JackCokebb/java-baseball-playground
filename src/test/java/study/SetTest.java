package study;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class SetTest {

    private Set<Integer> numbers;

    @BeforeEach
    void setUp() {
        numbers = new HashSet<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(3);
    }

    @Test
    void setSize() {
        assertThat(numbers.size()).isEqualTo(3);
    }

    @Test
    void contain() {
        assertThat(numbers.contains(1)).isTrue();
        assertThat(numbers.contains(2)).isTrue();
        assertThat(numbers.contains(3)).isTrue();
        assertThat(numbers.contains(4)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void containWithParameterizedTest(Integer number) {
        assertTrue(numbers.contains(number));
    }

    @ParameterizedTest
    @CsvSource(value = {"1:true", "4:false"}, delimiter = ':')
    void containWithParameterizedTest(Integer number, Boolean expected) {
        assertThat(numbers.contains(number)).isEqualTo(expected);
    }

}
