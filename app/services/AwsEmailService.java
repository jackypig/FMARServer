package services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;
import models.EmailAuthenticationStatus;
import play.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Ling Hung
 * Comp: FetchMeARestaurant, Inc.
 * Proj: fmar-server
 * Date: 10/26/14
 * Time: 4:03 PM
 */
public class AwsEmailService implements IEmailService{
    private final AmazonSimpleEmailServiceAsyncClient client;

    public AwsEmailService() {
        client = new AmazonSimpleEmailServiceAsyncClient();
    }

    //Got this code from:
    //  http://docs.aws.amazon.com/ses/latest/DeveloperGuide/send-email-formatted.html
    @Override
    public void send(String fromAddress, String fromName, String [] to, String subject, String bodyHtml, String bodyPlainText) {
        Logger.debug("Prepare to send email!");
        String from = fromName + " <" + fromAddress + ">";
        //String from = fromAddress;
        SendEmailRequest request = new SendEmailRequest().withSource(from);
        List<String> toAddresses = new ArrayList<String>();
        for (String toEmail : to) {
            toAddresses.add(toEmail);
        }

        Destination dest = new Destination().withToAddresses(toAddresses);
        request.setDestination(dest);

        Content subjContent = new Content().withData(subject);
        Message message = new Message().withSubject(subjContent);

        // Avoid Empty Html body and Plain Text body
        if (bodyHtml != null && bodyHtml.trim().equals("")) {
            bodyHtml = null;
        }

        if (bodyPlainText != null && bodyPlainText.trim().equals("")) {
            bodyPlainText = null;
        }

        // Include a body in both text and HTML formats.
        Content textContent = new Content().withData(bodyPlainText);
        Content htmlContent = new Content().withData(bodyHtml);
        Body body = new Body().withHtml(htmlContent).withText(textContent);
        message.setBody(body);

        request.setMessage(message);


        // Call Amazon SES to send the message.
        try {
            client.sendEmail(request);
            Logger.debug("Special Offer Email has been sent to: " + toAddresses);
        } catch (AmazonClientException e) {
            e.printStackTrace();
//            throw ExceptionUtil.rethrowError(e);
        } catch (Exception e) {
            e.printStackTrace();
//            throw ExceptionUtil.rethrowError(e);
        }
    }

    @Override
    public void authenticateEmail(String fromEmail) {
        VerifyEmailAddressRequest emailAddressRequest = new VerifyEmailAddressRequest();
        emailAddressRequest.setEmailAddress(fromEmail);
        client.verifyEmailAddress(emailAddressRequest);
    }

    @Override
    public EmailAuthenticationStatus checkAuthenticationStatus (String email) {
        GetIdentityVerificationAttributesRequest request = new GetIdentityVerificationAttributesRequest();
        request.setIdentities(new ArrayList<String>());
        request.getIdentities().add(email);
        GetIdentityVerificationAttributesResult result = client.getIdentityVerificationAttributes(request);
        EmailAuthenticationStatus status = EmailAuthenticationStatus.NOT_ATTEMPTED;
        IdentityVerificationAttributes attributes = result.getVerificationAttributes().get(email);
        if (attributes == null) {
            return status;
        }

        String sesStatusString = attributes.getVerificationStatus();
        Logger.trace("AWS Status for: " + email + " Status: " + sesStatusString);
        VerificationStatus sesStatus = VerificationStatus.fromValue(sesStatusString);
        if (sesStatus == VerificationStatus.Pending || sesStatus == VerificationStatus.TemporaryFailure) {
            status = EmailAuthenticationStatus.PENDING;
        } else if (sesStatus == VerificationStatus.Success) {
            status = EmailAuthenticationStatus.AUTHENTICATED;
        } else if (sesStatus == VerificationStatus.Failed) {
            status = EmailAuthenticationStatus.FAILED;
        }

        return status;
    }
}
