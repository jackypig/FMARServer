package models;

import util.EnumUtil;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: Server
 * Date: 12/9/14
 * Time: 4:03 PM
 */
public enum MediaType {
    AUDIO, IMAGE;

    public String display () {
        return this.toString().toLowerCase();
    }

    public static MediaType fromString (String value) {
        return EnumUtil.fromString(MediaType.class, value);
    }
}
