package study;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

public class BaseBallGameTest {

    private static final HashSet<Integer> answerSet = new HashSet<>();
    private static final ArrayList<Integer> answerList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final int ANSWER_LENGTH = 3;

    @Test
    public void generateAnswerTest() {
        generateAnswer();
        Assertions.assertThat(answerSet.size()).isEqualTo(Integer.valueOf(ANSWER_LENGTH));
        Assertions.assertThat(answerList.size()).isEqualTo(answerSet.size());

    }

    @ParameterizedTest
    @CsvSource(value = {"123", "345", "777", "89"})
    public void readUserInputTest(String inputStr) {

        System.setIn(new ByteArrayInputStream(inputStr.getBytes()));
        scanner = new Scanner(System.in);

        if (inputStr.length() != ANSWER_LENGTH) {

            Assertions.assertThatThrownBy(BaseBallGameTest::readUserInput)
                .hasMessageContaining("INVALID INPUT");
            return;
        }

        List<Integer> input = readUserInput();
        Assertions.assertThat(input.size()).isEqualTo(ANSWER_LENGTH);
    }

    @ParameterizedTest
    @MethodSource("generateData")
    public void diffTest(List<Integer> answer, List<Integer> userInput, int ballCount,
        int strikeCount) {

        BaseballResult result = diff(answer, userInput);

        Assertions.assertThat(result.getBallCount()).isEqualTo(ballCount);
        Assertions.assertThat(result.getStrikeCount()).isEqualTo(strikeCount);
    }

    static Stream<Arguments> generateData() {
        return Stream.of(
            Arguments.of(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3), 0, 3),
            Arguments.of(Arrays.asList(2, 1, 3), Arrays.asList(1, 2, 3), 2, 1),
            Arguments.of(Arrays.asList(4, 5, 6), Arrays.asList(7, 8, 9), 0, 0)

        );
    }

    @ParameterizedTest
    @CsvSource(value = {"1:2:1볼 2스트라이크", "0:0:0볼", "3:0:3볼", "0:2:2스트라이크"}, delimiter = ':')
    public void printResultTest(int ballCount, int strikeCount, String answerPrinted) {
        BaseballResult result = new BaseballResult(ballCount, strikeCount);

        Assertions.assertThat(printResult(result)).isEqualTo(answerPrinted);
    }

    @ParameterizedTest
    @CsvSource(value = {"1:2:false", "0:3:true", "3:0:false"}, delimiter = ':')
    public void isGameOverTest(int ballCount, int strikeCount, boolean isOver) {
        BaseballResult result = new BaseballResult(ballCount, strikeCount);

        Assertions.assertThat(isGameOver(result)).isEqualTo(isOver);
    }

    @ParameterizedTest
    @CsvSource(value = {"Y:true", "y:true", "N:false", "n:false", "321:false"}, delimiter = ':')
    public void wantRestartTest(String input, boolean isUserWant) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        scanner = new Scanner(System.in);

        Assertions.assertThat(wantRestart()).isEqualTo(isUserWant);
    }

    // ====================================================================================
    // ====================================================================================

    public static void main(String[] args) {
        while (playBaseballGame()) continue;
    }

    private static boolean playBaseballGame() {
        init();
        generateAnswer();

        while (true) {
            List<Integer> userInput = readUserInput();
            BaseballResult diffResult = diff(answerList, userInput);
            System.out.println(printResult(diffResult));

            if (isGameOver(diffResult)){
                return wantRestart();
            }
        }
    }

    private static void init() {

        answerSet.clear();
        answerList.clear();
    }

    private static void generateAnswer() {

        System.out.println("Generating the Answer...");
        while (answerSet.size() < ANSWER_LENGTH) {

            int randomInt = (int) (Math.random() * 10);
            answerSet.add(randomInt);
        }

        answerList.addAll(answerSet);
    }

    private static List<Integer> readUserInput() {

        System.out.print("숫자를 입력해 주세요: ");
        String inputStr = scanner.nextLine();
        if (inputStr.length() != ANSWER_LENGTH) {

            throw new IllegalArgumentException("INVALID INPUT");
        }

        return Arrays.stream(inputStr.split(""))
            .mapToInt(Integer::valueOf)
            .boxed()
            .collect(Collectors.toList());

    }

    private static BaseballResult diff(List<Integer> answer, List<Integer> userInput) {

        BaseballResult result = new BaseballResult();

        for (int i = 0; i < ANSWER_LENGTH; i++) {

            diffSingle(answer, userInput, i, result);
        }

        return result;
    }

    private static void diffSingle(List<Integer> answer, List<Integer> userInput, int idx,
        BaseballResult result) {

        if (Objects.equals(answer.get(idx), userInput.get(idx))) {
            result.addStrikeCount();
            return;

        }

        if (answer.contains(userInput.get(idx))) {
            result.addBallCount();

        }
    }

    private static String printResult(BaseballResult result) {

        StringBuilder sb = new StringBuilder();

        if (result.getBallCount() > 0) {
            sb.append(result.getBallCount()).append("볼");
        }

        if (sb.length() > 0 && result.getStrikeCount() > 0) {
            sb.append(" ");
        }

        if (result.getStrikeCount() > 0) {
            sb.append(result.getStrikeCount()).append("스트라이크");
        }

        if (sb.length() < 1) {
            sb.append("0볼");
        }

        return sb.toString();
    }

    private static boolean isGameOver(BaseballResult result) {

        return result.getStrikeCount() == 3;
    }

    private static boolean wantRestart() {
        System.out.println(
            "USER WON!!!\nWant to play again? Enter \"Y\", else Enter anything else....");
        String userInput = scanner.nextLine();

        return userInput.equalsIgnoreCase("Y");
    }

    private static class BaseballResult {

        private int ballCount;

        private int strikeCount;

        public BaseballResult(int ballCount, int strikeCount) {
            this.ballCount = ballCount;
            this.strikeCount = strikeCount;
        }

        public BaseballResult() {
            this.ballCount = 0;
            this.strikeCount = 0;
        }

        public int getBallCount() {
            return ballCount;
        }

        public int getStrikeCount() {
            return strikeCount;
        }

        public void addBallCount() {

            this.ballCount++;
        }

        public void addStrikeCount() {

            this.strikeCount++;
        }
    }

}
