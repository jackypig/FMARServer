package rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import controllers.RestController;
import play.libs.Json;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 10/29/14
 * Time: 11:09 PM
 */
@ApiModel
public class RestReply {
    public RestReply () {

    }

    public RestReply(boolean success) {
        this.success = success;
    }

    public RestReply(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @ApiModelProperty(
            allowableValues = RestController.ERROR_PARAMETER_INVALID + ","
                    + RestController.ERROR_PARAMETER_MISSING + ","
                    + RestController.ERROR_OTHER,
            required = false
    )
    public String errorCode;

    @ApiModelProperty(required=false)
    public String errorMessage;

    @ApiModelProperty(required=true)
    public boolean success;

    public JsonNode toJson() {
        return Json.toJson(this);
    }
}
