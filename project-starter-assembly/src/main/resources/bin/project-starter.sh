#!/bin/bash
#################################################################################################################
# Modified from:
#
# https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-tools/
#   spring-boot-loader-tools/src/main/resources/org/springframework/boot/loader/tools/launch.script
#
#################################################################################################################

# ANSI Colors
echoRed() { echo $'\e[0;31m'"$1"$'\e[0m'; }
echoGreen() { echo $'\e[0;32m'"$1"$'\e[0m'; }
echoYellow() { echo $'\e[0;33m'"$1"$'\e[0m'; }

[[ -n "$DEBUG" ]] && set -x

# Determine the location of the current script
BIN_FILE=$(basename "$0")
BIN_FOLDER="$( cd "$( dirname "$0" )" && pwd )"

# Determine the location of the env file and source it
[[ -z "$ENV_FOLDER" ]] && ENV_FOLDER="${BIN_FOLDER}"
[[ -z "$ENV_FILENAME" ]] && ENV_FILENAME="${BIN_FILE%.*}-env.sh"
[[ -r "${ENV_FOLDER}/${ENV_FILENAME}" ]] && source "${ENV_FOLDER}/${ENV_FILENAME}"

# Set up defaults for anything not set in env file
[[ -z "$APP_NAME" ]] && APP_NAME="${BIN_FILE%.*}"
[[ -z "$APP_HOME" ]] && APP_HOME="$( cd "${BIN_FOLDER}" && cd .. && pwd )"
[[ -z "$APP_CONF_FOLDER" ]] && APP_CONF_FOLDER="${APP_HOME}/conf"
[[ -z "$APP_LIB_FOLDER" ]] && APP_LIB_FOLDER="${APP_HOME}/lib"
[[ -z "$APP_PID_FOLDER" ]] && APP_PID_FOLDER="${APP_HOME}/run"
[[ -z "$APP_LOG_FOLDER" ]] && APP_LOG_FOLDER="${APP_HOME}/logs"
[[ -z "$STOP_WAIT_TIME" ]] && STOP_WAIT_TIME=20
[[ -z "$USE_START_STOP_DAEMON" ]] && USE_START_STOP_DAEMON=true

# Set the jar file to launch based on the lib folder and app name
JAR_FILE="${APP_LIB_FOLDER}/${APP_NAME}.jar"

# Initialize pid/log locations
PID_FOLDER=${APP_PID_FOLDER}
LOG_FOLDER=${APP_LOG_FOLDER}
! [[ -x "$PID_FOLDER" ]] && mkdir ${PID_FOLDER}
! [[ -x "$LOG_FOLDER" ]] && mkdir ${LOG_FOLDER}

# Specify the name of the spring config and where to load it from
# NOTE: Any config locations must end with a trailing slash
export SPRING_CONFIG_NAME=application
export SPRING_CONFIG_LOCATION="${APP_CONF_FOLDER}/"

# Utility functions
checkPermissions() {
  touch "$pid_file" &> /dev/null || { echoRed "Operation not permitted (cannot access pid file)"; return 4; }
  touch "$log_file" &> /dev/null || { echoRed "Operation not permitted (cannot access log file)"; return 4; }
}

isRunning() {
  ps -p "$1" &> /dev/null
}

await_file() {
  end=$(date +%s)
  let "end+=10"
  while [[ ! -s "$1" ]]
  do
    now=$(date +%s)
    if [[ $now -ge $end ]]; then
      break
    fi
    sleep 1
  done
}

printEnv() {
    echoGreen ""
    echoGreen "Environment: "
    echoGreen "=============================================="
    echoGreen "BIN_FILE = ${BIN_FOLDER}/${BIN_FILE}"
    echoGreen "ENV_FILE = ${ENV_FOLDER}/${ENV_FILENAME}"
    echoGreen ""
    echoGreen "APP_NAME = ${APP_NAME}"
    echoGreen "APP_HOME = ${APP_HOME}"
    echoGreen "APP_CONF_FOLDER = ${APP_CONF_FOLDER}"
    echoGreen "APP_LIB_FOLDER = ${APP_LIB_FOLDER}"
    echoGreen "APP_PID_FOLDER = ${APP_PID_FOLDER}"
    echoGreen "APP_LOG_FOLDER = ${APP_LOG_FOLDER}"
    echoGreen ""
    echoGreen "JAVA = ${javaexe}"
    echoGreen "JAVA_OPTS = ${JAVA_OPTS}"
    echoGreen "=============================================="
    echoGreen ""
}

# Set action to the first argument passed to this script
action="$1"
shift

# Build the pid and log filenames
pid_file="$PID_FOLDER/${APP_NAME}"
log_file="$LOG_FOLDER/${APP_NAME}.log"

# Determine the user to run as if we are root
# shellcheck disable=SC2012
[[ $(id -u) == "0" ]] && run_user=$(ls -ld "$JAR_FILE" | awk '{print $3}')

# Issue a warning if the application will run as root
[[ $(id -u ${run_user}) == "0" ]] && { echoYellow "Application is running as root (UID 0). This is considered insecure."; }

# Find Java
if [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]]; then
    javaexe="$JAVA_HOME/bin/java"
elif type -p java > /dev/null 2>&1; then
    javaexe=$(type -p java)
elif [[ -x "/usr/bin/java" ]];  then
    javaexe="/usr/bin/java"
else
    echo "Unable to find Java"
    exit 1
fi

arguments=(-Dsun.misc.URLClassPath.disableJarChecking=true $JAVA_OPTS -jar "$JAR_FILE" $RUN_ARGS "$@")

# Action functions
start() {
  printEnv
  if [[ -f "$pid_file" ]]; then
    pid=$(cat "$pid_file")
    isRunning "$pid" && { echoYellow "Already running [$pid]"; return 0; }
  fi
  do_start "$@"
}

do_start() {
  working_dir=$(dirname "$APP_HOME")
  pushd "$working_dir" > /dev/null
  if [[ ! -e "$PID_FOLDER" ]]; then
    mkdir -p "$PID_FOLDER" &> /dev/null
    if [[ -n "$run_user" ]]; then
      chown "$run_user" "$PID_FOLDER"
    fi
  fi
  if [[ ! -e "$log_file" ]]; then
    touch "$log_file" &> /dev/null
    if [[ -n "$run_user" ]]; then
      chown "$run_user" "$log_file"
    fi
  fi
  if [[ -n "$run_user" ]]; then
    checkPermissions || return $?
    if [ $USE_START_STOP_DAEMON = true ] && type start-stop-daemon > /dev/null 2>&1; then
      start-stop-daemon --start --quiet \
        --chuid "$run_user" \
        --name "${APP_NAME}" \
        --make-pidfile --pidfile "$pid_file" \
        --background --no-close \
        --startas "$javaexe" \
        --chdir "$working_dir" \
        -- "${arguments[@]}" \
        >> "$log_file" 2>&1
      await_file "$pid_file"
    else
      su -s /bin/sh -c "$javaexe $(printf "\"%s\" " "${arguments[@]}") >> \"$log_file\" 2>&1 & echo \$!" "$run_user" > "$pid_file"
    fi
    pid=$(cat "$pid_file")
  else
    checkPermissions || return $?
    nohup "$javaexe" "${arguments[@]}" >> "$log_file" 2>&1 &
    pid=$!
    echo "$pid" > "$pid_file"
  fi
  [[ -z $pid ]] && { echoRed "Failed to start"; return 1; }
  echoGreen "Started [$pid]"
}

stop() {
  working_dir=$(dirname "$APP_HOME")
  pushd "$working_dir" > /dev/null
  [[ -f $pid_file ]] || { echoYellow "Not running (pidfile not found)"; return 0; }
  pid=$(cat "$pid_file")
  isRunning "$pid" || { echoYellow "Not running (process ${pid}). Removing stale pid file."; rm -f "$pid_file"; return 0; }
  do_stop "$pid" "$pid_file"
  isRunning "$pid" && { echoRed "Performing force stop..."; return 0; } && do_force_stop "$pid" "$pid_file"
}

do_stop() {
  echoGreen "Stopping..."
  kill "$1" &> /dev/null || { echoRed "Unable to kill process $1"; return 1; }
  for i in $(seq 1 $STOP_WAIT_TIME); do
    isRunning "$1" || { echoGreen "Stopped [$1]"; rm -f "$2"; return 0; }
    [[ $i -eq STOP_WAIT_TIME/2 ]] && kill "$1" &> /dev/null
    sleep 1
  done
  echoRed "Unable to kill process $1";
  return 1;
}

force_stop() {
  [[ -f $pid_file ]] || { echoYellow "Not running (pidfile not found)"; return 0; }
  pid=$(cat "$pid_file")
  isRunning "$pid" || { echoYellow "Not running (process ${pid}). Removing stale pid file."; rm -f "$pid_file"; return 0; }
  do_force_stop "$pid" "$pid_file"
}

do_force_stop() {
  kill -9 "$1" &> /dev/null || { echoRed "Unable to kill process $1"; return 1; }
  for i in $(seq 1 $STOP_WAIT_TIME); do
    isRunning "$1" || { echoGreen "Stopped [$1]"; rm -f "$2"; return 0; }
    [[ $i -eq STOP_WAIT_TIME/2 ]] && kill -9 "$1" &> /dev/null
    sleep 1
  done
  echoRed "Unable to kill process $1";
  return 1;
}

restart() {
  stop && start
}

force_reload() {
  working_dir=$(dirname "$APP_HOME")
  pushd "$working_dir" > /dev/null
  [[ -f $pid_file ]] || { echoRed "Not running (pidfile not found)"; return 7; }
  pid=$(cat "$pid_file")
  rm -f "$pid_file"
  isRunning "$pid" || { echoRed "Not running (process ${pid} not found)"; return 7; }
  do_stop "$pid" "$pid_file"
  do_start
}

status() {
  working_dir=$(dirname "$APP_HOME")
  pushd "$working_dir" > /dev/null
  [[ -f "$pid_file" ]] || { echoRed "Not running"; return 3; }
  pid=$(cat "$pid_file")
  isRunning "$pid" || { echoRed "Not running (process ${pid} not found)"; return 1; }
  echoGreen "Running [$pid]"
  return 0
}

run() {
  pushd "$(dirname "$APP_HOME")" > /dev/null
  "$javaexe" "${arguments[@]}"
  result=$?
  popd > /dev/null
  return "$result"
}

# Call the appropriate action function
case "$action" in
start)
  start "$@"; exit $?;;
stop)
  stop "$@"; exit $?;;
force-stop)
  force_stop "$@"; exit $?;;
restart)
  restart "$@"; exit $?;;
force-reload)
  force_reload "$@"; exit $?;;
status)
  status "$@"; exit $?;;
run)
  run "$@"; exit $?;;
*)
  echo "Usage: $0 {start|stop|force-stop|restart|force-reload|status|run}"; exit 1;
esac

exit 0