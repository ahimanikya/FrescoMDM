<?xml version="1.0" encoding="UTF-8"?>
<!--
 # DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 #
 # Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 #
 # The contents of this file are subject to the terms of the Common 
 # Development and Distribution License ("CDDL")(the "License"). You 
 # may not use this file except in compliance with the License.
 #
 # You can obtain a copy of the License at
 # https://open-dm-mi.dev.java.net/cddl.html
 # or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 # specific language governing permissions and limitations under the  
 # License.  
 #
 # When distributing the Covered Code, include this CDDL Header Notice 
 # in each file and include the License file at
 # open-dm-mi/bootstrap/legal/license.txt.
 # If applicable, add the following below this CDDL Header, with the 
 # fields enclosed by brackets [] replaced by your own identifying 
 # information: "Portions Copyrighted [year] [name of copyright owner]"
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:project="http://www.netbeans.org/ns/project/1"
                xmlns:ear="http://www.netbeans.org/ns/j2ee-earproject/2"
                xmlns:xalan="http://xml.apache.org/xslt"
                exclude-result-prefixes="xalan project">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" xalan:indent-amount="4"/>
    <xsl:template match="/">
    
        <!-- Annoyingly, the JAXP impl in JRE 1.4.2 seems to randomly reorder attrs. -->
        <!-- (I.e. the DOM tree gets them in an unspecified order?) -->
        <!-- As a workaround, use xsl:attribute for all but the first attr. -->
        <!-- This seems to produce them in the order you want. -->
        <!-- Tedious, but appears to do the job. -->
        <!-- Important for build.xml, which is very visible; not so much for build-impl.xml. -->

        <xsl:comment> You may freely edit this file. See commented blocks below for </xsl:comment>
        <xsl:comment> some examples of how to customize the build. </xsl:comment>
        <xsl:comment> (If you delete it and reopen the project it will be recreated.) </xsl:comment>
        
        <xsl:variable name="name" select="/project:project/project:configuration/ear:data/ear:name"/>
        <!-- Synch with build-impl.xsl: -->
        <!-- XXX really should translate all chars that are *not* safe (cf. PropertyUtils.getUsablePropertyName): -->
        <xsl:variable name="codename" select="translate($name, ' ', '_')"/>
        <project name="{$codename}">
            <xsl:attribute name="default">default</xsl:attribute>
            <xsl:attribute name="basedir">.</xsl:attribute>
            <description>Builds, tests, and runs the project <xsl:value-of select="$name"/>.</description>
            <import file="nbproject/build-impl.xml"/>

            <xsl:comment><![CDATA[

    There exist several targets which are by default empty and which can be 
    used for execution of your tasks. These targets are usually executed 
    before and after some main targets. They are: 

      pre-init:                 called before initialization of project properties 
      post-init:                called after initialization of project properties 
      pre-compile:              called before javac compilation 
      post-compile:             called after javac compilation 
      pre-dist:                 called before jar building 
      post-dist:                called after jar building 
      post-clean:               called after cleaning build products 
      pre-run-deploy:           called before deploying
      post-run-deploy:          called after deploying

    Example of pluging an obfuscator after the compilation could look like 

        <target name="post-compile">
            <obfuscate>
                <fileset dir="${build.classes.dir}"/>
            </obfuscate>
        </target>

    For list of available properties check the imported 
    nbproject/build-impl.xml file. 


    Other way how to customize the build is by overriding existing main targets.
    The target of interest are: 

      do-dist:                jar archive building
      run:                    execution of project 

    Example of overriding the target for project execution could look like 

        <target name="run" depends="<PROJNAME>-impl.jar">
            <exec dir="bin" executable="launcher.exe">
                <arg file="${dist.jar}"/>
            </exec>
        </target>

    Notice that overridden target depends on jar target and not only on 
    compile target as regular run target does. Again, for list of available 
    properties which you can use check the target you are overriding in 
    nbproject/build-impl.xml file. 

    ]]></xsl:comment>

        </project>

    </xsl:template>
    
</xsl:stylesheet> 
