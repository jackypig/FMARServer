package controllers;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import rest.RestReply;
import util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 10/29/14
 * Time: 11:08 PM
 */
public class RestController extends Controller{
    public static final String ERROR_PARAMETER_INVALID = "PARAMETER_INVALID";
    public static final String ERROR_PARAMETER_MISSING = "PARAMETER_MISSING";
    public static final String ERROR_OTHER = "OTHER";

    protected static final String PARAMETER_ERROR_CODE = "errorCode";
    protected static final String PARAMETER_ERROR_MESSAGE = "errorMessage";
    protected static final String PARAMETER_SUCCESS = "success";

    public static <T> RestReply validateUsingSwagger(T restObject, String pathPrefix) {
        //Instrumentor.enter("VALIDATE_USING_SWAGGER");
        List<Field> fields = ReflectionUtil.allFieldsForClass(restObject.getClass());
        for (Field field : fields) {

            ApiModelProperty apiProperty = field.getAnnotation(ApiModelProperty.class);
            if (apiProperty == null) {
                continue;
            }

            String fieldName = field.getName();
            if (pathPrefix != null) {
                fieldName = pathPrefix + "." + fieldName;
            }

            Object fieldValue = ReflectionUtil.get(restObject, field);
            //Logger.info("Validate: " + fieldName + " Value: " + fieldValue);

            if (apiProperty.required() && fieldValue == null) {
                //Instrumentor.exit("VALIDATE_USING_SWAGGER");
                return errorMissingParameter(fieldName);
            }

            //Validate against a set of values
            if (apiProperty.allowableValues() != null
                    && !apiProperty.allowableValues().trim().equals("")
                    && fieldValue != null) {
                boolean match = false;
                for (String value : apiProperty.allowableValues().split(",")) {
                    value = value.trim();
                    if (fieldValue.toString().equals(value)) {
                        match = true;
                        break;
                    }
                }

                if (!match) {
                    //Instrumentor.exit("VALIDATE_USING_SWAGGER");
                    return error(ERROR_PARAMETER_INVALID, "Invalid value: " + fieldValue.toString() + " for field: " + fieldName + ". Should be one of: " + apiProperty.allowableValues());
                }
            }

            //Recurse into apiClass
            ApiModel apiClass = field.getType().getAnnotation(ApiModel.class);
            if (apiClass != null && fieldValue != null) {
                RestReply response = validateUsingSwagger(fieldValue, fieldName);
                if (response != null) {
                    //Instrumentor.exit("VALIDATE_USING_SWAGGER");
                    return response;
                }
            }
        }

        //Instrumentor.exit("VALIDATE_USING_SWAGGER");
        return null;
    }

    public static String restPath () {
        String path = request().path();
        String restPath = path.substring(path.lastIndexOf("/") + 1);
        return restPath;
    }

    public static Result ok(RestReply response) {
        Logger.debug(restPath().toUpperCase() + "  REPLY  200  " + Json.toJson(response));
        return ok(Json.toJson(response));
    }

    public static Result badRequest(RestReply response) {
        Logger.debug(restPath().toUpperCase() + "  REPLY  400  " + Json.toJson(response));
        return badRequest(Json.toJson(response));
    }

    public static Result notFoundRequest(RestReply response) {
        Logger.debug(restPath().toUpperCase() + "  REPLY  404  " + Json.toJson(response));
        return badRequest(Json.toJson(response));
    }

    public static RestReply errorMissingParameter(String parameter) {
        return error(ERROR_PARAMETER_MISSING, "Required field missing: " + parameter);
    }

    public static RestReply errorInvalidParameter(String parameter, String value) {
        return error(ERROR_PARAMETER_INVALID, "Invalid value for field: " + parameter + " value: " + value);
    }

    public static RestReply error(String errorCode, String errorMessage) {
        return new RestReply(errorCode, errorMessage);
    }

    public static RestReply success() {
        return new RestReply(true);
    }
}
