export BASE_DIR=/workdir
export NB_HOME=$BASE_DIR/netbeans-6.1
export FULL_PATH_TO_ORACLE_JDBC_JAR=$BASE_DIR/oracle10/jlib/ojdbc14.jar
export FULL_PATH_TO_SQLSERVER_JDBC_JAR=$BASE_DIR/sqljdbc_1.1/enu/sqljdbc.jar
export GF_HOME=/opt/SUNWappserver
export MAVEN_HOME=/opt/apache-maven-2.0.9
export RUN_JUNIT=oracle

export OPEN_DM_MI_ROOT=$BASE_DIR/open-dm-mi
export FULL_PATH_TO_LOADER_RESOURCES_JAR=$OPEN_DM_MI_ROOT/loader/test/config/resources.jar
export FULL_PATH_TO_LOADER_MASTER_INDEX_CLIENT_JAR=$OPEN_DM_MI_ROOT/loader/test/config/master-index-client.jar

export PATH=$MAVEN_HOME/bin:$PATH
export ANT_OPTS=-Xmx512m
export MAVEN_OPTS=-Xmx512m

mvn install:install-file -DgroupId=oracle.jdbc -DartifactId=ojdbc14 -Dversion=10.1.0.2.0 -Dpackaging=jar -Dfile=$FULL_PATH_TO_ORACLE_JDBC_JAR
mvn install:install-file -DgroupId=msft.sqlserver.jdbc -DartifactId=sqljdbc -Dversion=1.0 -Dpackaging=jar -Dfile=$FULL_PATH_TO_SQLSERVER_JDBC_JAR
mvn install:install-file -DgroupId=javaee -DartifactId=javaee-api -Dversion=5 -Dpackaging=jar -Dfile=$GF_HOME/lib/javaee.jar

mvn install:install-file -DgroupId=loader.test -DartifactId=resources -Dversion=6.0 -Dpackaging=jar -Dfile=$FULL_PATH_TO_LOADER_RESOURCES_JAR
mvn install:install-file -DgroupId=loader.test -DartifactId=master-index-client -Dversion=6.0 -Dpackaging=jar -Dfile=$FULL_PATH_TO_LOADER_MASTER_INDEX_CLIENT_JAR
