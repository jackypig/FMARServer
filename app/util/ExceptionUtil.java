package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import core.Global;
import org.apache.commons.lang3.exception.ExceptionUtils;
import play.Logger;
import play.libs.Json;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 10/29/14
 * Time: 11:03 PM
 */
public class ExceptionUtil {
    //TODO add this to configuration
    public static final String logglyURL = "https://logs-01.loggly.com/inputs/a0144683-c953-44cb-af5a-2174ee6d8be2/tag/http-client/";

    public static class RethrownException extends RuntimeException {
        public RethrownException() {
        }

        public RethrownException(String s) {
            super(s);
        }

        public RethrownException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public RethrownException(Throwable throwable) {
            super(throwable);
        }
    }


    public static void logError(Throwable throwable) {
        logError(throwable, getLoggingClassName(3), getLoggingMethodName(3));
    }

    public static void logErrorWithContext(Throwable throwable, JsonNode contextData) {
        logError(throwable, getLoggingClassName(3), getLoggingMethodName(3), contextData);
    }

    public static void logError(String message) {
        logError(message, getLoggingClassName(3), getLoggingMethodName(3));
    }

    public static void logError (Throwable throwable, String className, String routine, JsonNode contextData) {
        Logger.error(null, throwable);
//        logErrorRemote("", throwable, className, routine, contextData);
    }

    public static void logError (Throwable throwable, String className, String routine) {
        logError(throwable, className, routine, null);
    }

    public static void logError (Throwable throwable, String message) {
        Logger.error(null, throwable);
//        logErrorRemote(message, throwable, getLoggingClassName(3), getLoggingMethodName(3), null);
    }

    public static void logError (String message, String className, String routine) {
        Logger.error(message);
//        logErrorRemote(message, null, className, routine, null);
    }

    public static void logWarning(Throwable throwable) {
        logWarning(throwable, getLoggingClassName(3), getLoggingMethodName(3));
    }

    public static void logWarning(String message) {
        logWarning(message, getLoggingClassName(3), getLoggingMethodName(3));
    }

    public static void logWarning (Throwable throwable, String className, String routine, JsonNode contextData) {
        Logger.warn(null, throwable);
//        logWarningRemote("", throwable, className, routine, contextData);
    }

    public static void logWarning (Throwable throwable, String className, String routine) {
        logWarning(throwable, className, routine, null);
    }

    public static void logWarning (String message, String className, String routine) {
        Logger.warn(message);
//        logWarningRemote(message, null, className, routine, null);
    }

//    public static void logErrorRemote(String message, Throwable throwable, String className, String routine, JsonNode contextData) {
//        ObjectNode node = createMessageJson("ERROR", message, throwable, className, routine, contextData);
//
//        LogglyUtil.slowLogMessage(node.toString());
//    }
//
//    public static void logWarningRemote(String message, Throwable throwable, String className, String routine, JsonNode contextData) {
//        ObjectNode node = createMessageJson("WARNING", message, throwable, className, routine, contextData);
//        LogglyUtil.slowLogMessage(node.toString());
//    }

    private static ObjectNode createMessageJson(String level, String message, Throwable throwable, String className, String routine, JsonNode contextData) {
        ObjectNode node = Json.newObject();
        node.put("level", level);
//        node.put("environment", Global.applicationKey);
        node.put("class", className);
        node.put("methodName", routine);
        node.put("message", message);
        if (contextData != null) {
            node.put("context", contextData);
        }
        if (throwable != null) {
            node.put("throwableMessage", throwable.getMessage());
            node.put("stackTrace", ExceptionUtils.getStackTrace(throwable));
        }

        return node;
    }

    public static RuntimeException throwRuntime(String message) {
        RethrownException exception = new RethrownException(message);
        logError(message, getLoggingClassName(3), getLoggingMethodName(3));
        return exception;
    }

    public static RuntimeException rethrowError (Throwable throwable) {
        return rethrowErrorWithContext(throwable, null);
    }

    public static RuntimeException rethrowErrorWithContext(Throwable throwable, JsonNode errorContext) {
        if (throwable instanceof RethrownException) {
            return (RethrownException) throwable;
        }

        logError(throwable, getLoggingClassName(3), getLoggingMethodName(3), errorContext);
        return new RethrownException(throwable);
    }

    private static String getLoggingMethodName(int stackLocation) {
        int stackDepth = 1 + stackLocation;
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        if (ste.length <= stackDepth) {
            return "";
        }
        return ste[stackDepth].getMethodName();
    }

    private static String getLoggingClassName(int stackLocation) {
        int stackDepth = 1 + stackLocation;
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        if (ste.length <= stackDepth) {
            return "";
        }
        return ste[stackDepth].getClassName();
    }

}
