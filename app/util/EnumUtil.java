package util;

/**
 * User: Ling Hung
 * Comp: ChiShenMe, Inc.
 * Proj: fmar-server
 * Date: 10/29/14
 * Time: 6:36 PM
 */
public class EnumUtil {
    public static <T extends Enum<T>> T fromString(Class<T> enumType, String name) {
        for (T enumValue : enumType.getEnumConstants()) {
            if (enumValue.name().equalsIgnoreCase(name)) {
                return enumValue;
            }
        }

        return null;
    }

    public static final <T extends Enum> String toCommaDelimitedString(Class<T> enumType) {
        StringBuilder s = new StringBuilder();
        for (T enumValue : enumType.getEnumConstants()) {
            s.append(enumValue.name());
        }

        return s.toString();
    }
}
