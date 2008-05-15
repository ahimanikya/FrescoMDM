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
# @(#)buildenv.csh - ver 1.1 - 01/04/2006
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
# IIS_BRANCH_NAME - override the default CVS_BRANCH_NAME setting
# IIS_CODELINE    - override the default CODELINE setting
# IIS_GFBASE     - override the default GFBASE setting
# IIS_NETBEANS_HOME     - override the default NETBEANS_HOME setting
#
# WARNING:  the above variables are PRIVATE and are RESERVED for
#           this definition file.  They are unexported after use.

#### this is precautionary only, to divorce from old forte tools:
unsetenv MAINROOT

setenv PRODUCT odmmi

if !($?JNET_USER) then
    setenv JNET_USER "guest"
    echo "WARNING: your java.net user name (JNET_USER) was defaulted to '$JNET_USER'."
    echo "To remove this warning, please export JNET_USER in the environment."
endif

if !($?BOOTSTRAP_TOOLS) setenv BOOTSTRAP_TOOLS "/net/asharp.sfbay.sun.com/asharp1/bld/open-jbi-components/main/build/open-jbi-components/tools"
if !($?JAVABASE)    setenv JAVABASE /usr
if !($?JAVAVERS)    setenv JAVAVERS java
if !($?IIS_CVSROOT) setenv IIS_CVSROOT :pserver:${JNET_USER}@cvs.dev.java.net:/cvs
if !($?MAVEN_OPTS)    setenv MAVEN_OPTS -Xmx512m

### is the user using VSPMS?
if ($?PROJECTRC && $?MYPROJECTS) then
    setenv USING_VSPMS 1
else
    setenv USING_VSPMS 0
endif

set pwd = "`pwd`"

### set up source "base" variable:
if ( ! $?PROJECT ) then
    setenv PROJECT ""
endif
if ( ! $?SRCROOT ) then
    setenv SRCROOT ""
endif

#cannonicalize $SRCROOT if it is set:
if ( "$SRCROOT" != "" ) then
    set srcroot=`sh -c "cd $SRCROOT; pwd"`
endif

if ( "$SRCROOT" == "$cwd" || "$SRCROOT" == "$pwd" ) then
    setenv SRCROOT "$cwd"
else if ( "$PROJECT" != "" && ("$SRCROOT" == "$PROJECT" || "$SRCROOT" == "" )) then
    ## ASSUME we are using VSPMS
    setenv SRCROOT $PROJECT
else
    echo SRCROOT=$SRCROOT
    echo PROJECT=$PROJECT
    echo cwd=$cwd
    echo pwd=$pwd
    cat << 'EOF'
PLEASE SET $SRCROOT OR USE chpj BEFORE SOURCING buildenv.csh

###  EXAMPLE 1:  without VSPMS:

% cd /somedisk/cvs/mywork
% cat > mysetup
setenv SRCROOT /somedisk/cvs/mywork
setenv JAVABASE /opt/java
setenv JAVAVERS j2sdk1.4.0
source $SRCROOT/tools/boot/buildenv.csh
pathinfo    #optional - display key environment settings.
^D

% source mysetup
    [do this whenever you want work on your project]

###  EXAMPLE 2:  using VSPMS:

% cd /somedisk/cvs/mywork
% addpj mywork
    [name your project whatever you want]

% cat > $PROJECTRC
setenv JAVABASE /opt/java
setenv JAVAVERS j2sdk1.4.0
source $PROJECT/tools/boot/buildenv.csh
pathinfo    #optional - display key environment settings.
^D

% chpj mywork
    [do this whenever you want work on your project]

SETUP ABORTED
'EOF'

  goto HALT
endif


######
#allow user to override codeline var, which determines cvs branch names
#for main repository, and determines the placement of log and kit directories.
######
if ($?IIS_CODELINE) then
    setenv CODELINE $IIS_CODELINE
else
    setenv CODELINE sierra
endif

#have to unset or set it to zero this if you are building in a release environment
setenv DEVELOPER_BUILD 1

####
#CVS BRANCH NAMES.  Use IIS_BRANCH_NAME to override "main" when bootstraping a branch.
####
if ($?IIS_BRANCH_NAME) then
    setenv JBI_BRANCH_NAME "$IIS_BRANCH_NAME"
else
    setenv JBI_BRANCH_NAME sierra
endif

setenv CVS_BRANCH_NAME "$JBI_BRANCH_NAME"

#now we can set TOOLS_CVSROOT, which is based on the branch and product name:
if !($?TOOLS_CVSROOT)   setenv TOOLS_CVSROOT ":pserver:anoncvs@iis.sfbay:/tooldist/$PRODUCT/$CVS_BRANCH_NAME/retools"

#################
# CVS_BRANCH_NAME is used in the following scripts to denote the
# toolsBuild    - checkout tools src
# makedrv.pl    - checkout tools src
# buildenv.csh  - setup file
# buildenv.ksh  - setup file
# bldcmn.sh     - assert
# fortepj.rc    - cosmetic (sets $REV VSPMS var)
# fortepj.ksh   - cosmetic (sets $REV VSPMS var)
#################

#### CVS DEFS
setenv CVSREAD 1
setenv CVSROOT "$IIS_CVSROOT"

####### TOOLS SETUP

if ($?IIS_TOOLROOT) then
    setenv TOOLROOT $IIS_TOOLROOT
else
    foreach tr ($SRCROOT/tools $BOOTSTRAP_TOOLS)
        setenv TOOLROOT $tr
        if (-x $TOOLROOT/boot/whatport ) then
            break
        endif
    end
endif

if (-x $TOOLROOT/boot/whatport ) then
    setenv FORTE_PORT `$TOOLROOT/boot/whatport`
else
    echo ERROR:  could not find tools - please check your IIS_TOOLROOT setting or create a copy in $SRCROOT/tools.
    goto HALT
endif

set path = ($TOOLROOT/bin/$FORTE_PORT $TOOLROOT/bin/cmn $path)

if ($?PERL5_HOME) then
    if ( -d $PERL5_HOME ) then
        #perl installations on solaris and linux differ - solaris has {bin,lib} subdirs:
        if ( -d $PERL5_HOME/lib ) then
            setenv PERL_LIBPATH ".;$PERL5_HOME/lib;$TOOLROOT/lib/cmn;$TOOLROOT/lib/cmn/perl5"
        else
            setenv PERL_LIBPATH ".;$PERL5_HOME;$TOOLROOT/lib/cmn;$TOOLROOT/lib/cmn/perl5"
        endif
        if ( -d $PERL5_HOME/bin ) then
            set path = ( $PERL5_HOME/bin $path )
            #otherwise, we assume perl is already in the path
        endif
    else
        echo "WARNING: not a directory, PERL5_HOME='$PERL5_HOME'. Please fix."
    endif
else
    #use port-specific perl libs in $TOOLROOT; this is for old solaris
    #and mks perl installs:
    setenv PERL_LIBPATH ".;$TOOLROOT/lib/cmn;$TOOLROOT/lib/$FORTE_PORT/perl5;$TOOLROOT/lib/cmn/perl5"
endif

#used by makemf utility:
setenv MAKEMF_LIB $TOOLROOT/lib/cmn

#used by codegen utility:
setenv CG_TEMPLATE_PATH ".;$TOOLROOT/lib/cmn/templates;$TOOLROOT/lib/cmn/templates/java"

if ($TOOLROOT == $BOOTSTRAP_TOOLS) then
    echo WARNING - using tools from $BOOTSTRAP_TOOLS - use gettools to get a private copy.
endif

####### END TOOLS SETUP

#### set up env required for release and tools builds:
setenv PATHNAME `basename $SRCROOT`
setenv PATHREF $SRCROOT
setenv RELEASE_ROOT  $SRCROOT/release
setenv RELEASE_DISTROOT  $SRCROOT/release
setenv HOST_NAME "`uname -n`"

setenv PRIMARY_PORT solsparc
setenv TARGET_OS_LIST "cmn,ntcmn,nt,solsparc,linux,macosx,solx86,cygwin"

if !($?FORTE_LINKROOT) setenv FORTE_LINKROOT    $SRCROOT/$CODELINE/$FORTE_PORT
if !($?DISTROOT) setenv DISTROOT    $FORTE_LINKROOT/dist/tools
if !($?KITROOT) setenv KITROOT  $FORTE_LINKROOT/kits
if !($?KIT_DISTROOT) setenv KIT_DISTROOT    $FORTE_LINKROOT/kits/$PRODUCT
if !($?KIT_REV) setenv KIT_REV  $CODELINE

if !($?REGRESS_DISPLAY) then
    if ($?DISPLAY) then
        setenv REGRESS_DISPLAY "$DISPLAY"
    else
        setenv REGRESS_DISPLAY NULL
    endif
endif

##### JAVA SDK SETUP
#adjust this to where you have java installed:
setenv JAVA_HOME $JAVABASE/$JAVAVERS
set path = ($JAVA_HOME/bin $path)

##### JAVA TOOLS
#ant
setenv ANT_HOME $TOOLROOT/java/ant
if !($?ANT_OPTS)    setenv ANT_OPTS -Xmx512m

if !($?MAVEN_HOME)  setenv MAVEN_HOME $TOOLROOT/java/maven
if !($?M2_HOME)     setenv M2_HOME $TOOLROOT/java/maven2

##### TIMEOUTS
if !($?JREGRESS_TIMEOUT)    setenv JREGRESS_TIMEOUT 650
if !($?RUNBLD_TIMEOUT)    setenv RUNBLD_TIMEOUT 10800

##### Default GFBASE:
if ($?IIS_GFBASE) then
    setenv GFBASE    $IIS_GFBASE
else if ($?IIS_AS8BASE) then
    echo WARNING: IIS_AS8BASE is deprecated - please use IIS_GFBASE to specify appserver install root
    setenv GFBASE    $IIS_AS8BASE
else
    setenv GFBASE    $SRCROOT/install/gf
endif
set path = ( $GFBASE/bin $path )

##### Default JBIROOT:
#this can be set in the login env, as it is usually invariant
if !($?JBI_USER_NAME) setenv JBI_USER_NAME "$user"

if !($?JBIROOT_BASE) setenv JBIROOT_BASE    $GFBASE
if !($?JBIROOT) setenv JBIROOT    $JBIROOT_BASE/jbi

### useful aliases:
alias mkregresslink 'ln -s $FORTE_LINKROOT/regress $SRCROOT/regress'
alias gettools '(mkdir -p tools.new; cd $SRCROOT/tools.new;  cvs -f -d $TOOLS_CVSROOT co ${FORTE_PORT}tools; echo new tools are in tools.new)'
alias cvstools 'cvs -d $TOOLS_CVSROOT'
alias changes 'svn status'

#standard project defs:
set fortepj="$TOOLROOT/lib/cmn/fortepj.rc"
if (-r $fortepj) then
    source $fortepj
else
    echo WARNING - cannot open $fortepj
endif

### checkstyle helpers:
#note - the \| makes '|' the delimiter in the address range.
#it is necessary because the filename expression could contain (/) delimiters.
alias cserrs 'sed -n -e "\|^<file .*\!$|,\|<\/file>|p" $SRCROOT/\!^/bld/checkstyle_src_report.xml | grep -v "</file>" |            sed -e "s|$SRCROOT/||g; s|<file name=.||; s/.>//" | fixcserrs | xml2ascii | nodoublequotes '
alias cserrsu 'echo Usage: cserr service java_classname'

alias fixcserrs 'sed -e "s|<error line=.| |; s|. column=.|/|; s|. severity=.error. message=.|	|; s|source=.*||" '

alias nodoublequotes 'tr -d \\042'

#this is obviously incomplete.  RT 10/6/04
alias xml2ascii sed -e '"' "s|&apos;|'|g; s|&lt;|<|g;; s|&gt;|>|g;"'"'
###

##### be safe, remove old tools from path:
set opt=`optpath -rm '^/forte1/d,^/bld/tools/'` >& /dev/null
if ($status == 0) then
    setenv PATH $opt
else
    echo path not optimized
endif

#########
#NetBeans setup:
#########
if ($?IIS_NETBEANS_HOME) then
    setenv NETBEANS_HOME "$IIS_NETBEANS_HOME"
endif

#make a random guess, based on platform:
if !($?NETBEANS_HOME) then
    set nbvers = 5.5beta2
    if ( "$FORTE_PORT" == "macosx" ) then
        setenv NETBEANS_HOME    /Applications/NetBeans.app/Contents/Resources/NetBeans
    else if ( "$FORTE_PORT" == "cygwin" ) then
        setenv NETBEANS_HOME /netbeans/$nbvers
    else
        setenv NETBEANS_HOME /opt/netbeans/$nbvers
    endif

    unset nbvers
endif

if ( ! -d "$NETBEANS_HOME" ) then
    echo "WARNING: not a directory, NETBEANS_HOME='$NETBEANS_HOME'. Please provide IIS_NETBEANS_HOME parameter."
endif

alias runide '"$NETBEANS_HOME"/bin/netbeans -J-Dmaven.repo.local="$JV_SRCROOT"/m2/repository --jdkhome "$JAVA_HOME"'

#this is a hack to bootstrap the boms.  RT 9/26/05
setenv JBICOMPS_DOT_VERSION "1.0"

#######
#cygwin: set java versions of SRCROOT, TOOLROOT (used in ant scripts):
#######
if ( "$FORTE_PORT" == "cygwin" ) then
    #warning - run cygpath on each, to convert paths of the /cygdrive form.  RT 9/13/06
    setenv JV_SRCROOT  `cygpath -wm "$SRCROOT"`
    setenv JV_TOOLROOT `cygpath -wm "$TOOLROOT"`
    setenv JV_GFBASE  `cygpath -wm "$GFBASE"`
    setenv JV_NETBEANS_HOME  `cygpath -wm "$NETBEANS_HOME"`
else
    setenv JV_SRCROOT "$SRCROOT"
    setenv JV_TOOLROOT "$TOOLROOT"
    setenv JV_GFBASE "$GFBASE"
    setenv JV_NETBEANS_HOME  "$NETBEANS_HOME"
endif

#for backward compatibility:
setenv AS8BASE "$GFBASE"
setenv JV_AS8BASE "$JV_GFBASE"

HALT:

#dangerous to leave csh variables laying about:
unset pwd
unset srcroot
unset opt
unset fortepj

#these variables are PRIVATE and RESERVED for this definition file:
unsetenv IIS_TOOLROOT
unsetenv IIS_CVSROOT
unsetenv IIS_BRANCH_NAME
unsetenv IIS_CODELINE
unsetenv IIS_GFBASE
unsetenv IIS_NETBEANS_HOME

#these variables are needed to update the local project tool sources during toolsBuild
setenv CVS_SRCROOT_PREFIX "open-dm-mi"
setenv CVS_CO_ROOT "${SRCROOT}/.."
setenv CVSIGNORE "Makefile bld target"

#finally, set the current release tag:
setenv REV "SM10"
