#!/bin/sh
cd "`dirname "$0"`"             # the copious quoting is for handling paths with spaces
# -server                       use server VM for highest performance
# -Djava.library.path=./lib     ensure JOGL can find native libraries 
# -Djava.ext.dirs=              ignore any existing JOGL installation
# -XX:MaxPermSize=128m          avoid OutOfMemory errors for large models
# -Xss16m                       increase stack size to allow deeper recursion
# -Xmx1024m                     use up to 1GB RAM (edit to increase)
# -Dfile.encoding=UTF-8         ensure Unicode characters in model files are compatible cross-platform
# -jar NetLogo.jar              specify main jar
# "$@"                          pass along any command line arguments
java -server -Djava.library.path=./lib -Djava.ext.dirs= -XX:MaxPermSize=128m -Xss16m -Xmx1024m -Dfile.encoding=UTF-8 -jar NetLogo.jar "$@"
