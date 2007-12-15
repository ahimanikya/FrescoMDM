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
# @(#)runbld.sh
# Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
#
# END_HEADER - DO NOT EDIT
#

#
# runBuild - wrapper script to setup logging & build tools for the build.
#   This allows us to replace tools all tools except this one.
#
# Conventions:  Lowercase names are generally local vars (not exported).
#               Uppercase names are generally exported vars.
#

############################### USAGE ROUTINES ################################

usage()
{
    exit_status=$1

    cat << EOF
Usage:  $p [-help] [local_options...] [local_build_script_args...]

 Update the tools, and then run the $PRODUCT build system.

Options:
 -help     Display this message.
 -test     Run all scripts in non-destructive test mode.
           Do not actually build anything.
 -wait     Wait for tools.rdy file appear on primary build port
 -clean    Do a clean build.  Removes RELEASE_ROOT.
 -cleanmavenrepo
           Removes the $SRCROOT/maven directory
 -pull     Update tools before building.  RELEASE DEFAULT
 -nopull   Skip tools update.  DEVELOPER DEFAULT
 -update|-fast|-fastupdate
           Pull tools and pass flag to toolsBuild and $localBuild.
 -nopull   Skip the tools update. DEVELOPER DEFAULT
 -tools    Build the tools distribution if we are running on \$PRIMARY_PORT.
           On non primary ports, wait for primary tools build before pull
           when -pull specified.  RELEASE DEFAULT.
 -notools  Do NOT build the tools distribution.  DEVELOPER DEFAULT.
 -email    Send email on success/failure. RELEASE DEFAULT.
 -noemail  Do NOT send email.  DEVELOPER DEFAULT.
 -bres     Include first 500 lines ofbuildResults output in email.
 -nobres   Do not include buildResults in the email.
 -autobuild
           build only if files have been integrated. \$MAKE_BUILD_DECISION
           can be used to override default script name.  See below.
 -bldnum num 
           Use <num> for BLDNUM instead of generating a new one.
           (Useful for re-relasing kits without a new build).

 Remaining arguments are passed on to local build script.

Environment:

\$HTTP_LOGROOT        set this to the root URL where you want to expose
                     the test and build logs on the web.  (used to report
                     web locations in build email).

\$RELATIVE_LOGDIR     This is the intermediate directory used to expose
                     build and test logs through HTTP_LOGROOT.
                     I.e., \$HTTP_LOGROOT/\$RELATIVE_LOGDIR/\$BUILD_DATE
                     will be the generated URL for build logs.
                     Do not set over-ride this setting unless you have
                     special requirements on your web server.

\$TESTPAGE_URL        This a page relative to your build log url that
                     summarize test results for a build.  Defaults
                     to junitreport/html/index.html.

\$MAKE_BUILD_DECISION executable script that returns 0 if the build is to proceed,
                     1 if there are no updates to process, and 2 if there are
                     errors detected by the script.  This script is run at the
                     very beginning of the script, prior to setting up \$LOGROOT.
                     Script name defaults to "need\${PRODUCT}Build".

EOF

}

parse_args()
{
    ### set option defaults according to builder profile.
    bld_set_builder_profile

    DOCLEAN=0; cleanarg=
    DOMAVENCLEAN=0; cleanmavenarg=
    DOHELP=0; helparg=
    DOWAIT=0
    DOAUTOBUILD=0
    DOBRES=1
    BLDNUM=
    fbargs=

    if [ $RELEASE_BUILD -eq 1 ]; then
        DOEMAIL=1
        DOUPDATE=1
        DOTOOLS=1
        DOSRCUPDATE=1
        DOPULL=1
    else
        DOEMAIL=0
        DOUPDATE=0
        DOTOOLS=0
        DOSRCUPDATE=0
        DOPULL=0
    fi

    while [ $# -gt 0 ]
    do
        arg=$1; shift

        case $arg in
        -h* )
            DOHELP=1; helparg="-help"
            usage 0
            ;;
        -srcupdate )
            DOSRCUPDATE=1
            ;;
        -nosrcupdate )
            DOSRCUPDATE=0
            ;;
        -tools )
            DOTOOLS=1
            ;;
        -notools )
            DOTOOLS=0
            ;;
        -update|-cleansrc|-fast|-fastupdate )
            DOPULL=1
            DOUPDATE=1
            DOSRCUPDATE=1
            #we pass this one on to the local build script:
            fbargs="$fbargs $arg"
            ;;
        -noupdate)
            DOPULL=0
            DOUPDATE=0
            DOSRCUPDATE=0
            ;; 
        -pull )
            DOPULL=1
            ;;
        -nopull )
            DOPULL=0
            ;;
        -email )
            #note - ignored if port is not email capable
            DOEMAIL=1
            ;;
        -noemail )
            DOEMAIL=0
            ;;
        -bres )
            DOBRES=1
            ;;
        -nobres )
            DOBRES=0
            ;;
        -clean )
            DOCLEAN=1; cleanarg="-clean"
            ;;
        -cleanmavenrepo )
            DOMAVENCLEAN=1; cleanmavenarg="-cleanmavenrepo"
            ;;
         -autobuild )
            DOAUTOBUILD=1
            DOPULL=1
            #the autobuild option implies an update - make sure we do it:
            fbargs="$fbargs -update"
            ;;
        -bldnum )
            if [ $# -gt 0 ]; then
                BLDNUM=$1; shift
            else
                echo "${p}: -bldnum requires an argument"
                usage 1
            fi
            ;;
        -* )
            fbargs="$fbargs $arg"
            ;;
        *=* )
            tmp=`echo $arg|sed -e 's/"/\\\\"/g'`
            #echo A arg=.$arg. tmp is .$tmp.
            tmp=`echo $tmp|sed -e 's/^\([^=][^=]*\)=\(.*\)/\1="\2"; export \1/'`

            #echo B tmp 
            eval $tmp
            ;;
        * )
            fbargs="$fbargs $arg"
            ;;
        esac
    done
}

################################## UTILITIES ##################################

require()
#import external shell routines - fatal error if we can't find it.
{
    libname=$1

    if [ x$libname = x ]; then
        echo "BUILD_ERROR: ${p}::require:  missing file name - ABORT"
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
        echo "BUILD_ERROR: ${p}::require: errors sourcing $libname - ABORT"
        exit 1
    fi
}

############################### INITIALIZATION ################################

set_global_vars()
{
    #global status vars:
    export RUNBUILD_STATUS TOOLSBUILD_STATUS
    RUNBUILD_STATUS=0
    TOOLSBUILD_STATUS=0

    #we require a LINKROOT separate from SRCROOT:
    # if set in buildenv.csh this will be $SRCROOT/$CODELINE/$FORTE_PORT
    if [ x$FORTE_LINKROOT = x ]; then
        if [ "$FORTE_PORT" != "nt" ]; then
            bld_fatal_error "FORTE_LINKROOT must be set to build $PRODUCT - ABORT"
        fi
    fi

    #LOGS
    export MAINLOG TOOLSLOG SRCUPLOG SUMMARY_LOG KIT_SUMMARY_REPORT
    MAINLOG=$LOGDIR/${PRODUCT}_build.$FORTE_PORT
    TOOLSLOG=$LOGDIR/toolsBuild.log
    SRCUPLOG=$LOGDIR/srcUpdate.log
    SUMMARY_LOG=$LOGDIR/runRegress.rpt
    KIT_SUMMARY_REPORT=$LOGDIR/kitSummary.rpt

    #EMAIL
    export BUILD_EMAIL
    if [ $DOEMAIL -eq 1 ]; then
        #set default email list:
        if [ x$BUILD_EMAIL = x ]; then
            BUILD_EMAIL=$LOGNAME
        fi
    fi

    if [ $DOBRES -eq 1 ]; then
        #only has meaning if email is turned on.
        DOBRES=$DOEMAIL
    fi

    export IS_UNIX IS_NT
    IS_NT=0; IS_UNIX=0
    bldhost -is_nt $FORTE_PORT
    if [ $? -eq 0 ]; then
        IS_NT=1
    fi

    bldhost -is_unix $FORTE_PORT
    if [ $? -eq 0 ]; then
        IS_UNIX=1
    fi

    #LOCKS:
    if [ "$PRIMARY_PORT" = "$FORTE_PORT" ]; then
        toolsrdy=$LOCKDIR/tools.rdy
    else
        toolsrdy=$LOCK_READDIR/tools.rdy
    fi

    if [ $DOAUTOBUILD -eq 1 ]; then
        #-autobuild implies -update
        DOUPDATE=1
        if [ x$MAKE_BUILD_DECISION = x ]; then
            export MAKE_BUILD_DECISION
            MAKE_BUILD_DECISION="need${PRODUCT}Build"
        fi

        #make sure the script is available:
        if [ ! -x $MAKE_BUILD_DECISION ]; then
            tmp=`fwhich "$MAKE_BUILD_DECISION"`
            if [ $? -ne 0 -o ! -x "$tmp" ]; then
                bld_fatal_error "[-autobuild]: cannot find $MAKE_BUILD_DECISION or script is not executable - ABORT"
            fi
        fi
    fi

    ##############
    # HTTP LOGGING
    ##############
    export HTTP_LOGROOT RELATIVE_LOGDIR HTTP_TEST_RESULTS HTTP_BUILD_RESULTS TESTPAGE_URL

    if [ "$HTTP_LOGROOT" = "" ]; then
        HTTP_LOGROOT=$SRCROOT

        #url-encode ':' (0x3a) chars - this will generate a usable url for netscape on NT:
        HTTP_LOGROOT=file:///`echo $HTTP_LOGROOT | sed -e 's/:/%3a/g'`
    fi

    if [ "$TESTPAGE_URL" = "" ]; then
        TESTPAGE_URL=junitreport/html/index.html
    fi

    #calculate "relative" log dir, which uniquely identifies logs under http root.
    #user can over-ride this.
    if [ "$RELATIVE_LOGDIR" = "" ]; then
        echo $LOGDIR | grep "^$SRCROOT/" > /dev/null
        if [ $? -eq 0 ]; then
            RELATIVE_LOGDIR=`echo $LOGDIR | sed -e "s|$SRCROOT/||" | sed -e "s|/$BUILD_DATE||"`
        else
            #otherwise, assume a $FORTE_LINKROOT style setup:
            RELATIVE_LOGDIR=$CODELINE/$REGRESS_FORTE_PORT/regress/log
        fi
    fi

    HTTP_BUILD_RESULTS=$HTTP_LOGROOT/$RELATIVE_LOGDIR/$BUILD_DATE
    HTTP_TEST_RESULTS=$HTTP_BUILD_RESULTS/$TESTPAGE_URL

    #this is the maximum time in seconds we let the build script run before we kill it.
    #call bld_reset_watchdog to restart the timeout
    export RUNBLD_TIMEOUT
    #no build step currently takes over 60 minutes:
    if [ "$RUNBLD_TIMEOUT" = "" ]; then
        RUNBLD_TIMEOUT=`echo '60 60 *p' | dc`
    fi

}

################################ HOUSEKEEPING #################################

cleanup()
{
    if [ "$RUNBLD_PIDFILE" != "" ]; then
        rm -f $RUNBLD_PIDFILE
    fi
}

rec_signal()
{
    cleanup

    RUNBUILD_STATUS=1
    #we don't have time to collect build results, so turn it off
    DOBRES=0

    bldmsg -mark -error -p $p Interrupted
    bldmsg -mark -error -p $p Interrupted >> $MAINLOG

    if [ $DOEMAIL -eq 1 ]; then
        send_buildmail
    fi

    exit 2
}

############################### INFO ROUTINES #################################

show_options()
{
    bldmsg -mark Running $p $saveargs

    cat << EOF
OPTION SETTINGS FOR $p - $*
    DOCLEAN is         $DOCLEAN
    DOMAVENCLEAN is    $DOMAVENCLEAN
    DOHELP is          $DOHELP
    DOTOOLS is         $DOTOOLS
    DOSRCUPDATE is     $DOSRCUPDATE
    DOUPDATE is        $DOUPDATE
    DOPULL is          $DOPULL
    DOPULL is          $DOPULL
    DOEMAIL is         $DOEMAIL
    DOAUTOBUILD is     $DOAUTOBUILD

    $localBuild args:  $fbargs

EOF
}

show_build_environment()
{

    #show general environment:
    bld_show_env

    show_options FINAL

    #show local additions:
    cat << EOF

*** YOU CAN OVER-RIDE THESE SETTINGS ***

    MAKE_BUILD_DECISION is $MAKE_BUILD_DECISION
    BUILD_EMAIL is         $BUILD_EMAIL
    HTTP_LOGROOT is        $HTTP_LOGROOT
    RELATIVE_LOGDIR is     $RELATIVE_LOGDIR
    TESTPAGE_URL is        $TESTPAGE_URL

*** GENERATED VARIABLES FOLLOW ***

    MAINLOG is             $MAINLOG
    TOOLSLOG is            $TOOLSLOG

*** GENERATED FROM \$HTTP_ROOT + \$RELATIVE_LOGDIR ***
    HTTP_BUILD_RESULTS is $HTTP_BUILD_RESULTS

*** GENERATED FROM \$HTTP_ROOT + \$RELATIVE_LOGDIR + \$TESTPAGE_URL ***
    HTTP_TEST_RESULTS is  $HTTP_TEST_RESULTS

EOF
}

############################# SUPPORT ROUTINES ################################

make_build_decision()
{
    #pass -tools arg if we are building tools:
    _args=""
    if [ $DOTOOLS -eq 1 ]; then
        _args="$args -tools"
    fi

    bldmsg -p $p -mark "Running \$MAKE_BUILD_DECISION script '$MAKE_BUILD_DECISION' $_args"

    ### TODO:  trap stdout/stderr output to send with email
    $MAKE_BUILD_DECISION $_args
    status=$?

    case $status in
    0 )
        # yes - we want to build
        bldmsg -p $p -MARK $MAKE_BUILD_DECISION script says we need to build - CONTINUE
        ;;
    1 )
        # there are no updates to process
        bldmsg -p $p -MARK $MAKE_BUILD_DECISION script says no changes to $PRODUCT - HALT
        ### TODO:  send email
        ;;
    * )
        # there was some error running the script
        bldmsg -p $p -error -status $status $MAKE_BUILD_DECISION script detected errors - ABORT
        ### TODO:  send email
        ;;
    esac

    return $status
}

send_buildmail()
{
    TMPA=/tmp/${p}_tmpA.$$
    rm -f $TMPA
    touch $TMPA

    if [ $RUNBUILD_STATUS -eq 0 -a $TOOLSBUILD_STATUS -eq 0 ]; then
        strstatus="SUCCEEDED"
    else
        strstatus="FAILED"
    fi

    cat << MSG_EOF >> $TMPA
Subject: $PRODUCT $CODELINE build of $BUILD_DATE $strstatus on $HOST_NAME ( $REGRESS_FORTE_PORT )

HOSTNAME is $HOSTNAME
CODELINE is $CODELINE
FORTE_PORT is $FORTE_PORT
REGRESS_FORTE_PORT is $REGRESS_FORTE_PORT
LOGDIR is $LOGDIR

===========
BUILD LOGS:
===========

    $HTTP_BUILD_RESULTS

MSG_EOF

    if [ -r "$KIT_SUMMARY_REPORT" ]; then
        cat $KIT_SUMMARY_REPORT >> $TMPA
    fi


    if [ -r "$SUMMARY_LOG" ]; then
        cat << MSG_EOF >> $TMPA
SUMMARY:

MSG_EOF
        cat $SUMMARY_LOG >> $TMPA
    fi

    if [ -r "$LOGDIR/bldtime.log" ]; then
        showtimes "$LOGDIR/bldtime.log" >> $TMPA
    fi

    if [ $DOBRES -eq 1 ]; then

        cat << MSG_EOF >> $TMPA

BUILD RESULTS:

MSG_EOF
        buildResults -nofold $FORTE_PORT | head -500 >> $TMPA 2>&1
    fi

    bmail -to $BUILD_EMAIL < $TMPA
    rm -f $TMPA
}


scm_update_tmp()
{
    bldmsg -p $p -mark "Running local subversion code update of open-dm-mi"

    cd $SRCROOT
    svn update
    status=$?

    if [ $status -ne 0 ]; then
       bldmsg -p $p -error -status $status could not update open-dm-mi code - ABORT
       return $status
    fi

    bldmsg -p $p -mark "Running local subversion code update of open-dm-dq"
    cd $SRCROOT/..
    rm -rf open-dm-dq
    svn checkout https://open-dm-dq.dev.java.net/svn/open-dm-dq/trunk open-dm-dq --username $JBI_USER_NAME
    status=$?

    if [ $status -ne 0 ]; then
       bldmsg -p $p -error -status $status could not checkout open-dm-dq code - ABORT
       return $status
    fi

    return $status
}


#################################### MAIN #####################################

p=`basename $0`
#formulate localBuild script name:
localBuild=`echo $p | sed -e s/run//`

saveargs="$@"

#source common build routines or die:
require bldcmn.sh

parse_args "$@"

#if asking for help, display help of all scripts called by this one.
if [ $DOHELP -eq 1 ]; then
    echo "##################"
    echo "#$localBuild -help"
    echo "##################"
    $localBuild $helparg

    exit 0
fi

show_options INITIAL

bld_check_usepath
if [ $? -ne 0 ]; then
    bld_fatal_error "one or more usePathDef variables not set - ABORT"
fi

bld_setup_logdir_env    # in bldcmn.sh
if [ $? -ne 0 ]; then
    bld_fatal_error "failed to set up log directory - ABORT"
fi

bld_setup_lockdir  # in bldcmn.sh

set_global_vars    

########################
#INITIAL CHECK FOR RUNNING BUILD (kill it if it is timed out):
########################
checkpid -verbose -clean -killontimeout $RUNBLD_TIMEOUT $RUNBLD_PIDFILE 
if [ $? -ne 0 ]; then
    bldmsg -mark -warn -p $p "ACTIVE BUILD ALREADY IN PROGRESS (PID=`cat $RUNBLD_PIDFILE`) - HALT (A)."
    exit 0
fi

if [ $DOAUTOBUILD -eq 1 ]; then
    make_build_decision
    status=$?
    if [ $status -ne 0 ]; then
        # note that the subroutine worries about sending email, etc.
        exit $status
    fi
fi

######
#SINCE the build query can take awhile, check to see if another build has started.
#No one knows we are running until we commit the log dir.
######
checkpid -verbose -clean -killontimeout $RUNBLD_TIMEOUT $RUNBLD_PIDFILE 
if [ $? -ne 0 ]; then
    bldmsg -mark -warn -p $p "ACTIVE BUILD ALREADY IN PROGRESS (PID=`cat $RUNBLD_PIDFILE`) - HALT (B)."
    exit 0
fi

################
#COMMIT TO BUILD (now it is safe to write to log files):
################
bld_create_logdir

#now it is safe to trap signals, which will clean up my PID file (not a previous invocation):
#the pid file is created when we commited the logdir above.
trap rec_signal 2 15

bldmsg -p $p -markbeg "$p" >> $MAINLOG
show_build_environment

if [ $DOAUTOBUILD -eq 1 ]; then
    bldmsg -mark -p $p "delaying for one minute to allow any pending integrations to clear"
    sleep 60
fi

bldmsg -mark -p $p "Redirect log output to $MAINLOG"

#tell buildResults who we are so it doesn't have to guess:
echo BUILDRESULTS_HOSTNAME=$HOST_NAME >> $MAINLOG
echo BUILDRESULTS_TYPE=general >> $MAINLOG

if [ "$PRIMARY_PORT" = "$FORTE_PORT" ]; then
    bldmsg -mark -p $p  Clearing all lock files >> $MAINLOG
    rm -f $LOCKDIR/* >> $MAINLOG 2>&1
fi

if [ $DOPULL -eq 1 ]; then
    #update tools before we start:
    bldmsg -p $p -mark "Updating tools on $HOST_NAME" >> $MAINLOG
    $TOOLROOT/boot/updateDist >> $MAINLOG 2>&1
fi

if [ $DOSRCUPDATE -eq 1 ]; then
    bldmsg -p $p -markbeg "local source update on $HOST_NAME" >> $MAINLOG
    bldmsg -p $p -mark "source update log is $SRCUPLOG" >> $MAINLOG
    scm_update_tmp >> $SRCUPLOG 2>&1
    SRCUPDATE_STATUS=$?
    bldmsg -p $p -markend -status $SRCUPDATE_STATUS "local source update on $HOST_NAME" >> $MAINLOG
fi

if [ $DOTOOLS -eq 1 ]; then
    if [ "$PRIMARY_PORT" = "$FORTE_PORT" ]; then

        #remove release if clean build.  toolsBuild will restore it.
        if [ $DOCLEAN -eq 1 ]; then
            bldmsg -mark -p $p  Removing $RELEASE_DISTROOT >> $MAINLOG
            mv $RELEASE_DISTROOT $RELEASE_DISTROOT.$$  >> $MAINLOG 2>&1 
            rm -rf $RELEASE_DISTROOT.$$   >> $MAINLOG 2>&1 
            mkdir -p $RELEASE_DISTROOT  >> $MAINLOG 2>&1 
        fi

        toolsbld_args=
        [ $DOPULL -eq 1 ] && toolsbld_args="-dopull $toolsbld_args"

       ### if [ $DOUPDATE -eq 1 ]; then
            ### DO NOT WANT TO UPDATE IN TOOLBLD -KGH
            ###toolsbld_args="-cvsupdate $toolsbld_args"
        ###fi
        
        bldmsg -p $p -markbeg "tools build on $HOST_NAME" >> $MAINLOG
        bldmsg -p $p -mark "tools build log is $TOOLSLOG" >> $MAINLOG
        #for now, don't allow -clean for tools.  too dangerous.  RT 2/16/99
        toolsBuild $testarg $toolsbld_args -readyfile $toolsrdy >> $TOOLSLOG 2>&1
        TOOLSBUILD_STATUS=$?
        bldmsg -p $p -markend -status $TOOLSBUILD_STATUS "tools build on $HOST_NAME" >> $MAINLOG
    else
        if [ $DOWAIT -eq 1 ]; then
            bldwait -p $p -marktime $toolsrdy
        else
            bldmsg -p $p -warn "Not building tools because you are not a primary port" >> $MAINLOG
        fi

        #tools build is done - re-update unless user has requested that we not:
        if [ $DOPULL -eq 1 ]; then
            bldmsg -p $p -mark "Updating tools on $HOST_NAME" >> $MAINLOG
            $TOOLROOT/boot/updateDist >> $MAINLOG 2>&1
        fi
    fi
else
    #create tools.rdy, even if we don't build tools:
    if [ "$PRIMARY_PORT" = "$FORTE_PORT" ]; then
        date > $toolsrdy
    fi
fi

#run the main build:
bldmsg -markbeg "run of $localBuild from $p" >> $MAINLOG
$localBuild -calledFromWrapper $testarg $cleanarg $fbargs $cleanmavenarg >> $MAINLOG 2>&1
RUNBUILD_STATUS=$?
bldmsg -markend -status $RUNBUILD_STATUS "run of $localBuild from $p" >> $MAINLOG

bldmsg -p $p -markend "$p" >> $MAINLOG

if [ $DOEMAIL -eq 1 ]; then
    send_buildmail
fi

cleanup
exit $status
