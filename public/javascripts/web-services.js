/**
 * Created by lh on 11/3/14.
 */
var AdminService = {
    authenticate: function(email, password, callback) {
        var url = "/adminManager/authenticate?email=" + email + "&password=" + password;

        BusyDialog.show("Authenticating");
        WebService.ajaxSetup();
        $.getJSON(url, null, function (status) {
            BusyDialog.hide(function () {
                if (!status.success) {
                    Alert.show("Invalid Login", "Invalid email or password. Please try again.");
                } else {
                    callback.call(this, status);
                }
            });
        }).error(function () {
            BusyDialog.hide();
            Alert.show("Invalid Login", "Unknown error. Please try again.");
        });
    },

    emailPasswordReset: function(email, callback) {
        var url = "/adminManager/emailPasswordReset?email=" + email;
        BusyDialog.show("Sending email...");
        WebService.ajaxSetup();
        $.getJSON(url, null, function (status) {
            BusyDialog.hide(function () {
                if (!status.success) {
                    Alert.show("Email Failed", status.errorMessage);
                } else {
                    callback.call(this, status);
                }
            });
        }).error(function () {
            BusyDialog.hide();
            Alert.show("Operation Failed", "Unknown error. Please try again.");
        });
    },

    resetPassword: function(password, callback) {
        var url = "/adminManager/resetPassword?newPassword=" + password;
        BusyDialog.show("Resetting Password...");
        WebService.ajaxSetup();
        $.getJSON(url, null, function (status) {
            BusyDialog.hide(function () {
                if (!status.success) {
                    Alert.show("Operation Failed", status.errorMessage);
                } else {
                    callback.call(this, status);
                }
            });
        }).error(function () {
            BusyDialog.hide();
            Alert.show("Operation Failed", "Unknown error. Please try again.");
        });
    },
};
