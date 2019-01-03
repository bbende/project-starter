#!/bin/bash

# The home directory for the application, WORKING_DIR will be set from the script that sources this env file
export APP_HOME=$(cd "${BIN_FOLDER}" && cd .. && pwd)

# The basename of the application
export APP_NAME="project-starter"

# The folder where application.properties is located
export APP_CONF_FOLDER="${APP_HOME}/conf"

# The folder where the application jar is located
export APP_LIB_FOLDER="${APP_HOME}/lib"

# The folder where the pid file will be written
export APP_PID_FOLDER="${APP_HOME}/run"

# The folder where log files will be written
export APP_LOG_FOLDER="${APP_HOME}/logs"

export JAVA_OPTS="-Xms512m -Xmx512m -Djava.net.preferIPv4Stack=true"

#export JAVA_HOME=/usr/java/jdk1.8.0/
