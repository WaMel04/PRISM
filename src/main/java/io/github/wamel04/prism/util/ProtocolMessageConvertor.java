package io.github.wamel04.prism.util;

import java.util.Arrays;

public class ProtocolMessageConvertor {

    public static String convert(Object... values) {
        return String.join("|||", Arrays.stream(values)
                .map(Object::toString)
                .toArray(String[]::new));
    }

}
