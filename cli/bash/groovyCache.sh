#!/usr/bin/env bash
#
# Function to speed up loading of Bootstrap by precompiling Groovy files as needed
# in ${BOOTSTRAP_CACHE_DIR}/commonCache area.
#

function updateGroovyCache() {

   mkdir -p "${BOOTSTRAP_CACHE_DIR}"
   
   rsync -ua "${BOOTSTRAP_TOP_DIR}/cli/groovy" \
            "${BOOTSTRAP_CACHE_DIR}/"
   
   if pushd "${BOOTSTRAP_CACHE_DIR}" &> /dev/null ; then
   
      groovyFiles=($(find . -name "*.groovy"))
      
      needToRecompile=0
      for groovyFile in "${groovyFiles[@]}" ; do
         if [ ! -f "${groovyFile}.built" ] || [ "${groovyFile}" -nt "${groovyFile}.built" ] ; then
            needToRecompile=1
            break
         fi 
      done
      
      if [ $needToRecompile == 1 ] ; then
         # Compile and if successful, touch all of the .built files that we need to...
         #echo "Compiling line: groovyc ${groovyFiles[@]}"
         # groovyc -cp ${CLASSPATH} "${groovyFiles[@]}"

         # The below line includes a kludge to filter out lines printed by Groovy and Java which
         # are not appropriate to show the user. Hopefully a clean up of the Groovy implementation
         # will clean up its output in the future so that this unsavory hack can be removed.
         groovyc "${groovyFiles[@]}" 2>&1 | grep -ve "^WARNING:" | grep -ve "^Picked up"
         if [ $? -eq 0 ] ; then
            for groovyFile in "${groovyFiles[@]}" ; do
               if [ ! -f "${groovyFile}.built" ] || [ "${groovyFile}" -nt "${groovyFile}.built" ] ; then
                  touch "${groovyFile}.built"
               fi
            done
         fi
      fi
      
      popd &> /dev/null
   fi
}


function findGroovy() {

   if ! which groovyc &> /dev/null ; then
      if [[ "${GROOVY_HOME}TMP" == "TMP" ]] ; then
         echo "ERROR: groovy cannot be located. Set GROOVY_HOME or add appropriate PATH statement to groovy/bin folder" > /dev/stderr
         return 1
      else
         if [ -e "${GROOVY_HOME}/bin/groovyc" ] ; then
            export PATH=${PATH}:"${GROOVY_HOME}/bin"
            return 0
         else
            echo "ERROR: groovy cannot be located. Set GROOVY_HOME or add appropriate PATH statement to groovy/bin folder" > /dev/stderr
            return 1
         fi
      fi
   fi

   return 0
}

