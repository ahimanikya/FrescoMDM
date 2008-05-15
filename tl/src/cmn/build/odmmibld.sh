#!/bin/sh

#
# BEGIN_HEADER - DO NOT EDIT
#
# The contents of this file are subject to the terms
# of the Common Development and Distribution License
# (the "License").  You may not use this file except
# in compliance with the License.
#
# You can obtain a copy of the license at
# https://open-esb.dev.java.net/public/CDDLv1.0.html.
# See the License for the specific language governing
# permissions and limitations under the License.
#
# When distributing Covered Code, include this CDDL
# HEADER in each file and include the License file at
# https://open-esb.dev.java.net/public/CDDLv1.0.html.
# If applicable add the following below this CDDL HEADER,
# with the fields enclosed by brackets "[]" replaced with
# your own identifying information: Portions Copyright
# [year] [name of copyright owner]
#

#
# @(#)odmmi.sh
# Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
#
# END_HEADER - DO NOT EDIT
#
#


############################### USAGE ROUTINES ################################

usage()
{
    status=$1

    cat << EOF

Usage:  $p [options...] [var=def...] [-Dprop=val] [target(s)]

 Master build script for JBI products.

Options:
 -help           Display this message.

 -clean          Do a clean build (call ant with clean options). DEFAULT.
 -noclean        Do not do a clean build.

### -cleansrc       Clean out the source code before building.

 -cleanmavenrepo Remove maven local repository, and require build to redownload all maven dependencies

 -keepon         Keep running, even if compile fails. RELEASE DEFAULT
 -nokeepon       Don't run tests if it doesn't compile.  DEVELOPER DEFAULT.

 -update         Update (or create) working directory. DEFAULT FOR RELEASE BUILD.
 -noupdate       Do not checkout or update source code from CVS. DEVELOPER DEFAULT.

 -test           Run all scripts in non-destructive test mode.
 -updatemaven    Update external dependencies in local maven repository.  DEFAULT IF -update
 -noupdatemaven  Do not update external dependencies in local maven repository.
                 (pass --offline option to all mvn commands.)
 -popmaven       populate the remote maven repository defined by REMOTE_MAVEN_REPOS with build artifacts . DEFAULT FOR RELEASE BUILD.
 -nopopmaven     do not populate any remote maven repository.  DEVELOPER DEFAULT.
 -fast|-fastupdate
                 Short-hand for:  $p -update -junit -nojavadoc --> NEED TO ADD KH
 -integreport    Create integration report. DEFAULT FOR RELEASE BUILD.
 -nointegreport  Do not create integration report. DEFAULT FOR DEVELOPER BUILD.
 -taskreport     Create project task report. DEFAULT FOR PRIMARY RELEASE BUILD.
 -notaskreport   Do not create project task report. DEVELOPER DEFAULT.
 -nobuild        Do not build.
 -product <prod> Build and test against <prod>, which can be  "shasta"
                 or "whitney".  Default is "shasta".
 -javadoc        Create javadoc. DEFAULT FOR RELEASE BUILD.
 -nojavadoc      Do not create javadoc. DEVELOPER DEFAULT.
 -release        Build all release kits.  DEFAULT FOR RELEASE BUILD.
 -norelease      Do not build any release kits.  DEVELOPER DEFAULT.
 -install        Build the installer.  DEFAULT FOR RELEASE BUILD.
 -noinstall      Do not build the installer.  DEVELOPER DEFAULT.
 -pushkit        Push kits to a location other than the usual kitroot.
 -nopushkit      Do not push kits to a location other than the usual kitroot (DEFAULT)
 -junit          Run the junit tests 
 -nojunit        Do not run the junit tests. (DEFAULT)
 -ant_output     Send the ANT build output to the terminal as well as to
                 the log file.
 -verbose        Tell ant to be verbose.
 -bldnum num     use <num> for BLDNUM instead of generating a new one.
                 useful for re-releasing kits without a new build.
 var=def...      Substitute <def> for each <var>, and export to the environment.
 -Dprop=val...   Pass the given propery/value to ant.
 target(s)...    Build the given targets in the main build.xml file.

Environment:
 LOGDIR          Where to put all log files.
 LOCKDIR         Where to look for and write files used to communicate
                 completion of various build steps.

 KITROOT         The root of all product kits.
 KIT_DISTROOT    Where to distribute kits.  Normally set to \$KITROOT/\$PRODUCT

 DEVELOPER_BUILD Set this to 1 if you are building for development.

 RELEASE_BUILD   Set this to 1 if for official release builds only.
                 DO NOT SET THIS ENVIRONMENT VARIABLE IN A DEVELOPMENT PATH.

 INTEG_REPORT    Set to the name of script to create the integration report.

Developer Build Example:
 $p -update

Release Build Example:
 $p -bldnum \$BLDNUM

EOF

    exit $status
}


parse_args()
{
    ### set option defaults according to builder profile.
    bld_set_builder_profile

    DOHELP=0
    TESTMODE=0; testarg=
    CALLEDFROMWRAPPER=0
    BLDNUM_ARG=NULL
    ANT_PROPS=
    VERBOSE_ARG=
    REGRESS_PRODUCT=odmmi
    DOMAVEN_UPDATE=1
    DOMAVEN_POP=0
    DOTASK_REPORT=0
    DEBUG=0
    DOPUSHKIT=0
    DOINSTALL=0

    I_AM_PRIMARY=0
    if [ "$FORTE_PORT" = "$PRIMARY_PORT" ]; then
        I_AM_PRIMARY=1
    fi

    if [ $RELEASE_BUILD -eq 1 ]; then
        DOARCHIVELOGS=0
        DOBUILD=1
        #always clean object files:
        DOCLEAN=1
        DOCLEANSRC=0
        DOMAVENCLEAN=0
        DOUPDATE=1
        DOMAVEN_UPDATE=1
        DOJAVADOC=1
        DOJUNIT=1
        #do not ignore build failures, even for release,
        #as this leaves appserver instances running.  RT 3/2/06
        KEEPON=0
        if [ $I_AM_PRIMARY -eq 1 ]; then
            DOINTEGREPORT=1
            DORELEASE=1
            #default is to only do the task report on the primary release build:
            #disabled; unused now.  RT 7/19/06
            DOTASK_REPORT=0
            DOINSTALL=1
            DOARCHIVEDOC=1
            #DOMAVEN_POP=1
            DOMAVEN_POP=0
        else
            DOINTEGREPORT=0
            DORELEASE=0
            DOARCHIVEDOC=0
            DOINSTALL=0
            DOMAVEN_POP=0
        fi
    else
        DOCLEAN=1
        DOCLEANSRC=0
        DOMAVENCLEAN=0
        DOMAVEN_POP=0
        DOUPDATE=0
        DOMAVEN_UPDATE=1
        DOINTEGREPORT=0
        DORELEASE=0
        DOINSTALL=0
        DOJUNIT=0
        DOBUILD=1
        DOJAVADOC=1
        DOARCHIVEDOC=0
        KEEPON=0
        DOARCHIVELOGS=0
    fi

    while [ $# -gt 0 -a "$1" != "" ]
    do
        arg=$1; shift

        case $arg in
        -h* )
            usage 0
            ;;
        -debug )
            DEBUG=1
            ;;
        -t* )
            TESTMODE=1; testarg="-test"
            ;;
        -calledFromWrapper )
            #true if called from runjbiBuild wrapper:
            CALLEDFROMWRAPPER=1
            ;;
        -clean )
            DOCLEAN=1
            ;;
        -cleanmavenrepo )
            DOMAVENCLEAN=1
            DOMAVEN_UPDATE=1
            ;;
        -cleansrc )
            DOCLEANSRC=1
            DOUPDATE=1
            ;;
        -bldnum )
            if [ $# -gt 0 ]; then
                BLDNUM_ARG=$1; shift
            else
                echo "${p}: -bldnum requires an argument"
                usage 1
            fi
            ;;

        -noclean )
            DOCLEAN=0
            ;;
        -pushkit )
            DOPUSHKIT=1
            ;;
         -nopushkit )
            DOPUSHKIT=0
            ;;
        -keepon )
            KEEPON=1
            ;;
        -nokeepon )
            KEEPON=0
            ;;
        -junit )
            DOJUNIT=1
            ;;
        -nojunit )
            DOJUNIT=0
            ;;
        -javadoc )
            DOJAVADOC=1
            ;;
        -nojavadoc )
            DOJAVADOC=0
            ;;
        -release )
            DORELEASE=1
            ;;
        -norelease )
            DORELEASE=0
            ;;
        -taskreport )
            DOTASK_REPORT=1
            ;;
        -notaskreport )
            DOTASK_REPORT=0
            ;;
        -install )
            DOINSTALL=1
            ;;
        -noinstall )
            DOINSTALL=0
            ;;
        -nobuild )
            DOBUILD=0
            #don't clean if we're not building
            DOCLEAN=0
            ;;
        -update )
            #update product sources:
            DOUPDATE=1
            DOMAVEN_UPDATE=1
            ;;
        -noupdate )
            DOUPDATE=0
            ;;
        -updatemaven )
            DOMAVEN_UPDATE=1
            ;;
        -noupdatemaven )
            DOMAVEN_UPDATE=0
            ;;
        -popmaven )
            DOMAVEN_POP=1
            ;;
        -nopopmaven )
            DOMAVEN_POP=0
            ;;
        -fast|-fastupdate )
            DOUPDATE=1
            #only run junit tests (this will force use to compile tests):
            DOJUNIT=0
            DOJAVADOC=0
            ;;
        -integreport )
            DOINTEGREPORT=1
            ;;
        -nointegreport )
            DOINTEGREPORT=0
            ;;
        -verbose )
            VERBOSE_ARG="-verbose"
            ;;
        -D*=* )
            if [ -z "$ANT_PROPS" ]; then
                ANT_PROPS="$arg"
            else
                ANT_PROPS="$ANT_PROPS $arg"
            fi
            ;;
        -D* )
            echo "${p}: illegal option, $arg"
            usage 1
            ;;
        *=* )
            tmp=`echo $arg|sed -e 's/"/\\\\"/g'`
            #echo A arg=.$arg. tmp is .$tmp.
            tmp=`echo $tmp|sed -e 's/^\([^=][^=]*\)=\(.*\)/\1="\2"; export \1/'`
            #echo B tmp is .$tmp.
            eval $tmp
            ;;
        -* )
            echo "${p}: unknown option, $arg"
            usage 1
            ;;
        esac
    done

    # NEED TO FIX - KH
    # reset DOCLEAN if it is not needed.
    if [ $DOCLEAN -eq 1 ]; then
        if [ $DOCLEANSRC -eq 1 ]; then
            #we don't need to run the clean step if we are removing the src tree:
            DOCLEAN=0
            bldmsg -p $p -warn Ignoring -clean because -cleansrc is also specified.
  #      elif [ ! -d "$SRCROOT/build-common" ]; then
  #          #we don't need to run the clean step because source has not been checked out yet:
  #          DOCLEAN=0
  #          bldmsg -p $p -warn Ignoring -clean because build-common is missing - assume first build.
        fi
    fi

    ### NOTE: more option setup in set_global_vars.
}

############################### INFO ROUTINES #################################

show_initial_options()
{
    bldmsg -mark Running $p $saveargs
    show_options INITIAL
}

show_final_options()
{
    show_options FINAL
}

show_options()
{
    cat << EOF
$1 OPTION SETTINGS FOR $p -
    DOHELP is          $DOHELP
    TESTMODE is        $TESTMODE
    DOCLEAN is         $DOCLEAN
    DOMAVENCLEAN is    $DOMAVENCLEAN
    DOMAVEN_UPDATE is  $DOMAVEN_UPDATE
    DOMAVEN_POP is     $DOMAVEN_POP
    DOCLEANSRC is      $DOCLEANSRC

    DOUPDATE is        $DOUPDATE

    DOBUILD is         $DOBUILD
    DOJUNIT is         $DOJUNIT

    DOJAVADOC is       $DOJAVADOC
    DOARCHIVEDOC is    $DOARCHIVEDOC
    DOARCHIVELOGS is   $DOARCHIVELOGS

    DORELEASE is       $DORELEASE

    DOINSTALL is       $DOINSTALL

    DOINTEGREPORT is   $DOINTEGREPORT
    DOTASK_REPORT is   $DOTASK_REPORT

EOF

}

show_build_environment()
{
    #show general environment:
    bld_show_env

    #show local additions:
    cat << EOF

    I_AM_PRIMARY is $I_AM_PRIMARY

    UPDATELOG is    $UPDATELOG
    CLEANLOG is     $CLEANLOG
    BUILDLOG is     $BUILDLOG
    JAVADOCLOG is   $JAVADOCLOG
    RELEASELOG is   $RELEASELOG
    POPMAVENLOG is  $POPMAVENLOG
    PUSHKITLOG is   $PUSHKITLOG
    UNITTESTLOG is  $UNITTESTLOG

    INSTALLOG is         $INSTALLOG
    INSTALLER_TESTLOG is $INSTALLER_TESTLOG is

    INTEGRATIONLOG is    $INTEGRATIONLOG

    JBI_CVSROOT is    $JBI_CVSROOT
    CVS_OPTS is        $CVS_OPTS

    HTML_SRCROOT is     $HTML_SRCROOT

    BLDNUM is   $BLDNUM
    KIT_REV is  $KIT_REV

    DEVELOPER_BUILD is $DEVELOPER_BUILD
    RELEASE_BUILD is   $RELEASE_BUILD

    JBI_BRANCH_NAME is $JBI_BRANCH_NAME

EOF
}

################################## UTILITIES ##################################

require()
#import external shell routines - fatal error if we can't find it.
{
    libname=$1

    if [ x$libname = x ]; then
        echo "BUILD_ERROR: ${p}:require:  missing file name - ABORT"
        exit 1
    fi

    #look in a couple of familiar places:
    if [ -f "$TOOLROOT/lib/cmn/$libname" ]; then
        libname=$TOOLROOT/lib/cmn/$libname
    elif [ -f "./$libname" ]; then
        #we assume this is a test env!
        echo "$p - BUILD_WARNING: loading $libname from current directory."
        libname=./$libname
    fi

    . $libname
    if [ $? -ne 0 ]; then
        echo "BUILD_ERROR: ${p}:require: errors sourcing $libname - ABORT"
        exit 1
    fi
}

has_ant_errors()
#search for ant task errors:
#   [javac] 60 errors
#   [javadoc] 1 error
#   [junit] TEST com.sun.iis.ebxml.internal.support.logger.TestGlobalLogger FAILED
{
    if [ "$1" = "" ]; then
        bldmsg -error -p $p "Usage:  has_ant_errors LOGFILE"
        return 0
    fi

    #check for javac/javadoc errors:
    grep '\] [0-9]* error'  $1 > /dev/null
    if [ $? -eq 0 ]; then
        return 1
    fi

    #check for junit errors:
    grep 'Tests run:' $1 | grep -v 'Failures: 0, Errors: 0,' > /dev/null
    if [ $? -eq 0 ]; then
        #we found something matching, which means we had failures:
        return 1
    fi

    return 0
}

filter_maven_log()
#funtion to reduce the maven download progress strings to just the final download total.
#INPUT:  stdin
#OUTPUT:  stdout
{
    perl -n -a -e 's|^.*\r([0-9][0-9Kb]* downloaded)$|$1|g; print;'
}

filter_maven_log_in_place()
#Usage:  filter_maven_log_in_place fn
#replace contents of <fn> with results of filter_maven_log
{
    if [ "$1" != "" ]; then
        filter_maven_log < "$1" > "$TMPA"
        if [ $? -eq 0 ]; then
            cmp -s "$1" "$TMPA"
            if [ $? -ne 0 ]; then
                #files are different:
                mv "$TMPA" "$1"
                return $?
            fi

            ## files are the same
            rm -f "$TMPA"
            return 0
        fi
        rm -f "$TMPA"
        return 1
    fi

    #FAILED:
    return 1
}

############################### INITIALIZATION ################################

setup_wrapper_env()
#setup up variables that are normally set by wrapper.
{
    #we require a LINKROOT separate from SRCROOT:
    if [ x$FORTE_LINKROOT = x ]; then
        if [ "$FORTE_PORT" != "nt" ]; then
            bld_fatal_error "FORTE_LINKROOT must be set to build JBI - ABORT"
        fi
    fi

    bld_setup_logdir
    if [ $? -ne 0 ]; then
        bld_fatal_error "failed to set up log directory - ABORT"
    fi

    bld_setup_lockdir

    #set up port-class vars:
    export IS_UNIX IS_NT

    IS_NT=0; bldhost -is_nt $FORTE_PORT
    if [ $? -eq 0 ]; then
        IS_NT=1
    fi

    IS_UNIX=0; bldhost -is_unix $FORTE_PORT
    if [ $? -eq 0 ]; then
        IS_UNIX=1
    fi
}

set_global_vars()
{
    p=`basename $0`
    TMPA=/tmp/${p}A.$$

    ###############
    # BUILD CONTROL
    ###############

    export MAVEN_OPTIONS MAVEN_OFFLINE KITROOT_URL

    #some build steps can be run offline:
    MAVEN_OFFLINE="-o -npu"

    if [ $DOMAVEN_UPDATE -eq 0 ]; then
      MAVEN_OPTIONS="$MAVEN_OFFLINE"
      #if we incorporte offline options into MAVEN_OPTIONS,
      #then don't repeat for offline #build steps:
      MAVEN_OFFLINE=
    fi

    if [ $DOJUNIT -eq 1 ]; then
        #Make sure required environment is defined:
        if [ x$RUN_JUNIT = x ]; then
            DOJUNIT=0
            bldmsg -error -p $p "turning off -junit because RUN_JUNIT is not set to oracle"
        fi
    fi

    if [ $DOMAVEN_POP -eq 1 ]; then
        #Make sure required environment is defined:
        if [ x$MAVENPOP_IDENTITY = x ]; then
            DOMAVEN_POP=0
            bldmsg -error -p $p "turning off -popmaven because MAVENPOP_IDENTITY is not set"
        fi
    fi

    if [ $DOPUSHKIT -eq 1 ]; then
        #Make sure required environment is defined:
        if [ x$PUSHKIT_IDENTITY = x ]; then
            DOPUSHKIT=0
            bldmsg -error -p $p "turning off -pushkit because PUSHKIT_IDENTITY is not set"
        fi
        if [ x$PUSHKIT_DEST = x ]; then
            DOPUSHKIT=0
            bldmsg -error -p $p "turning off -pushkit because PUSHKIT_DEST is not set"
        fi
        if [ x$PUSHKIT_SRC = x ]; then
            DOPUSHKIT=0
            bldmsg -error -p $p "turning off -pushkit because PUSHKIT_SRC is not set"
        fi
    fi

    #if we passed the -pushkit sainty tests:
    if [ $DOPUSHKIT -eq 1 ]; then
        #make sure we do the release step:
        if [ $DORELEASE -eq 0 ]; then
            DORELEASE=1
            bldmsg -warn -p $p "turning on -release because -pushkit requires it."
        fi
        if [ $DOINSTALL -eq 0 ]; then
            DOINSTALL=1
            bldmsg -warn -p $p "turning on -install because -pushkit requires it."
        fi
    fi

    if [ $DOINSTALL -eq 1 ]; then
        #make sure we do the release step:
        if [ $DORELEASE -eq 0 ]; then
            DORELEASE=1
            bldmsg -warn -p $p "turning on -release because -install requires it."
        fi
    fi

    #note - I_AM_PRIMARY is set by parse_args.
    if [ $I_AM_PRIMARY -eq 0 ]; then
        if [ $RELEASE_BUILD -eq 1 ]; then
            #we don't build & release on non-primary machines during RE builds:
            if [ $DORELEASE -eq 1 ]; then
                #user specified -release on non-primary - issue usage warning and unset
                bldmsg -warn -p $p "-release has no effect on non-primary RE build machines."
                DORELEASE=0
            fi
        fi
    fi

    if [ $DORELEASE -eq 1 -a $DOBUILD -eq 1 -a $DOJAVADOC -eq 0 ]; then
        #we must build javadoc if we are building release:
        bldmsg -warn -p $p "turning on -javadoc because -release requires it."
        DOJAVADOC=0
    fi

    ##############
    # BRANCH NAMES
    ##############
    export JBI_BRANCH_NAME
    if [ "$JBI_BRANCH_NAME" = "trunk" ]; then
        JBI_BRANCH_NAME="main"
        bldmsg -warn -p $p "JBI_BRANCH_NAME 'trunk' is deprecated; changing to '$JBI_BRANCH_NAME'"
    elif [ "$JBI_BRANCH_NAME" = "" ]; then
        JBI_BRANCH_NAME="main"
        bldmsg -warn -p $p "defaulting JBI_BRANCH_NAME to '$JBI_BRANCH_NAME'"
    fi

    #########
    # JAVADOC
    # Warning:  if you change $JAVADOC_BASE here, change it in common_defs.bom and antbld/inc/prolog.ant
    #########
    export JAVADOC_BASE LASSEN_JAVADOC WHITNEY_JAVADOC SHASTA_JAVADOC ARCHIVE_JAVADOC 
    ARCHIVE_JAVADOC=$SRCROOT/$JBI_BRANCH_NAME/javadoc_stable
    JAVADOC_BASE=$SRCROOT/antbld/bld/doc
    LASSEN_JAVADOC=$JAVADOC_BASE/lassen
    WHITNEY_JAVADOC=$JAVADOC_BASE/whitney
    SHASTA_JAVADOC=$JAVADOC_BASE/shasta

    ###########
    # X-DISPLAY
    ###########
    export DISPLAY
    DISPLAY=$REGRESS_DISPLAY

    ##############
    # HTTP_KITROOT
    ##############
    export HTTP_KITROOT
    if [ "$HTTP_KITROOT" = "" ]; then
        HTTP_KITROOT="file://$KIT_DISTROOT"
        bldmsg -warn -p $p "defaulted HTTP_KITROOT to '$HTTP_KITROOT'"
    fi

    ###########
    # LOG FILES
    ###########
    export ANTLOGDIR
    ANTLOGDIR=$LOGDIR/antlogs

    export CLEANLOG BUILDLOG RELEASELOG INSTALLOG POPMAVENLOG
    export UNITTESTLOG UPDATELOG INTEGRATIONLOG PUSHKITLOG
    export JAVADOCLOG INSTALLER_TESTLOG 

    CLEANLOG=$LOGDIR/javaClean.log
    BUILDLOG=$LOGDIR/javaBuild.log
    RELEASELOG=$LOGDIR/makeRelease.log
    POPMAVENLOG=$LOGDIR/populateMaven.log
    INSTALLOG=$LOGDIR/makeInstall.log
    INSTALLER_TESTLOG=$LOGDIR/installerTest.log
    PUSHKITLOG=$LOGDIR/pushkit.log
    UNITTESTLOG=$LOGDIR/runUnitTests.log
    UPDATELOG=$LOGDIR/cvs_update.log
    INTEGRATIONLOG=$LOGDIR/integ_${PRODUCT}.txt
    JAVADOCLOG=$LOGDIR/javadoc.log

    #init vars for build_summary() routine.
    export TEST_SUMMARY_REPORT KIT_SUMMARY_REPORT
    TEST_SUMMARY_REPORT=$LOGDIR/runRegress.rpt
    KIT_SUMMARY_REPORT=$LOGDIR/kitSummary.rpt

    ### setup file to tell testResults which suite we are testing:
    REGSTAT=$LOGDIR/../regstat.ok
    rm -f $REGSTAT

    if [ "$PRI_BLD_LOCATION" = "" ]; then
      PRI_BLD_LOCATION=$SRCROOT
    fi

    ############
    # LOCK FILES
    ############
    KITSREADY=$LOCKDIR/kits.rdy
    SETRELEASEREADY=$LOCKDIR/release.rdy

    export MAX_KIT_WAIT
    if [ "$MAX_KIT_WAIT" = "" ]; then
        #wait up to 30 minutes (1800 seconds) for primary build:
        MAX_KIT_WAIT=1800
    fi

    #if we have a -bldnum arg, then reset BLDNUM (set by bld_setup_logdir).
    if [ $BLDNUM_ARG != NULL ]; then
        BLDNUM=$BLDNUM_ARG
    fi

    ###############
    # JBI_CVSROOT - this is the product CVSROOT used by this script.
    #                 You can override it if you are calling this script from
    #                 a special env. where CVSROOT means something else.
    ###############
    export JBI_CVSROOT
    if [ "$JBI_CVSROOT" = "" ]; then
        JBI_CVSROOT=$CVSROOT
    else
        bldmsg -warn -p $p "Setting JBI_CVSROOT from environment."
    fi

    #############
    # CVS OPTIONS
    #############
    export CVS_OPTS CVSIGNORE
    if [ "$CVS_OPTS" = "" ]; then
        #default is quiet, ignore ~/.cvsrc, read-only, compression:
        CVS_OPTS="-q -f -r -z6"
    else
        bldmsg -warn -p $p "Setting CVS_OPTS from environment."
    fi
    if [ "$CVSIGNORE" = "" ]; then
        CVSIGNORE='bld Makefile'
    else
        bldmsg -warn -p $p "Setting CVSIGNORE from environment."
    fi

    #####################
    # CVS CHECKOUT TARGET
    #####################
    # Use module lists from the environment, if specified. #
    if [ ! -z "$JBI_MODULES" ]; then
        JBI_CO_TARGET="$JBI_MODULES"
    else
        if [ "$JBI_BRANCH_NAME" = "main" ]; then
            JBI_CO_TARGET="trunk_build"
        else
            JBI_CO_TARGET="${JBI_BRANCH_NAME}_build"
        fi
    fi

    ####################
    # INTEGRATION REPORT
    ####################
    if [ $DOINTEGREPORT -eq 1 ]; then
        export INTEG_REPORT
        if [ "$INTEG_REPORT" = "" ]; then
            INTEG_REPORT=integrationReport
            bldmsg -warn -p $p "defaulting INTEG_REPORT to '$INTEG_REPORT'"
        fi
    fi

    ##########
    # JXTOHTML
    ##########
    export HTML_SRCROOT EXCEPTION_INDEX
    if [ "$HTML_SRCROOT" = "" ]; then
        if [ "$HTTP_LOGROOT" != "" ]; then
            HTML_SRCROOT="$HTTP_LOGROOT/$CODELINE/javadoc/regress_javadoc/src-html"
        else
            HTML_SRCROOT="http://iis.sfbay/jbi/main/javadoc/regress_javadoc/src-html"
        fi

        bldmsg -warn -p $p "defaulted HTML_SRCROOT to '$HTML_SRCROOT'"
    fi

    EXCEPTION_INDEX=$LOGDIR/_EXCEPTIONS.html

    return 0
}

check_local_vars()
#This routine is largely obsolete.  however you can
#include checks for variables that must be set in the
#environment and are not checked earlier in set_global_vars()
{
    localvarerrs=0

    if [ $localvarerrs -ne 0 ]; then
        return 1
    fi

    return 0
}

clear_local_locks()
{
    #place-holder for now.
    echo ""
}

################################# HOUSEKEEPING #################################

cleanup()
{
    if [ x$TMPA != x ]; then
        rm -f $TMPA
    fi

    #remove the pid file if we were not called from wrapper:
    if [ $CALLEDFROMWRAPPER -eq 0 ]; then
        rm -f $RUNBLD_PIDFILE
    fi

}

rec_signal()
{
    cleanup
    bldmsg -error -p $p Interrupted
    bldmsg -markend -status 2 $p

    exit 2
}

################################### INTERNAL ###################################

update_repo()
# Usage:  eval update_repo [-D date_tag] [-r alpha_tag] cvsroot dest_dir repos_name co_target
# you must quote date arg if it has spaces.
{
    _datearg=""
    _tagarg=""
    _prunearg=""

    while [ $# -gt 0 -a "$1" != "" ]
    do
        arg=$1; shift

        case $arg in
        -D )
            _datearg="-D '$1'"
            shift
            ;;
        -r )
            _tagarg="-r '$1'"
            shift
            ;;
        -* )
            echo "update_repo: unknown option, $arg"
            return 1
            ;;
        * )
            break
            ;;
        esac
    done

    cvsroot=$arg        #already did shift in loop
    _dest_dir=$1; shift
    repo_name="$1"; shift
    co_target="$1"

#echo update_repos: cvsroot=$cvsroot _dest_dir=$_dest_dir repo_name=$repo_name co_target=$co_target _datearg=$_datearg _tagarg=$_tagarg

    installdir $SRCROOT/$_dest_dir
    ret=$?
    if [ $ret -ne 0 ]; then
        bldmsg -error -p $p/update_repo "cannot create $SRCROOT/$_dest_dir - aborting checkout of $_dest_dir."
        return $ret
    fi

    bldmsg -markbeg -p $p update $repo_name source

    if [ "$_datearg" = "" ]; then
        _prunearg="-P"
    else
        #Note that -D <date> implies -P, so we don't need it:
        _prunearg=""
    fi

    #bldmsg -warn "turning off CVS timestamp and prune args to update until jbi source tree is populated"
    #_prunearg=""
    #_datearg=""

    #hack to install top-level CVS dir
    if [ ! -r $SRCROOT/CVS/Root ]; then
        rm -rf  $SRCROOT/CVS
        rm -rf $SRCROOT/tmp/$CVS_SRCROOT_PREFIX

        mkdir -p $SRCROOT/tmp
        cd $SRCROOT/tmp


        bldmsg -mark "Create top-level CVS dir: `echo $cmd`"
        cmd="cvs $CVS_OPTS -d $cvsroot checkout -l -A $_prunearg $_tagarg $_datearg $CVS_SRCROOT_PREFIX"
        eval $cmd
        ret=$?

        if [ $ret -ne 0 ]; then
            bldmsg -error -p $p Could not checkout top-level CVS directory, $CVS_SRCROOT_PREFIX/CVS
            bldmsg -markend -p $p -status $ret update $repo_name source
            return $ret
        else
            mv $SRCROOT/tmp/$CVS_SRCROOT_PREFIX/CVS $SRCROOT
            rm -rf $SRCROOT/tmp/$CVS_SRCROOT_PREFIX
        fi
    fi

    cd $SRCROOT/$_dest_dir
    cwd=`pwd`

    # Use 'eval' to delay interpretation of embedded quotes:
    cmd="cvs $CVS_OPTS -d $cvsroot checkout -A $_prunearg $_tagarg $_datearg $co_target"
    bldmsg -mark "In $cwd: `echo $cmd`"
    eval $cmd
    ret=$?

    bldmsg -markend -p $p -status $ret update $repo_name source

    return $ret
}

set_update_time()
{
    export BLDTIME UCVSUPDATETIME JBI_SNAPSHOT_TIME

    BLDTIME=""
    touch $SRCROOT/bldlock/bldenv.sh

    UCVSUPDATETIME=`bld_gmttime`

    if [ $RELEASE_BUILD -eq 1 ]; then
        if [ $I_AM_PRIMARY -eq 1 ]; then
            #always set the build time for primary
            if [ $DOUPDATE -eq 1 ]; then
                shprops -set $SRCROOT/bldlock/bldenv.sh LASTUPDATETIME=$UCVSUPDATETIME
            else
                tmpPRODUCT=jbi
                eval `shprops -get $LASTBLDPARMS ${tmpPRODUCT}_last_update`
                cmd="echo \$${tmpPRODUCT}_last_update"
                LOCAL_LASTUPDATETIME=`eval $cmd`
                shprops -set $SRCROOT/bldlock/bldenv.sh LASTUPDATETIME=$LOCAL_LASTUPDATETIME
            fi
        else
            if [ ! -f $PRIPATHREF/bldlock/bldenv.sh ]; then
                bldmsg -mark -error -p $p "RELEASE BUILD:  Cannot access \$PRIPATHREF/bldlock/bldenv.sh - has primary build fired? PRIPATHREF='$PRIPATHREF'"
                return 1
            fi

            eval `shprops -get $PRIPATHREF/bldlock/bldenv.sh LASTUPDATETIME`
            UCVSUPDATETIME=$LASTUPDATETIME
            echo "LASTUPDATETIME=${LASTUPDATETIME}" > $SRCROOT/bldlock/bldenv.sh
        fi
    fi

    BLDTIME=`bld_tocvstime $UCVSUPDATETIME`
    JBI_SNAPSHOT_TIME=`echo $UCVSUPDATETIME | perl -n -e '$xx=$_; printf "%s.%s-1", substr($xx,0,8), substr($xx,8,6);'`
    bldmsg "JBI_SNAPSHOT_TIME=$JBI_SNAPSHOT_TIME"
    return 0
}

update_src()
{
    if [ $TESTMODE -eq 1 ]; then
        return 0
    fi

    bldmsg "BUILDRESULTS_TYPE=cvsupdate"

    # Print the time in both formats. *
    bldmsg -mark -p $p/update_src BLDTIME in CVS format: \"$BLDTIME\"

    #######
    # setup the source update:
    #######
    #default branch to env name if it is not mainline:
    if [ "$JBI_BRANCH_NAME" != "main" ]; then
        jbi_revarg="-r '$JBI_BRANCH_NAME'";
    else
        jbi_revarg="";
    fi

    #setup the date arg. default, which is BLDTIME for RE builds:
    if [ $RELEASE_BUILD -eq 1 ]; then
        jbi_datearg="-D '$BLDTIME'"
    else
        jbi_datearg=""
    fi

    srcdirs="NEED_TO_ADD"

    #######
    # start the source update:
    #######
    if [ $DOCLEANSRC -eq 1 ]; then
        #also remove local bdb and release files:
        cleandirs="$srcdirs $SRCROOT/bdb $RELEASE_DISTROOT"

        bldmsg -mark -p $p/update_src "Removing source: $cleandirs"
        cd $SRCROOT/..
        bld_killall -bg $cleandirs
        if [ $? -ne 0 ]; then
            bldmsg -warn -p $p/update_src "had trouble removing source, continuing"
        fi
        cd $SRCROOT
    fi

    update_src_errs=0

    #note that all module checkouts for open-esb are relative to $SRCROOT/..
    eval update_repo $jbi_datearg $jbi_revarg $JBI_CVSROOT .. product "'$JBI_CO_TARGET'"
    c_status=$?

    if [ $c_status -eq 0 ]; then
        bldmsg -p $p/update_src -mark $PRODUCT source update SUCCESSFUL.

        bldmsg -mark -p $p "Setting jbi_last_update to '$UCVSUPDATETIME' in '$BLDPARMS'"
        shprops -set $BLDPARMS jbi_last_update=$UCVSUPDATETIME
    else
        update_src_errs=1
        bldmsg -p $p/update_src -error $PRODUCT FAILED with status $c_status - check log for errors.
        bldmsg -warn -p $p "Not setting jbi_last_update in '$BLDPARMS' because the CVS update failed"
    fi

    return $update_src_errs
}

build_product()
{
    if [ $TESTMODE -eq 1 ]; then
        return 0
    fi

    bldmsg "BUILDRESULTS_TYPE = ant_generic"
    build_product_errs=0

    cd $SRCROOT

    MAVEN_GOALS="install"
    cmd="mvn $MAVEN_OPTIONS -DSRCROOT='$JV_SRCROOT' -Dmaven.repo.local='$JV_SRCROOT/m2/repository' -Dmaven.test.skip=true -DBUILD_NUMBER=$BLDNUM $MAVEN_GOALS"
    bldmsg -mark -p $p/build_product `echo $cmd`

    eval $cmd
    status=$?

    if [ $status -ne 0 ]; then
       bldmsg -error -p $p/build_product "$cmd FAILED"
       build_product_errs=1
       return $build_product_errs
    fi

    #cd $SRCROOT/../loader
    #cmd="ant jar"
    #bldmsg -mark -p $p/build_product `echo $cmd`
    #eval $cmd
    #status=$?
    #
    #if [ $status -ne 0 ]; then
    #   bldmsg -error -p $p/build_product "$cmd FAILED"
    #   build_product_errs=1
    #   return $build_product_errs
    #fi

    return $build_product_errs
}



build_javadoc()
{
    if [ $TESTMODE -eq 1 ]; then
        return 0
    fi

    bldmsg "BUILDRESULTS_TYPE = odmmi_javabuild"

    cd $SRCROOT/build-common
    cmd="ant -DSRCROOT='$JV_SRCROOT' -Dmaven.repo.local='$JV_SRCROOT/m2/repository' -f m2.ant openesb-javadoc"

    eval $cmd
    return $?
}

clean_maven()
{
  rm -rf "$SRCROOT/m2"
  return $?
}


clean_build_tree()
#NOTE:  maven always loads dependencies prior to running a target.
#Therefore, we have to do a clean:build stepwise for each sub-project.
{
    if [ $TESTMODE -eq 1 ]; then
        return 0
    fi

    cd $SRCROOT

    bldmsg "BUILDRESULTS_TYPE = odmmi_javabuild"

    MAVEN_GOALS="clean"

    cmd="mvn $MAVEN_OPTIONS -DSRCROOT='$JV_SRCROOT' -Dmaven.repo.local='$JV_SRCROOT/m2/repository' -Dmaven.test.skip=true -DBUILD_NUMBER=$BLDNUM $MAVEN_GOALS"
    bldmsg -mark -p $p/clean_build_tree `echo $cmd`

    eval $cmd
    return $?
}


make_release()
#this routine is normally only called on primary port
{
    if [ $TESTMODE -eq 1 ]; then
        return 0
    fi

    kiterrs=0

    bldmsg "BUILDRESULTS_TYPE=general"

    ######
    #build release dir:
    ######
    makedrv -auto -q -c bdb
    #makedrv -auto -q -b jbiroot.cmn -c mmf

    kitbase=$KIT_DISTROOT/$KIT_REV
    bldnum=Build$BLDNUM
    bldnumfile=$kitbase/.bldnum
    relstage="$SRCROOT/bld/release"

    #create release staging area:
    rm -rf $relstage
    installdir -m 0775 $relstage

    #create version files:
    versionfile="${relstage}/version.txt"
    echo $bldnum  >> $versionfile 2>&1


    if [ ! -d $kitbase ]; then
        installdir -m 0775 $kitbase
    fi

    if [ ! -f $bldnumfile ]; then
        touch $bldnumfile
    fi

    kitdir=$kitbase/$bldnum/cmn
    bom=odmmi.bom

    bldmsg -mark -p $p/make_release Release $bom to $kitdir

#start creating the kit report:
cat << MSG_EOF >> "$KIT_SUMMARY_REPORT"
===========
KITS POSTED:
===========

MSG_EOF


    release -bomloc $SRCROOT/rl/src/cmn/bom -ver $BLDNUM -log -checksum -w $kitdir $bom
    status=$?

    if [ $status -ne 0 ]; then
        bldmsg -error -p $p/make_release Release of $bom to $KIT_DISTROOT FAILED
        kiterrs=1
    else
        echo "    $HTTP_KITROOT/$KIT_REV/$bldnum"  >> "$KIT_SUMMARY_REPORT"
        
    fi

    ##note the build number, status, etc in top level dir:
    touch -f "$bldnumfile" && chmod +w "$bldnumfile"
    if [ $? -eq 0 ]; then
        tmp1=`date '+%Y%m%d%H%M%S'`
        tmp2=`logname`
        echo $bldnum $kiterrs $tmp1 $tmp2 >> "$bldnumfile"
        bldmsg -mark Updated bldnum file, $bldnumfile
    else
        bldmsg -error -p $p/make_release "Failed to update bldnum file, '$bldnumfile'."
        kiterrs=1
    fi

    if [ $kiterrs -eq 0 ]; then
        #put the bldnum in the release.ready file
        bldmsg -mark "Creating $SETRELEASEREADY file."
        echo $bldnum > $SETRELEASEREADY
    fi

    # save off the maven components used in the build
    kitdir=$kitbase/$bldnum
    make_mavenarchive "$kitdir"
    if [ $? -ne 0 ]; then
      bldmsg -error -p $p/make_release make_mavenarchive FAILED
      kiterrs=1
    fi

    if [ $kiterrs -ne 0 ]; then
        echo "    ERRORS POSTING KITS - ONE OR MORE KITS INVALID"  >> "$KIT_SUMMARY_REPORT"
    fi
    echo " "  >> "$KIT_SUMMARY_REPORT"


    # Create latest symlink in kits directory
    cmd="rm ${kitbase}/.previouslatest"
    bldmsg -mark removing .previouslatest symlink - `echo $cmd`
    eval $cmd
    cmd="mv ${kitbase}/latest ${kitbase}/.previouslatest"
    bldmsg -mark moving latest symlink to .previouslatest symlink - `echo $cmd`
    eval $cmd
    cmd="ln -s ${kitbase}/${bldnum} ${kitbase}/latest"
    bldmsg -mark adding new latest symlink - `echo $cmd`
    eval $cmd

    return $kiterrs
}


populate_maven()
{
    if [ $TESTMODE -eq 1 ]; then
        return 0
    fi

    populate_maven_errs=0
    bldmsg "BUILDRESULTS_TYPE=general"

    deploy_defs="deploy:deploy-file -Durl=file://$MAVENPOP_IDENTITY -DrepositoryId=sun-private -Dpackaging=jar -DgeneratePom=true"

    ## matcher.jar ##
    filedir=$SRCROOT/../open-dm-dq/matcher/target
    groupId=mural
    file="matcher.jar"
    artifactId=matcher
    version=1.0-*SNAPSHOT
    echo "mvn $deploy_defs -Dfile=$filedir/$file -DgroupId=$groupId -DartifactId=$artifactId -Dversion=$version -Dmaven.repo.local=$SRCROOT/m2/repository"
    cmd="mvn ${deploy_defs} -Dfile=${filedir}/${file} -DgroupId=${groupId} -DartifactId=${artifactId} -Dversion=${version} -Dmaven.repo.local=${SRCROOT}/m2/repository"

    eval $cmd
    return $?
}


push_kit()
{
    if [ $TESTMODE -eq 1 ]; then
        return 0
    fi

    push_kit_errs=0

    pushkitsrc="$PUSHKIT_SRC/$bldnum"
    pushkitlist="$pushkitsrc/index.html $pushkitsrc/CORE $pushkitsrc/jbise"

    #Make sure source location is reachable
    if [ ! -d "$pushkitsrc" ]; then
        bldmsg -error -p $p/pushkit "can not access source dir '$pushkitsrc' - ABORT"
        push_kit_errs=1
        return $push_kit_errs
    fi

    #####
    #test the ssh connection, by copying a file from local /tmp to remote /tmp:
    #####
    _pushkit_test=/tmp/pushkit_sshtest.$$
    touch -f $_pushkit_test
    scp -B $_pushkit_test ${PUSHKIT_IDENTITY}:$_pushkit_test
    if [ $? -ne 0 ]; then
        bldmsg -error -p $p/push_kit "Cannot copy test file '$_pushkit_test' using ssh identity '$PUSHKIT_IDENTITY' - ABORT"
        push_kit_errs=1
        rm -f $_pushkit_test
        return $push_kit_errs
    fi

    ###########
    #connection okay - remove local & remote test files, create destination directory:
    ###########
    rm -f $_pushkit_test
    cmd="ssh $PUSHKIT_IDENTITY 'rm -f $_pushkit_test && mkdir -p $PUSHKIT_DEST/$bldnum'"
    bldmsg -mark -p $p/pushkit "$cmd"
    eval "$cmd"
    status=$?
    if [ $status -ne 0 ]; then
        bldmsg -error -p $p/push_kit "$cmd FAILED"
        push_kit_errs=1
        return $push_kit_errs
    fi

     #copy kits.  note -B option is for "batch", and prevents looping for password entry:
     cmd="scp -B -pr $pushkitlist ${PUSHKIT_IDENTITY}:$PUSHKIT_DEST/$bldnum"
     bldmsg -mark -p $p/pushkit "$cmd"
     eval $cmd
     status=$?
     if [ $status -ne 0 ]; then
         bldmsg -error -p $p/push_kit "$cmd FAILED"
         push_kit_errs=1
         return $push_kit_errs
     else
         bldmsg -p $p/pushkit "copy to pushkit location succeeded"
     fi

     ####
     # Create latest symlink in PUSHKIT_DEST directory
     ####
     cmd="ssh $PUSHKIT_IDENTITY 'cd $PUSHKIT_DEST && rm -f .previouslatest && ((test -d latest &&  mv latest .previouslatest) || rm -f latest) && ln -s $bldnum latest'"
     bldmsg -mark -p $p/pushkit "$cmd"
     eval $cmd
     status=$?
     if [ $status -ne 0 ]; then
         bldmsg -error -p $p/push_kit "$cmd FAILED"
         push_kit_errs=1
         return $push_kit_errs
     fi

     return $push_kit_errs
}




make_mavenarchive()
#Usage:  make_mavenarchive  "$kitdir"
{
    kitdir="$1"
    if [ -z "$kitdir" ]; then
        bldmsg -error -p $p/make_mavenarchive "Usage: make_mavenarchive kitdir "
        return 1
    fi

    if [ $DEBUG -ne 0 ]; then
        cat << EOF
IN make_mavenarchive:
    kitdir=>$kitdir<
EOF
    fi

    make_mavenarchive_errs=0

    maven_kitbase=$kitdir/maven

    bldmsg -mark -p $p/make_mavenarchive Archiving m2 to bld/m2.zip using maven_archive.bom
    cd $SRCROOT
    jar -Mcf $SRCROOT/bld/release/m2.zip m2
    bldmsg -mark -p $p/make_mavenarchive Release maven_archive.bom to $maven_kitbase
    installdir $maven_kitbase
    release -bomloc $SRCROOT/rl/src/cmn/bom -ver $BLDNUM -log -checksum -w $maven_kitbase maven_archive.bom
    status=$?
    if [ $status -ne 0 ]; then
        bldmsg -error -p $p/make_mavenarchive releasing m2.zip via maven_archive.bom FAILED
        make_mavenarchive_errs=1
    fi

    return $make_mavenarchive_errs
}


run_unit_tests()
{
    if [ $TESTMODE -eq 1 ]; then
        return 0
    fi

    bldmsg "BUILDRESULTS_TYPE=jbi_regress"

    cd $SRCROOT
    run_tests_status=0

    ######
    #junit 
    ######
    bldmsg -markbeg ${p}:junit
    cmd="mvn $MAVEN_OPTIONS -DSRCROOT='$JV_SRCROOT' -Dmaven.repo.local='$JV_SRCROOT/m2/repository' -DBUILD_NUMBER=$BLDNUM test"
    bldmsg -mark -p $p/run_unit_tests `echo $cmd`
    eval $cmd
    status=$?
    if [ $status -ne 0 ]; then
        run_tests_status=1
        bldmsg -error -p $p/run_unit_tests junit test step FAILED
    fi
    bldmsg -markend -status $status ${p}:junit
    bld_reset_watchdog


    #####
    # run the surefire report
    #####
    cmd="mvn $MAVEN_OPTIONS -DSRCROOT='$JV_SRCROOT' -Dmaven.repo.local='$JV_SRCROOT/m2/repository' -DBUILD_NUMBER=$BLDNUM surefire-report:report-only"
    bldmsg -mark -p $p/run_unit_tests `echo $cmd`
    eval $cmd
    status=$?
    if [ $status -ne 0 ]; then
        run_tests_status=1
        bldmsg -error -p $p/run_unit_tests surefire report step FAILED
    fi
    bldmsg -markend -status $status ${p}:junit
    bld_reset_watchdog



    ############
    #junitreport (archival; report is done above, in "test" step)
    ############

    #archive junit report:
    if [ -d "$SRCROOT/index-core/target/JUnitReport" ]; then
        bldmsg -mark -p $p/run_tests Archive junit test results
        cp -rp $SRCROOT/index-core/target/JUnitReport $LOGDIR
        if [ $? -ne 0 ]; then
            run_tests_status=1
            bldmsg -error -p $p/run_tests Archive junit test results FAILED
        fi
    fi
    bld_reset_watchdog

    #archive surefire report:
    if [ -d "$SRCROOT/target/site" ]; then
        bldmsg -mark -p $p/run_tests Archive surefire test results
        cp -rp $SRCROOT/target/site $LOGDIR/surefire_tests
        if [ $? -ne 0 ]; then
            run_tests_status=1
            bldmsg -error -p $p/run_tests Archive surefire test results FAILED
        fi
    fi
    bld_reset_watchdog

    return $run_tests_status
}

build_summary()
#create a summary status of the test step, if tests were run
{
    rm -f "$TEST_SUMMARY_REPORT"
    touch "$TEST_SUMMARY_REPORT"

    jdk_version=`2>&1 java -version | grep 'Java(TM)'`
    date=`date`
    host=`uname -n`

    #write the summary header:
    cat >> "$TEST_SUMMARY_REPORT" << EOF
    TEST RESULTS $date ($host/$FORTE_PORT/${BLDNUM}):
    $jdk_version
    ODMMI Codeline = open-dm-mi{$CVS_BRANCH_NAME}

EOF

    # if no tests were run ...
    if [ $DOJUNIT -eq 0 ]; then
        cat >> "$TEST_SUMMARY_REPORT" << EOF
NO TESTS RUN

EOF
    ##
    else
        #... we ran some tests:
        #junit_summary="NO JUNIT TEST RESULTS"

        if [ -r "$UNITTESTLOG"  ]; then
           junit_summary=`junitreport $UNITTESTLOG`
        fi

        #if [ -r "$UNITTESTLOG"  ]; then
        #    junit_summary=`sed -n -e "/${PRODUCT}:junit_report/,/junit.failure.list/p" "$UNITTESTLOG" | grep 'junit[\._]'`
        #    [ "$junit_summary" = "" ] && junit_summary="NO JUNIT TEST RESULTS"
        #fi

        cat >> "$TEST_SUMMARY_REPORT" << EOF
 
    JUNIT Report:  
$junit_summary

EOF
    ##
    fi
}


run_background_tasks()
#start a thread to run supplemental build tasks in parallel with regression
#WARNING:  we cannot set any vars in parent process, so we use shprops to
#communicate results.
{
    #note - we add the begin/end marks here
    #so we know the actual time the background tasks take,
    #(otherwise, get the run_tests times).
    bldmsg -markbeg ${p}:background tasks

    BG_BUILD_STATUS=0
    shprops -set $BG_RESULTS BG_BUILD_STATUS=$BG_BUILD_STATUS

#    if [ $DOJAVADOC -eq 1 ]; then
#        bldmsg -mark Building javadoc - Log is $JAVADOCLOG
#        bldmsg -markbeg ${p}:javadoc
#        build_javadoc >> $JAVADOCLOG 2>&1
#        status=$?
#        if [ $status -ne 0 ]; then
#            bldmsg -error -p $p build_javadoc failed. Check $JAVADOCLOG for errors.
#            BG_BUILD_STATUS=1
#            shprops -set $BG_RESULTS BG_BUILD_STATUS=$BG_BUILD_STATUS
#        fi
#        bldmsg -markend -status $status ${p}:javadoc
#        bld_reset_watchdog
#    fi

    release_failed=0
    if [ $DORELEASE -eq 1 -a $BUILD_FAILED -eq 0 ]; then
        bldmsg -mark Releasing $PRODUCT - Log is $RELEASELOG
        bldmsg -markbeg ${p}:make_release
        make_release >> $RELEASELOG 2>&1
        status=$?
        if [ $status -ne 0 ]; then
            bldmsg -error -p $p make_release failed. Check $RELEASELOG for errors.
            release_failed=1
            BG_BUILD_STATUS=1
            shprops -set $BG_RESULTS BG_BUILD_STATUS=$BG_BUILD_STATUS
        fi
        bldmsg -markend -status $status ${p}:make_release
        bld_reset_watchdog
    elif [ $DORELEASE -eq 1 -a $BUILD_FAILED -ne 0 ]; then
        bldmsg -error Skipping make_release because build step failed
    fi
    bld_reset_watchdog


    popmaven_failed=0
    if [ $DOMAVEN_POP -eq 1 -a $release_failed -eq 0 -a $BUILD_FAILED -eq 0 ]; then
        bldmsg -mark Populating maven repository - Log is $POPMAVENLOG
        bldmsg -markbeg ${p}:populate_maven
        populate_maven >> $POPMAVENLOG 2>&1
        status=$?
        if [ $status -ne 0 ]; then
            bldmsg -error -p $p populate_maven failed. Check $POPMAVENLOG for errors.
            popmaven_failed=1
            BG_BUILD_STATUS=1
            shprops -set $BG_RESULTS BG_BUILD_STATUS=$BG_BUILD_STATUS
        fi
        bldmsg -markend -status $status ${p}:populate_maven
        bld_reset_watchdog
    else
        bldmsg -error Skipping populate_maven because release or build step failed
    fi
    bld_reset_watchdog


#    pushkit_failed=0
#    if [ $DOPUSHKIT -eq 1 -a $release_failed -eq 0 -a $install_failed -eq 0 -a $BUILD_FAILED -eq 0 ]; then
#        bldmsg -mark Pushing kits of $PRODUCT to external site - Log is $PUSHKITLOG
#        bldmsg -markbeg ${p}:push_kit
#        push_kit >> $PUSHKITLOG 2>&1
#        status=$?
#        if [ $status -ne 0 ]; then
#            bldmsg -error -p $p push_kit failed. Check $PUSHKITLOG for errors.
#            pushkit_failed=1
#            BG_BUILD_STATUS=1
#            shprops -set $BG_RESULTS BG_BUILD_STATUS=$BG_BUILD_STATUS
#        fi
#        bldmsg -markend -status $status ${p}:push_kit
#        bld_reset_watchdog
#    elif [ $DOPUSHKIT -eq 1 -a $DORELEASE -eq 1 -a $BUILD_FAILED -ne 0 ]; then
#        bldmsg -error Skipping push_kit because build step failed
#    fi

    bldmsg -markend -status $BG_BUILD_STATUS ${p}:background tasks
}

#################################### MAIN #####################################

p=`basename $0`
saveargs="$@"
BUILD_STATUS=0

#source common build routines or die.
#NOTE - all "bld_<name>" routines come from this module:
require bldcmn.sh

parse_args "$@"
if [ $? -ne 0 ]; then
    echo
fi

#if asking for help, display help of all scripts called by this one.
if [ $DOHELP -eq 1 ]; then
    #none written yet...
    exit 0
fi

show_initial_options

#make sure we have a /tmp dir on nt:
bld_check_create_tmp

bld_check_usepath
if [ $? -ne 0 ]; then
    bld_fatal_error "one or more usePathDef variables not set - ABORT"
fi

if [ $CALLEDFROMWRAPPER -eq 0 ]; then
    setup_wrapper_env
fi

set_global_vars
if [ $? -ne 0 ]; then
    bld_fatal_error "Error setting global variables.  Message should have been provided. - ABORT"
fi

check_local_vars
if [ $? -ne 0 ]; then
    bld_fatal_error "one or more required environment variables not set - ABORT"
fi

show_final_options
show_build_environment

#this will get updated at the end of the build.
#if it does not, then program was interrupted or crashed:
shprops -set $BLDPARMS BUILD_STATUS=1

bldmsg -markbeg $p
#don't trap interrupts until initial start message.
trap rec_signal 2 15

clear_local_locks

#this updates the runBuild wrapper watchdog timer if it exists:
bld_reset_watchdog

if [ $DOMAVENCLEAN -eq 1 ]; then
    bldmsg -mark Removing maven local repository in $SRCROOT/m2
    bldmsg -markbeg ${p}:clean_maven
    clean_maven
    status=$?
    if [ $status -ne 0 ]; then
        bldmsg -error -p $p clean_maven failed.
        BUILD_STATUS=1
    fi
    bldmsg -markend -status $status ${p}:clean_maven
    bld_reset_watchdog
fi

if [ $DOCLEAN -eq 1 ]; then
    bldmsg -mark Cleaning build areas - log is $CLEANLOG
    bldmsg -markbeg ${p}:clean_build_tree
    clean_build_tree >> $CLEANLOG 2>&1
    status=$?
    if [ $status -ne 0 ]; then
        bldmsg -error -p $p clean_build_tree failed. Check $CLEANLOG for errors.
        BUILD_STATUS=1
    fi

    #reduce download noise:
    filter_maven_log_in_place $CLEANLOG

    bldmsg -markend -status $status ${p}:clean_build_tree
    bld_reset_watchdog
fi

#set the update time, which is used by the update task and the integration report task
set_update_time
status=$?

if [ $status -ne 0 ]; then
    bld_fatal_error "CANNOT set CVS update time - ABORT"
fi

# ADD LATER - KH
#if [ $DOINTEGREPORT -eq 1 ]; then
#    #run integration report:
#    bldmsg -mark Creating integration report - output is $INTEGRATIONLOG
#    bldmsg -markbeg ${p}:$INTEG_REPORT
#    $INTEG_REPORT -o $INTEGRATIONLOG $UCVSUPDATETIME
#    if [ $? -ne 0 ]; then
#        bldmsg -error ${p}:$INTEG_REPORT failed for $PRODUCT repostiorty.
#        status=1
#    fi
#
#    $INTEG_REPORT -tools -o $TMPA $UCVSUPDATETIME
#    if [ $? -ne 0 ]; then
#        bldmsg -error ${p}:$INTEG_REPORT failed for devtools repostiorty.
#        status=2
#    else
#        cat $TMPA >> $INTEGRATIONLOG
#        rm -f $TMPA
#    fi
#
#    bldmsg -markend -status $status ${p}:$INTEG_REPORT
#
#
#    bld_reset_watchdog
#fi

# FIX LATER KH
#if [ $DOUPDATE -eq 1 ]; then
#    bldmsg -mark Updating source code - log is $UPDATELOG
#    bldmsg -markbeg ${p}:update_src
#    update_src >> $UPDATELOG 2>&1
#    status=$?
#    bldmsg -markend -status $status ${p}:update_src
#    if [ $status -ne 0 ]; then
#        bldmsg -error -p $p update_src failed. Check $UPDATELOG for errors.
#        BUILD_STATUS=1
#
#        ### abort entire build if RE build, since we cannot issue kits
#        ### built from a corrupted working directory:
#        if [ $RELEASE_BUILD -eq 1 ]; then
#            bldmsg -error -p $p BUILD ABORTED DUE TO CVS UPDATE ERRORS.
#            bldmsg -markend -status $BUILD_STATUS $p
#            exit $BUILD_STATUS
#        fi
#    else
#        #temporarily ignore m2 files, so we can get meaningful buildResults.  RT 7/24/06
#        mv $UPDATELOG ${UPDATELOG}.tmp
#        egrep -v '/pom.xml$|/settings.xml$|/m2.ant$' ${UPDATELOG}.tmp > $UPDATELOG
#        rm -f ${UPDATELOG}.tmp
#    fi
#    bld_reset_watchdog
#fi

BUILD_FAILED=0
if [ $DOBUILD -eq 1 ]; then
    bldmsg -mark Building $PRODUCT - Log is $BUILDLOG
    bldmsg -markbeg ${p}:build_product
    build_product >> $BUILDLOG 2>&1
    status=$?

    #reduce download noise:
    filter_maven_log_in_place $BUILDLOG

    #this additional check is necessary because we tell ant to ignore various errors,
    #so ant always returns a zero status.
    if [ $status -eq 0 ]; then
        has_ant_errors $BUILDLOG
        status=$?
    fi

    if [ $status -ne 0 ]; then
        bldmsg -error -p $p build_product failed. Check $BUILDLOG for errors.
        BUILD_STATUS=1
        BUILD_FAILED=1

        #if -keepon specified ...
        if [ $KEEPON -eq 1 ]; then
            #.. then pretend the compile didn't fail:
            BUILD_FAILED=0
        fi
    fi
    bldmsg -markend -status $status ${p}:build_product
    bld_reset_watchdog
fi

#####
#FORK background tasks:
#####
export BG_LOG BG_RESULTS
BG_LOG=$LOGDIR/background_tasks.log
BG_RESULTS=$LOGDIR/background_results.sh

run_background_tasks > $BG_LOG &
bgpid=$!

######
#junit
######
if [ $DOJUNIT -eq 1 -a $BUILD_FAILED -eq 0 ]; then
    run_unit_tests >> $UNITTESTLOG 2>&1
    status=$?
    bld_reset_watchdog

    #scan for ignored ant task errors:
    if [ $status -eq 0 ]; then
        has_ant_errors $UNITTESTLOG
        status=$?
    fi

    if [ $status -ne 0 ]; then
        bldmsg -error -p $p One or more unit tests failed. Check $UNITTESTLOG for errors.
        BUILD_STATUS=1
    fi
elif [ $DOJUNIT -eq 1  -a $BUILD_FAILED -ne 0 ]; then
    bldmsg -error Skipping unit tests because build step failed
fi


#########
#WAIT FOR background tasks if necessary:
#########
wait $bgpid
eval `shprops -get $BG_RESULTS BG_BUILD_STATUS`

bldmsg -mark Results of run_background_tasks tasks follows:
cat $BG_LOG
bldmsg -mark "##### EOF (run_background_tasks)"

#rm -f $BG_LOG $BG_RESULTS

if [ $BG_BUILD_STATUS -ne 0 ]; then
    BUILD_STATUS=1
fi

if [ $BUILD_STATUS -eq 0 ]; then
    #update the last good build time in the build parameters file:
    shprops -set $BLDPARMS ULASTGOODBLDTIME=$UBLDSTARTTIME
fi

shprops -set $BLDPARMS BUILD_STATUS=$BUILD_STATUS

#collect logs and run ant summary report:
build_summary

bldmsg -markend -status $BUILD_STATUS $p

cleanup

exit $BUILD_STATUS
