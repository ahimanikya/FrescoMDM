FULL_PATH_TO_LOADER_RESOURCES_JAR=$JV_SRCROOT/loader/test/config/resources.jar
FULL_PATH_TO_LOADER_MASTER_INDEX_CLIENT_JAR=$JV_SRCROOT/loader/test/config/master-index-client.jar

mvn install:install-file -Dmaven.repo.local=$JV_SRCROOT/m2/repository -DgroupId=loader.test -DartifactId=resources -Dversion=6.0 -Dpackaging=jar -Dfile=$FULL_PATH_TO_LOADER_RESOURCES_JAR

mvn install:install-file -Dmaven.repo.local=$JV_SRCROOT/m2/repository -DgroupId=loader.test -DartifactId=master-index-client -Dversion=6.0 -Dpackaging=jar -Dfile=$FULL_PATH_TO_LOADER_MASTER_INDEX_CLIENT_JAR 
