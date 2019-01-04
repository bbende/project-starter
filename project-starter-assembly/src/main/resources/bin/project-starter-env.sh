#!/bin/bash

# The base name of the application.
# If specified, the APP_LIB_FOLDER must contain a jar named <APP_NAME>.jar
export APP_NAME="project-starter"

# The home folder for the application. Defaults to the parent of the bin directory.
export APP_HOME=

# The folder where application.properties is located. Defaults to APP_HOME/conf.
export APP_CONF_FOLDER=

# The folder where the application jar is located. Defaults to APP_HOME/lib.
export APP_LIB_FOLDER=

# The folder where the pid file will be written. Defaults to APP_HOME/run.
export APP_PID_FOLDER=

# The folder where log files will be written. Defaults to APP_HOME/logs.
export APP_LOG_FOLDER=

# The time in seconds to wait when stopping the application before forcing a shutdown. Defaults to 20.
export STOP_WAIT_TIME=

# Whether the start-stop-daemon command, when it’s available, should be used to control the process. Defaults to true.
export USE_START_STOP_DAEMON=

# The Java installation to use.
export JAVA_HOME=

# The options to pass to the JVM
export JAVA_OPTS="-Xms512m -Xmx512m -Djava.net.preferIPv4Stack=true"
