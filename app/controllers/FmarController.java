package controllers;

import play.Logger;
import play.mvc.Controller;
import util.RequestLocal;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeSet;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 10/25/14
 * Time: 5:05 PM
 */
public class FmarController extends Controller {
    private static RequestLocal<Boolean> parametersDumped = new RequestLocal<Boolean> () {
        @Override
        protected Boolean initialize() {
            return false;
        }
    };

    public static String queryString (String key) {
        return parameter(request().queryString(), key);
    }

    public static Integer queryStringAsInteger(String key) {
        return parameterAsInteger(request().queryString(), key);
    }

    public static Long queryStringAsLong(String key) {
        return parameterAsLong(request().queryString(), key);
    }

    public static Double queryStringAsDouble(String key) {
        return parameterAsDouble(request().queryString(), key);
    }

    public static Boolean queryStringAsBoolean(String key, boolean defaultValue) {
        return parameterAsBoolean(request().queryString(), key, defaultValue);
    }

    public static String formValue(String key) {
        return parameter(request().body().asFormUrlEncoded(), key);
    }

    public static Integer formValueAsInteger(String key) {
        return parameterAsInteger(request().body().asFormUrlEncoded(), key);
    }

    public static Long formValueAsLong(String key) {
        return parameterAsLong(request().body().asFormUrlEncoded(), key);
    }

    public static Double formValueAsDouble(String key) {
        return parameterAsDouble(request().body().asFormUrlEncoded(), key);
    }

    public static Boolean formValueAsBoolean(String key, boolean defaultValue) {
        return parameterAsBoolean(request().body().asFormUrlEncoded(), key, defaultValue);
    }

    public static BigDecimal formValueAsBigDecimal(String key) {
        return parameterAsBigDecimal(request().body().asFormUrlEncoded(), key);
    }

    public static String parameter (Map<String, String[]> map, String key) {
        TreeSet<String> keys = new TreeSet<String>(map.keySet());
        for (String k : keys) {
            String [] value = map.get(k);
            String v = null;
            if (value.length > 1) {
                v = Arrays.toString(value);
            } else {
                v = value[0];
            }

            if (!parametersDumped.get(Controller.request())) {
                Logger.debug(k + ":" + v);
            };
        }

        parametersDumped.set(Controller.request(), true);
        String [] values = map.get(key);
        if (values == null) {
            return null;
        }

        String value = values[0];
        if (values.length > 1) {
            for (int i=1;i<values.length;i++) {
                value += ", " + values[i];
            }
        }
        return value;
    }

    public static Boolean parameterAsBoolean(Map<String, String[]> map, String key, boolean defaultValue) {
        String s = parameter(map, key);
        if (s == null) {
            return defaultValue;
        }
        s = s.trim();
        return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("on");
    }

    public static Integer parameterAsInteger(Map<String, String[]> map, String key) {
        String s = parameter(map, key);
        if (s == null || s.trim().equals("")) {
            return null;
        }
        return new Integer(s);
    }

    public static Long parameterAsLong(Map<String, String[]> map, String key) {
        String s = parameter(map, key);
        if (s == null || s.trim().equals("")) {
            return null;
        }
        return new Long(s);
    }

    public static Double parameterAsDouble(Map<String, String[]> map, String key) {
        String s = parameter(map, key);
        if (s == null || s.trim().equals("")) {
            return null;
        }
        return new Double(s);
    }

    public static BigDecimal parameterAsBigDecimal(Map<String, String[]> map, String key) {
        String s = parameter(map, key);
        if (s == null || s.trim().equals("")) {
            return null;
        }
        return new BigDecimal(s);
    }
}
