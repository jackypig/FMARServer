package core;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.wordnik.swagger.config.ConfigFactory;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;
import scala.concurrent.duration.Duration;
import services.*;
import util.ExceptionUtil;
import util.IOUtil;
import util.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: Server
 * Date: 10/26/14
 * Time: 4:37 PM
 */
public class Global extends GlobalSettings{
    public static Configuration configuration;
    public static String applicationKey;
    public static String applicationVersion ;

    public static String getApplicationUrl () {
        return play.Configuration.root().getString("application.url");
    }

    public static String getAwsAccessKeyId () {
        return configuration.getAwsAccessKeyId();
    }

    public static String getAwsAccessKeySecret () {
        return configuration.getAwsAccessKeySecret();
    }

//    public static String getBaseUrlHttp () {
//        return "http://" + getServerHost() + ":" + getServerPort();
//    }

    public static IEmailService getEmailService() {
        return configuration.getEmailService();

    }

    public static String getSystemEmail () {
        return "jackypig0906@gmail.com";
    }

    public static IBlobService getBlobService() {
        return configuration.getBlobService();
    }

    public static String getServerHost() {
        return configuration.getServerHost();
    }

    public static int getServerPort() {
        return configuration.getServerPort();
    }

    public static String getCdnAudioDomain () {
        return play.Configuration.root().getString("cdn.audio.domain");
    }

    public static String getCdnImageDomain () {
        return play.Configuration.root().getString("cdn.image.domain");
    }

    public static boolean isDebugEnabled () {
        return false;
    }

    public static AuthenticateService getAuthenticationService () {
        return new AuthenticateService();
    }

    public static String getApplicationKey () {
        if (applicationKey != null) {
            return applicationKey;
        }

        String fullConfigFile = System.getProperty("config.file");
        System.out.println("FullConfigFile: " + fullConfigFile);
        Pattern pattern = Pattern.compile(".*application\\.(.*)(\\.replicator)?\\.conf");
        Matcher matcher = pattern.matcher(fullConfigFile);
        String key = null;
        if (matcher.find()) {
            key = matcher.group(1);
        } else {
            throw ExceptionUtil.throwRuntime("No application key defined for instance.");
        }
        //Application keys shouldn't have replicator in them
        key = key.replace(".replicator", "");

        applicationKey = key;
        return key;
    }

    public static String getApplicationVersion() {
        String version = "";
        if (applicationVersion != null) {
            version = applicationVersion;
        } else {
            if (!IOUtil.resourceExists("version.sbt")) {
                return "dev-no-file";
            }
            String versionRaw = IOUtil.resourceToString("version.sbt");
            String[] pieces = versionRaw.split("\"");
            if (pieces.length < 2) {
                return "dev-wrong-format";
            }
            version = pieces[1];
        }

        return version.trim();
    }

    public static boolean isFakeApplication () {
        return play.Configuration.root().getBoolean("application.fake", false);
    }

    @Override
    public void onStart(Application app) {
        //We call this so that the swagger api version shows our application version (which in turn comes from Git)
        ConfigFactory.config().setApiVersion(getApplicationVersion());

        //Add this here to prevent excessive logging from SpyCache
//        System.setProperty("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.SunLogger");
//        java.util.logging.Logger.getLogger("net.spy.memcached").setLevel(Level.WARNING);

        if (configuration != null) {
            return;
        }

        String configurationClass = play.Configuration.root().getString("configuration.class");
//        Logger.info("StartingApp Key: " + getApplicationKey() + " ConfigClass: " + configurationClass);
//        Logger.info("StartingApp Key: " + getApplicationKey());
        String dbUrl = play.Configuration.root().getString("db.default.url");
        Logger.info("DB: " + dbUrl);

//        Logger.info("Setting ebean transaction logging to NONE");

        if (configurationClass == null) {
            configuration = new Configuration();
        } else {
            configuration = (Configuration) ReflectionUtil.newInstance(configurationClass);
        }

        //Start all of the utility actors that run in the system
        startUtilityActors();
    }

    @Override
    public F.Promise<SimpleResult> onError(Http.RequestHeader request, Throwable t) {
        ExceptionUtil.logError(t, "Got an error with request: " + request.toString());
        //Returns the default error page
        return null;
    }


    private void startUtilityActors() {
        //Schedule the dispatching of special offer email
        if (!isFakeApplication()) {
            ActorRef SpecialOfferDispatcherActor = Akka.system().actorOf(Props.create(SpecialOfferEmailDispatcher.sendingSpecialOfferEmailActor.class));
            Akka.system().scheduler().schedule(
                    Duration.create(0, TimeUnit.MILLISECONDS),
                    Duration.create(8, TimeUnit.HOURS),
                    SpecialOfferDispatcherActor,
                    "status",
                    Akka.system().dispatcher(),SpecialOfferDispatcherActor);
        }
    }

    @Override
    public Action onRequest(Http.Request request, Method method) {
        String path = request.path();
        Logger.debug("<------ NEW REQUEST " + path + " ----------------->");

        Action action = super.onRequest(request, method);
//        Action action;
//        if (path.startsWith("/rest")) {
//            action = super.onRequest(request, method);
//        } else {
//            action = XsController.initializeNewRequest(request, method);
//        }
        return action;
    }
}
