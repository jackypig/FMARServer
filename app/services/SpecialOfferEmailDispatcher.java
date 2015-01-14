package services;

import akka.actor.UntypedActor;
import core.Global;
import models.Restaurant;
import models.User;
import util.EmailHelper;

import java.util.List;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: server
 * Date: 01/13/15
 * Time: 01:23 PM
 */
public class SpecialOfferEmailDispatcher {
    public static class sendingSpecialOfferEmailActor extends UntypedActor {
        @Override
        public void onReceive(Object o) throws Exception {
            //Logger.info("OnReceive " + o.getClass() + " " + o.toString());
            if (o instanceof String && o.toString().equals("status")) {
                SpecialOfferEmailDispatcher specialOfferEmailDispatcher = new SpecialOfferEmailDispatcher();
                specialOfferEmailDispatcher.sendSpecialOfferEmail();
            } else {
                unhandled(o);
            }
        }
    }

    public void sendSpecialOfferEmail () {
        List<User> users = User.findAll();
        for (User user: users) {
            EmailHelper.sendSpecialOfferEmail(user.email);
        }
    }

    public static void main (String [] args) {
//
//        EncodingDotComHelper helper = new EncodingDotComHelper();
//        helper.checkStatusWithEncodingDotCom();
    }

}
