set NB_HOME=C:\CAPS52\netbeans
set FULL_PATH_TO_ORACLE_JDBC_JAR=C:\Mural\lib\jdbc\oracle\ojdbc14.jar
set FULL_PATH_TO_SQLSERVER_JDBC_JAR=C:\Mural\lib\jdbc\sqlserver\sqljdbc.jar
set GF_HOME=C:\CAPS52\glassfish
set MAVEN_HOME=C:\maven-2.0.7
set RUN_JUNIT=no


set PATH=%MAVEN_HOME%/bin;%PATH%
set ANT_OPTS=-Xmx512m
set MAVEN_OPTS=-Xmx512m

call mvn install:install-file -DgroupId=oracle.jdbc -DartifactId=ojdbc14 -Dversion=10.1.0.2.0 -Dpackaging=jar -Dfile=%FULL_PATH_TO_ORACLE_JDBC_JAR%
call mvn install:install-file -DgroupId=msft.sqlserver.jdbc -DartifactId=sqljdbc -Dversion=1.0 -Dpackaging=jar -Dfile=%FULL_PATH_TO_SQLSERVER_JDBC_JAR%
call mvn install:install-file -DgroupId=javaee -DartifactId=javaee-api -Dversion=5 -Dpackaging=jar -Dfile=%GF_HOME%/lib/javaee.jar
call mvn install:install-file -DgroupId=org.netbeans.api -DartifactId=org-netbeans-modules-compapp-projects-base -Dversion=RELEASE60-BETA1 -Dpackaging=jar -Dfile=%NB_HOME%/soa1/modules/org-netbeans-modules-compapp-projects-base.jar



