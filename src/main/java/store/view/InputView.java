package store.view;

import camp.nextstep.edu.missionutils.Console;
import org.junit.platform.commons.util.StringUtils;
import store.constant.ExceptionMessage;

public class InputView {
    private static final String YES_ANSWER = "Y";
    private static final String NO_ANSWER = "N";

    public String read() {
        String input = Console.readLine();
        validateEmptyString(input);
        return input;
    }

    private void validateEmptyString(final String input) {
        if (StringUtils.isBlank(input)) {
            throw new IllegalArgumentException(ExceptionMessage.INPUT_INVALID_VALUE.getMessage());
        }
    }

    public boolean readBoolean() {
        String input = read();
        if (input.equals(YES_ANSWER)) return true;
        if (input.equals(NO_ANSWER)) return false;
        throw new IllegalArgumentException(ExceptionMessage.INPUT_INVALID_VALUE.getMessage());
    }
}
