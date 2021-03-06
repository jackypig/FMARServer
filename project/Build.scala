import com.typesafe.config.ConfigFactory
import com.typesafe.sbt.packager.linux.{LinuxPackageMapping, LinuxSymlink}
import sbt._
import play.Project._

object ApplicationBuild extends Build {
    val conf = ConfigFactory.parseFile(new File("conf/application.conf"))
    val appName    = "fmar-server"
    val appVersion = conf.getString("application.version")

    val appDependencies = Seq(
      "com.amazonaws" % "aws-java-sdk" % "1.7.5",
      "mysql" % "mysql-connector-java" % "5.1.22",
      "com.wordnik" %% "swagger-play2" % "1.3.1" exclude("org.scala-stm", "scala-stm_2.10.0")
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(

    )

    ebeanEnabled := true
}
