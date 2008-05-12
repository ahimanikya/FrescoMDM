set NB_HOME=C:\CAPS52\netbeans
set FULL_PATH_TO_ORACLE_JDBC_JAR=C:\Mural\lib\jdbc\oracle\ojdbc14.jar
set FULL_PATH_TO_SQLSERVER_JDBC_JAR=C:\Mural\lib\jdbc\sqlserver\sqljdbc.jar
set GF_HOME=C:\CAPS52\glassfish
set MAVEN_HOME=C:\maven-2.0.7
set RUN_JUNIT=no

set OPEM_DM_MI_ROOT=%CD%
set FULL_PATH_TO_LOADER_RESOURCES_JAR=%OPEM_DM_MI_ROOT%\loader\test\config\resources.jar
set FULL_PATH_TO_LOADER_MASTER_INDEX_CLIENT_JAR=%OPEM_DM_MI_ROOT%\loader\test\config\master-index-client.jar

set PATH=%MAVEN_HOME%/bin;%PATH%
set ANT_OPTS=-Xmx512m
set MAVEN_OPTS=-Xmx512m

call mvn install:install-file -DgroupId=oracle.jdbc -DartifactId=ojdbc14 -Dversion=10.1.0.2.0 -Dpackaging=jar -Dfile=%FULL_PATH_TO_ORACLE_JDBC_JAR%
call mvn install:install-file -DgroupId=msft.sqlserver.jdbc -DartifactId=sqljdbc -Dversion=1.0 -Dpackaging=jar -Dfile=%FULL_PATH_TO_SQLSERVER_JDBC_JAR%
call mvn install:install-file -DgroupId=javaee -DartifactId=javaee-api -Dversion=5 -Dpackaging=jar -Dfile=%GF_HOME%/lib/javaee.jar

call mvn install:install-file -DgroupId=loader.test -DartifactId=resources -Dversion=6.0 -Dpackaging=jar -Dfile=%FULL_PATH_TO_LOADER_RESOURCES_JAR%
call mvn install:install-file -DgroupId=loader.test -DartifactId=master-index-client -Dversion=6.0 -Dpackaging=jar -Dfile=%FULL_PATH_TO_LOADER_MASTER_INDEX_CLIENT_JAR%


