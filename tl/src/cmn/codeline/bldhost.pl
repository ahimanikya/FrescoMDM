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
# @(#)bldhost.pl
# Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
#
# END_HEADER - DO NOT EDIT
#

package bldhost;    #info about codeline build machines.

#require "dumpvar.pl";
require "codeline.pl";

$p = $'p;       #$main'p is the program name set by the skeleton

sub main
{
    local(*ARGV, *ENV) = @_;
    local($FS) = " ";       #default field separator

    #set global flags:
    return (1) if (&parse_args(*ARGV, *ENV) != 0);
    return (0) if ($HELPFLAG);

    #check for queries first...
    if ($NQUERIES > 0) {
        $port = $PORTS[0];
        if ($UNIX_QUERY) { $ans = &cl'is_unix_port($port);
        } elsif ($NT_QUERY) {
            $ans = &cl'is_nt_port($port);
        } elsif ($VMS_QUERY) {
            $ans = &cl'is_vms_port($port);
        } elsif ($EMAIL_QUERY) {
            $ans = &cl'has_email($port);
        } elsif ($BASE_PORT_QUERY) {
            $ans = &cl'productbaseport();
        } elsif ($PORT_OFFSET_QUERY) {
            $ans = &cl'userportoffset($USER_NAME);
        } elsif ($VERSION_QUERY) {
            $ans = &cl'versionproperties($PRODUCT_ARG);
        } else {
            print STDERR "$p - INTERNAL ERROR - NQUERIES\n";
            return 1;
        }

        #return status 0 if query is true.
        if ($ans) {
            print "YES\n" if ($VERBOSE);
            return 0;
        } else {
            print "NO\n" if ($VERBOSE);
            return 1;
        }
    }

    local ($cnt) = 0;
    local(@portlist);
    if ($KIT_PORTS_ONLY) {
        @portlist = keys(%cl::KIT_PORTS);
    } else {
        @portlist = @PORTS;
    }

    for $port (@portlist) {
        local($sep) = ($cnt == 0) ? "" : $LIST_DELIMITER;
        ++$cnt;

        if ($PORTS_ONLY) {
            print "$sep$port";
            next;
        }

        if ($PATHVARS_OPTION) {
            $ans = join($LIST_DELIMITER, &cl'pathvars($port));
        } else {
            $ans = &cl'which_machine($port);
        }

        if ($VERBOSE) {
            print "$port    $ans\n";
        } else {
            print "$sep$ans";
        }
    }
    print "\n" unless($VERBOSE && !$PORTS_ONLY);

    return 0;
}

sub parse_args
#proccess command-line aguments
{
    local(*ARGV, *ENV) = @_;
    local ($flag, $arg);

    $HELPFLAG = 0;
    $VERBOSE = 0;
    $ALL_PORTS = 0;
    $PORTS_ONLY = 0;
    $KIT_PORTS_ONLY = 0;
    @PORTS = ();
    $LIST_DELIMITER = " ";  #default list delimiter
    $NT_QUERY = 0;
    $UNIX_QUERY = 0;
    $VMS_QUERY = 0;
    $EMAIL_QUERY = 0;
    $PATHVARS_OPTION = 0;
    $PORT_OFFSET_QUERY = 0;
    $BASE_PORT_QUERY = 0;
    $PRODUCT_ARG = 'shasta';  #default product name for this config.
    $VERSION_QUERY = 0;

    #eat up flag args:
    while ($#ARGV+1 > 0 && $ARGV[0] =~ /^-/) {
        $flag = shift(@ARGV);

        if ($flag eq '-a') {
            $ALL_PORTS = 1;
            @PORTS = (@PORTS, &cl'forte_ports());
        } elsif ($flag =~ '^-unix') {
            @PORTS = (@PORTS, &cl'unix_ports());
        } elsif ($flag =~ '^-other') {
            @PORTS = (@PORTS, &cl'other_ports());
        } elsif ($flag =~ '^-psuedo') {
            @PORTS = (@PORTS, &cl'psuedo_ports());
        } elsif ($flag =~ '^-aux') {
            @PORTS = (@PORTS, &cl'auxiliary_ports());
        } elsif ($flag =~ '^-port') {
            $PORTS_ONLY = 1;
        } elsif ($flag =~ '^-kitports') {
            #limit output to ports we are building kits for:
            $KIT_PORTS_ONLY = 1;
        } elsif ($flag =~ '^-colon') {
            $LIST_DELIMITER = ":";
        } elsif ($flag =~ '^-comma') {
            $LIST_DELIMITER = ",";
        } elsif ($flag =~ '^-is_nt') {
            $NT_QUERY = 1;
        } elsif ($flag =~ '^-is_vms') {
            $VMS_QUERY = 1;
        } elsif ($flag =~ '^-is_unix') {
            $UNIX_QUERY = 1;
        } elsif ($flag =~ '^-has_email') {
            $EMAIL_QUERY = 1;
        } elsif ($flag =~ /^-vers/) {
            $VERSION_QUERY = 1;
        } elsif ($flag =~ '^-v') {
            $VERBOSE = 1;
        } elsif ($flag =~ '^-prod') {
            if (!@ARGV) {
                printf STDERR "Usage:  %s -product lassen|shasta|openesb|whitney\n", $p;
                return(1);
            }
            $PRODUCT_ARG = shift(@ARGV);
            if ($PRODUCT_ARG !~ /^(lassen|shasta|openesb|whitney)/) {
                printf STDERR "%s: '%s' is not a valid product name\n", $p, $PRODUCT_ARG;
                printf STDERR "Usage:  %s -product lassen|shasta|openesb|whitney\n", $p;
                return(1);
            }
        } elsif ($flag =~ '^-productbaseport') {
            $BASE_PORT_QUERY = 1;
        } elsif ($flag =~ '^-userportoffset') {
            $PORT_OFFSET_QUERY = 1;
            if (!@ARGV) {
                printf STDERR "Usage:  %s -userportoffset USER_NAME\n", $p;
                return(1);
            }
            $USER_NAME = shift(@ARGV);
            if ($USER_NAME =~ '^-') {
                printf STDERR "%s: '%s' is not a valid user name\n", $p, $USER_NAME;
                return(1);
            }

        } elsif ($flag =~ '^-pathvar') {
            $PATHVARS_OPTION = 1;
        } elsif ($flag =~ '^-h') {
            $HELPFLAG = 1;
            return(&usage(0));
        } else {
            print STDERR "option $flag not recognized.\n";;
            return(&usage(1));
        }
    }

    #add remaining args:
    if ($#ARGV >= 0 && $ARGV[0] ne "") {
        @PORTS = @ARGV;
    }

    $NQUERIES = $NT_QUERY + $UNIX_QUERY + $VMS_QUERY + $EMAIL_QUERY + $PORT_OFFSET_QUERY
                + $BASE_PORT_QUERY + $VERSION_QUERY;
    if ($NQUERIES > 1) {
        print STDERR "too many queries - only one allowed per call.\n";
        return &usage(1);
    } elsif ($NQUERIES == 1 && $#PORTS > 0) {
        print STDERR "can have exactly one port per query\n";
        return &usage(1);
    }

    return(0);
}

sub usage
{
    local($status) = @_;
    local($CODELINE) = &cl'codeline();

    print STDERR <<"!";
Usage:  $p [-help] [options...] [forte_port...]

Synopsis:

Returns build machine names for the $CODELINE codeline.

Options:
  -help      display usage message.
  -v         include the FORTE_PORT names in display.
  -port      print the ports only.
  -kitports  limit output to ports we are building kits for.
  -a         return the name of all the build machines.
  -unix      return unix build hosts only.
  -other     return non-unix port names (win30, mac, etc.)
  -psuedo    return psuedo port names (cmn, ntcmn, etc.))
  -aux       return auxiliary unix hosts (linux, sparc, etc.).
  -pathvars  return the names of BIN_PATH & SHLIB_PATH variable names.
  -is_unix   return 0 status if port is a unix port; otherwise 1.
  -is_nt     return 0 status if port is an NT class port; otherwise 1.
  -is_vms    return 0 status if port is an VMS class port; otherwise 1.
  -has_email return 0 status if port is email capable; otherwise 1.
  -product lassen|shasta|openesb|whitney
             generate information for lassen, openesb, whitney, or shasta.  Default is shasta.
  -versionproperties
             display version properties for -product.
  -productbaseport
             return the base network port assigned to this codeline.
  -userportoffset <user>
             return the network port offset assigned to <user>.
  -comma     separate lists with commas instead of spaces
  -colon     separate lists with colons instead of spaces

NOTES:
  Options are additive - e.g., "-unix -aux" will return
  unix and auxiliary ports, "-a -a" will return all ports
  twice, etc.

  The -port option is useful for setting up parallel arrays
  (csh/tcsh) syntax:

      set myhosts=`bldhost -unix`
      set myports=`bldhost -port -unix`

  will set up two array variables with corresponding indexes -
  i.e., \$myhosts[1] is the hostname of \$myports[1].

EXAMPLES:
  set myportoffset=`bldhost -userportoffset \$LOGNAME`

!
    return($status);
}

sub cleanup
{
}
1;
