#!/usr/bin/env bash

function findANTLR4() {
   # Attempt to find ANTLR4 jar
   if [ ! -z "${ANTLR4_INSTALL_DIR}" ] ; then
   
      if [ -d "${ANTLR4_INSTALL_DIR}/lib/jars" ] ; then
         antlr4jar=($(ls "${ANTLR4_INSTALL_DIR}"/lib/jars/antlr-4.*.jar 2> /dev/null))
         if [ $? -ne 0 ] ; then
            echo "ANTLR4 jar file could not be found" > /dev/stderr
            return 1
         else
            export ANTLR4_JAR="${antlr4jar[0]}"
            return 0
         fi
   
      elif [ -d "${ANTLR4_INSTALL_DIR}" ] ; then
         # Note: Mac's BSD version of find seems to have a bug
         # if finding from a single root directory such as /etc
         # However, two levels or more are fine such as /Users/foo
         antlr4jar=($(find "${ANTLR4_INSTALL_DIR}" -name "antlr-4.*.jar" 2> /dev/null))
         if [ -z "${antlr4jar[0]}" ] ; then
            echo "ANTLR4 jar file could not be found" > /dev/stderr
            return 1
         else
            export ANTLR4_JAR="${antlr4jar[0]}"
            return 0
         fi
      else
          echo "ANTLR4 jar file could not be found" > /dev/stderr
         return 1
      fi
   
   elif [ ! -z "${ANTLR4_HOME}" ] ; then
      if [ -d "${ANTLR4_HOME}" ] ; then
         # Note: Mac's BSD version of find seems to have a bug
         # if finding from a single root directory such as /etc
         # However, two levels or more are fine such as /Users/foo
         antlr4jar=($(find "${ANTLR4_HOME}" -name "antlr-4.*.jar" 2> /dev/null))
         if [ -z "${antlr4jar[0]}" ] ; then
            echo "ANTLR4 jar file could not be found" > /dev/stderr
            return 1
         else
            export ANTLR4_JAR="${antlr4jar[0]}"
            return 0
         fi
      else
          echo "ANTLR4 jar file could not be found" > /dev/stderr
         return 1
      fi
   
   elif [ ! -z "${ANTLR4_JAR}" ] ; then
      if [ -e "${ANTLR4_JAR}" ] ; then
         return 0
      else
         return 1
      fi
   else
      echo "ANTLR4 jar file could not be found" > /dev/stderr
      return 1
   fi
}

