package util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 11/3/14
 * Time: 11:02 PM
 */
public class ReflectionUtil {
    //Gets all the instance fields, public or private, for a class
    public static List<Field> allFieldsForClass (Class<?> clazz) {
        Field [] fieldArray = clazz.getDeclaredFields();
        List<Field> fields = new ArrayList<Field>();
        for (Field field : fieldArray) {
            //Ignore static fields
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            fields.add(field);
        }
        if (clazz.getSuperclass() != null) {
            fields.addAll(allFieldsForClass(clazz.getSuperclass()));
        }
        return fields;
    }

    public static Field getField (Class<?> clazz, String fieldName) {
        List<Field> fields = allFieldsForClass(clazz);
        for (Field field : fields) {
            //Logger.info("Field: " + field.getName());
            if (field.getName().equals(fieldName)) {
                //Logger.info("Match");
                return field;
            }
        }
        return null;
    }

    public static void set(Object objectToUpdate, String fieldName, Object value) {
        Field field = getField(objectToUpdate.getClass(), fieldName);
        set(objectToUpdate, field, value);
    }

    public static void set(Object objectToUpdate, Field field, Object value) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(objectToUpdate, value);
        } catch (Exception e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }

    public static Object get(Object object, String fieldName) {
        Field field = ReflectionUtil.getField(object.getClass(), fieldName);
        return get(object, field);
    }

    public static Object get(Object object, Field field) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return field.get(object);
        } catch (Exception e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }

    public static Object newInstance (String classString) {
        try {
            Class<?> c = Class.forName(classString);
            return c.newInstance();
        } catch (Throwable e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }
}
