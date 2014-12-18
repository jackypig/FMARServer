/**
 * Created by lh on 11/3/14.
 */
var Alert = {
    show: function (message) {
        $( "#alertBody" ).text(message);
        $( "#alertDialog").modal('toggle');
    }
};

var UserService = {
    signInAuthenticate: function(email, password, callback) {
        var url = "/userManager/signInAuthenticate?email=" + email + "&password=" + password;

//        BusyDialog.show("Authenticating");
        UserService.ajaxSetup();
        $.getJSON(url, null, function (status) {
//            BusyDialog.hide(function () {
                if (!status.success) {
//                    Alert.show("Invalid Login", "Invalid email or password. Please try again.");
                    alert("Invalid email or password. Please try again.");
                } else {
                    callback.call(this, status);
                }
//            });
        }).error(function () {
//            BusyDialog.hide();
//            Alert.show("Invalid Login", "Unknown error. Please try again.");
            alert("Unknown error. Please try again.");
        });
    },

    stayUpdatedAuthenticate: function(email, callback) {
//        alert(email);
        var url = "/userManager/stayUpdatedAuthenticate?email=" + email;

//        BusyDialog.show("Authenticating");
        UserService.ajaxSetup();
        $.getJSON(url, null, function (status) {
//            BusyDialog.hide(function () {
            if (!status.success) {
//                    Alert.show("Invalid Login", "Invalid email or password. Please try again.");
                alert("Invalid email, Please try again.");
            } else {
                callback.call(this, status);
            }
//            });
        }).error(function () {
//            BusyDialog.hide();
//            Alert.show("Invalid Login", "Unknown error. Please try again.");
            alert("Unknown error. Please try again.");
        });
    },

    ajaxSetup: function() {
        $.ajaxSetup({async:true, cache: false});
    }

//    emailPasswordReset: function(email, callback) {
//        var url = "/adminManager/emailPasswordReset?email=" + email;
//        BusyDialog.show("Sending email...");
//        WebService.ajaxSetup();
//        $.getJSON(url, null, function (status) {
//            BusyDialog.hide(function () {
//                if (!status.success) {
//                    Alert.show("Email Failed", status.errorMessage);
//                } else {
//                    callback.call(this, status);
//                }
//            });
//        }).error(function () {
//            BusyDialog.hide();
//            Alert.show("Operation Failed", "Unknown error. Please try again.");
//        });
//    },
//
//    resetPassword: function(password, callback) {
//        var url = "/adminManager/resetPassword?newPassword=" + password;
//        BusyDialog.show("Resetting Password...");
//        WebService.ajaxSetup();
//        $.getJSON(url, null, function (status) {
//            BusyDialog.hide(function () {
//                if (!status.success) {
//                    Alert.show("Operation Failed", status.errorMessage);
//                } else {
//                    callback.call(this, status);
//                }
//            });
//        }).error(function () {
//            BusyDialog.hide();
//            Alert.show("Operation Failed", "Unknown error. Please try again.");
//        });
//    }
};

var BusyDialog = {
    callback: null,
    delay: null,
    showTime: null,

    show: function (message, delay) {
        if (message !== undefined && message != null) {
            $( "#busyDialogText" ).text( message );
        }

        if ( delay === undefined ) {
            delay = 500;
        }

        BusyDialog.delay = delay;
        BusyDialog.showTime = $.now();

        //We display the dialog after a timeout, because otherwise this cancels any in-process events happening
        $( "#busyDialog" ).dialog({
            //height: 140,
            modal: false,
            open: function(event, ui) {
                $(".ui-dialog-titlebar-close", this.parentNode).hide();
            },
            position: {my: "center top+100", at: "center top", of: window}
        });
    },

    hide: function(callback) {
        //The second check is necessary for Firefox, which according to them on versions prior to 13
        //  passes an integer of "actual timeout" to the callback for setTimeout! This has been removed with version 13, mercifully
        if (callback !== undefined && typeof callback == "function") {
            BusyDialog.callback = callback;
        }
        //Wait for at least some decent amount of time to go by before hiding the busy dialog so it does not look so "flashy"
        if ($.now() - BusyDialog.showTime < BusyDialog.delay) {
            setTimeout(BusyDialog.hide, 100);
            return;
        }

        $( "#busyDialog" ).dialog( "close" );
        if (BusyDialog.callback != null) {
            BusyDialog.callback.call(this);
            BusyDialog.callback = null;
        }
    }
};
