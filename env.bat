set NB_HOME=C:\CAPS52\netbeans
set FULL_PATH_TO_ORACLE_JDBC_JAR=C:\hawaii\eView\lib\jdbc\oracle\ojdbc14.jar
set MAVEN_HOME=C:\maven-2.0.7


set PATH=%MAVEN_HOME%/bin;%PATH%
call mvn install:install-file -DgroupId=oracle.jdbc -DartifactId=ojdbc14 -Dversion=10.1.0.2.0 -Dpackaging=jar -Dfile=%FULL_PATH_TO_ORACLE_JDBC_JAR%
call mvn install:install-file -DgroupId=org.netbeans.api -DartifactId=org-netbeans-modules-compapp-projects-base -Dversion=RELEASE60-BETA1 -Dpackaging=jar -Dfile=%NB_HOME%/soa1/modules/org-netbeans-modules-compapp-projects-base.jar



