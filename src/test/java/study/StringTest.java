package study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StringTest {

    @Test
    void replace() {
        String actual = "abc".replace("b", "d");
        assertThat(actual).isEqualTo("adc");
    }

    @Test
    void split12() {
        String[] actual = "1,2".split(",");
        assertThat(actual).containsExactly("1", "2");
    }

    @Test
    void split1() {
        String[] actual = "1".split(",");
        assertThat(actual).containsExactly("1");
    }

    @Test
    void substring() {
        String origin = "(1,2)";
        assertThat(origin.substring(1, origin.length() - 1)).isEqualTo("1,2");
    }

    @Test
    @DisplayName("\"abc\" 값이 주어졌을 때 String의 charAt() 메소드를 활용해 특정 위치의 문자를 가져오는 테스트를 구현")
    void charAt() {
        String origin = "abc";
        assertThat(origin.charAt(0)).isEqualTo('a');
        assertThat(origin.charAt(1)).isEqualTo('b');
        assertThat(origin.charAt(2)).isEqualTo('c');
        assertThatExceptionOfType(IndexOutOfBoundsException.class)
            .isThrownBy(() -> origin.charAt(3)).withMessageMatching("String index out of range: \\d+");
        assertThatThrownBy(() -> origin.charAt(3))
            .isInstanceOf(IndexOutOfBoundsException.class).hasMessageContaining("String index out of range:");
    }
}
