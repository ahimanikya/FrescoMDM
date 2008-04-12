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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:p="http://www.netbeans.org/ns/project/1" xmlns:xalan="http://xml.apache.org/xslt"
                xmlns:ear="http://www.netbeans.org/ns/j2ee-earproject/1"
                xmlns:ear2="http://www.netbeans.org/ns/j2ee-earproject/2"
                xmlns:projdeps="http://www.netbeans.org/ns/ant-project-references/1"
                exclude-result-prefixes="xalan p ear projdeps">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" xalan:indent-amount="4" />
    <xsl:template match="/">
        
        <xsl:comment><![CDATA[
*** GENERATED FROM project.xml - DO NOT EDIT  ***
***         EDIT ../build.xml INSTEAD         ***

For the purpose of easier reading the script
is divided into following sections:

  - initialization
  - compilation
  - dist
  - execution
  - debugging
  - cleanup

        ]]></xsl:comment>
        
        <xsl:variable name="name" select="/p:project/p:configuration/ear2:data/ear2:name" />
        <xsl:variable name="codename" select="translate($name, ' ', '_')" />
        <project name="{$codename}-impl">
            <xsl:attribute name="default">build</xsl:attribute>
            <xsl:attribute name="basedir">..</xsl:attribute>
            <import file="ant-deploy.xml" />
            <target name="default">
                <xsl:attribute name="depends">dist</xsl:attribute>
                <xsl:attribute name="description">Build whole project.</xsl:attribute>
            </target>
            
            <xsl:comment>INITIALIZATION SECTION</xsl:comment>
            
            <target name="pre-init">
                <xsl:comment>Empty placeholder for easier customization.</xsl:comment>
                <xsl:comment>You can override this target in the ../build.xml file.</xsl:comment>
            </target>
            
            <target name="init-private">
                <xsl:attribute name="depends">pre-init</xsl:attribute>
                <property file="nbproject/private/private.properties" />
            </target>
            
            <target name="init-userdir">
                <xsl:attribute name="depends">pre-init,init-private</xsl:attribute>
                <property name="user.properties.file"
                          location="${{netbeans.user}}/build.properties" />
            </target>
            
            <target name="init-user">
                <xsl:attribute name="depends">pre-init,init-private,init-userdir</xsl:attribute>
                <property file="${{user.properties.file}}" />
            </target>
            
            <target name="init-project">
                <xsl:attribute name="depends">pre-init,init-private,init-userdir,init-user</xsl:attribute>
                <property file="nbproject/project.properties" />
            </target>
            
            <target name="do-init">
                <xsl:attribute name="depends">pre-init,init-private,init-userdir,init-user,init-project</xsl:attribute>
                <xsl:if test="/p:project/p:configuration/ear2:data/ear2:explicit-platform">
                    <!--Setting java and javac default location -->
                    <property name="platforms.${{platform.active}}.javac"
                              value="${{platform.home}}/bin/javac" />
                    <property name="platforms.${{platform.active}}.java"
                              value="${{platform.home}}/bin/java" />
                    <!-- XXX Ugly but Ant does not yet support recursive property evaluation: -->
                    <tempfile property="file.tmp" prefix="platform" suffix=".properties" />
                    <echo file="${{file.tmp}}">
                        platform.home=$${platforms.${platform.active}.home}
                        platform.bootcp=$${platforms.${platform.active}.bootclasspath}
                        build.compiler=$${platforms.${platform.active}.compiler}
                        platform.java=$${platforms.${platform.active}.java}
                        platform.javac=$${platforms.${platform.active}.javac}
                    </echo>
                    <property file="${{file.tmp}}" />
                    <delete file="${{file.tmp}}" />
                    <fail unless="platform.home">Must set platform.home</fail>
                    <fail unless="platform.bootcp">Must set platform.bootcp</fail>
                    <fail unless="platform.java">Must set platform.java</fail>
                    <fail unless="platform.javac">Must set platform.javac</fail>
                </xsl:if>
                <xsl:comment>The two properties below are usually overridden</xsl:comment>
                <xsl:comment>by the active platform. Just a fallback.</xsl:comment>
                <property name="default.javac.source" value="1.4" />
                <property name="default.javac.target" value="1.4" />
                <xsl:if test="/p:project/p:configuration/ear2:data/ear2:use-manifest">
                    <fail unless="manifest.file">Must set manifest.file</fail>
                </xsl:if>
                <condition property="do.compile.jsps">
                    <istrue value="${{compile.jsps}}" />
                </condition>
                <condition property="do.display.browser">
                    <and>
                        <istrue value="${{display.browser}}" />
                        <isset property="client.module.uri" />
                        <not>
                            <isset property="app.client" />
                        </not>
                    </and>
                </condition>
                <available property="has.custom.manifest" file="${{meta.inf}}/MANIFEST.MF" />
                <condition property="j2ee.appclient.mainclass.tool.param"
                           value="-mainclass ${{main.class}}" else="">
                    <and>
                        <isset property="main.class" />
                        <not>
                            <equals arg1="${{main.class}}" arg2="" trim="true" />
                        </not>
                    </and>
                </condition>
                <condition property="j2ee.appclient.jvmoptions.param"
                           value="${{j2ee.appclient.jvmoptions}}" else="">
                    <and>
                        <isset property="j2ee.appclient.jvmoptions" />
                        <not>
                            <equals arg1="${{j2ee.appclient.jvmoptions}}" arg2="" trim="true" />
                        </not>
                    </and>
                </condition>
                <condition property="application.args.param" value="${{application.args}}"
                           else="">
                    <and>
                        <isset property="application.args" />
                        <not>
                            <equals arg1="${{application.args}}" arg2="" trim="true" />
                        </not>
                    </and>
                </condition>
                <condition property="can.debug.appclient">
                    <and>
                        <isset property="netbeans.home" />
                        <isset property="app.client" />
                    </and>
                </condition>
                <path id="generate.class.path">
                    <pathelement location="${{module.install.dir}}/ext/mdm/com-sun-mdm-index-project-anttasks.jar" />
                    <pathelement location="${{module.install.dir}}/com-sun-mdm-index-project.jar" />
                    <pathelement location="${{module.install.dir}}/ext/mdm/index-core.jar" />
                    <pathelement location="${{module.install.dir}}/ext/mdm/standardizer/lib/commons-logging-1.0.4.jar"/>
                </path>
                <taskdef name="generate-mdm-index-files"
                         classname="com.sun.mdm.index.project.anttasks.EViewGeneratorTask">
                    <classpath refid="generate.class.path"/>
                </taskdef>
                
                <taskdef name="generate-loader-zip"
                         classname="com.sun.mdm.index.project.anttasks.LoaderGeneratorTask">
                    <classpath>
                        <pathelement
                            location="${{module.install.dir}}/ext/mdm/com-sun-mdm-index-project-anttasks.jar" />
                        <pathelement
                            location="${{module.install.dir}}/com-sun-mdm-index-project.jar" />
                        <pathelement location="${{module.install.dir}}/ext/mdm/index-core.jar" />
                    </classpath>
                </taskdef>
                
            </target>
            
            <target name="post-init">
                <xsl:comment>Empty placeholder for easier customization.</xsl:comment>
                <xsl:comment>You can override this target in the ../build.xml file.</xsl:comment>
            </target>
            
            <target name="init-check">
                <xsl:attribute name="depends">pre-init,init-private,init-userdir,init-user,init-project,do-init</xsl:attribute>
                <!-- XXX XSLT 2.0 would make it possible to use a for-each here -->
                <!-- Note that if the properties were defined in project.xml that would be easy -->
                <!-- But required props should be defined by the AntBasedProjectType, not stored in each project -->
                <fail unless="build.dir">Must set build.dir</fail>
                <fail unless="build.generated.dir">Must set build.generated.dir</fail>
                <fail unless="dist.dir">Must set dist.dir</fail>
                <fail unless="build.classes.excludes">Must set build.classes.excludes</fail>
                <fail unless="dist.jar">Must set dist.jar</fail>
            </target>
            
            <target name="init">
                <xsl:attribute name="depends">pre-init,init-private,init-userdir,init-user,init-project,do-init,post-init,init-check</xsl:attribute>
            </target>
            
            <xsl:comment>COMPILATION SECTION</xsl:comment>
            
            <xsl:call-template name="deps.target">
                <xsl:with-param name="targetname" select="'deps-jar'" />
                <xsl:with-param name="type" select="'jar'" />
            </xsl:call-template>
            
            <!--<xsl:call-template name="deps.target">
				<xsl:with-param name="targetname" select="'deps-war'"/>
				<xsl:with-param name="type" select="'j2ee_ear_archive'"/>
				</xsl:call-template>-->

            <xsl:call-template name="deps.target">
                <xsl:with-param name="targetname" select="'deps-j2ee-archive'" />
                <xsl:with-param name="type" select="'j2ee_ear_archive'" />
            </xsl:call-template>
            
            <target name="gen-mdm-index-files">
                <xsl:attribute name="depends">init</xsl:attribute>
                <generate-mdm-index-files srcdir="${{src.dir}}" ejbdir="${{ejb.dir}}"
                                          wardir="${{war.dir}}" />
            </target>
            
            
            
            
            <target name="gen-loader-zip" depends="pre-pre-compile"
                    description="generate loader zip">
                <mkdir dir="loader-generated/loader/conf" />
                <mkdir dir="loader-generated/loader/lib" />
                <mkdir dir="loader-generated/bulkloader"/>
                <copy todir="loader-generated/loader/lib">
                    
                    <fileset dir="lib">
                        <include name="*.jar" />
                    </fileset>
                    <fileset dir="${{module.install.dir}}/ext/mdm/loader">
                        <include name="*.jar" />
                    </fileset>
                </copy>
                <copy todir="loader-generated/bulkloader">
                    <fileset dir="${{module.install.dir}}/ext/mdm/bulkloader"/>
                </copy>
                <generate-loader-zip srcDir="${{src.dir}}" configDir="loader-generated/loader/conf" />
                
                <zip destfile="loader-generated/loader.zip" basedir="loader-generated"
                     excludes="loader.zip">
                </zip>
                
                <delete dir="loader-generated/loader"></delete>
                <delete dir="loader-generated/bulkloader"/>
            </target>
            
            <target name="gen-cleanser-zip" depends="pre-pre-compile" description="generate cleanser zip">
                <mkdir dir="cleanser-generated/cleanser"/>
                <copy todir="cleanser-generated/cleanser">
                    <fileset dir="${{module.install.dir}}/ext/mdm/dataanalysis/cleanser"/>
                </copy>
                <jar destfile="cleanser-generated/cleanser/lib/resources.jar" basedir="files-generated/resource"/>
                <copy file="files-generated/resource/object.xml" todir="cleanser-generated/cleanser"/>
                <copy file="lib/master-index-client.jar" todir="cleanser-generated/cleanser/lib"/>
                <copy todir="cleanser-generated/cleanser/lib">
                    <fileset dir="${{module.install.dir}}/ext/mdm/standardizer/lib"/>
                </copy>
                <zip destfile="cleanser-generated/cleanser.zip" basedir="cleanser-generated" excludes="cleanser.zip"/>
                <delete dir="cleanser-generated/cleanser"/>
            </target>
            
            <target name="gen-profiler-zip" depends="pre-pre-compile" description="generate profiler zip">
                
                <mkdir dir="profiler-generated/profiler"/>
                <copy todir="profiler-generated/profiler">
                    <fileset dir="${{module.install.dir}}/ext/mdm/dataanalysis/profiler"/>
                </copy>
                
                <copy file="files-generated/resource/object.xml" todir="profiler-generated/profiler"/>
                <copy file="lib/master-index-client.jar" todir="profiler-generated/profiler/lib"/>
                <zip destfile="profiler-generated/profiler.zip" basedir="profiler-generated" excludes="profiler.zip"/>
                <delete dir="profiler-generated/profiler"/>
            </target>
            
            <target name="gen-bulkloader-zip" depends="gen-mdm-index-files" description="generate bulk loader zip">
                <mkdir dir="bulkloader-generated"/>
                <zip destfile="bulkloader-generated/bulkloader.zip" basedir="${{module.install.dir}}/ext/mdm/bulkloader" excludes="bulkloader.zip"/>
            </target>
            
            <target name="dist_se">
                <xsl:attribute name="depends">dist</xsl:attribute>
                <copy file="${{dist.jar}}" tofile="${{dist.dir}}/jbi/${{jbi.jar}}" />
                <jar destfile="${{dist.dir}}/jbi/${{jbi.jar}}"
                     basedir="${{eView.generated.dir}}/jbi" update="true" />
            </target>
            
            <target name="pre-pre-compile">
                <xsl:attribute name="depends">init,gen-mdm-index-files,deps-jar,deps-j2ee-archive</xsl:attribute>
                <!--mkdir dir="${{build.classes.dir}}"/-->
            </target>
            
            <target name="pre-compile">
                <xsl:comment>Empty placeholder for easier customization.</xsl:comment>
                <xsl:comment>You can override this target in the ../build.xml file.</xsl:comment>
            </target>
            
            <target name="do-compile">
                <xsl:attribute name="depends">init,deps-jar,pre-pre-compile,pre-compile</xsl:attribute>
                
                <copy todir="${{build.dir}}/META-INF">
                    <fileset dir="${{meta.inf}}" />
                </copy>
                
                <xsl:for-each
                    select="/p:project/p:configuration/ear2:data/ear2:web-module-additional-libraries/ear2:library[ear2:path-in-war]">
                    <xsl:variable name="copyto" select=" ear2:path-in-war" />
                    <xsl:if test="//ear2:web-module-additional-libraries/ear2:library[@files]">
                        <xsl:if test="(@files &gt; 1) or (@files &gt; 0 and (@dirs &gt; 0))">
                            <xsl:call-template name="copyIterateFiles">
                                <xsl:with-param name="files" select="@files" />
                                <xsl:with-param name="target"
                                                select="concat('${build.dir}/',$copyto)" />
                                <xsl:with-param name="libfile" select="ear2:file" />
                            </xsl:call-template>
                        </xsl:if>
                        <xsl:if test="@files = 1 and (@dirs = 0 or not(@dirs))">
                            <xsl:variable name="target" select="concat('${build.dir}/',$copyto)" />
                            <xsl:variable name="libfile" select="ear2:file" />
                            <copy file="{$libfile}" todir="{$target}" />
                        </xsl:if>
                    </xsl:if>
                    <xsl:if test="//ear2:web-module-additional-libraries/ear2:library[@dirs]">
                        <xsl:if test="(@dirs &gt; 1) or (@files &gt; 0 and (@dirs &gt; 0))">
                            <xsl:call-template name="copyIterateDirs">
                                <xsl:with-param name="files" select="@dirs" />
                                <xsl:with-param name="target"
                                                select="concat('${build.dir}/',$copyto)" />
                                <xsl:with-param name="libfile" select="ear2:file" />
                            </xsl:call-template>
                        </xsl:if>
                        <xsl:if test="@dirs = 1 and (@files = 0 or not(@files))">
                            <xsl:variable name="target" select="concat('${build.dir}/',$copyto)" />
                            <xsl:variable name="libfile" select="ear2:file" />
                            <copy todir="{$target}">
                                <fileset dir="{$libfile}" includes="**/*" />
                            </copy>
                        </xsl:if>
                    </xsl:if>
                </xsl:for-each>
                
            </target>
            
            <target name="post-compile">
                <xsl:comment>Empty placeholder for easier customization.</xsl:comment>
                <xsl:comment>You can override this target in the ../build.xml file.</xsl:comment>
            </target>
            
            <target name="compile">
                <xsl:attribute name="depends">init,deps-jar,pre-pre-compile,pre-compile,do-compile,post-compile</xsl:attribute>
                <xsl:attribute name="description">Compile project.</xsl:attribute>
            </target>
            
            <xsl:comment>DIST BUILDING SECTION</xsl:comment>
            <target name="-pre-dist-mdm-lib-">
                <copy todir="${{build.dir}}/lib">
                    <fileset dir="lib"/>
                </copy>
            </target>            
            <target name="pre-dist">
                <xsl:attribute name="depends">-pre-dist-mdm-lib-</xsl:attribute>
                <xsl:comment>Empty placeholder for easier customization.</xsl:comment>
                <xsl:comment>You can override this target in the ../build.xml file.</xsl:comment>
            </target>
            
            <target name="do-dist-without-manifest">
                <xsl:attribute name="depends">init,compile,pre-dist</xsl:attribute>
                <xsl:attribute name="unless">has.custom.manifest</xsl:attribute>
                <dirname property="dist.jar.dir" file="${{dist.jar}}" />
                <mkdir dir="${{dist.jar.dir}}" />
                <jar jarfile="${{dist.jar}}" compress="${{jar.compress}}">
                    <fileset dir="${{build.dir}}" />
                </jar>
            </target>
            
            <target name="do-dist-with-manifest">
                <xsl:attribute name="depends">init,compile,pre-dist</xsl:attribute>
                <xsl:attribute name="if">has.custom.manifest</xsl:attribute>
                <dirname property="dist.jar.dir" file="${{dist.jar}}" />
                <mkdir dir="${{dist.jar.dir}}" />
                <jar jarfile="${{dist.jar}}" compress="${{jar.compress}}"
                     manifest="${{meta.inf}}/MANIFEST.MF">
                    <fileset dir="${{build.dir}}" />
                </jar>
            </target>
            
            <target name="post-dist">
                <xsl:comment>Empty placeholder for easier customization.</xsl:comment>
                <xsl:comment>You can override this target in the ../build.xml file.</xsl:comment>
            </target>
            
            <target name="dist">
                <xsl:attribute name="depends">init,compile,pre-dist,do-dist-without-manifest,do-dist-with-manifest,post-dist</xsl:attribute>
                <xsl:attribute name="description">Build distribution (JAR).</xsl:attribute>
            </target>
            
            <xsl:comment>EXECUTION SECTION</xsl:comment>
            <target name="run">
                <xsl:attribute name="depends">run-deploy,run-display-browser,run-ac</xsl:attribute>
                <xsl:attribute name="description">Deploy to server.</xsl:attribute>
            </target>
            
            <target name="pre-run-deploy">
                <xsl:comment>Empty placeholder for easier customization.</xsl:comment>
                <xsl:comment>You can override this target in the ../build.xml file.</xsl:comment>
            </target>
            
            <target name="post-run-deploy">
                <xsl:comment>Empty placeholder for easier customization.</xsl:comment>
                <xsl:comment>You can override this target in the ../build.xml file.</xsl:comment>
            </target>
            
            <target name="-pre-nbmodule-run-deploy">
                <xsl:comment>Empty placeholder for easier customization.</xsl:comment>
                <xsl:comment>
                    This target can be overriden by NetBeans modules. Don't override it directly,
                    use -pre-run-deploy task instead.
                </xsl:comment>
            </target>
            
            <target name="-post-nbmodule-run-deploy">
                <xsl:comment>Empty placeholder for easier customization.</xsl:comment>
                <xsl:comment>
                    This target can be overriden by NetBeans modules. Don't override it directly,
                    use -post-run-deploy task instead.
                </xsl:comment>
            </target>
            
            <target name="-run-deploy-am" unless="no.deps">
                <xsl:comment>Task to deploy to the Access Manager runtime.</xsl:comment>
                <xsl:call-template name="am.target">
                    <xsl:with-param name="targetname" select="'-run-deploy-am'" />
                </xsl:call-template>
            </target>
            
            <target name="run-deploy">
                <xsl:attribute name="depends">dist,pre-run-deploy,-pre-nbmodule-run-deploy,-run-deploy-nb,-init-deploy-ant,-deploy-ant,-run-deploy-am,-post-nbmodule-run-deploy,post-run-deploy</xsl:attribute>
            </target>
            
            <target name="-run-deploy-nb" if="netbeans.home">
                <nbdeploy debugmode="false" forceRedeploy="${{forceRedeploy}}"
                          clientUrlPart="${{client.urlPart}}" clientModuleUri="${{client.module.uri}}" />
            </target>
            
            <target name="-init-deploy-ant" unless="netbeans.home">
                <property name="deploy.ant.archive" value="${{dist.jar}}" />
                <property name="deploy.ant.resource.dir" value="${{resource.dir}}" />
                <property name="deploy.ant.enabled" value="true" />
            </target>
            
            <target name="run-undeploy">
                <xsl:attribute name="depends">dist,-run-undeploy-nb,-init-deploy-ant,-undeploy-ant</xsl:attribute>
            </target>
            
            <target name="-run-undeploy-nb" if="netbeans.home">
                <fail message="Undeploy is not supported from within the IDE" />
            </target>
            
            <target name="verify">
                <xsl:attribute name="depends">dist</xsl:attribute>
                <nbverify file="${{dist.jar}}" />
            </target>
            
            <target name="run-display-browser">
                <xsl:attribute name="depends">run-deploy,-init-display-browser,-display-browser-nb,-display-browser-cl</xsl:attribute>
            </target>
            
            <target name="-init-display-browser" if="do.display.browser">
                <condition property="do.display.browser.nb">
                    <isset property="netbeans.home" />
                </condition>
                <condition property="do.display.browser.cl">
                    <and>
                        <isset property="deploy.ant.enabled" />
                        <isset property="deploy.ant.client.url" />
                    </and>
                </condition>
            </target>
            
            <target name="-display-browser-nb" if="do.display.browser.nb">
                <nbbrowse url="${{client.url}}" />
            </target>
            
            <target name="-get-browser" if="do.display.browser.cl" unless="browser">
                <condition property="browser" value="rundll32">
                    <os family="windows" />
                </condition>
                <condition property="browser.args" value="url.dll,FileProtocolHandler" else="">
                    <os family="windows" />
                </condition>
                <condition property="browser" value="/usr/bin/open">
                    <os family="mac" />
                </condition>
                <property environment="env" />
                <condition property="browser" value="${{env.BROWSER}}">
                    <isset property="env.BROWSER" />
                </condition>
                <condition property="browser" value="/usr/bin/firefox">
                    <available file="/usr/bin/firefox" />
                </condition>
                <condition property="browser" value="/usr/local/firefox/firefox">
                    <available file="/usr/local/firefox/firefox" />
                </condition>
                <condition property="browser" value="/usr/bin/mozilla">
                    <available file="/usr/bin/mozilla" />
                </condition>
                <condition property="browser" value="/usr/local/mozilla/mozilla">
                    <available file="/usr/local/mozilla/mozilla" />
                </condition>
                <condition property="browser" value="/usr/sfw/lib/firefox/firefox">
                    <available file="/usr/sfw/lib/firefox/firefox" />
                </condition>
                <condition property="browser" value="/opt/csw/bin/firefox">
                    <available file="/opt/csw/bin/firefox" />
                </condition>
                <condition property="browser" value="/usr/sfw/lib/mozilla/mozilla">
                    <available file="/usr/sfw/lib/mozilla/mozilla" />
                </condition>
                <condition property="browser" value="/opt/csw/bin/mozilla">
                    <available file="/opt/csw/bin/mozilla" />
                </condition>
            </target>
            
            <target name="-display-browser-cl" depends="-get-browser" if="do.display.browser.cl">
                <fail unless="browser">
                    Browser not found, cannot launch the deployed application. Try to set the
                    BROWSER environment variable.
                </fail>
                <property name="browse.url" value="${{deploy.ant.client.url}}${{client.urlPart}}" />
                <echo>Launching ${browse.url}</echo>
                <exec executable="${{browser}}" spawn="true">
                    <arg line="${{browser.args}} ${{browse.url}}" />
                </exec>
            </target>
            
            <!-- application client execution -->
            <xsl:call-template name="run.target">
                <xsl:with-param name="id" select="'j2ee-module-car'" />
                <xsl:with-param name="type" select="'j2ee_ear_archive'" />
            </xsl:call-template>
            
            <xsl:comment>DEBUGGING SECTION</xsl:comment>
            <target name="debug">
                <xsl:attribute name="depends">run-debug,run-display-browser,run-debug-appclient</xsl:attribute>
                <xsl:attribute name="description">Deploy to server.</xsl:attribute>
            </target>
            <target name="run-debug">
                <xsl:attribute name="description">Debug project in IDE.</xsl:attribute>
                <xsl:attribute name="depends">dist</xsl:attribute>
                <xsl:attribute name="if">netbeans.home</xsl:attribute>
                <xsl:attribute name="unless">app.client</xsl:attribute>
                <nbdeploy debugmode="true" clientUrlPart="${{client.urlPart}}"
                          clientModuleUri="${{client.module.uri}}" />
                <antcall target="connect-debugger" />
            </target>
            
            <target name="connect-debugger" unless="is.debugged">
                <nbjpdaconnect name="${{name}}" host="${{jpda.host}}" address="${{jpda.address}}"
                               transport="${{jpda.transport}}">
                    <classpath>
                        <path path="${{debug.classpath}}" />
                    </classpath>
                    <sourcepath>
                        <path path="${{ear.docbase.dirs}}" />
                    </sourcepath>
                    <xsl:if test="/p:project/p:configuration/ear2:data/ear2:explicit-platform">
                        <bootclasspath>
                            <path path="${{platform.bootcp}}" />
                        </bootclasspath>
                    </xsl:if>
                </nbjpdaconnect>
            </target>
            
            <!-- application client debugging -->
            <xsl:call-template name="debug.target">
                <xsl:with-param name="id" select="'j2ee-module-car'" />
                <xsl:with-param name="type" select="'j2ee_ear_archive'" />
            </xsl:call-template>
            
            <xsl:comment>CLEANUP SECTION</xsl:comment>
            
            <xsl:call-template name="deps.target">
                <xsl:with-param name="targetname" select="'deps-clean'" />
            </xsl:call-template>
            
            <target name="do-clean">
                <xsl:attribute name="depends">init</xsl:attribute>
                <delete dir="${{build.dir}}" />
                <delete dir="${{dist.dir}}" />
                <delete dir="${{eView.generated.dir}}" />
            </target>
            
            <target name="post-clean">
                <xsl:comment>Empty placeholder for easier customization.</xsl:comment>
                <xsl:comment>You can override this target in the ../build.xml file.</xsl:comment>
            </target>
            
            <target name="clean">
                <xsl:attribute name="depends">init,deps-clean,do-clean,post-clean</xsl:attribute>
                <xsl:attribute name="description">Clean build products.</xsl:attribute>
            </target>
        </project>
        
        <!-- TBD items:
			
			Could pass <propertyset> to run, debug, etc. under Ant 1.6,
			optionally, by doing e.g.
			
			<propertyset>
			<propertyref prefix="sysprop."/>
			<mapper type="glob" from="sysprop.*" to="*"/>
			</propertyset>
			
			Now user can add to e.g. project.properties e.g.:
			sysprop.org.netbeans.modules.javahelp=0
			to simulate
			-Dorg.netbeans.modules.javahelp=0
			
		-->

    </xsl:template>
    
    <!---
		Generic template to build subdependencies of a certain type.
		Feel free to copy into other modules.
		@param targetname required name of target to generate
		@param type artifact-type from project.xml to filter on; optional, if not specified, uses
		all references, and looks for clean targets rather than build targets
		@return an Ant target which builds (or cleans) all known subprojects
	-->
    <xsl:template name="deps.target">
        <xsl:param name="targetname" />
        <xsl:param name="type" />
        <target name="{$targetname}">
            <xsl:attribute name="depends">init</xsl:attribute>
            <xsl:attribute name="unless">no.deps</xsl:attribute>
            <xsl:variable name="references" select="/p:project/p:configuration/projdeps:references" />
            <xsl:for-each
                select="$references/projdeps:reference[not($type) or projdeps:artifact-type = $type]">
                <xsl:variable name="subproj" select="projdeps:foreign-project" />
                <xsl:variable name="subtarget">
                    <xsl:choose>
                        <xsl:when test="$type">
                            <xsl:value-of select="projdeps:target" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="projdeps:clean-target" />
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <xsl:variable name="script" select="projdeps:script" />
                <ant target="{$subtarget}" inheritall="false"
                     antfile="${{project.{$subproj}}}/{$script}">
                    <property name="dist.ear.dir" location="${{build.dir}}" />
                </ant>
            </xsl:for-each>
        </target>
    </xsl:template>
    
    
    <!-- Template to generate run target(s) for AC -->
    <xsl:template name="run.target">
        <xsl:param name="id" />
        <xsl:param name="type" />
        <target name="run-ac" depends="init" if="app.client">
            <antcall target="run-${{app.client}}" />
        </target>
        <xsl:variable name="references" select="/p:project/p:configuration/projdeps:references" />
        <xsl:variable name="name" select="/p:project/p:configuration/ear2:data/ear2:name" />
        <xsl:for-each
            select="$references/projdeps:reference[not($type) or (projdeps:artifact-type = $type and projdeps:id = $id)]">
            <xsl:variable name="subprojname" select="projdeps:foreign-project" />
            <xsl:variable name="script" select="projdeps:script" />
            <target name="run-{$subprojname}" depends="-tool-{$subprojname},-java-{$subprojname}" />
            <target name="-tool-{$subprojname}" unless="j2ee.clientName"
                    if="j2ee.appclient.mainclass.args" depends="-as-retrieve-option-workaround">
                <java fork="true" classname="${{j2ee.appclient.tool.mainclass}}">
                    <xsl:if test="/p:project/p:configuration/ear2:data/ear2:explicit-platform">
                        <xsl:attribute name="jvm">${platform.java}</xsl:attribute>
                    </xsl:if>
                    <jvmarg line="${{j2ee.appclient.tool.jvmoptions}}" />
                    <jvmarg line="${{j2ee.appclient.jvmoptions.param}}" />
                    <arg line="${{j2ee.appclient.tool.args}}" />
                    <arg line="-client ${{client.jar}}" />
                    <arg line="${{j2ee.appclient.mainclass.tool.param}}" />
                    <arg line="${{application.args.param}}" />
                    <classpath>
                        <path path="${{j2ee.platform.classpath}}:${{j2ee.appclient.tool.runtime}}" />
                    </classpath>
                    <syspropertyset>
                        <propertyref prefix="run-sys-prop." />
                        <mapper type="glob" from="run-sys-prop.*" to="*" />
                    </syspropertyset>
                </java>
            </target>
            <target name="-java-{$subprojname}" if="j2ee.clientName"
                    unless="j2ee.appclient.mainclass.args">
                <java fork="true" classname="${{main.class}}">
                    <xsl:if test="/p:project/p:configuration/ear2:data/ear2:explicit-platform">
                        <xsl:attribute name="jvm">${platform.java}</xsl:attribute>
                    </xsl:if>
                    <jvmarg line="${{j2ee.appclient.tool.jvmoptions}}" />
                    <jvmarg line="-Dj2ee.clientName=${{app.client}}" />
                    <jvmarg line="${{j2ee.appclient.jvmoptions.param}}" />
                    <arg line="${{application.args.param}}" />
                    <classpath>
                        <path
                            path="${{jar.content.additional}}:${{j2ee.platform.classpath}}:${{j2ee.appclient.tool.runtime}}" />
                    </classpath>
                    <syspropertyset>
                        <propertyref prefix="run-sys-prop." />
                        <mapper type="glob" from="run-sys-prop.*" to="*" />
                    </syspropertyset>
                </java>
            </target>
        </xsl:for-each>
        
        <!--
			Idea is to add new non-mandatory option to nbdeploy task. This
			option should be a replacement for asadmin deploy -retrieve local_dir
			command. See also http://www.netbeans.org/issues/show_bug.cgi?id=82929.
		-->
        <target name="-as-retrieve-option-workaround">
            <xsl:attribute name="if">j2ee.appclient.mainclass.args</xsl:attribute>
            <xsl:attribute name="unless">j2ee.clientName</xsl:attribute>
            <property name="client.jar" value="${{dist.dir}}/{$name}Client.jar" />
            <sleep seconds="3" />
            <copy file="${{wa.copy.client.jar.from}}/{$name}/{$name}Client.jar"
                  todir="${{dist.dir}}" />
        </target>
    </xsl:template>
    
    <xsl:template name="debug.target">
        <xsl:param name="id" />
        <xsl:param name="type" />
        <target name="-init-debug-args">
            <xsl:choose>
                <xsl:when test="/p:project/p:configuration/ear2:data/ear2:explicit-platform">
                    <exec executable="${{platform.java}}" outputproperty="version-output">
                        <arg value="-version" />
                    </exec>
                </xsl:when>
                <xsl:otherwise>
                    <property name="version-output"
                              value="java version &quot;${{ant.java.version}}" />
                </xsl:otherwise>
            </xsl:choose>
            <condition property="have-jdk-older-than-1.4">
                <!-- <matches pattern="^java version &quot;1\.[0-3]" string="${version-output}"/> (ANT 1.7) -->
                <or>
                    <contains string="${{version-output}}" substring="java version &quot;1.0" />
                    <contains string="${{version-output}}" substring="java version &quot;1.1" />
                    <contains string="${{version-output}}" substring="java version &quot;1.2" />
                    <contains string="${{version-output}}" substring="java version &quot;1.3" />
                </or>
            </condition>
            <condition property="debug-args-line" value="-Xdebug -Xnoagent -Djava.compiler=none"
                       else="-Xdebug">
                <istrue value="${{have-jdk-older-than-1.4}}" />
            </condition>
        </target>
        <target name="run-debug-appclient" depends="init,-init-debug-args"
                if="can.debug.appclient">
            <macrodef>
                <xsl:attribute name="name">debug-appclient</xsl:attribute>
                <xsl:attribute name="uri">http://www.netbeans.org/ns/j2ee-earproject/2</xsl:attribute>
                <attribute>
                    <xsl:attribute name="name">mainclass</xsl:attribute>
                </attribute>
                <attribute>
                    <xsl:attribute name="name">classpath</xsl:attribute>
                    <xsl:attribute name="default">${debug.classpath}</xsl:attribute>
                </attribute>
                <element>
                    <xsl:attribute name="name">customize</xsl:attribute>
                    <xsl:attribute name="optional">true</xsl:attribute>
                </element>
                <attribute>
                    <xsl:attribute name="name">args</xsl:attribute>
                    <xsl:attribute name="default">${application.args.param}</xsl:attribute>
                </attribute>
                <sequential>
                    <parallel>
                        <java fork="true" classname="@{{mainclass}}">
                            <xsl:if
                                test="/p:project/p:configuration/ear2:data/ear2:explicit-platform">
                                <xsl:attribute name="jvm">${platform.java}</xsl:attribute>
                                <bootclasspath>
                                    <path path="${{platform.bootcp}}" />
                                </bootclasspath>
                            </xsl:if>
                            <jvmarg line="${{j2ee.appclient.tool.jvmoptions}}" />
                            <jvmarg line="${{debug-args-line}}" />
                            <jvmarg
                                value="-Xrunjdwp:transport=${{jpda.transport}},server=y,address=${{jpda.address}},suspend=y" />
                            <jvmarg line="${{j2ee.appclient.jvmoptions.param}}" />
                            <arg line="@{{args}}" />
                            <classpath>
                                <path
                                    path="${{j2ee.platform.classpath}}:${{j2ee.appclient.tool.runtime}}" />
                                <path path="@{{classpath}}" />
                            </classpath>
                            <syspropertyset>
                                <propertyref prefix="run-sys-prop." />
                                <mapper type="glob" from="run-sys-prop.*" to="*" />
                            </syspropertyset>
                            <customize />
                        </java>
                        <nbjpdaconnect name="${{name}}" host="${{jpda.host}}"
                                       address="${{jpda.address}}" transport="${{jpda.transport}}">
                            <classpath>
                                <path
                                    path="${{j2ee.platform.classpath}}:${{j2ee.appclient.tool.runtime}}" />
                                <path path="@{{classpath}}" />
                            </classpath>
                            <sourcepath>
                                <path path="${{src.dir}}" />
                            </sourcepath>
                        </nbjpdaconnect>
                    </parallel>
                </sequential>
            </macrodef>
            <nbdeploy debugmode="false" clientUrlPart="${{client.urlPart}}"
                      clientModuleUri="${{client.module.uri}}" />
            <antcall target="debug-${{app.client}}" />
        </target>
        <xsl:variable name="references" select="/p:project/p:configuration/projdeps:references" />
        <xsl:variable name="name" select="/p:project/p:configuration/ear2:data/ear2:name" />
        <xsl:for-each
            select="$references/projdeps:reference[not($type) or (projdeps:artifact-type = $type and projdeps:id = $id)]">
            <xsl:variable name="subprojname" select="projdeps:foreign-project" />
            <xsl:variable name="script" select="projdeps:script" />
            <target name="debug-{$subprojname}"
                    depends="-debug-tool-{$subprojname},-debug-java-{$subprojname}" />
            <target name="-debug-tool-{$subprojname}" unless="j2ee.clientName"
                    if="j2ee.appclient.mainclass.args" depends="init,-as-retrieve-option-workaround">
                <ear2:debug-appclient mainclass="${{j2ee.appclient.tool.mainclass}}"
                                      args="-client ${{client.jar}} ${{j2ee.appclient.tool.args}} ${{j2ee.appclient.mainclass.tool.param}} ${{application.args.param}}" />
            </target>
            <target name="-debug-java-{$subprojname}" if="j2ee.clientName"
                    unless="j2ee.appclient.mainclass.args">
                <ear2:debug-appclient mainclass="${{main.class}}" args="${{application.args.param}}"
                                      classpath="${{jar.content.additional}}" />
            </target>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template name="copyIterateFiles">
        <xsl:param name="files" />
        <xsl:param name="target" />
        <xsl:param name="libfile" />
        <xsl:if test="$files &gt; 0">
            <xsl:variable name="fileNo" select="$files+(-1)" />
            <xsl:variable name="lib"
                          select="concat(substring-before($libfile,'}'),'.libfile.',$files,'}')" />
            <copy file="{$lib}" todir="{$target}" />
            <xsl:call-template name="copyIterateFiles">
                <xsl:with-param name="files" select="$fileNo" />
                <xsl:with-param name="target" select="$target" />
                <xsl:with-param name="libfile" select="$libfile" />
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="copyIterateDirs">
        <xsl:param name="files" />
        <xsl:param name="target" />
        <xsl:param name="libfile" />
        <xsl:if test="$files &gt; 0">
            <xsl:variable name="fileNo" select="$files+(-1)" />
            <xsl:variable name="lib"
                          select="concat(substring-before($libfile,'}'),'.libdir.',$files,'}')" />
            <copy todir="{$target}">
                <fileset dir="{$lib}" includes="**/*" />
            </copy>
            <xsl:call-template name="copyIterateDirs">
                <xsl:with-param name="files" select="$fileNo" />
                <xsl:with-param name="target" select="$target" />
                <xsl:with-param name="libfile" select="$libfile" />
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    
    <!---
		Access Manager deploy template to build subdependencies.
		@return an Ant target which invokes the Access Manager deployment
		for all known subprojects
	-->
    <xsl:template name="am.target">
        <xsl:variable name="references" select="/p:project/p:configuration/projdeps:references" />
        <xsl:for-each
            select="$references/projdeps:reference[(projdeps:id='dist-ear') or (projdeps:id='j2ee-module-car')]">
            <xsl:variable name="subproj" select="projdeps:foreign-project" />
            <xsl:variable name="script" select="projdeps:script" />
            <ant target="-run-deploy-am" inheritall="false"
                 antfile="${{project.{$subproj}}}/{$script}">
            </ant>
        </xsl:for-each>
    </xsl:template>
    
</xsl:stylesheet>
