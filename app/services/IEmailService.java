package services;

import models.EmailAuthenticationStatus;

/**
 * User: Ling Hung
 * Comp: FetchMeARestaurant, Inc.
 * Proj: fmar-server
 * Date: 10/26/14
 * Time: 2:37 PM
 */
public interface IEmailService {
    /**
     * Sends an email
     * @param fromAddress
     * @param fromName
     * @param toAddresses
     * @param subject
     * @param bodyHtml
     * @param bodyPlainText
     */
    public void send(String fromAddress, String fromName, String [] toAddresses, String subject, String bodyHtml, String bodyPlainText);

    /**
     * Handles authenticating that a from email is valid to use
     * @param email
     */
    public void authenticateEmail (String email);


    /**
     * Checks if the email has been authenticated
     * @param email
     * @return
     */
    public EmailAuthenticationStatus checkAuthenticationStatus (String email);
}
