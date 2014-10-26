package models;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * User: Ling Hung
 * Comp: FetchMeARestaurant, Inc.
 * Proj: fmar-server
 * Date: 10/25/14
 * Time: 4:44 PM
 */
@Embeddable
public class Coordinates {
    @Basic(optional=true) @Column(precision=38,scale=20)
    public BigDecimal latitude;

    @Basic(optional=true) @Column(precision=38,scale=20)
    public BigDecimal longitude;

    public Coordinates () {

    }

    public Coordinates(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates(String latitude, String longitude) {
        this(new BigDecimal(latitude), new BigDecimal(longitude));
    }

    /**
     * Takes coordinates as a comma-delimited list and turns them into a
     *  coordiantes object
     * @param coordinatesString
     * @return
     */
//    public static Coordinates fromString (String coordinatesString) {
//        if (coordinatesString == null) {
//            return null;
//        }
//        String [] latLong = coordinatesString.split(",");
//        BigDecimal latitude = new BigDecimal(latLong[0].trim());
//        BigDecimal longitude = new BigDecimal(latLong[1].trim());
//        Coordinates coordinates = new Coordinates(latitude, longitude);
//        return coordinates;
//    }
//
//    public static Coordinates fromJson (JsonNode coordinatesJson) {
//        if (coordinatesJson == null || coordinatesJson.isMissingNode()) {
//            return null;
//        }
//
//        BigDecimal latitude = coordinatesJson.findPath("latitude").getDecimalValue();
//        BigDecimal longitude = coordinatesJson.findPath("longitude").getDecimalValue();
//        Coordinates coordinates = new Coordinates(latitude, longitude);
//        return coordinates;
//    }
}
