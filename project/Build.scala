import com.typesafe.config.ConfigFactory
import com.typesafe.sbt.packager.linux.{LinuxPackageMapping, LinuxSymlink}
import sbt._
import play.Project._

object ApplicationBuild extends Build {
    val conf = ConfigFactory.parseFile(new File("conf/application.conf"))
    val appName    = "fmar-server"
    val appVersion = conf.getString("application.version")

    val appDependencies = Seq(
      "mysql" % "mysql-connector-java" % "5.1.22"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(

    )

    ebeanEnabled := true
}
