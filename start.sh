#!/bin/bash 
# Absolute path to this script, e.g. /home/user/bin/foo.sh
SCRIPT=$(readlink -f "$0")
# Absolute path this script is in, thus /home/user/bin
SCRIPTPATH=$(dirname "$SCRIPT")
echo $SCRIPTPATH

(cd $SCRIPTPATH/project; exec ./mvnw clean package)
java -jar $SCRIPTPATH/project/target/demo-0.1.0.war --server.address=0.0.0.0 --server.port=8181
