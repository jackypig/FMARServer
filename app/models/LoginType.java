package models;

import util.EnumUtil;

/**
 * User: Ling Hung
 * Comp: ChiSengMe, Inc.
 * Proj: fmar-server
 * Date: 10/29/14
 * Time: 6:31 PM
 */
public enum LoginType {
    RESTAURANT_REPRESENTATIVE, GUEST, SUPER_USER;

    public static LoginType fromString (String s) {
        return EnumUtil.fromString(LoginType.class, s);
    }
}
