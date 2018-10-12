############################################################################
# Helper functions for operating on repos - these should be preferred over
#   direct calls to hg, git, etc, as they may be used to track how files
#   are collected and thus allow BootstrapBuild files to be converted to
#   other build types.
#
# BootstrapBuild - a powerful but easy tool to build and install modular
#   applications in a Unix environment that supports Bash shell.
#
# Copyright FogRising 2018
# Authors: Ryan Fogarty, Charles J. Remeikas
#
############################################################################

import os

def bootstrapLog(level,mesg):
    logLevel = os.getenv("BOOTSTRAP_LOG_ERROR","Info");

    x = 2
    # Add log statement later...

def bootstrapHgGet(url,artifactName):
    haveCommand = os.system('which hg')
    if (haveCommand == 0):
        command = "hg clone " + url + " " + artifactName
        os.system(command)
    else:
         bootstrapLog("ERROR: bootstrapHgGet requires hg (Mercurial) to be installed")
         return 1

def bootstrapGitGet(url,artifactName):
    haveCommand = os.system('which git')
    if (haveCommand == 0):
        command = "git clone " + url + " " + artifactName
        os.system(command)
    else:
         bootstrapLog("ERROR: bootstrapGitGet requires git to be installed")
         return 1

def bootstrapSvnGet(url,artifactName):
    haveCommand = os.system('which svn')
    if (haveCommand == 0):
        command = "svn checkout " + url + " " + artifactName
        os.system(command)
    else:
         bootstrapLog("ERROR: bootstrapGitGet requires svn to be installed")
         return 1

def bootstrapCvsGet(url,artifactName):
    haveCommand = os.system('which cvs')
    if (haveCommand == 0):
        # TODO: this may also fail because CVS root pserver is not configured
        # wondering if I should write a warning like the below. Or simply allow
        # CVS to fail (and thus return a non-zero retval).
        command = "cvs checkout " + url + " " + artifactName
        os.system(command)
    else:
         bootstrapLog("ERROR: bootstrapCvsGet requires cvs to be installed")
         return 1

def bootstrapHttpGet(url,artifactName):
    haveCommand1 = os.system('which curl')
    haveCommand2 = os.system('which wget')
    if (haveCommand1 == 0):
        command = "curl --location-trusted -O" + url + " " + artifactName
        os.system(command)
    elif (haveCommand2 == 0):
        command = "wget " + url + " " + artifactName
        os.system(command)
    else:
         bootstrapLog("ERROR: bootstrapGitGet requires svn to be installed")
         return 1

