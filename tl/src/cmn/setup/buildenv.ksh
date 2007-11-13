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
# @(#)buildenv.ksh - ver 1.1 - 01/04/2006
#
# Copyright 2004-2006 Sun Microsystems, Inc. All Rights Reserved.
# 
# END_HEADER - DO NOT EDIT
#

###################
# INPUT PARAMETERS:
###################

# IIS_TOOLROOT    - override the default TOOLROOT setting.
# IIS_CVSROOT     - override the default CVSROOT setting.
# IIS_BRANCH_NAME - override the default JBI_BRANCH_NAME setting
# IIS_CODELINE    - override the default CODELINE setting
# IIS_GFBASE     - override the default GFBASE setting
# IIS_NETBEANS_HOME     - override the default NETBEANS_HOME setting
#
# WARNING:  the above variables are PRIVATE and are RESERVED for
#           this definition file.  They are unexported after use.

####
#set the path-separator for this platform:
####
export PS
echo $PATH | grep ';' > /dev/null
if [ $? -eq 0 ]; then
    PS=';'
else
    PS=':'
fi

#### this is precautionary only, to divorce from old forte tools:
unset MAINROOT

export PRODUCT
PRODUCT=odmmi

export JNET_USER
if [ x$JNET_USER = x ]; then
    JNET_USER="guest"
    echo "WARNING: your java.net user name (JNET_USER) was defaulted to '$JNET_USER'."
    echo "To remove this warning, please export JNET_USER in the environment."
fi

export MAVEN_OPTS
if [ x$MAVEN_OPTS = x ]; then
    MAVEN_OPTS="-Xmx400m"
fi

#########
# CVSROOT:
#########
export CVSROOT
if [ x$IIS_CVSROOT = x ]; then
    #use default setting (note - CVSROOT is set later):
    IIS_CVSROOT=":pserver:${JNET_USER}@cvs.dev.java.net:/cvs"
fi

pwd=`pwd`

#cannonicalize $SRCROOT if it is set:
if [ "$SRCROOT" != "" ]; then
    srcroot=`sh -c "cd $SRCROOT; pwd"`
else
    srcroot=""
fi

export SRCROOT
if [ "$srcroot" = "$pwd" ]; then
    SRCROOT="$pwd"
elif [ "$PROJECT" != "" -a \( "$srcroot" = "" -o "$srcroot" = "$PROJECT" \) ]; then
    ## ASSUME we are using VSPMS
    SRCROOT="$PROJECT"
else
   cat << 'EOF'
PLEASE SET $SRCROOT OR USE chpj BEFORE SOURCING buildenv.ksh

NOTE:  if you are not using VSPMS and *have* set $SRCROOT, then
       "cd $SRCROOT"  before sourcing this setup file.  Make sure that
       the pwd command returns the same string as your $SRCROOT setting.

###  EXAMPLE 1:  without VSPMS:

$ mkdir -p /somedisk/cvs/mywork
$ cd /somedisk/cvs/mywork
$ cat >> mysetup
export SRCROOT JAVABASE JAVAVERS
SRCROOT=/somedisk/cvs/mywork
JAVABASE=C:
JAVAVERS=j2sdk1.4.0
. $SRCROOT/tools/boot/buildenv.ksh
pathinfo    #optional - display key environment settings.
^D

$ . mysetup
    [do this whenever you want work on your project]

###  EXAMPLE 2:  using VSPMS:

$ cd /somedisk/cvs/mywork
$ addpj mywork
    [name your project whatever you want]

$ cat >> $PROJECTRC
export JAVABASE JAVAVERS
JAVABASE=C:
JAVAVERS j2sdk1.4.0
. $PROJECT/tools/boot/buildenv.ksh
pathinfo    #optional - display key environment settings.
^D

% chpj mywork
    [do this whenever you want work on your project]

SETUP ABORTED
EOF

    return

fi

#########
#CODELINE:
#########
export CODELINE

#allow user to override codeline var, which determines cvs branch names
#for main repository, and determines the placement of log and kit directories.

if [ x$IIS_CODELINE != x ]; then
    CODELINE=$IIS_CODELINE
else
    CODELINE=main
fi

####
#CVS BRANCH NAMES.  Use IIS_BRANCH_NAME to override "main" when bootstraping a branch.
####
export JBI_BRANCH_NAME CVS_BRANCH_NAME
JBI_BRANCH_NAME=main
if [ x$IIS_BRANCH_NAME != x ]; then
    JBI_BRANCH_NAME="$IIS_BRANCH_NAME"
fi

CVS_BRANCH_NAME=$JBI_BRANCH_NAME

#now we can set TOOLS_CVSROOT, which is based on the branch and product name:
export TOOLS_CVSROOT
if [ x$TOOLS_CVSROOT = x ]; then
    TOOLS_CVSROOT=":pserver:anoncvs@ojcbuilds.stc.com:/tooldist/$PRODUCT/$CVS_BRANCH_NAME/retools"
fi

#################
# CVS_BRANCH_NAME is used in the following scripts to denote the
# major codeline revision.  It is meant to be generic for generic tools (devtools).
# toolsBuild    - checkout tools src
# makedrv.pl    - checkout tools src
# buildenv.csh  - setup file
# buildenv.ksh  - setup file
# bldcmn.sh     - assert
# fortepj.rc    - cosmetic (sets $REV VSPMS var)
# fortepj.ksh   - cosmetic (sets $REV VSPMS var)
#################

export DEVELOPER_BUILD
DEVELOPER_BUILD=1

alias gettools='(mkdir -p tools.new; cd $SRCROOT/tools.new;  cvs -f -d $TOOLS_CVSROOT co ${FORTE_PORT}tools; echo new tools are in tools.new)'
alias cvstools='cvs -d $TOOLS_CVSROOT'
alias cvstools='svn status'

#### CVS DEFS
export CVSREAD CVSROOT
CVSREAD=1
CVSROOT="$IIS_CVSROOT"

####### TOOLS SETUP
export TOOLROOT FORTE_PORT

if [ x$IIS_TOOLROOT != x ]; then
    TOOLROOT=$IIS_TOOLROOT
else
    TOOLROOT=$SRCROOT/tools
fi

if [ -x $TOOLROOT/boot/whatport.ksh -o -x $TOOLROOT/boot/whatport ]; then
    FORTE_PORT=`$TOOLROOT/boot/whatport`
else
    echo ERROR:  could not find tools - please bootstrap your tools.
    return
fi

PATH="$TOOLROOT/bin/$FORTE_PORT${PS}$TOOLROOT/bin/cmn${PS}$PATH"

export PERL_LIBPATH
if [ x$PERL5_HOME != x ]; then
    if [ -d $PERL5_HOME ]; then
        #perl installations on solaris and linux differ - solaris has {bin,lib} subdirs:
        if [ -d $PERL5_HOME/lib ]; then
            PERL_LIBPATH=".;$PERL5_HOME/lib;$TOOLROOT/lib/cmn;$TOOLROOT/lib/cmn/perl5"
        else
            PERL_LIBPATH=".;$PERL5_HOME;$TOOLROOT/lib/cmn;$TOOLROOT/lib/cmn/perl5"
        fi
        if [ -d $PERL5_HOME/bin ]; then
            PATH="$PERL5_HOME/bin${PS}$PATH"
            #otherwise, we assume perl is already in the path
        fi
    else
        echo "WARNING: not a directory, PERL5_HOME='$PERL5_HOME'. Please fix."
    fi
else
    #use port-specific perl libs in $TOOLROOT; this is for old solaris
    #and mks perl installs:
    PERL_LIBPATH=".;$TOOLROOT/lib/cmn;$TOOLROOT/lib/$FORTE_PORT/perl5;$TOOLROOT/lib/cmn/perl5"
fi

#used by makemf utility:
export MAKEMF_LIB
MAKEMF_LIB=$TOOLROOT/lib/cmn

#used by codegen utility:
export CG_TEMPLATE_PATH
CG_TEMPLATE_PATH=".;$TOOLROOT/lib/cmn/templates;$TOOLROOT/lib/cmn/templates/java"

alias pull='$TOOLROOT/boot/updateDist'

#### set up env required for release and tools builds:
export PATHNAME PATHREF RELEASE_ROOT RELEASE_DISTROOT HOST_NAME PRIMARY_PORT TARGET_OS_LIST FORTE_LINKROOT

PATHNAME=`basename $SRCROOT`
PATHREF=$SRCROOT
RELEASE_ROOT=$SRCROOT/release
RELEASE_DISTROOT=$SRCROOT/release
HOST_NAME="`uname -n`"

PRIMARY_PORT=solsparc
TARGET_OS_LIST="cmn,ntcmn,nt,solsparc,linux,macosx,solx86,cygwin"

export FORTE_LINKROOT DISTROOT KITROOT KIT_DISTROOT KIT_REV
if [ x$FORTE_LINKROOT = x ]; then
    FORTE_LINKROOT=$SRCROOT
fi
if [ x$DISTROOT = x ]; then
    DISTROOT=$FORTE_LINKROOT/dist/tools
fi

if [ x$KITROOT = x ]; then
    KITROOT=$FORTE_LINKROOT/kits
fi
if [ x$KIT_DISTROOT = x ]; then
    KIT_DISTROOT=$FORTE_LINKROOT/kits/$PRODUCT
fi

if [ x$KIT_REV = x ]; then
    KIT_REV=$CODELINE
fi

export REGRESS_DISPLAY
if [ x$REGRESS_DISPLAY = x ]; then
    if [ x$DISPLAY != x ]; then
        REGRESS_DISPLAY="$DISPLAY"
    else
        REGRESS_DISPLAY=NULL
    fi
fi

##### JAVA BASE
#adjust this to where you have java installed:
if [ x$JAVABASE = x ]; then
    bldmsg -error You must set JAVABASE and JAVAVERS
    bldmsg EXAMPLE:  'export JAVABASE=c:; export JAVAVERS=jdk1.3_0'
    return 1
fi

export JAVA_HOME=$JAVABASE/$JAVAVERS
PATH="$JAVA_HOME/bin${PS}$PATH"
##########

##### JAVA TOOLS
#ant
export ANT_HOME ANT_OPTS
ANT_HOME=$TOOLROOT/java/ant
if [ x$ANT_OPTS = x ]; then
    ANT_OPTS=-Xmx200m
fi

export MAVEN_HOME M2_HOME
MAVEN_HOME=$TOOLROOT/java/maven
M2_HOME=$TOOLROOT/java/maven2

##### JREGRESS
export JREGRESS_TIMEOUT RUNBLD_TIMEOUT
[ -z "$JREGRESS_TIMEOUT" ] && JREGRESS_TIMEOUT=650
[ -z "$RUNBLD_TIMEOUT" ]   && RUNBLD_TIMEOUT=10800

##### set up JBI_USER_NAME:
#this can be set in the login env, as it is usually invariant
export JBI_USER_NAME
if [ x$JBI_USER_NAME = x ]; then
    if [ xLOGNAME != x ]; then
        JBI_USER_NAME="$LOGNAME"
    elif [ xUSER != x ]; then
        JBI_USER_NAME="$USER"
    else
        echo 'ERROR cannot set JBI_USER_NAME from $LOGNAME or $USER - PLEASE DEFINE IT MANUALLY'
        JBI_USER_NAME="NONAME"
    fi
fi

##### Default GFBASE:
export GFBASE
if [ "$IIS_GFBASE" != "" ]; then
    GFBASE="$IIS_GFBASE"
elif [ "$IIS_AS8BASE" != "" ]; then
    echo WARNING: IIS_AS8BASE is deprecated - please use IIS_GFBASE to specify appserver install root
    GFBASE="$IIS_GFBASE"
else
    GFBASE="$SRCROOT/install/gf"
fi
PATH="$GFBASE/bin${PS}$PATH"

#standard project defs:
unset fortepj
export fortepj
fortepj="$TOOLROOT/lib/cmn/fortepj.ksh"
if [ -r $fortepj ]; then
    . $fortepj
else
    echo WARNING - cannot open $fortepj
fi

opt=`optpath` > /dev/null 2>&1
if [ $? -eq 0 ]; then
    PATH="$opt"
else
    echo PATH not optimized
fi

#########
#NetBeans setup:
#########
export NETBEANS_HOME JV_NETBEANS_HOME
if [ "$IIS_NETBEANS_HOME" != "" ]; then
    NETBEANS_HOME="$IIS_NETBEANS_HOME"
fi

#make a random guess, based on platform:
if [ "$NETBEANS_HOME" = "" ]; then
    nbvers=5.5beta2
    if [ "$FORTE_PORT" = "macosx" ]; then
        NETBEANS_HOME=/Applications/NetBeans.app/Contents/Resources/NetBeans
    elif [ "$FORTE_PORT" = "cygwin" ]; then
        NETBEANS_HOME=/netbeans/$nbvers
    else
        NETBEANS_HOME=/opt/netbeans/$nbvers
    fi

    unset nbvers
fi

if [  ! -d "$NETBEANS_HOME"  ]; then
    echo "WARNING: not a directory, NETBEANS_HOME='$NETBEANS_HOME'. Please provide IIS_NETBEANS_HOME parameter."
fi

alias runide='"$NETBEANS_HOME"/bin/netbeans -J-Dmaven.repo.local="$JV_SRCROOT"/m2/repository --jdkhome "$JAVA_HOME"'

#this is a hack to bootstrap the boms.
export JBICOMPS_DOT_VERSION
JBICOMPS_DOT_VERSION="1.0"

#######
#cygwin: set java versions of SRCROOT, TOOLROOT (used in ant scripts):
#######
export JV_SRCROOT JV_TOOLROOT JV_GFBASE
if [ "$FORTE_PORT" = "cygwin" ]; then
    JV_SRCROOT=`cygpath -wm "$SRCROOT"`
    JV_TOOLROOT=`cygpath -wm "$TOOLROOT"`
    JV_GFBASE=`cygpath -wm "$GFBASE"`
    JV_NETBEANS_HOME=`cygpath -wm "$NETBEANS_HOME"`
else
    JV_SRCROOT="$SRCROOT"
    JV_TOOLROOT="$TOOLROOT"
    JV_GFBASE="$GFBASE"
    JV_NETBEANS_HOME="$NETBEANS_HOME"
fi

#for backward compatibility:
export AS8BASE JV_AS8BASE
AS8BASE="$GFBASE"
JV_AS8BASE="$JV_GFBASE"

#these variables are PRIVATE and RESERVED for this definition file:
unset IIS_TOOLROOT
unset IIS_CVSROOT
unset IIS_BRANCH_NAME
unset IIS_CODELINE
unset IIS_GFBASE
unset IIS_NETBEANS_HOME

#these variables are needed to update the local project tool sources during toolsBuild
export CVS_SRCROOT_PREFIX CVS_CO_ROOT CVSIGNORE JBI_MODULES
CVS_SRCROOT_PREFIX="open-dm-mi"
CVS_CO_ROOT="${SRCROOT}/.."
CVSIGNORE="Makefile bld"
CVSIGNORE="Makefile bld target"

#finally, set the current release tag:
export REV
REV="SM04"
