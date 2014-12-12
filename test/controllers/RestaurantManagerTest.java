package controllers;

import org.junit.Test;
import play.mvc.Result;
import play.test.FakeRequest;
import play.test.Helpers;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: Server
 * Date: 12/12/14
 * Time: 8:35 AM
 */
public class RestaurantManagerTest {
    private static final String GET_RESTAURANT_URL = "/restaurantManager/list";

    @Test
    public void testAccessRestaurantListPage() {
        FakeRequest fakeRequest = Helpers.fakeRequest(Helpers.GET, GET_RESTAURANT_URL);
        Result result = Helpers.route(fakeRequest);
        assertThat(Helpers.status(result)).isEqualTo(Helpers.OK);
    }
}
