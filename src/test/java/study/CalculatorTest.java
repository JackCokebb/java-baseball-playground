package study;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class CalculatorTest {

    private Scanner scanner = new Scanner(System.in);
    private final Deque<String> operatorQueue = new LinkedList<>();
    private final Deque<String> operandQueue = new LinkedList<>();

    private static final List<String> operators = Arrays.asList("+", "-", "*", "/");

    private long calculateAllFromUserInput(){

        readUserInput();
        long result = calculateAll();
        cleanUp();

        return result;
    }

    private void readUserInput() {

        String input = scanner.nextLine();
        String[] values = input.split(" ");

        for (String value : values) {
            if (operators.contains(value)) {
                operatorQueue.addLast(value);
            }

            if (isNumber(value)) {
                operandQueue.addLast(value);
            }
        }
    }

    private long calculateAll() {

        if(operatorQueue.isEmpty() || operandQueue.isEmpty()) {
            throw new ArithmeticException();
        }

        if(operandQueue.size() - operatorQueue.size() != 1) {
            throw new ArithmeticException();
        }

        return operandQueue.stream()
            .mapToLong(Long::parseLong)
            .reduce((x, y) -> calculate(x, y, operatorQueue.pollFirst()))
            .getAsLong();
    }

    private long calculate(long x, long y, String operator) {

        if ("+".equals(operator)) {
            return x + y;
        }
        if ("-".equals(operator)) {
            return x - y;
        }
        if ("*".equals(operator)) {
            return x * y;
        }

        return x / y;
    }

    private boolean isNumber(String input) {
        try {

            Long.valueOf(input);

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    private void cleanUp(){

        operatorQueue.clear();
        operandQueue.clear();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1 + 2 - 3 * 4 / 5"})
    void userInputTest(String input) {

        System.setIn(new ByteArrayInputStream(input.getBytes()));
        scanner = new Scanner(System.in);

        readUserInput();

        assertThat(operatorQueue.size()).isGreaterThan(0);
        assertThat(operandQueue.contains("1")).isEqualTo(true);
        assertThat(operandQueue.contains("2")).isEqualTo(true);
        assertThat(operandQueue.contains("3")).isEqualTo(true);
        assertThat(operandQueue.contains("4")).isEqualTo(true);
        assertThat(operandQueue.contains("5")).isEqualTo(true);
    }

    @ParameterizedTest
    @CsvSource(value = {"1:2:+:3", "2:8:-:-6", "3:9:*:27", "7:7:/:1"}, delimiter = ':')
    void calculateTest(long x, long y, String operator, long result) {

        assertThat(calculate(x, y, operator)).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvSource(value = {"5 + 4 - 3 * 2 / 1:12", "6 / 3 * 2 - 4 + 1:1"}, delimiter = ':')
    void calculateAllTest(String input, long result) {

        System.setIn(new ByteArrayInputStream(input.getBytes()));
        scanner = new Scanner(System.in);

        assertThat(calculateAllFromUserInput()).isEqualTo(result);
    }

}
