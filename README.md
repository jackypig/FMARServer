FMARServer
==========

MDChiShenMe core server-side functionality

Getting Started
---------------
To change the configuration properties, add a new conf file under the application.conf, and set the system property
    config.file=<FULL_PATH>/application.ci.conf
This file should then include the main conf if it is just overriding certain properties with a line like this:
     include "application.conf"

The commands are;
    DEV         play -Dconfig.file=/home/ec2-user/projects/xs/conf/application.ci.conf run
    PROD        play -Dconfig.file=/home/ec2-user/projects/xs/conf/application.prod.conf run

Read about configuration here:
    http://www.playframework.org/documentation/2.0/Configuration

And also here:
    http://www.playframework.org/documentation/2.0/ProductionConfiguration

To start mysql locally, go to your mysql directory and type the following:
    ./mysqld --console

If accidentally deleted .ssh file such that you can't login to ec2 instance, refer to the steps in the following link:
    https://forums.aws.amazon.com/thread.jspa?threadID=83069

If javac is not found in your environment, do the following:
    sudo yum install java-devel
