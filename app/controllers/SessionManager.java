package controllers;

import core.Global;
import models.LoginSession;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import util.ExceptionUtil;
import util.RequestLocal;

import java.util.*;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 10/29/14
 * Time: 10:55 PM
 */
public class SessionManager {
    public static final String KEY_USER_SESSION_ID = "sessionUuid";
    public static final String KEY_ADVERTISER_ID = "advertiserId";
    public static final String KEY_PUBLISHER_ID = "publisherId";
    public static RequestLocal<HashMap<String, Object>> requestMap = new RequestLocal<HashMap<String, Object>>() {
        @Override
        protected HashMap<String, Object> initialize() {
            return new HashMap<String, Object>();
        }
    };

    public static HashMap<String, Object> requestMap(Http.Request request) {
        HashMap<String, Object> attributes = requestMap.get(request);
        return attributes;
    }

    public static void associate (String uuid) {
        Controller.session(KEY_USER_SESSION_ID, uuid);
    }

    public static void clear() {
        Controller.session().clear();
    }

    public static LoginSession create (User user) {
        LoginSession session = new LoginSession();
        session.user = user;
        session.sessionUuid = UUID.randomUUID().toString();
        session.createdTimestamp = new Date();
        session.save();
        Controller.session(KEY_USER_SESSION_ID, session.sessionUuid);
        return session;
    }

//    public static Admin getAdmin () {
//        AdminSession session = getAdminSession();
//        if (session == null) {
//            return null;
//        }
//        return session.admin;
//    }
//
//    public static AdminSession getAdminSession () {
//        return getAdminSession(null);
//    }
//
//    /**
//     * We allow the session to be passed in because sometimes this is called from the hook in Action.Simple
//     * At the time Action.Simple calls back, it has not yet set the session on the Controller, so we need to pass it
//     * rather than calling Controller.session()
//     * @param context
//     * @return
//     */
//    public static AdminSession getAdminSession (Http.Context context) {
//        Http.Session session = null;
//        Http.Request request = null;
//
//        if (context == null) {
//            session = Controller.session();
//            request = Controller.request();
//        } else {
//            session = context.session();
//            request = context.request();
//        }
//
//        //Get the admin session from a ThreadLocal if possible to avoid constant lookups
//        if (requestMap(request).get(KEY_ADMIN_SESSION_ID) != null) {
//            return (AdminSession) requestMap(request).get(KEY_ADMIN_SESSION_ID);
//        }
//
//        String sessionUuid = session.get(KEY_ADMIN_SESSION_ID);
//        if (sessionUuid == null) {
//            if (Global.isDebugEnabled()) {
//                Admin admin = Admin.findById(1L);
//                AdminSession adminSession = create(admin);
//                sessionUuid = adminSession.sessionUuid;
//            } else {
//                Logger.trace("SessionManager NoSessionId!");
//                return null;
//            }
//        }
//
//        AdminSession adminSession = AdminSession.findByUuid(sessionUuid);
//        //Set the admin session in the threadlocal to avoid future lookups
//        requestMap(request).put(KEY_ADMIN_SESSION_ID, adminSession);
//        return adminSession;
//    }
//
//    public static List<Publisher> allPublishers () {
//        List<Publisher> publishers = Publisher.findAll();
//        Collections.sort(publishers);
//        return publishers;
//    }
//
//    public static boolean hasPublisher () {
//        return getPublisher() != null;
//    }
//
//    public static Publisher getPublisher() {
//        if (requestMap(Controller.request()).get(KEY_PUBLISHER_ID) != null) {
//            return (Publisher) requestMap(Controller.request()).get(KEY_PUBLISHER_ID);
//        }
//
//        String publisherId = Controller.session(KEY_PUBLISHER_ID);
//        if (publisherId == null) {
//            if (Global.isDebugEnabled()) {
//                publisherId = "1";
//                Controller.session(KEY_PUBLISHER_ID, publisherId);
//            } else {
//                return null;
//            }
//        }
//
//        Publisher publisher = Publisher.findById(new Long(publisherId));
//        requestMap(Controller.request()).put(KEY_PUBLISHER_ID, publisher);
//        return publisher;
//    }
//
//    public static void setPublisher (Publisher publisher) {
//        Controller.session(KEY_PUBLISHER_ID, publisher.id.toString());
//    }
//
//    public static Advertiser getAdvertiser () {
//        if (getAdmin() != null && getAdmin().advertiser != null) {
//            return getAdmin().advertiser;
//        }
//
//        String advertiserId = Controller.session(KEY_ADVERTISER_ID);
//        if (advertiserId == null) {
//            if (Global.isDebugEnabled()) {
//                advertiserId = "1";
//                Controller.session(KEY_ADVERTISER_ID, advertiserId);
//            } else {
//                throw ExceptionUtil.throwRuntime("No advertiser ID in session! Cannot go to page!");
//            }
//        }
//
//        return Advertiser.findById(new Long(advertiserId));
//    }
//
//    //This should be called in places where the threadlocal to hold request and session stuff has not been set
//    public static boolean isLoggedIn (Http.Context context) {
//        return (getAdminSession(context) != null);
//    }
//
//    public static boolean isLoggedIn () {
//        return isLoggedIn(null);
//    }
//
//    public static boolean canAddAccounts () {
//        if (getAdmin() == null) {
//            return false;
//        }
//
//        if (isSuperUser() || isXappUser() || getAdmin().accountAdminType == AccountAdminType.MANAGER ) {
//            return true;
//        }
//
//        return false;
//    }
//
//    public static boolean canCopyAds () {
//        if (getAdmin() == null) {
//            return false;
//        }
//
//        if (isSuperUser() || isXappUser() || getAdmin().accountAdminType == AccountAdminType.MANAGER)
//        {
//            return true;
//        }
//
//        return false;
//    }
//
//    public static boolean canOverridePrices () {
//        return isSuperUser() || isXappUser();
//    }
//
//    public static boolean canAccessConfiguration () {
//        if (getAdmin() == null || isXappUser()) {
//            return false;
//        }
//
//        if (getAdmin().publisher.accessConfiguration) {
//            return true;
//        }
//
//        return false;
//    }
//
//    public static boolean canViewPrices () {
//        if (getAdmin() == null) {
//            return false;
//        }
//
//        if (isSuperUser() || isXappUser() || getAdmin().accountAdminType == AccountAdminType.MANAGER) {
//            return true;
//        }
//
//        return false;
//    }
//
//    public static boolean canViewReports () {
//        if (getAdmin() == null) {
//            return false;
//        }
//
//        if (
//                isSuperUser() ||
//                        isXappUser()  ||
//                        getAdmin().accountAdminType == AccountAdminType.MANAGER ||
//                        getAdmin().accountAdminType == AccountAdminType.TRAFFICKER
//                ) {
//            return true;
//        }
//
//        return false;
//    }
//
//    public static boolean isSuperUser () {
//        return getAdmin() != null && getAdmin().isSuperuser();
//    }
//
//    public static boolean isXappUser () {
//        return getAdmin() != null && getAdmin().isXappUser();
//    }
//
//    public static boolean isManager () {
//        return getAdmin().accountAdminType == AccountAdminType.MANAGER;
//    }
//
//    public static void setAdvertiser(Advertiser advertiser) {
//        Logger.info("SetAdvertiser: " + advertiser.id);
//        Controller.session(KEY_ADVERTISER_ID, advertiser.id.toString());
//    }
//
//    public static void setValue(String key, String value) {
//        AdminSession session = getAdminSession();
//        AdminSessionKey sessionKey = session.key(key);
//        String debug = session.admin != null ? "Admin: " + session.admin.email + " " : "";
//        debug += "Key: " + key + " Value: " + value;
//        if (sessionKey == null) {
//            Logger.info("CreatingKey " + debug);
//            sessionKey = new AdminSessionKey();
//            sessionKey.adminSession = session;
//
//        } else {
//            Logger.info("UpdatingKey " + debug);
//        }
//        sessionKey.key = key;
//        sessionKey.value = value;
//        sessionKey.save();
//    }
//
//    public static String getValue (String key) {
//        AdminSessionKey sessionKey = getAdminSession().key(key);
//        if (sessionKey == null) {
//            return null;
//        } else {
//            return sessionKey.value;
//        }
//    }

//    public static String companyName () {
//        if (hasPublisher()) {
//            return getPublisher().name;
//        } else if (getAdmin().advertiser != null) {
//            return getAdmin().advertiser.name;
//        } else {
//            return "ChiShenMe";
//        }
//    }
}
