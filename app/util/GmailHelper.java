package util;

import play.Logger;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 1/12/15
 * Time: 09:58 PM
 */
public class GmailHelper {
    private Store store;

    public GmailHelper (String username, String password) {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", username, password);
            //session.
        } catch (Exception e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }

    public void close () {
        try {
            store.close();
        } catch (MessagingException e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }

    public Message[] getMessages () {
        try {
            Folder defaultFolder = store.getFolder("Inbox");
            defaultFolder.open(Folder.READ_WRITE);
            Message[] messages = defaultFolder.getMessages();
            return messages;
        } catch (Exception e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }

    public void moveMessage(Message message, String toFolderString) {
        try {
            Folder toFolder = store.getFolder(toFolderString);
            toFolder.appendMessages(new Message [] {message});
            delete(message);
        } catch (Exception e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }

    public static String getSubject (Message message) {
        try {
            return message.getSubject();
        } catch (Exception e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }

    public static String getContent (Message message) {
        try {
            MimeMultipart mimeMessage = (MimeMultipart) message.getContent();
            BodyPart bodyPart = mimeMessage.getBodyPart(0);
            Logger.info("BodyPart: " + bodyPart.getContent().getClass());
            return bodyPart.getContent().toString();
        } catch (Exception e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }

    public static void delete(Message message) {
        try {
            message.setFlag(Flags.Flag.DELETED, true);
        } catch (Exception e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }
}
