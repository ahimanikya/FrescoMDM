#
# BEGIN_HEADER - DO NOT EDIT
# 
# The contents of this file are subject to the terms
# of the Common Development and Distribution License
# (the "License").  You may not use this file except
# in compliance with the License.
#
# You can obtain a copy of the license at
# https://open-jbi-components.dev.java.net/public/CDDLv1.0.html.
# See the License for the specific language governing
# permissions and limitations under the License.
#
# When distributing Covered Code, include this CDDL
# HEADER in each file and include the License file at
# https://open-jbi-components.dev.java.net/public/CDDLv1.0.html.
# If applicable add the following below this CDDL HEADER,
# with the fields enclosed by brackets "[]" replaced with
# your own identifying information: Portions Copyright
# [year] [name of copyright owner]
#

#
# @(#)codeline.pl - ver 1.1 - 01/04/2006
#
# Copyright 2004-2006 Sun Microsystems, Inc. All Rights Reserved.
# 
# END_HEADER - DO NOT EDIT
#

#
#codeline.pl - official codeline information.  This information
#              is PERL_LIBPATH dependent and specific to the local codeline.
#
package cl;

$TESTING = 0;

#what is the release management codeline name?
#warning - don't read this from the environment, as all
#values in this file apply to a specific RE codeline.
$CODELINE = "mdmpatch1";

#official name of the product. Same comments as above:
$PRODUCT = "odmmi";

%FULL_PRODUCT_NAME = (
    'odmmi', 'Open DM MI',
);

%SHORT_PRODUCT_NAME = (
    'odmmi', 'odmmi',
);

%MAJOR_VERSION = (
    'odmmi', '1',
);

%MINOR_VERSION = (
    'odmmi', '1.2',
);

#this is the current release number:
%MILESTONE_VERSION = (
    'odmmi', 'R6U1',
);

#this is the milestone date this release is based on.
%MILESTONE_DATE = (
    'odmmi', '05/08/2007',
);


#BASE port offset to be used for this branch:
#NOTE:  assign next base port in increments of 6400,
#which allows for 64 developers. RT 8/23/02
#ACTIVE ASSIGNMENTS as of 10/24/06:
#   odmmi{trunk}       0 
#   UNASSIGNED      6400
#   UNASSIGNED     12800
#   UNASSIGNED     19200
#   UNASSIGNED     25600
#   UNASSIGNED     32000
#   UNASSIGNED     38400
#   UNASSIGNED     44800
#   UNASSIGNED     51200
#   UNASSIGNED     57600
#   UNASSIGNED     64000

#odmmi{trunk}:
$PRODUCT_BASE_PORT = "0";

#for newer codelines, the regress root can be set to be the web
#document root.  Example:  /usr/iplanet/servers/docs
if (defined($ENV{'WEBDOC_ROOT'})) {
    $REGRESS_ROOT="$ENV{'WEBDOC_ROOT'}/$PRODUCT/$CODELINE";
} else {
    $REGRESS_ROOT="/regressDirs/$PRODUCT/$CODELINE";
}

$KIT_DIRNAME = $PRODUCT;        #this is not always the same, e.g., "conduct"

@UNIX_PORTS = (
    "solsparc","cygwin","solx86","redhat","macosx"
);

@NT_PORTS = (
   "xp" 
);

@VMS_PORTS = ();
@MAC_PORTS = ();

@OTHER_PORTS = (@NT_PORTS, @MAC_PORTS, @VMS_PORTS);

#name the ports that have email configured:
@EMAIL_CAPABLE = (@UNIX_PORTS, "nt");

#tools development only:
@AUX_PORTS = ();

#places where scripts go:
@PSUEDO_PORTS = (
    "cmn",      #unix common
    "ntcmn",    #nt5, xp
);

#offical codeline port names:
@PORT_NAMES = (@UNIX_PORTS, @OTHER_PORTS);
@NO_BUILDRESULTS = ("w95");

#regress port names vary slightly:
@REGRESS_PORTS = (@UNIX_PORTS, @OTHER_PORTS);

#offical build machines assigned to each port:
#multiple build machines are concatenated with "+"
%BUILD_MACHINES=(
#unix ports:
    'solsparc', 'birch.stc.com',
    'solx86', 'drcook.stc.com',
    'redhat', 'unassigned',
    'cygwin', 'browns.stc.com',
    'macosx', 'unassigned',
#non-unix ports:
     'xp',      'unassigned',
#psuedo ports:
    "cmn",      'PSUEDO',
    "ntcmn",    'PSUEDO',
);

#define here if you want to produce a kit for this port name:
%KIT_PORTS = (
    'solsparc', '1',
    'solx86', '1',
    'linux', '1',
    'macosx', '1',
    'rs6000', '1',
    'nt5',      '1',
);

#
#NOTE:  if the regress dir is undefined in the following structure,
#       it is set to:
#           /net/<machine>/$FORTE_LINKROOT/regress
# this is normally only standard on unix machines.
#
# if the port is "NULL", then the build/test results are not available
# in a standardized way.
#
$LOG_SERVER="luna";
$LOG_VOLUME="rmstage";
%REGRESS_DIRS=(
    "solsparc", "/net/birch.stc.com/space/bld/ojc/main/build/open-jbi-components/main/solsparc",
    "solx86", "/net/drcook.stc.com/space/bld/ojc/main/build/open-jbi-components/main/solx86",
    "redhat", "/net/tbd.stc.com/space/bld/ojc/main/build/open-jbi-components/main/redhat",
    "macosx", "/net/tbd.stc.com/space/bld/ojc/main/build/open-jbi-components/main/macosx",
    "cygwin", "/net/browns.stc.com/bld/open-jbi-components/main/cygwin",
);

%NIGHTBUILD_LOGNAMES = (
    '<DEFAULT>',        '${PRODUCT}_build.$PORT',
    "nt5", "${PRODUCT}_build.nt",
    "xp", "${PRODUCT}_build.nt",
);

#
#   File:    $SRCROOT/bdb/portdata.dat
#   Purpose: Mapping of Forte Ports to Window Systems and Repository Types.
#   Used By: release and makeRelease
#
#   xm   = MIT X Window System
#   w30  = Microsoft Windows 3.x Display
#   macds    = Macintosh Display System
#   ob   = Objectivity Repository
#   gs   = Gemstone Repository
#   ""   = Btree Repository
#
# Platform    Display       Repository    Comments
#  Name        Type            Type

#alpha      xm      ""  # OpenVMS Alpha
#alphaosf   xm      ob  # Digital Unix
#axpnt      w30     ""  # Windows/NT Alpha
#dgux88k        xm      ob  # DG/UX for Motorola 88100
#dguxi86        xm      ""  # DG/UX for Intel
#hp9000     xm      gs  # HP 9000 PA/RISC
#mac        macds       ""  # Macintosh for Motorola 680x0 
#nt     w30     ob  # Windows/NT Intel
#powermac   macds       ""  # Macintosh for Power PC
#ptxi86     xm      ""  # DYNIX/ptx Intel V4
#rs6000     xm      gs  # IBM AIX
#sequent        xm      gs  # DYNIX/ptx Intel V2
#solsparc   xm      ob  # Sun Solaris SPARC
#sparc      xm      gs  # Sun Sunos SPARC
#vax        xm      ""  # OpenVMS VAX
#w30        w30     ""  # Windows V3x

%DISPLAY_TYPE=(
               'alphaosf',  'xm',
               'dgux88k',   'xm',
               'dguxi86',   'xm',
               'hp9000',    'xm',
               'ptxi86',    'xm',
               'rs6000',    'xm',
               'solsparc',  'xm',
               'solx86',  'xm',
               'sollassen',  'xm',
               'solwhitney',  'xm',
               'linux',  'xm',
               'macosx',  'xm',
               'snimips',  'xm',
#non-unix ports:
               'alpha', 'xm',
               'axpnt', 'nt',
               'nt',        'nt',
               'cygwin',        'nt',
               'nt5',       'nt',
               'xp',       'nt',
               'mvs390',    'mvs390',
               'powermac',  'macds',
               'mac',   'macds',
                );

#NOTE - only need to list exceptions - the default is "PATH"
%BIN_PATHVAR = (
    'mac',  'Commands',
    'powermac', 'Commands',
);

# the shlib PATH VARIABLE name:
%SHLIB_PATHVAR = (
    'alphaosf', 'LD_LIBRARY_PATH',
    'dgux88k',  'LD_LIBRARY_PATH',
    'dguxi86',  'LD_LIBRARY_PATH',
    'ptxi86',   'LD_LIBRARY_PATH',
    'solsparc', 'LD_LIBRARY_PATH',
    'solx86', 'LD_LIBRARY_PATH',
    'sollassen', 'LD_LIBRARY_PATH',
    'solwhitney', 'LD_LIBRARY_PATH',
    'linux', 'LD_LIBRARY_PATH',
    'macosx', 'LD_LIBRARY_PATH',
    'cygwin', 'LD_LIBRARY_PATH',
    'snimips',  'LD_LIBRARY_PATH',
    'alpha',    'NULL',
    'axpnt',    'PATH',
    'nt',       'PATH',
    'nt5',      'PATH',
    'xp',      'PATH',
    #the exceptions:
    'hp9000',   'SHLIB_PATH',
    'rs6000',   'LIBPATH',
    'mvs390',   'LIBPATH',
    'mac',  'Commands',
    'powermac', 'Commands',
);

# the shlib filename extension
%SHLIB_EXT_TYPE = (
                   'alphaosf', '.so',
                   'dgux88k',   '.so',
                   'dguxi86',   '.so',
                   'hp9000',    '.sl',
                   'ptxi86',    '.so',
                   'rs6000',    '.a',
                   'solsparc',  '.so',
                   'solx86',  '.so',
                   'sollassen',  '.so',
                   'solwhitney',  '.so',
                   'linux',  '.so',
                   'macosx',  '.so',
                   'snimips',  '.so',
                   'alpha', '.exe',
                   'axpnt', '.dll',
                   'nt',        '.dll',
                   'cygwin',        '.dll',
                   'nt5',       '.dll',
                   'xp',       '.dll',
                   'mvs390',    '.so',
                   );
# the shlib filename prefix
%SHLIB_PRE_TYPE = (
                   'alphaosf', 'lib',
                   'dgux88k',   'lib',
                   'dguxi86',   'lib',
                   'hp9000',    'lib',
                   'ptxi86',    'lib',
                   'rs6000',    'lib',
                   'solsparc',  'lib',
                   'solx86',  'lib',
                   'sollassen',  'lib',
                   'solwhitney',  'lib',
                   'linux',  'lib',
                   'macosx',  'lib',
                   'snimips',  'lib',
                   'alpha', 'lib',
                   'axpnt', '',
                   'nt',        '',
                   'cygwin',        '',
                   'nt5',       '',
                   'xp',       '',
                   'mvs390',    'lib',
                   );
# Some platforms don't put their shlibs into lib
%SHLIB_DIR_TYPE = (
                   'alphaosf', 'lib',
                   'dgux88k',   'lib',
                   'dguxi86',   'lib',
                   'hp9000',    'lib',
                   'ptxi86',    'lib',
                   'rs6000',    'lib',
                   'solsparc',  'lib',
                   'solx86',  'lib',
                   'sollassen',  'lib',
                   'solwhitney',  'lib',
                   'linux',  'lib',
                   'macosx',  'lib',
                   'snimips',  'lib',
                   'alpha', 'lib',
                   'axpnt', 'bin',
                   'nt',        'bin',
                   'cygwin',        'bin',
                   'nt5',       'bin',
                   'xp',       'bin',
                   'mvs390',    'lib',
                   );
# the extension for executables
%BIN_EXT_TYPE = (
                 'alphaosf', '',
                 'dgux88k', '',
                 'dguxi86', '',
                 'hp9000',  '',
                 'ptxi86',  '',
                 'rs6000',  '',
                 'solsparc',    '',
                 'solx86',    '',
                 'sollassen',    '',
                 'solwhitney',    '',
                 'linux',    '',
                 'macosx',    '',
                 'snimips',  '',
                 'alpha',   '.exe',
                 'axpnt',   '.exe',
                 'nt',  '.exe',
                 'cygwin',  '.exe',
                 'nt5', '.exe',
                 'xp', '.exe',
                 'mvs390',  '',
                 );

%USERPORTOFFSET = (
    #Assign unallocated (userNNN) first.  RT 1/26/06
    'Useradmin',     '0',
    'abuilds',       '0',
    'user100',   '100',
    'cartman',   '200',
    'chikkala',  '300',
    'user400',   '400',
    'user500',   '500',
    'df98440',   '600',
    'ebratt',    '700',
    'gr194358',   '800',   #gopalan.raj
    'gc140975',  '900',    #gangadhar.cm
    'ic100252', '1000',    #ian.chalmers
    'user1100', '1100',
    'kcbabo',   '1200',
    'user1300', '1300',
    'khasling', '1400',
    'lautz',    '1500',
    'ls3',      '1600',
    'markrs',   '1700',
    'mikewr',   '1800',
    'myk',      '1900',
    'user2000', '2000',
    'nikita',   '2100',
    'poying',   '2200',
    'user2300', '2300',
    'raghavks', '2400',
    'rp138409', '2500',    #ramesh.parthasarathy
    'user2600', '2600',
    'user2700', '2700',
    'rtenhove', '2800',
    'russt',    '2900',
    'rtremain', '2900',
    'user3000', '3000',
    'sc112017', '3100',    #stuart.cooper
    'sjward',   '3200',
    'user3300', '3300',
    'spotiny',  '3400',
    'user3500',  '3500',
    'user3600', '3600',
    'user3700', '3700',
    'user3800', '3800',
    'na140156', '3900',    #nilesh.apte
    'pt145033', '4000',    #priti.tiwary
    'sr198758', '4100',    #srinivasan.rengarajan
    #DO NOT ASSIGN ANY NUMBERS PAST 4100.  Use unallocated first.  RT 1/26/06
);

sub which_display
{
    local ($port) = @_;
    local ($ret);

    $ret = $DISPLAY_TYPE{$port};
    return ($ret) if (defined($ret));
    return ("NULL");
}

sub display_types
{
    local($disp);
    local(%uniqKeyTable);
    
    foreach $disp (values %DISPLAY_TYPE) {
        $uniqKeyTable{$disp} = 1;
    }
    return (keys %uniqKeyTable);
}

sub ports_with_display
{
    local($disp_type) = @_;
    local(@port_list);
    local($port);

    @port_list = ();
    foreach $port (keys %DISPLAY_TYPE) {
        if ($disp_type eq $DISPLAY_TYPE{$port}) {
            push(@port_list, $port);
        }
    }
    return @port_list;
}

sub shlib_ext
{
    local ($port) = @_;
    local ($ret);

    $ret = $SHLIB_EXT_TYPE{$port};
    return ($ret) if (defined($ret));
    return ("NULL");
}

sub shlib_pre
{
    local ($port) = @_;
    local ($ret);

    $ret = $SHLIB_PRE_TYPE{$port};
    return ($ret) if (defined($ret));
    return ("NULL");
}

sub shlib_dir
{
    local ($port) = @_;
    local ($ret);

    $ret = $SHLIB_DIR_TYPE{$port};
    return ($ret) if (defined($ret));
    return ("NULL");
}

sub bin_ext
{
    local ($port) = @_;
    local ($ret);

    $ret = $BIN_EXT_TYPE{$port};
    return ($ret) if (defined($ret));
    return ("NULL");
}

sub product
{
    return($PRODUCT);
}

sub codeline
{
    return($CODELINE);
}

sub regress_root
{
    return($REGRESS_ROOT);
}

sub regress_ports
{
    return (@REGRESS_PORTS);
}

sub which_machine
{
    local ($port) = @_;
    local ($ret);

    $ret = $BUILD_MACHINES{$port};
    return ($ret) if (defined($ret));
    return ("NULL");
}

sub regress_dir
{
    local ($port) = @_;
    local ($ret);

    $ret = $REGRESS_DIRS{$port};

#printf("regress_dir port=%s ret=%s\n", $port, defined($ret) ? $ret : "UNDEF");

    if (!defined($ret)) {
        #return "standard" automounter location:

        #e.g.:  /net/tigger/bld/fusion/main
        $ret = "/net/" . &which_machine($port) . "/bld/$PRODUCT/$CODELINE";
    }

    return ($ret);
}

sub nightbuild_log
#usage:  $logfilename = &codeline'nightbuild_log(<port>, <date>);
{
    local ($PORT, $YYMMDD) = @_;
    local ($ret);

    if (!defined($YYMMDD)) {
        return("NIGHTBUILD_LOG_BAD_ARGS");
    }

    $ret = $NIGHTBUILD_LOGNAMES{$PORT};

    if (!defined($ret)) {
        $ret = $NIGHTBUILD_LOGNAMES{'<DEFAULT>'};
    }

    local($cmd) = "\$ret = \"$ret\";";

#printf STDERR ("PORT='%s' YYMMDD='%s' cmd='%s'\n", $PORT, $YYMMDD, $cmd);

    eval($cmd);

    return ($ret);
}

sub other_ports
{
    return (@OTHER_PORTS);
}

sub unix_ports
{
    return (@UNIX_PORTS);
}

sub nt_ports
{
    return (@NT_PORTS);
}

sub mac_ports
{
    return (@MAC_PORTS);
}

sub vms_ports
{
    return (@VMS_PORTS);
}

sub auxiliary_ports
{
    return (@AUX_PORTS);
}

sub psuedo_ports
{
    return (@PSUEDO_PORTS);
}

sub forte_ports
{
    return (@PORT_NAMES);
}

sub is_unix_port
#returns true if <query_port> is an unix-class port.
{
    local ($queryport) = @_;

    if (!defined($queryport)) {
        print STDERR "Usage:  \&cl::is_unix_port(port_name)\n";
        return 0;
    } else {
#printf "q=%s unix=(%s) grep=%d\n", $queryport, join(',', @UNIX_PORTS), scalar grep($_ eq $queryport, @UNIX_PORTS);
        return scalar grep($_ eq $queryport, @UNIX_PORTS);
    }
}

sub is_vms_port
#returns true if <query_port> is an vms-class port.
{
    local ($queryport) = @_;

    if (!defined($queryport)) {
        print STDERR "Usage:  \&cl::is_vms_port(port_name)\n";
        return 0;
    } else {
        return scalar grep($_ eq $queryport, @VMS_PORTS);
    }
}

sub is_nt_port
#returns true if <query_port> is an nt-class port.
{
    local ($queryport) = @_;

    if (!defined($queryport)) {
        print STDERR "Usage:  \&cl::is_nt_port(port_name)\n";
        return 0;
    } else {
        return scalar grep($_ eq $queryport, @NT_PORTS);
    }
}

sub pathvars
#returns the list:  (<BIN_PATHVAR>, <SHLIB_PATHVAR>).
#returns "NULL" for missing values.
{
    local ($queryport) = @_;
    my(@ret) = ();

    if (!defined($queryport)) {
        print STDERR "Usage:  \&cl::pathvars(port_name)\n";
        return("NULL", "NULL");
    }
    
    if (defined($BIN_PATHVAR{$queryport})) {
        push @ret, $BIN_PATHVAR{$queryport};
    } else {
        #this is the default:
        push @ret, "PATH";
    }

    if (defined($SHLIB_PATHVAR{$queryport})) {
        push @ret, $SHLIB_PATHVAR{$queryport};
    } else {
        push @ret, "NULL";
    }

    return (@ret);
}

sub has_email
#returns true if <query_port> is email capable
{
    local ($queryport) = @_;

    if (!defined($queryport)) {
        print STDERR "Usage:  \&cl::has_email(port_name)\n";
        return 0;
    } else {
        return scalar grep($_ eq $queryport, @EMAIL_CAPABLE);
    }
}

sub productbaseport
#prints the product base network port to stdout.
#return true if successful
{
    if (defined($PRODUCT_BASE_PORT)) {
        printf"%s\n", $PRODUCT_BASE_PORT;
        return 1;  #success
    }

    print "NULL_BASE_PORT\n";
    return 0;  #failure
}

sub userportoffset
#prints a port offset based on the user's login name to stdout.
#returns true if offset is defined for user.
{
    local ($username) = @_;

    if (!defined($username)) {
        print STDERR "Usage:  \&cl::userportoffset(user_name)\n";
        return 0;
    }

    if (defined($USERPORTOFFSET{$username})) {
        printf"%d\n", $USERPORTOFFSET{$username} + $PRODUCT_BASE_PORT;
        return 1;  #success
    } else {
        printf STDERR "USER PORT OFFSET FOR '%s' is not assigned\n", $username;
        print "NULL_PORT_OFFSET\n";
    }

    return 0;  #failure
}

sub versionproperties
#display version properties for <product>.
#true if <product> arg is recognized.
#NOTE:  the output is meant to be read as a java property file.
#colons (:) are therefore escaped with backslash.
{
    local ($product) = @_;
    my ($errs) = 0;
    my ($tmp) = "";
    my ($minor) = "";
    my ($major) = "";

    if (!defined($product)) {
        print STDERR "Usage:  \&cl::versionproperties(product)\n";
        return 0;
    }

    if (defined($FULL_PRODUCT_NAME{$product})) {
        $tmp = $FULL_PRODUCT_NAME{$product}; $tmp =~ s/:/\\:/g;
        printf "FULL_PRODUCT_NAME=%s\n", $tmp;
    } else {
        printf STDERR "ERROR:  FULL_PRODUCT_NAME for '%s' is undefined\n", $product;
        printf "FULL_PRODUCT_NAME=%s\n", "NULL";
        $errs++;
    }

    if (defined($SHORT_PRODUCT_NAME{$product})) {
        $tmp = $SHORT_PRODUCT_NAME{$product}; $tmp =~ s/:/\\:/g;
        printf"SHORT_PRODUCT_NAME=%s\n", $tmp;
    } else {
        printf STDERR "ERROR:  SHORT_PRODUCT_NAME for '%s' is undefined\n", $product;
        printf "SHORT_PRODUCT_NAME=%s\n", "NULL";
        $errs++;
    }

    if (defined($MAJOR_VERSION{$product})) {
        $tmp = $MAJOR_VERSION{$product}; $tmp =~ s/:/\\:/g;
        $major = $tmp; 
        printf"MAJOR_VERSION=%s\n", $tmp;
    } else {
        printf STDERR "ERROR:  MAJOR_VERSION for '%s' is undefined\n", $product;
        printf "MAJOR_VERSION=%s\n", "NULL";
        $errs++;
    }

    if (defined($MINOR_VERSION{$product})) {
        $tmp = $MINOR_VERSION{$product}; $tmp =~ s/:/\\:/g;
        $minor = $tmp;
        printf"MINOR_VERSION=%s\n", $tmp;
    } else {
        printf STDERR "ERROR:  MINOR_VERSION for '%s' is undefined\n", $product;
        printf "MINOR_VERSION=%s\n", "NULL";
        $errs++;
    }

    $FULL_VERSION_UL = "$major.$minor";
    $FULL_VERSION_UL =~ s/\./_/g;
    printf "FULL_VERSION_UL=%s\n", $FULL_VERSION_UL;
    $FULL_VERSION = "$major.$minor";
    printf"FULL_VERSION=%s\n", $FULL_VERSION;

    if (defined($MILESTONE_VERSION{$product})) {
        $tmp = $MILESTONE_VERSION{$product}; $tmp =~ s/:/\\:/g;
        printf"MILESTONE_VERSION=%s\n", $tmp;
    } else {
        printf STDERR "ERROR:  MILESTONE_VERSION for '%s' is undefined\n", $product;
        printf "MILESTONE_VERSION=%s\n", "NULL";
        $errs++;
    }

    if (defined($MILESTONE_DATE{$product})) {
        $tmp = $MILESTONE_DATE{$product}; $tmp =~ s/:/\\:/g;
        printf"MILESTONE_DATE=%s\n", $tmp;
    } else {
        printf STDERR "ERROR:  MILESTONE_DATE for '%s' is undefined\n", $product;
        printf "MILESTONE_DATE=%s\n", "NULL";
        $errs++;
    }

    return ($errs == 0);  #true if no errors
}

sub squawk_off
#quiet extraneous errors
{
    if (1 > 2) {
        *RELEASE = *RELEASE;
        *NO_BUILDRESULTS = *NO_BUILDRESULTS;
        *NO_TESTRESULTS = *NO_TESTRESULTS;
    }
}

#####################################
#TEST PROGRAM:
#example - to test on unix:
#   % ln -s `fwhich prlskel` codeline
#   % codeline solsparc
#####################################
if ($TESTING) {

    package main;

    @tmp = split('/', $0); $p = $tmp[$#tmp];

    die ("Usage:  $p port\n") if ($#ARGV < 0);
    $ans = &cl'which_machine($ARGV[0]);
    print "$ans\n";
    $ans = &cl'nightbuild_log($ARGV[0], "970812");
    print "$ans\n";
    exit 0;

    &cl'squawk_off; #never called
}

1;
