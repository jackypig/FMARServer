package models;

/**
 * User: Ling Hung
 * Comp: FetchMeARestaurant, Inc.
 * Proj: fmar-server
 * Date: 10/26/14
 * Time: 4:06 PM
 */
public enum  EmailAuthenticationStatus {
    PENDING("Pending"), AUTHENTICATED("Authenticated"), FAILED("Failed"), NOT_ATTEMPTED("Not Attempted");

    private String display;

    EmailAuthenticationStatus(String display) {
        this.display = display;
    }

    public String display () {
        return display;
    }
}
