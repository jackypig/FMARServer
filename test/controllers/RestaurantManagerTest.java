package controllers;

import core.Global;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;
import play.test.Helpers;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: Server
 * Date: 12/12/14
 * Time: 8:35 AM
 */
public class RestaurantManagerTest {
    public static FakeApplication fakeApplicationForTest;
    private static final String GET_RESTAURANT_URL = "/restaurantManager/list";

    @Before
    public void before() {
        Helpers.start(fakeApplication());
    }

    @After
    public void after() {
        Helpers.stop(fakeApplication());
    }

    @Test
    public void testAccessRestaurantListPage() {
        FakeRequest fakeRequest = Helpers.fakeRequest(Helpers.GET, GET_RESTAURANT_URL);
        Result result = Helpers.route(fakeRequest);
        assertThat(Helpers.status(result)).isEqualTo(Helpers.OK);
    }

    public static FakeApplication fakeApplication () {
        if (fakeApplicationForTest == null) {

            Map<String, String> appProperties = getStandardFakeApplicationProperties();
            fakeApplicationForTest = Helpers.fakeApplication(appProperties);
        }
        return fakeApplicationForTest;
    }

    public static Map<String, String> getStandardFakeApplicationProperties() {
        Map<String, String> appProperties = new HashMap<String, String>();
        appProperties.put("db.default.driver", "com.mysql.jdbc.Driver");
        appProperties.put("db.default.url", getTestDataBasePath());
        appProperties.put("db.default.user", "xappuser");
        appProperties.put("db.default.password", "XappXapp");
        appProperties.put("application.fake", "true");
        appProperties.put("ebean.default", "models.*");
        appProperties.put("configuration.class", "core.ConfigurationUnit");

        //Set to INFO or DEBUG for detailed logging in the tests
        setTestLoggingLevel(appProperties, "ERROR");
        return appProperties;
    }

    public static String getTestDataBasePath() {
        return "mysql://xappuser:XappXapp@localhost/fmar-dev";
    }

    public static void setTestLoggingLevel(Map<String, String> appProperties, String loggingLevel) {
        appProperties.put("logger.application", loggingLevel);
        appProperties.put("logger.swagger", loggingLevel);
        appProperties.put("logger.play", loggingLevel);
        appProperties.put("logger.root", loggingLevel);
    }
}
