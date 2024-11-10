package store.view;

import camp.nextstep.edu.missionutils.Console;
import org.junit.platform.commons.util.StringUtils;
import store.constant.ExceptionMessage;

public class InputView {
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
}
